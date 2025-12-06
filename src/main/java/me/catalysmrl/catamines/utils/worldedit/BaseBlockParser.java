package me.catalysmrl.catamines.utils.worldedit;

import java.util.Set;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.world.block.BaseBlock;

/**
 * Utility class for parsing blocks
 */
public final class BaseBlockParser {

    private BaseBlockParser() {
    }

    public static BaseBlock parse(String input) throws InputParseException, IllegalArgumentException {
        
        ParserContext ctx = new ParserContext();
        ctx.setRestricted(false);
        ctx.setPreferringWildcard(false);
        ctx.setTryLegacy(true);
        ctx.setWorld(null);
        ctx.setSession(null);

        Set<BaseBlock> blocks = WorldEdit.getInstance()
                .getBlockFactory()
                .parseFromListInput(input, ctx);

        if (blocks.isEmpty()) {
            throw new IllegalArgumentException("Invalid block");
        }

        if (blocks.size() > 1) {
            throw new IllegalArgumentException("Multiple blocks found");
        }

        return blocks.iterator().next();
    }

}
