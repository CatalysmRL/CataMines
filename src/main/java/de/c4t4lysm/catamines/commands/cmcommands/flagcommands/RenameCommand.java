package de.c4t4lysm.catamines.commands.cmcommands.flagcommands;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.commands.CommandInterface;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.File;

public class RenameCommand implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (args.length == 4) {

            CuboidCataMine cuboidCataMine = MineManager.getInstance().getMine(args[1]);
            if (MineManager.getInstance().getMineListNames().contains(args[3])) {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Exist"));
                return true;
            }

            File file = new File(CataMines.getInstance().getDataFolder() + "/mines/" + cuboidCataMine.getName() + ".yml");
            cuboidCataMine.setName(args[3]);
            file.renameTo(new File(CataMines.getInstance().getDataFolder() + "/mines/" + cuboidCataMine.getName() + ".yml"));
            cuboidCataMine.resetFiles();
            cuboidCataMine.save();
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Flag.Rename").replaceAll("%oldmine%", args[1]).replaceAll("%newmine%", args[3]));
        } else sender.sendMessage(CataMines.PREFIX + "§b/cm flag <mine> rename [name]");

        return true;
    }
}
