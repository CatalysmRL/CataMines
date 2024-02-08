package me.catalysmrl.catamines.commands.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCataCommand;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand extends AbstractCataCommand {
    public HelpCommand() {
        super("help", null, integer -> true, false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) throws CommandException {

        if (!sender.hasPermission("catamines.help")) {
            String versionString = "&7Running version &a" + plugin.getDescription().getVersion();
            String mineString = " ".repeat((versionString.length() - 11) / 2) + "&4&lC&ca&6t&ea&a&lM&bi&3n&9e&1s";

            sender.sendMessage("");
            Messages.sendColorized(sender, mineString);
            Messages.sendColorized(sender, versionString);
            sender.sendMessage("");
            return;
        }

        StringBuilder sb = new StringBuilder(100);
        sb.append(Message.HELP_HEADER.getMessage(plugin.getDescription().getVersion()));
        sb.append("\n");
        plugin.getCommandManager().getCommandMap().forEach((k, v) -> sb
                .append("&7&l- &6")
                .append(v.getUsage())
                .append("&7 - &f")
                .append(v.getDescription())
                .append("\n"));

        Messages.sendColorized(sender, sb.toString());
    }

    @Override
    public String getDescription() {
        return Message.HELP_DESCRIPTION.getMessage();
    }

    @Override
    public String getUsage() {
        return "/cm help";
    }
}
