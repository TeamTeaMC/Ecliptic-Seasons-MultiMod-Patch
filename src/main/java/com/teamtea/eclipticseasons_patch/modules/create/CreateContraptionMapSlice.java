package com.teamtea.eclipticseasons_patch.modules.create;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.render.ClientContraption;
import com.simibubi.create.content.contraptions.render.ClientContraption.RenderedBlocks;
import com.simibubi.create.foundation.utility.worldWrappers.WrappedBlockAndTintGetter;
import com.teamtea.eclipticseasons.api.misc.client.IMapSlice;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons.common.core.map.SnowyRemover;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Adapts Create's local contraption render world to Ecliptic Seasons' map-slice
 * contract. Neighbour states stay local, while biome and snow queries use the
 * contraption's real world position at model-bake time.
 */
public final class CreateContraptionMapSlice extends WrappedBlockAndTintGetter implements IMapSlice {
    private static final Map<ClientContraption, CreateContraptionMapSlice> CACHE =
            Collections.synchronizedMap(new WeakHashMap<>());

    private final Contraption contraption;
    private final RenderedBlocks renderedBlocks;
    private final int structureVersion;
    private final Long2IntMap blockHeights = new Long2IntOpenHashMap();
    private final Long2IntMap solidHeights = new Long2IntOpenHashMap();
    private final Long2IntMap fakeSnowLevels = new Long2IntOpenHashMap();
    private final BlockPos.MutableBlockPos modelCheckPos = new BlockPos.MutableBlockPos();

    private CreateContraptionMapSlice(
            BlockAndTintGetter wrapped,
            Contraption contraption,
            RenderedBlocks renderedBlocks,
            int structureVersion
    ) {
        super(wrapped);
        this.contraption = contraption;
        this.renderedBlocks = renderedBlocks;
        this.structureVersion = structureVersion;
        blockHeights.defaultReturnValue(Integer.MIN_VALUE);
        solidHeights.defaultReturnValue(Integer.MIN_VALUE);
        fakeSnowLevels.defaultReturnValue(NONE_CHECK_FAKE_SNOW_LEVEL);
        buildHeightMaps();
    }

    public static CreateContraptionMapSlice getOrCreate(
            BlockAndTintGetter wrapped,
            ClientContraption clientContraption,
            Contraption contraption
    ) {
        int version = clientContraption.structureVersion();
        CreateContraptionMapSlice cached = CACHE.get(clientContraption);
        if (cached != null
                && cached.structureVersion == version
                && cached.wrapped == wrapped) {
            return cached;
        }

        CreateContraptionMapSlice created = new CreateContraptionMapSlice(
                wrapped,
                contraption,
                clientContraption.getRenderedBlocks(),
                version
        );
        CACHE.put(clientContraption, created);
        return created;
    }

    private void buildHeightMaps() {
        for (BlockPos pos : renderedBlocks.positions()) {
            BlockState state = renderedBlocks.lookup().apply(pos);
            long column = columnKey(pos);
            if (!state.isAir()) {
                blockHeights.put(column, Math.max(blockHeights.get(column), pos.getY()));
            }
            if (state.blocksMotion()) {
                solidHeights.put(column, Math.max(solidHeights.get(column), pos.getY()));
            }
        }
    }

    private static long columnKey(BlockPos pos) {
        return ((long) pos.getX() << 32) ^ (pos.getZ() & 0xffffffffL);
    }

    private Level level() {
        return contraption.entity.level();
    }

    private BlockPos toWorldPos(BlockPos localPos) {
        return BlockPos.containing(
                contraption.entity.toGlobalVector(Vec3.atCenterOf(localPos), 1.0F)
        );
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return renderedBlocks.lookup().apply(pos);
    }

    @Override
    public int getBlockHeight(BlockPos pos) {
        int height = blockHeights.get(columnKey(pos));
        return height == Integer.MIN_VALUE ? pos.getY() : height;
    }

    @Override
    public int getSolidBlockHeight(BlockPos pos) {
        int height = solidHeights.get(columnKey(pos));
        return height == Integer.MIN_VALUE ? pos.getY() : height;
    }

    @Override
    public int getSurfaceFaceBiomeId(BlockPos pos) {
        BlockPos worldPos = toWorldPos(pos);
        return MapChecker.biomeToId(
                level(),
                MapChecker.getSurfaceBiome(level(), worldPos).value()
        );
    }

    @Override
    public int getSnowyStatus(BlockPos pos) {
        // A contraption has no persistent snowy-status attachment. Its look is
        // baked from the climate at the assembly/render position.
        return SnowyRemover.SNOWY;
    }

    @Override
    public boolean isSnowyBlock(BlockPos pos) {
        Level level = level();
        BlockPos worldPos = toWorldPos(pos);
        BlockState state = getBlockState(pos);
        return MapChecker.shouldSnowAtBiome(
                level,
                MapChecker.getSurfaceBiome(level, worldPos).value(),
                state,
                level.getRandom(),
                state.getSeed(pos),
                worldPos
        );
    }

    @Override
    public BlockPos.MutableBlockPos getModelCheckPos() {
        return modelCheckPos;
    }

    @Override
    public void setLevelForFakeSnow(long pos, int level) {
        fakeSnowLevels.put(pos, level);
    }

    @Override
    public int getLevelForFakeSnow(long pos) {
        return fakeSnowLevels.get(pos);
    }

    @Override
    public void forceMapSliceUpdate() {
        fakeSnowLevels.clear();
    }
}
