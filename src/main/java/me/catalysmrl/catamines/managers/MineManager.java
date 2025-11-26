package me.catalysmrl.catamines.managers;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.api.serialization.DeserializationException;
import me.catalysmrl.catamines.managers.blockmanagers.BlockApplicator;
import me.catalysmrl.catamines.managers.blockmanagers.BukkitBlockApplicationManager;
import me.catalysmrl.catamines.managers.blockmanagers.FastAsyncBlockApplicationManager;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.mine.mines.AdvancedCataMine;
import me.catalysmrl.catamines.utils.helper.CompatibilityProvider;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MineManager {

    private final Path minesPath;
    private final Path schematicsPath;

    private final CataMines plugin;
    private BukkitTask minesTask;
    private BlockApplicator blockApplicator;

    private final List<CataMine> mines = new ArrayList<>();

    public MineManager(CataMines plugin) {
        this.plugin = plugin;
        minesPath = plugin.getDataFolder().toPath().resolve("mines");
        schematicsPath = plugin.getDataFolder().toPath().resolve("schematics");
        try {
            createDirectoriesIfNotExists(minesPath);
            createDirectoriesIfNotExists(schematicsPath);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to create mines and/or schematics directory");
            plugin.getLogger().severe(e.getMessage());
            return;
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> loadMinesFromFolder(minesPath), 2L);
        start();
    }

    public void start() {
        initBlockApplicator();
        initMineTask();
    }

    public void shutDown() {
        blockApplicator.cancel();
        minesTask.cancel();

        for (CataMine mine : mines) {
            try {
                saveMine(mine);
            } catch (IOException e) {
                plugin.getLogger().severe(
                        Message.MINE_SAVE_EXCEPTION.format(plugin.getServer().getConsoleSender(), mine.getName()));
                plugin.getLogger().severe(e.getMessage());
            }
        }
    }

    /**
     * Initializes the BlockApplicationManager. It's responsible for Block efficient
     * Block manipulation as well as balancing workload.
     */
    private void initBlockApplicator() {
        Logger logger = plugin.getLogger();

        logger.info("Starting BlockApplicationManager...");

        if (blockApplicator != null) {
            logger.warning("BlockApplicationManager already running. Cancelling and running new manager.");
            blockApplicator.cancel();
        }

        if (CompatibilityProvider.isFaweEnabled()) {
            logger.info("Initializing FastAsyncBlockApplicationManager...");
            blockApplicator = new FastAsyncBlockApplicationManager(plugin);
        } else {
            logger.info("Initializing BukkitBlockApplicationManager...");
            blockApplicator = new BukkitBlockApplicationManager(plugin);
        }

        logger.info("Done initializing BlockApplicationManager.");

        logger.info("Starting BlockApplicationManager...");
        blockApplicator.start();
        logger.info("BlockApplicationManager started.");
    }

    /**
     * The mine task that ticks every mine once per second.
     */
    private void initMineTask() {
        Logger logger = plugin.getLogger();

        logger.info("Starting mine task...");

        if (minesTask != null) {
            logger.warning("Mine task already running. Overriding old mine task.");
            minesTask.cancel();
        }

        this.minesTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            for (CataMine cataMine : mines) {
                cataMine.tick();
            }

        }, 0L, 20L);
    }

    /**
     * Queues this region for reset. Resetting means filling the region with blocks
     * configured by the region.
     *
     * @param region the mine to reset
     */
    public void resetRegion(CataMineRegion region) {
        blockApplicator.queueForReset(region);
    }

    /**
     * Attempts to load all mines from a directory and returns it as a
     * List of CataMines. If the directory does not exist, is not a folder or
     * does not contain any files ending with '.yml', then an empty
     * ArrayList is returned. Otherwise, attempts to load mines from all files
     * ending with '.yml'. Note that only direct children files of the folder are
     * affected. Another folder inside the folder will be ignored.
     *
     * @param folder the path to load the mines from
     * @return A list of successfully loaded mines of direct children paths
     */
    public List<CataMine> getMinesFromFolder(Path folder) {
        Objects.requireNonNull(folder);
        List<CataMine> cataMines = new ArrayList<>();

        if (!Files.isDirectory(folder)) {
            plugin.getLogger().severe("Path is not a directory: " + folder);
            return cataMines;
        }

        try (Stream<Path> stream = Files.list(folder)) {
            cataMines = stream
                    .map(this::deserializeCataMineFromYaml)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            plugin.getLogger().severe("Failed loading directory: " + folder);
        }

        plugin.getLogger().info("Loaded " + cataMines.size() + " mines");
        return cataMines;
    }

    private Optional<CataMine> deserializeCataMineFromYaml(Path path) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(path.toFile());
        return deserializeCataMine(cfg);
    }

    private Optional<CataMine> deserializeCataMine(ConfigurationSection section) {
        CataMine mine = null;
        try {
            mine = AdvancedCataMine.deserialize(plugin, section);
        } catch (DeserializationException e) {
            // ignore
            e.printStackTrace();
        }
        return Optional.ofNullable(mine);
    }

    /**
     * Loads all mines of folder into this MineManager
     * {@link #getMinesFromFolder(Path)}
     *
     * @param folder the folder to load the mines from
     */
    public void loadMinesFromFolder(Path folder) {
        mines.clear();
        mines.addAll(getMinesFromFolder(folder));
    }

    public void callBlockBreak(BlockBreakEvent event) {

    }

    public void callBlockPlace(BlockPlaceEvent event) {

    }

    /**
     * Returns the mine with matching ID (name). Returns null if not present
     *
     * @param id ID or name of the mine
     * @return the mine if found, otherwise null
     */
    public Optional<CataMine> getMine(String id) {
        return mines.stream()
                .filter(cataMine -> cataMine.getName().equals(id))
                .findFirst();
    }

    /**
     * Returns the list containing all registered mines.
     *
     * @return the registered mines
     */
    public List<CataMine> getMines() {
        return mines;
    }

    /**
     * Returns a list of every mine name that is registered.
     *
     * @return a list of mine IDs
     */
    public List<String> getMineList() {
        return mines.stream().map(CataMine::getName).collect(Collectors.toList());
    }

    /**
     * Returns true if a mine with matching ID (name) is registered.
     *
     * @param id ID or name of the mine
     * @return true if mine with matching ID is registered
     */
    public boolean containsMine(String id) {
        return mines.stream().anyMatch(mine -> mine.getName().equals(id));
    }

    /**
     * {@link #containsMine(String)}
     *
     * @param mine the cata mine
     * @return true if a mine with matching ID is registered
     */
    public boolean containsMine(CataMine mine) {
        return containsMine(mine.getName());
    }

    /**
     * Registers a mine
     *
     * @param mine the mine to register
     * @throws IllegalArgumentException if the mine is already registered
     */
    public void registerMine(CataMine mine) {
        if (containsMine(mine))
            throw new IllegalArgumentException();
        mines.add(mine);
    }

    public void deleteMine(CataMine cataMine) throws IOException {
        mines.remove(cataMine);

        Files.deleteIfExists(plugin.getDataFolder().toPath().resolve("mines").resolve(cataMine.getName() + ".yml"));
    }

    public void saveMine(CataMine mine) throws IOException {
        Path file = plugin.getDataFolder().toPath().resolve("mines").resolve(mine.getName() + ".yml");
        FileConfiguration fileCfg = new YamlConfiguration();

        mine.serialize(fileCfg);

        fileCfg.save(file.toFile());
    }

    public Path getMinesPath() {
        return minesPath;
    }

    private static void createDirectoriesIfNotExists(Path path) throws IOException {
        if (Files.exists(path) && (Files.isDirectory(path) || Files.isSymbolicLink(path))) {
            return;
        }

        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException e) {
            // ignore
        }
    }
}
