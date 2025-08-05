package me.catalysmrl.catamines.utils.worldedit;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.world.block.BaseBlock;
import org.bukkit.Bukkit;

/**
 * Utility class for parsing blocks
 */
public final class BaseBlockParser {

    private BaseBlockParser() {
    }

    public static BaseBlock parseInput(String input) throws InputParseException {
        ParserContext parserContext = new ParserContext();
        parserContext.setActor(BukkitAdapter.adapt(Bukkit.getConsoleSender()));
        return WorldEdit.getInstance().getBlockFactory().parseFromInput(input, parserContext);
    }

}
