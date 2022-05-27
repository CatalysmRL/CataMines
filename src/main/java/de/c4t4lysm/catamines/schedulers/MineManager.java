package de.c4t4lysm.catamines.schedulers;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.utils.mine.AbstractCataMine;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MineManager extends BukkitRunnable {

    private static MineManager INSTANCE;
    private boolean stopTasks;

    private List<CuboidCataMine> mines;

    public MineManager() {
        INSTANCE = this;
        mines = loadMinesFromFiles();
        this.runTaskTimer(CataMines.getInstance(), 0, 20);
    }

    public static MineManager getInstance() {
        return INSTANCE;
    }

    public static boolean mineExists(String name) {
        return getInstance().getMineListNames().contains(name);
    }

    @Override
    public void run() {

        if (stopTasks) {
            return;
        }

        for (AbstractCataMine mine : mines) {
            mine.run();
        }
    }

    public boolean tasksStopped() {
        return stopTasks;
    }

    public void setStopTasks(boolean stopTasks) {
        this.stopTasks = stopTasks;
    }

    public List<CuboidCataMine> getMines() {
        return mines;
    }

    public CuboidCataMine getMine(String name) {
        for (CuboidCataMine mine : mines) {
            if (mine.getName().equals(name)) {
                return mine;
            }
        }

        return null;
    }

    public List<String> getMineListNames() {
        List<String> mineNames = new ArrayList<>();
        mines.forEach(cuboidCataMine -> mineNames.add(cuboidCataMine.getName()));
        return mineNames;
    }

    public List<CuboidCataMine> loadMinesFromFiles() {
        CataMines.getInstance().getLogger().info("Loading mines from files...");
        List<CuboidCataMine> mines = new ArrayList<>();

        File file = new File(CataMines.getInstance().getDataFolder() + "/mines");
        File[] files = file.listFiles(File::isFile);

        if (files == null) {
            return mines;
        }

        for (File value : files) {
            YamlConfiguration configurationFile = YamlConfiguration.loadConfiguration(value);
            if (configurationFile.contains("Mine") && configurationFile.get("Mine") != null) {
                CuboidCataMine mine = (CuboidCataMine) configurationFile.get("Mine");
                assert mine != null;
                mines.add(mine);
            }
        }

        return mines;
    }

    public void reloadMines() {
        this.mines = loadMinesFromFiles();
    }
}