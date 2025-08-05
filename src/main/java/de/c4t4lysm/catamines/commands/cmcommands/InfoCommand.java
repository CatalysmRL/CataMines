package de.c4t4lysm.catamines.commands.cmcommands;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.commands.CommandInterface;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.Utils;
import de.c4t4lysm.catamines.utils.mine.components.CataMineResetMode;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class InfoCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (!sender.hasPermission("catamines.info")) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
            return true;
        }

        if (args.length == 2) {

            if (!MineManager.getInstance().getMineListNames().contains(args[1])) {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Not-Exist"));
                return true;
            }

            CuboidCataMine cuboidCataMine = MineManager.getInstance().getMine(args[1]);

            // Header of the info
            sender.sendMessage(CataMines.PREFIX + "Information of §c" + args[1] + "§7:\n" +
                    "--------------------------------");

            // Displays the region
            TextComponent component = new TextComponent("Unable to load region");
            if (cuboidCataMine.getRegion() != null) {
                String[] strings = Utils.regionToArray(cuboidCataMine.getRegion());
                String regionString = "§bRegion:" + "\n" +
                        "  §6World: §c" + strings[0] + "\n" +
                        "    §6p1:" + "\n" +
                        "      §7x1: §c" + strings[1] + "\n" +
                        "      §7y1: §c" + strings[2] + "\n" +
                        "      §7z1: §c" + strings[3] + "\n" +
                        "    §6p2:" + "\n" +
                        "      §7x2: §c" + strings[4] + "\n" +
                        "      §7y2: §c" + strings[5] + "\n" +
                        "      §7z2: §c" + strings[6];

                component = new TextComponent(regionString);
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aClick to teleport!")));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cm tp " + cuboidCataMine.getName()));
            }
            sender.spigot().sendMessage(component);

            sender.sendMessage("§7--------------------------------");
            sender.sendMessage("§bComposition:");
            cuboidCataMine.getBlocks().forEach(block -> sender.sendMessage("  " + ChatColor.GOLD + block.getBlockData().getAsString(true) + ChatColor.AQUA + " , " + ChatColor.RED + block.getChance() + "%"));

            sender.sendMessage("§7--------------------------------");
            sender.sendMessage(
                    "§bReset mode: §6" + cuboidCataMine.getResetMode().name() +
                            "\n§bReset delay: §c" + cuboidCataMine.getResetDelay() + " §7seconds." +
                            "\n§bReset percentage: §c" + cuboidCataMine.getResetPercentage() + "%" +
                            "\n§bReplace mode: §c" + cuboidCataMine.isReplaceMode() +
                            "\n§bTeleports players: §c" + cuboidCataMine.isTeleportPlayers() +
                            "\n§bCould run: §c" + cuboidCataMine.checkRunnable() +
                            "\n§bStopped: §c" + cuboidCataMine.isStopped() +
                            "\n§bResets in: §c" + (cuboidCataMine.isRunnable() && !cuboidCataMine.isStopped() ? cuboidCataMine.getCountdown() + " §7seconds" : "Mine is inactive"));


            sender.sendMessage("§7--------------------------------");
            sender.sendMessage("§6Warning configuration:");

            sender.sendMessage("§6Warnings enabled: §c" + cuboidCataMine.isWarn()
                    + "\n§6Warns globally: §c" + cuboidCataMine.isWarnGlobal() + "\n" +
                    "§6Warns in hotbar: §c" + cuboidCataMine.isWarnHotbar());

            sender.sendMessage("§7--------------------------------");
            sender.sendMessage("§6Messages:");

            String warnMessage = ChatColor.translateAlternateColorCodes('&', cuboidCataMine.getWarnMessage().replaceAll("%cm%", StringUtils.chop(CataMines.PREFIX)).replaceAll("%mine%", cuboidCataMine.getName()));
            Arrays.stream(warnMessage.split("/n")).forEach(sender::sendMessage);

            String resetMessage = ChatColor.translateAlternateColorCodes('&', cuboidCataMine.getResetMessage().replaceAll("%cm%", StringUtils.chop(CataMines.PREFIX)).replaceAll("%mine%", cuboidCataMine.getName()));
            Arrays.stream(resetMessage.split("/n")).forEach(sender::sendMessage);

            String hotbarMessageTime = ChatColor.translateAlternateColorCodes('&', cuboidCataMine.getWarnHotbarMessage(CataMineResetMode.TIME).replaceAll("%mine%", cuboidCataMine.getName()));
            sender.sendMessage(hotbarMessageTime);

            String hotbarMessagePer = ChatColor.translateAlternateColorCodes('&', cuboidCataMine.getWarnHotbarMessage(CataMineResetMode.PERCENTAGE).replaceAll("%mine%", cuboidCataMine.getName()));
            sender.sendMessage(hotbarMessagePer);

            sender.sendMessage("");

            String warnSeconds = cuboidCataMine.getWarnSeconds().toString();
            sender.sendMessage("§6Warns on seconds: §e" + warnSeconds.substring(1, warnSeconds.length() - 1)
                    + "\n§6Warn distance: §e" + cuboidCataMine.getWarnDistance());


        } else sender.sendMessage(CataMines.PREFIX + "/cm info <mine>");
        return true;
    }
}
