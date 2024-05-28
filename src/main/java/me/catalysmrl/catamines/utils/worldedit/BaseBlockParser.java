package me.catalysmrl.catamines.utils.worldedit;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.util.nbt.TagStringIO;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;

import java.io.IOException;

/**
 * Utility class for parsing blocks
 */
public final class BaseBlockParser {

    private BaseBlockParser() {
    }

    public static BaseBlock asBaseBlock(String blockString) throws IOException {
        int nbtIndex = blockString.indexOf("{");
        if (nbtIndex == -1) {
            return BlockState.get(blockString).toBaseBlock();
        }

        BlockState blockState = BlockState.get(blockString.substring(0, nbtIndex));

        return blockState.toBaseBlock(TagStringIO.get().asCompound(blockString.substring(nbtIndex)));
    }

    public static BaseBlock parseInput(String input) throws InputParseException {
        WorldEdit.getInstance().getBlockFactory().parseFromInput(input, null);
        return null;
    }

}
