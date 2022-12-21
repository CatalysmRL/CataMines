package me.catalysmrl.legacycatamines.mine.mines;

import com.fastasyncworldedit.core.regions.PolyhedralRegion;
import com.google.common.base.Enums;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector2;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.*;
import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.legacy.mine.components.*;
import me.catalysmrl.catamines.mine.components.composition.CataMineBlock;
import me.catalysmrl.catamines.mine.components.region.SelectionRegion;
import me.catalysmrl.legacycatamines.utils.Utils;
import me.catalysmrl.legacycatamines.utils.configuration.ConfigFile;
import me.catalysmrl.legacycatamines.mine.AbstractCataMine;
import me.catalysmrl.legacycatamines.mine.components.CataMineExecutor;
import me.catalysmrl.legacycatamines.mine.components.CataMineResetMode;
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

        String world = "";
        Region region = null;

        if (serializedCataMine.containsKey("region")) {
            Map<String, Object> serializedRegion = (Map<String, Object>) serializedCataMine.get("region");
            world = (String) serializedRegion.get("world");
            World bukkitWorld = null;
            try {
                bukkitWorld = Bukkit.getWorld(world);
            } catch (Throwable throwable) {
                logger.severe("World " + world + " not found");
            }

            if (bukkitWorld != null) {
                com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(bukkitWorld);
                SelectionRegion.SelectionType mineType = SelectionRegion.SelectionType.valueOf((String) serializedRegion.get("type"));
                switch (mineType) {
                    case CYLINDER:
                        BlockVector3 cylCenter = Utils.unparseBlockVector3((String) serializedRegion.get("center"));
                        Vector2 cylRadius = Utils.unparseVector2((String) serializedRegion.get("radius"));
                        if (cylCenter == null || cylRadius == null) {
                            break;
                        }
                        region = new CylinderRegion(weWorld, cylCenter,
                                cylRadius, (int) serializedRegion.get("minY"),
                                (int) serializedRegion.get("maxY"));
                        break;
                    case ELLIPSOID:
                        BlockVector3 elipCenter = Utils.unparseBlockVector3((String) serializedRegion.get("center"));
                        Vector3 elipRadius = Utils.unparseVector3((String) serializedRegion.get("radius"));
                        if (elipCenter == null || elipRadius == null) break;
                        region = new EllipsoidRegion(weWorld, elipCenter, elipRadius);
                        break;
                    case POLYGONAL2D:
                        int minY = (int) serializedRegion.get("minY");
                        int maxY = (int) serializedRegion.get("maxY");

                        List<BlockVector2> blockVector2s = new ArrayList<>();
                        int i = 0;
                        while (serializedRegion.containsKey("p" + i)) {
                            blockVector2s.add(Objects.requireNonNull(Utils.unparseVector2((String) serializedRegion.get("p" + i))).toBlockPoint());
                            i++;
                        }
                        region = new Polygonal2DRegion(weWorld, blockVector2s, minY, maxY);
                        break;
                    case CONVEXPOLYHEDRAL:
                        ConvexPolyhedralRegion convex = new ConvexPolyhedralRegion(weWorld);
                        int k = 0;
                        while (serializedRegion.containsKey("p" + k)) {
                            convex.addVertex(Utils.unparseBlockVector3((String) serializedRegion.get("p" + k)));
                            k++;
                        }
                        region = convex;
                        break;
                    case CUBOID:
                    case NONE:
                    default:
                        region = new CuboidRegion(weWorld, Objects.requireNonNull(Utils.unparseBlockVector3((String) serializedRegion.get("minP"))), Objects.requireNonNull(Utils.unparseBlockVector3((String) serializedRegion.get("maxP"))));
                }
            }
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

        Map<Integer, List<String>> executor = new HashMap<>();
        if (serializedCataMine.containsKey("executor")) {
            Map<String, List<String>> serializedExecutor = (Map<String, List<String>>) serializedCataMine.get("executor");
            for (String s : serializedExecutor.keySet()) {
                executor.put(Integer.valueOf(s), serializedExecutor.get(s));
            }
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

        CataMineExecutor cataMineExecutor = new CataMineExecutor(mine);
        cataMineExecutor.setExecutionMap(executor);
        mine.setExecutor(cataMineExecutor);

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

            type = getType();

            Map<String, Object> mappedRegion = new LinkedHashMap<>();
            mappedRegion.put("type", type.toString());
            mappedRegion.put("world", world);

            switch (type) {
                case CYLINDER:
                    CylinderRegion cylinderRegion = (CylinderRegion) region;
                    mappedRegion.put("center", cylinderRegion.getCenter().toParserString());
                    mappedRegion.put("radius", cylinderRegion.getRadius().toParserString());
                    mappedRegion.put("minY", cylinderRegion.getMinimumY());
                    mappedRegion.put("maxY", cylinderRegion.getMaximumY());
                    break;
                case ELLIPSOID:
                    EllipsoidRegion ellipsoidRegion = (EllipsoidRegion) region;
                    mappedRegion.put("center", ellipsoidRegion.getCenter().toParserString());
                    mappedRegion.put("radius", ellipsoidRegion.getRadius().toParserString());
                    break;
                case POLYGONAL2D:
                    Polygonal2DRegion polygonal2DRegion = (Polygonal2DRegion) region;
                    mappedRegion.put("minY", polygonal2DRegion.getMinimumY());
                    mappedRegion.put("maxY", polygonal2DRegion.getMaximumY());
                    List<BlockVector2> points = polygonal2DRegion.getPoints();
                    for (int i = 0; i < points.size(); i++) {
                        mappedRegion.put("p" + i, points.get(i).toParserString());
                    }
                    break;
                case POLYHEDRAL:
                    PolyhedralRegion polyhedralRegion = (PolyhedralRegion) region;
                    Collection<BlockVector3> vertices = polyhedralRegion.getVertices();
                    Iterator<BlockVector3> verticesIterator = vertices.iterator();

                    int i = 0;
                    while (verticesIterator.hasNext()) {
                        mappedRegion.put("p" + i, verticesIterator.next().toParserString());
                        i++;
                    }
                    break;
                case CONVEXPOLYHEDRAL:
                    ConvexPolyhedralRegion convexPolyhedralRegion = (ConvexPolyhedralRegion) region;
                    Collection<BlockVector3> convexVertices = convexPolyhedralRegion.getVertices();
                    Iterator<BlockVector3> convexVerticesiterator = convexVertices.iterator();

                    int j = 0;
                    while (convexVerticesiterator.hasNext()) {
                        mappedRegion.put("p" + j, convexVerticesiterator.next().toParserString());
                        j++;
                    }
                    break;
                case CUBOID:
                case NONE:
                default:
                    mappedRegion.put("minP", region.getMinimumPoint().toParserString());
                    mappedRegion.put("maxP", region.getMaximumPoint().toParserString());
            }

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

        Map<String, Object> mappedExecutor = new LinkedHashMap<>();
        for (Map.Entry<Integer, List<String>> entry : executor.getExecutionMap().entrySet()) {
            mappedExecutor.put(String.valueOf(entry.getKey()), entry.getValue());
        }

        mapSerializer.put("executor", mappedExecutor);

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

        if (getMinEfficiencyLvl() > 0 && !event.getPlayer().hasPermission("catamines.minefficiency.bypass")) {
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

    public void save(ConfigFile configFile) {
        configFile.set("Mine", this);
        configFile.saveConfig();
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
