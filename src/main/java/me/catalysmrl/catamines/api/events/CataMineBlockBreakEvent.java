package me.catalysmrl.catamines.api.events;

import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.mine.components.composition.CataMineBlock;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class CataMineBlockBreakEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean isCancelled;

    private final CataMine cataMine;
    private final CataMineRegion cataMineRegion;
    private final CataMineComposition cataMineComposition;
    private final CataMineBlock cataMineBlock;

    private final BlockBreakEvent blockBreakEvent;

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public CataMineBlockBreakEvent(CataMine cataMine, CataMineRegion cataMineRegion, CataMineComposition cataMineComposition, CataMineBlock cataMineBlock, BlockBreakEvent blockBreakEvent) {
        this.cataMine = cataMine;
        this.cataMineRegion = cataMineRegion;
        this.cataMineComposition = cataMineComposition;
        this.cataMineBlock = cataMineBlock;
        this.blockBreakEvent = blockBreakEvent;
    }

    public CataMine getCataMine() {
        return cataMine;
    }

    public CataMineRegion getCataMineRegion() {
        return cataMineRegion;
    }

    public CataMineComposition getCataMineComposition() {
        return cataMineComposition;
    }

    public CataMineBlock getCataMineBlock() {
        return cataMineBlock;
    }

    public BlockBreakEvent getBlockBreakEvent() {
        return blockBreakEvent;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}
