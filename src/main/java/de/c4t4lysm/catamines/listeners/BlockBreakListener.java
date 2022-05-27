package de.c4t4lysm.catamines.listeners;

import com.sk89q.worldedit.math.BlockVector3;
import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        //TODO: Might want to revert to checking the gamemode
        if (event.isCancelled() || MineManager.getInstance().tasksStopped()) {
            return;
        }

        Location blockLocation = event.getBlock().getLocation();

        for (CuboidCataMine mine : MineManager.getInstance().getMines()) {
            if (mine.isStopped() || mine.getRegion() == null) {
                return;
            }
            if (blockLocation.getWorld().getName().equals(mine.getWorld())
                    && mine.getRegion().contains(BlockVector3.at(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ()))) {

                if (mine.getMinEfficiencyLvl() > 0 && !event.getPlayer().hasPermission("catamines.break")) {
                    Player player = event.getPlayer();
                    int efficiencyLvl = 0;
                    ItemStack itemInHand = player.getInventory().getItemInMainHand();
                    if (itemInHand.containsEnchantment(Enchantment.DIG_SPEED)) {
                        efficiencyLvl = itemInHand.getEnchantmentLevel(Enchantment.DIG_SPEED);
                    }

                    if (efficiencyLvl < mine.getMinEfficiencyLvl()) {
                        event.setCancelled(true);
                        player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getDefaultString("Tool-Too-Weak"));
                        return;
                    }
                }
                mine.handleBlockBreak(event);
            }
        }


    }


}
