package de.c4t4lysm.catamines.commands;

import de.c4t4lysm.catamines.CataMines;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CataMinesHelpCommand implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (!sender.hasPermission("catamines.help")) {
            sender.sendMessage("");
            sender.sendMessage("      §4§lC§ca§6t§ea§a§lM§bi§3n§9e§1s");
            sender.sendMessage("§7Running version §a" + CataMines.getInstance().getDescription().getVersion());
            sender.sendMessage("");
            return true;
        }

        sender.sendMessage(CataMines.PREFIX + "List of commands:");
        sender.sendMessage("");
        sender.sendMessage(
                "§b/cm §agui §6(mine) §7Opens the gui.\n" +
                        "§b/cm §alist          §7Lists every Mine.\n" +
                        "§b/cm §ainfo §6<mine> §7Displays information of the mine.\n" +
                        "§b/cm §acreate §6<mine> §7Creates a Mine.\n" +
                        "§b/cm §adelete §6<mine> §7Deletes a Mine.\n" +
                        "§b/cm §aset §6<mine> [block] (%) §7Sets the block (%) of the mine.\n" +
                        "§b/cm §aunset §6<mine> (block) §7Unsets the block of the mine.\n" +
                        "§b/cm §asetdelay §6<mine> [delay] §7Sets the reset timer in seconds.\n" +
                        "§b/cm §areset §6<mine> §7Resets a mine manually\n" +
                        "§b/cm §aflag §6<mine> <flag> §7Sets a flag for the mine.\n" +
                        "§b/cm §astart §6<mine> §7Starts the mine.\n" +
                        "§b/cm §astop §6<mine> §7Stops the mine.\n" +
                        "§b/cm §atp §6<mine> §7Teleports to the mine.\n" +
                        "§b/cm §asettp §6<mine> §7Sets the teleport of the mine.\n" +
                        "§b/cm §asetresettp §6<mine> §7Sets the reset teleport location of the mine.\n" +
                        "§b/cm §areload §6<mine> §7Reloads mines from config when it was manually changed.");

        return true;
    }
}
