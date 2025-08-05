package de.c4t4lysm.catamines.utils.mine;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockTypes;
import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.utils.Utils;
import de.c4t4lysm.catamines.utils.configuration.FileConfig;
import de.c4t4lysm.catamines.utils.mine.components.CataMineBlock;
import de.c4t4lysm.catamines.utils.mine.components.CataMineResetMode;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.BoundingBox;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCataMine implements Cloneable {

    protected String name;
    protected String world;
    protected Region region;
    protected List<CataMineBlock> blocks = new ArrayList<>();
    protected RandomPattern randomPattern;
    protected int resetDelay;
    protected double resetPercentage;
    protected CataMineResetMode resetMode = CataMineResetMode.TIME;
    protected boolean replaceMode;
    protected boolean warnHotbar;
    protected String warnHotbarMessage = "default";
    protected boolean warn;
    protected boolean warnGlobal;
    protected String warnMessage = "default";
    protected String resetMessage = "default";
    protected List<Integer> warnSeconds = CataMines.getInstance().getFileManager().getDefaultIntegers("Default-Warn-Seconds");
    protected int warnDistance = 5;
    protected boolean teleportPlayers;
    protected boolean isStopped;
    protected Location teleportLocation;
    protected int minEfficiencyLvl;
    protected boolean teleportPlayersToResetLocation;
    protected Location teleportResetLocation;
    protected long blockCount;
    protected boolean runnable;
    protected boolean firstCycle;
    protected int countdown;
    // Block count
    protected int countdownForAutoReset = 6000;

    public AbstractCataMine(String name, Region region) {
        this.name = name;
        if (region != null) {
            this.region = region.clone();
            this.world = region.getWorld() != null ? region.getWorld().getName() : null;
        }
    }

    public void run() {

        if (isStopped || !checkRunnable()) {
            return;
        }

        switch (resetMode) {
            case TIME:
                if (resetDelay <= 0) return;
                if (!firstCycle) {
                    --countdown;
                } else {
                    firstCycle = false;
                    countdown = resetDelay;
                }

                if (warn) {
                    if (warnSeconds.contains(countdown)) {
                        broadcastWarnMessage();
                    }
                }

                if (countdown <= 0) {
                    reset();
                    firstCycle = true;
                }
                break;
            case PERCENTAGE:
                --countdownForAutoReset;
                if (countdownForAutoReset <= 0) {
                    forceReset();
                    countdownForAutoReset = 6000;
                    break;
                }
                if (getRemainingBlocksPer() <= resetPercentage) {
                    reset();
                }
                break;
            case TIME_PERCENTAGE:
                if (resetDelay <= 0) return;
                if (!firstCycle) {
                    --countdown;
                } else {
                    firstCycle = false;
                    countdown = resetDelay;
                }

                if (warn) {
                    if (warnSeconds.contains(countdown)) {
                        broadcastWarnMessage();
                    }
                }

                if (countdown <= 0) {
                    reset();
                    firstCycle = true;
                    break;
                }

                if (getRemainingBlocksPer() <= resetPercentage) {
                    reset();
                    firstCycle = true;
                    break;
                }
                break;
        }

        if (warnHotbar) {
            broadcastHotbar();
        }
    }

    public void reset() {

        if (blockCount == getTotalBlocks() && CataMines.getInstance().getConfig().getBoolean("optimizeMines", true)) {
            return;
        }

        EditSession editSession = CataMines.getInstance().getEditSession(region.getWorld());
        blockCount = getTotalBlocks();
        if (!replaceMode) {
            editSession.setBlocks(region, randomPattern);
        } else {
            editSession.replaceBlocks(region, Collections.singleton(BlockTypes.AIR.getDefaultState().toBaseBlock()), randomPattern);
        }

        if (warn) {
            broadcastResetMessage();
        }
        if (teleportPlayers) {
            teleportPlayers();
        }
    }

    public void forceReset() {
        blockCount = 0;
        reset();
    }

    public boolean checkRunnable() {
        if (region == null) {
            FileConfig fileConfig = new FileConfig(CataMines.getInstance().getDataFolder() + "/mines", name + ".yml");
            if (Bukkit.getWorld(world) != null) {
                loadRegion(fileConfig);
            } else {
                setStopped(true);
                CataMines.getInstance().getLogger().warning("World " + world + " not found. Stopped " + name);
                return false;
            }
        }
        setRunnable(region != null && randomPattern != null);
        return runnable;
    }

    public boolean isRunnable() {
        return runnable;
    }

    public void setRunnable(boolean runnable) {
        this.runnable = runnable;
    }

    public void loadRegion(FileConfig fileConfig) {
        Location minimumPoint = (Location) fileConfig.get("Mine.region.p1");
        Location maximumPoint = (Location) fileConfig.get("Mine.region.p2");

        if (minimumPoint == null || maximumPoint == null || minimumPoint.getWorld() == null || maximumPoint.getWorld() == null || !(Objects.equals(minimumPoint.getWorld(), maximumPoint.getWorld()))) {
            CataMines.getInstance().getLogger().severe("Could not load region of " + name);
            setStopped(true);
            return;
        }

        region = new CuboidRegion(BukkitAdapter.adapt(minimumPoint.getWorld()),
                BlockVector3.at(minimumPoint.getX(), minimumPoint.getY(), minimumPoint.getZ()),
                BlockVector3.at(maximumPoint.getX(), maximumPoint.getY(), maximumPoint.getZ()));

    }

    public void addBlock(CataMineBlock cataMineBlock) {
        double chanceSum = 0;
        for (CataMineBlock block : blocks) {
            if (block.getBlockData().equals(cataMineBlock.getBlockData())) {
                continue;
            }

            chanceSum += block.getChance();
        }

        chanceSum += cataMineBlock.getChance();
        if (chanceSum > 100)
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Chance-Over-100"));

        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i).getBlockData().equals(cataMineBlock.getBlockData())) {
                blocks.set(i, cataMineBlock);
                blocksToRandomPattern();
                return;
            }
        }

        blocks.add(cataMineBlock);
        blocksToRandomPattern();
    }

    public void clearComposition() {
        blocks.clear();
        blocksToRandomPattern();
    }

    public void removeBlock(BlockData blockData) {
        if (!containsBlockData(blockData)) {
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Block-Not-In-Composition"));
        }
        blocks.remove(getBlock(blockData));
        blocksToRandomPattern();
    }

    public void removeBlock(int index) {
        blocks.remove(index);
        blocksToRandomPattern();
    }

    public double getCompositionChance() {

        double compositionChance = 0;

        for (CataMineBlock block : blocks) {
            compositionChance += block.getChance();
        }

        return Math.round(compositionChance * 100) / 100d;
    }

    public double getBlockChance(Material material) {
        double chance = 0;

        for (CataMineBlock block : blocks) {
            if (block.getBlockData().getMaterial().equals(material)) {
                chance = block.getChance();
            }
        }

        return chance;
    }

    public void setBlockChance(CataMineBlock block, double chance) {
        double chanceSum = 0;
        for (CataMineBlock cataMineBlock : blocks) {
            if (cataMineBlock.equals(block)) {
                chanceSum += chance;
                continue;
            }

            chanceSum += cataMineBlock.getChance();
        }

        if (chanceSum > 100) {
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Chance"));
        }

        block.setChance(chance);
        blocksToRandomPattern();
    }

    public void setBlockChance(BlockData blockData, double chance) {
        setBlockChance(getBlock(blockData), chance);
    }

    public CataMineBlock getBlock(BlockData blockData) {
        for (CataMineBlock block : blocks) {
            if (block.getBlockData().equals(blockData)) {
                return block;
            }
        }

        return null;
    }

    public boolean containsBlock(CataMineBlock block) {
        return blocks.contains(block);
    }

    public boolean containsBlockData(BlockData blockData) {
        for (CataMineBlock block : blocks) {
            if (block.getBlockData().equals(blockData)) return true;
        }

        return false;
    }

    public boolean containsBlockMaterial(Material material) {
        for (CataMineBlock block : blocks) {
            if (block.getBlockData().getMaterial().equals(material)) return true;
        }

        return false;
    }

    public void blocksToRandomPattern() {

        if (blocks.isEmpty() || blocks.stream().allMatch(cataMineBlock -> cataMineBlock.getChance() == 0)) {
            randomPattern = null;
            return;
        }

        randomPattern = new RandomPattern();

        blocks.stream().filter(cataMineBlock -> cataMineBlock.getChance() > 0)
                .forEach(cataMineBlock -> randomPattern.add(BukkitAdapter.adapt(cataMineBlock.getBlockData()).toBaseBlock(), cataMineBlock.getChance()));
    }

    public String getTranslatedTimeMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append("");
        String.format("%");
        return null;
    }

    public void broadcastHotbar() {
        String finalMessage = Utils.setPlaceholders(getWarnHotbarMessage(resetMode), this);
        getPlayersInDistance().forEach(player -> {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(finalMessage));
        });
    }

    public void broadcastWarnMessage() {
        String finalWarnMessage = Utils.setPlaceholders(getWarnMessage(), this);
        getPlayersToWarn().forEach(player -> {
            Arrays.stream(finalWarnMessage.split("/n")).forEach(player::sendMessage);
        });
    }

    public void broadcastResetMessage() {
        String finalResetMessage = Utils.setPlaceholders(getResetMessage(), this);
        getPlayersToWarn().forEach(player -> {
            Arrays.stream(finalResetMessage.split("/n")).forEach(player::sendMessage);
        });
    }


    public void teleportPlayers() {
        if (!teleportPlayersToResetLocation) {
            getPlayersInRegion().forEach(player -> player.teleport(new Location(player.getWorld(), player.getLocation().getX(), region.getMaximumPoint().y() + 1,
                    player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch())));
        } else {
            if (teleportResetLocation == null) {
                return;
            }
            getPlayersInRegion().forEach(player -> player.teleport(teleportResetLocation));
        }
    }

    public Collection<? extends Player> getPlayersInRegion() {
        return Bukkit.getOnlinePlayers().stream().filter(player -> Objects.equals(region.getWorld().getName(), player.getWorld().getName()) &&
                region.contains(BlockVector3.at(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()))).collect(Collectors.toList());
    }

    public Collection<? extends Player> getPlayersInDistance() {
        return Bukkit.getOnlinePlayers().stream().filter(player -> Objects.equals(region.getWorld().getName(), player.getWorld().getName()) &&
                player.getBoundingBox().overlaps(
                        new BoundingBox(
                                region.getMinimumPoint().x(),
                                region.getMinimumPoint().y(),
                                region.getMinimumPoint().z(),
                                region.getMaximumPoint().x() + 1,
                                region.getMaximumPoint().y() + 1,
                                region.getMaximumPoint().z() + 1)
                                .expand(warnDistance))).collect(Collectors.toList());
    }

    public Collection<? extends Player> getPlayersToWarn() {
        return !warnGlobal ? getPlayersInDistance() : Bukkit.getOnlinePlayers();
    }

    public String getFormattedTimeString() {
        CataMines plugin = CataMines.getInstance();
        String output;

        switch (countdown) {
            case 3600:
                output = plugin.getLangString("Time.Hour");
                break;
            case 60:
                output = plugin.getLangString("Time.Minute");
                break;
            case 1:
                output = plugin.getLangString("Time.Second");
                break;
            default:
                if (countdown >= 3600) {
                    output = plugin.getLangString("Time.Hours");
                } else if (countdown >= 60) {
                    output = plugin.getLangString("Time.Minutes");
                } else {
                    output = plugin.getLangString("Time.Seconds");
                }
        }

        return output;
    }

    public abstract void handleBlockBreak(BlockBreakEvent event);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
        this.world = region.getWorld().getName();
    }

    public List<CataMineBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<CataMineBlock> blocks) {
        this.blocks = blocks;
    }

    public RandomPattern getRandomPattern() {
        return randomPattern;
    }

    public void setRandomPattern(RandomPattern randomPattern) {
        this.randomPattern = randomPattern;
    }

    public CataMineResetMode getResetMode() {
        return resetMode;
    }

    public void setResetMode(CataMineResetMode resetMode) {
        this.resetMode = resetMode;
    }

    public int getResetDelay() {
        return resetDelay;
    }

    public void setResetDelay(int resetDelay) {
        this.resetDelay = resetDelay;
    }

    public double getResetPercentage() {
        return resetPercentage;
    }

    public void setResetPercentage(double resetPercentage) {
        if (resetPercentage < 0) resetPercentage = 0;
        if (resetPercentage > 100) resetPercentage = 100;
        this.resetPercentage = Math.round(resetPercentage * 100) / 100d;
    }

    public boolean isReplaceMode() {
        return replaceMode;
    }

    public void setReplaceMode(boolean replaceMode) {
        this.replaceMode = replaceMode;
    }

    public boolean isWarn() {
        return warn;
    }

    public void setWarn(boolean warn) {
        this.warn = warn;
    }

    public boolean isWarnHotbar() {
        return warnHotbar;
    }

    public void setWarnHotbar(boolean warnHotbar) {
        this.warnHotbar = warnHotbar;
    }

    public String getWarnHotbarMessage(CataMineResetMode resetMode) {
        return warnHotbarMessage.equalsIgnoreCase("default") ? CataMines.getInstance().getDefaultString("Default-Hotbar-Message-" + StringUtils.capitalize(resetMode.name())) : warnHotbarMessage;
    }

    public void setWarnHotbarMessage(String warnHotbarMessage) {
        this.warnHotbarMessage = warnHotbarMessage;
    }

    public boolean isWarnGlobal() {
        return warnGlobal;
    }

    public void setWarnGlobal(boolean warnGlobal) {
        this.warnGlobal = warnGlobal;
    }

    public String getWarnMessage() {
        return warnMessage.equalsIgnoreCase("default") ? CataMines.getInstance().getDefaultString("Default-Warn-Message") : warnMessage;
    }

    public void setWarnMessage(String warnMessage) {
        this.warnMessage = warnMessage;
    }

    public String getResetMessage() {
        return resetMessage.equalsIgnoreCase("default") ? CataMines.getInstance().getDefaultString("Default-Reset-Message") : resetMessage;
    }

    public void setResetMessage(String resetMessage) {
        this.resetMessage = resetMessage;
    }

    public List<Integer> getWarnSeconds() {
        return warnSeconds;
    }

    public void setWarnSeconds(List<Integer> warnSeconds) {
        this.warnSeconds = warnSeconds;
    }

    public int getWarnDistance() {
        return warnDistance;
    }

    public void setWarnDistance(int warnDistance) {
        this.warnDistance = warnDistance;
    }

    public boolean isTeleportPlayers() {
        return teleportPlayers;
    }

    public void setTeleportPlayers(boolean teleportPlayers) {
        this.teleportPlayers = teleportPlayers;
    }

    public boolean isTeleportPlayersToResetLocation() {
        return teleportPlayersToResetLocation;
    }

    public void setTeleportPlayersToResetLocation(boolean teleportPlayersToResetLocation) {
        this.teleportPlayersToResetLocation = teleportPlayersToResetLocation;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    protected abstract Location getTeleportLocation();

    public void setTeleportLocation(Location teleportLocation) {
        this.teleportLocation = teleportLocation;
    }

    public Location getTeleportResetLocation() {
        return teleportResetLocation;
    }

    public void setTeleportResetLocation(Location teleportResetLocation) {
        this.teleportResetLocation = teleportResetLocation;
    }

    public int getMinEfficiencyLvl() {
        return minEfficiencyLvl;
    }

    public void setMinEfficiencyLvl(int minEfficiencyLvl) {
        this.minEfficiencyLvl = minEfficiencyLvl;
    }

    public boolean isFirstCycle() {
        return firstCycle;
    }

    public void setFirstCycle(boolean firstCycle) {
        this.firstCycle = firstCycle;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    @Override
    public AbstractCataMine clone() {
        try {
            return (AbstractCataMine) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public abstract long getTotalBlocks();

    public long getBlockCount() {
        return blockCount;
    }

    public void setBlockCount(long blockCount) {
        this.blockCount = blockCount;
    }

    public long getMinedBlocks() {
        return getTotalBlocks() - getBlockCount();
    }

    public abstract double getRemainingBlocksPer();
}
