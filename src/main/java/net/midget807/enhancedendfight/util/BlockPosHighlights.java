package net.midget807.enhancedendfight.util;

import net.minecraft.core.BlockPos;

import java.util.concurrent.ConcurrentLinkedQueue;

public class BlockPosHighlights {
    public static final ConcurrentLinkedQueue<BlockPos> BLOCKS = new ConcurrentLinkedQueue<BlockPos>();

    public static void add(BlockPos pos) {
        if (!BLOCKS.contains(pos)) {
            BLOCKS.add(pos);
        }
    }

    public static void remove(BlockPos pos) {
        BLOCKS.remove(pos);
    }

    public static void clear() {
        BLOCKS.clear();
    }
}
