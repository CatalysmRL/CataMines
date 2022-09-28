package de.c4t4lysm.catamines.utils.mine.mines;

import com.google.common.base.Enums;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.utils.configuration.FileConfig;
import de.c4t4lysm.catamines.utils.mine.AbstractCataMine;
import de.c4t4lysm.catamines.utils.mine.components.CataMineBlock;
import de.c4t4lysm.catamines.utils.mine.components.CataMineLootItem;
import de.c4t4lysm.catamines.utils.mine.components.CataMineResetMode;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class CuboidCataMine extends AbstractCataMine implements Cloneable, ConfigurationSerializable {

    private final Random random = new Random();

    private File file;
    private YamlConfiguration fileConfig;

    public CuboidCataMine(String name, Region region) {
        super(name, region);
    }

    public static CuboidCataMine deserialize(Map<String, Object> serializedCataMine) {
        Logger logger = CataMines.getInstance().getLogger();

        String name = (String) serializedCataMine.get("name");

        Location minimumPoint = null;
        Location maximumPoint = null;
        String world = "";
        Region region = null;

        if (serializedCataMine.containsKey("region")) {
            Map<String, Object> regionLocations = (Map<String, Object>) serializedCataMine.get("region");
            world = (String) regionLocations.get("world");
            World bukkitWorld = null;
            boolean loadable = true;
            try {
                bukkitWorld = Bukkit.getWorld(world);
            } catch (Throwable throwable) {
                logger.severe("World " + regionLocations.get("world") + " not found");
            }
            if (bukkitWorld == null) {
                logger.severe("Could not find world " + world + ".");
                loadable = false;
            }
            try {
                minimumPoint = (Location) regionLocations.get("p1");
                maximumPoint = (Location) regionLocations.get("p2");
            } catch (Throwable throwable) {
                logger.severe("Could not load locations");
                loadable = false;
            }

            if (minimumPoint == null || maximumPoint == null || !Objects.equals(minimumPoint.getWorld(), maximumPoint.getWorld())) {
                logger.severe("Could not load locations, does the world exist?");
                loadable = false;
            }

            if (loadable)
                region = new CuboidRegion(BukkitAdapter.adapt(minimumPoint.getWorld()), BlockVector3.at(minimumPoint.getX(), minimumPoint.getY(),
                        minimumPoint.getZ()), BlockVector3.at(maximumPoint.getX(), maximumPoint.getY(), maximumPoint.getZ()));
        }

        ArrayList<CataMineBlock> blocks = new ArrayList<>();

        if (serializedCataMine.containsKey("composition")) {
            ArrayList<Map<String, Object>> serializedBlocks = (ArrayList<Map<String, Object>>) serializedCataMine.get("composition");

            for (Map<String, Object> serializedBlock : serializedBlocks) {
                blocks.add(CataMineBlock.deserialize(serializedBlock));
            }
        }

        CataMineResetMode resetMode = CataMineResetMode.TIME;
        if (serializedCataMine.containsKey("resetMode")) {
            resetMode = Enums.getIfPresent(CataMineResetMode.class, (String) serializedCataMine.get("resetMode")).or(CataMineResetMode.TIME);
        }

        int resetDelay = 0;
        if (serializedCataMine.containsKey("resetDelay")) {
            resetDelay = (int) serializedCataMine.get("resetDelay");
        }

        int countdown = resetDelay;
        if (serializedCataMine.containsKey("countdown")) {
            countdown = (int) serializedCataMine.get("countdown");
        }

        double resetPercentage = 0;
        if (serializedCataMine.containsKey("resetPercentage")) {
            resetPercentage = (double) serializedCataMine.get("resetPercentage");
        }

        boolean replaceMode = false;
        if (serializedCataMine.containsKey("replaceMode")) {
            replaceMode = (boolean) serializedCataMine.get("replaceMode");
        }

        boolean teleportPlayers = false;
        if (serializedCataMine.containsKey("teleportPlayers")) {
            teleportPlayers = (boolean) serializedCataMine.get("teleportPlayers");
        }

        boolean teleportPlayersToResetLocation = false;
        if (serializedCataMine.containsKey("teleportPlayersToResetLocation")) {
            teleportPlayersToResetLocation = (boolean) serializedCataMine.get("teleportPlayersToResetLocation");
        }

        boolean warnHotbar = false;
        String warnHotbarMessage = "&a%seconds%";
        boolean warn = false;
        boolean warnGlobal = false;
        String warnMessage = "default";
        String resetMessage = "default";
        List<Integer> warnSeconds = Arrays.asList(1, 2, 3, 5, 20, 60);
        int warnDistance = 5;
        if (serializedCataMine.containsKey("warn")) {
            Map<String, Object> serializedWarn = (Map<String, Object>) serializedCataMine.get("warn");
            if (serializedWarn.containsKey("warnHotbar")) {
                warnHotbar = (boolean) serializedWarn.get("warnHotbar");
            }
            if (serializedWarn.containsKey("warnHotbarMessage")) {
                warnHotbarMessage = (String) serializedWarn.get("warnHotbarMessage");
            }
            if (serializedWarn.containsKey("enableWarn")) {
                warn = (boolean) serializedWarn.get("enableWarn");
            }
            if (serializedWarn.containsKey("warnGlobal")) {
                warnGlobal = (boolean) serializedWarn.get("warnGlobal");
            }
            if (serializedWarn.containsKey("warnMessage")) {
                warnMessage = (String) serializedWarn.get("warnMessage");
            }
            if (serializedWarn.containsKey("resetMessage")) {
                resetMessage = (String) serializedWarn.get("resetMessage");
            }
            if (serializedWarn.containsKey("warnSeconds")) {
                warnSeconds = (List<Integer>) serializedWarn.get("warnSeconds");
            }
            if (serializedWarn.containsKey("warnDistance")) {
                warnDistance = (int) serializedWarn.get("warnDistance");
            }
        }
        int minEfficiencyLvl = 0;
        if (serializedCataMine.containsKey("minEfficiencyLvl")) {
            minEfficiencyLvl = (int) serializedCataMine.get("minEfficiencyLvl");
        }

        Location teleportLocation = null;
        if (serializedCataMine.containsKey("teleportLocation")) {
            teleportLocation = (Location) serializedCataMine.get("teleportLocation");
        }

        Location teleportResetLocation = null;
        if (serializedCataMine.containsKey("teleportResetLocation")) {
            teleportResetLocation = (Location) serializedCataMine.get("teleportResetLocation");
        }

        boolean isStopped = false;
        if (serializedCataMine.containsKey("isStopped")) {
            isStopped = (boolean) serializedCataMine.get("isStopped");
        }

        CuboidCataMine mine = new CuboidCataMine(name, region);
        mine.setWorld(world);
        mine.setBlocks(blocks);
        mine.setResetMode(resetMode);
        mine.setResetDelay(resetDelay);
        mine.setResetPercentage(resetPercentage);
        mine.setReplaceMode(replaceMode);
        mine.setWarnHotbar(warnHotbar);
        mine.setWarnHotbarMessage(warnHotbarMessage);
        mine.setWarn(warn);
        mine.setWarnGlobal(warnGlobal);
        mine.setWarnMessage(warnMessage);
        mine.setResetMessage(resetMessage);
        mine.setWarnSeconds(warnSeconds);
        mine.setWarnDistance(warnDistance);
        mine.setTeleportPlayers(teleportPlayers);
        mine.setStopped(isStopped);
        mine.setTeleportLocation(teleportLocation);
        mine.setMinEfficiencyLvl(minEfficiencyLvl);
        mine.setTeleportPlayersToResetLocation(teleportPlayersToResetLocation);
        mine.setTeleportResetLocation(teleportResetLocation);

        mine.setCountdown(countdown);
        mine.blocksToRandomPattern();

        if (region != null && mine.getRandomPattern() != null && resetMode != CataMineResetMode.TIME) mine.forceReset();
        return mine;
    }

    @Override
    @Nonnull
    public Map<String, Object> serialize() {
        Map<String, Object> mapSerializer = new LinkedHashMap<>();
        mapSerializer.put("name", name);

        if (region != null) {
            Map<String, Object> mappedRegion = new LinkedHashMap<>();
            mappedRegion.put("type", "CUBOID");
            mappedRegion.put("world", region.getWorld().getName());

            mappedRegion.put("p1", BukkitAdapter.adapt(BukkitAdapter.adapt(region.getWorld()), region.getMinimumPoint()));
            mappedRegion.put("p2", BukkitAdapter.adapt(BukkitAdapter.adapt(region.getWorld()), region.getMaximumPoint()));

            mapSerializer.put("region", mappedRegion);
        }


        ArrayList<Map<String, Object>> tempSerializeBlocks = new ArrayList<>();
        for (CataMineBlock block : blocks) {
            tempSerializeBlocks.add(block.serialize());
        }

        mapSerializer.put("composition", tempSerializeBlocks);

        mapSerializer.put("resetMode", resetMode.name());
        mapSerializer.put("resetDelay", resetDelay);
        mapSerializer.put("countdown", countdown);
        mapSerializer.put("resetPercentage", resetPercentage);
        mapSerializer.put("replaceMode", replaceMode);
        mapSerializer.put("teleportPlayers", teleportPlayers);
        mapSerializer.put("teleportPlayersToResetLocation", teleportPlayersToResetLocation);
        mapSerializer.put("isStopped", isStopped);
        mapSerializer.put("warn", warn);

        Map<String, Object> mappedWarn = new LinkedHashMap<>();
        mappedWarn.put("warnHotbar", warnHotbar);
        mappedWarn.put("warnHotbarMessage", warnHotbarMessage);
        mappedWarn.put("enableWarn", warn);
        mappedWarn.put("warnGlobal", warnGlobal);
        mappedWarn.put("warnMessage", warnMessage);
        mappedWarn.put("resetMessage", resetMessage);
        mappedWarn.put("warnSeconds", warnSeconds);
        mappedWarn.put("warnDistance", warnDistance);
        mapSerializer.put("warn", mappedWarn);

        mapSerializer.put("minEfficiencyLvl", minEfficiencyLvl);
        mapSerializer.put("teleportLocation", teleportLocation);
        mapSerializer.put("teleportResetLocation", teleportResetLocation);

        return mapSerializer;
    }

    public void handleBlockBreak(BlockBreakEvent event) {

        if (getMinEfficiencyLvl() > 0 && !event.getPlayer().hasPermission("catamines.break")) {
            Player player = event.getPlayer();
            int efficiencyLvl = 0;
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.containsEnchantment(Enchantment.DIG_SPEED)) {
                efficiencyLvl = itemInHand.getEnchantmentLevel(Enchantment.DIG_SPEED);
            }

            if (efficiencyLvl < getMinEfficiencyLvl()) {
                event.setCancelled(true);
                player.sendMessage(CataMines.PREFIX + ChatColor.translateAlternateColorCodes('&', CataMines.getInstance().getDefaultString("Tool-Too-Weak")
                        .replaceAll("%level%", String.valueOf(minEfficiencyLvl))));
                return;
            }
        }

        blockCount--;

        if (warnHotbar && (resetMode == CataMineResetMode.PERCENTAGE || resetMode == CataMineResetMode.TIME_PERCENTAGE)) {
            broadcastHotbar();
        }

        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;

        for (CataMineBlock block : blocks) {
            if (block.getLootTable().isEmpty() || !block.getBlockData().equals(event.getBlock().getBlockData())) {
                continue;
            }

            if (!block.isAddLootTable()) {
                event.setDropItems(false);
            }

            for (CataMineLootItem lootItem : block.getLootTable()) {
                ItemStack item = lootItem.getItem().clone();
                double chance = lootItem.getChance();
                double r1 = random.nextDouble() * 100;

                if (chance < r1) {
                    continue;
                }

                Location location = event.getBlock().getLocation();
                if (lootItem.isFortune()) {
                    ItemStack usedTool = event.getPlayer().getInventory().getItemInMainHand();
                    if (usedTool.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                        item.setAmount(lootItem.getDropCount(usedTool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)));
                    }
                }
                location.getWorld().dropItemNaturally(location, item);
            }
        }
    }

    public void save() {
        if (file == null) {
            file = new File(CataMines.getInstance().getDataFolder() + "/mines", name + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (fileConfig == null) {
            fileConfig = new YamlConfiguration();
        }
        fileConfig.set("Mine", this);
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(FileConfig fileConfig) {
        fileConfig.set("Mine", this);
        fileConfig.saveConfig();
    }

    public Location getTeleportLocation() {
        if (teleportLocation == null) {
            if (region != null) {
                return new Location(BukkitAdapter.adapt(region.getWorld()), region.getCenter().getX() + 0.5, region.getMaximumPoint().getY() + 1, region.getCenter().getZ() + 0.5);
            }
        }

        return teleportLocation;
    }

    @Override
    public CuboidCataMine clone() {
        return (CuboidCataMine) super.clone();
    }

    @Override
    public long getTotalBlocks() {
        return region.getVolume();
    }

    @Override
    public double getRemainingBlocksPer() {
        return Math.round(((double) getBlockCount() / (double) getTotalBlocks()) * 10000d) / 100d;
    }

    public void resetFiles() {
        file = null;
        fileConfig = null;
    }
}
