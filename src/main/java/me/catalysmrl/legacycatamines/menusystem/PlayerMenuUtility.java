package me.catalysmrl.legacycatamines.menusystem;

import me.catalysmrl.catamines.mine.components.composition.CataMineBlock;
import me.catalysmrl.legacycatamines.mine.components.CataMineLootItem;
import me.catalysmrl.legacycatamines.mine.mines.CuboidCataMine;
import org.bukkit.entity.Player;

public class PlayerMenuUtility {

    private Player owner;

    private CuboidCataMine cuboidCataMine;
    private CataMineBlock block;
    private CataMineLootItem item;

    private Menu menu;
    private int mineListMenuPage;

    public PlayerMenuUtility(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public CuboidCataMine getMine() {
        return cuboidCataMine;
    }

    public void setMine(CuboidCataMine cuboidCataMine) {
        this.cuboidCataMine = cuboidCataMine;
    }

    public CataMineBlock getBlock() {
        return block;
    }

    public void setBlock(CataMineBlock block) {
        this.block = block;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getMineListMenuPage() {
        return mineListMenuPage;
    }

    public void setMineListMenuPage(int mineListMenuPage) {
        this.mineListMenuPage = mineListMenuPage;
    }

    public CataMineLootItem getItem() {
        return item;
    }

    public void setItem(CataMineLootItem item) {
        this.item = item;
    }
}
