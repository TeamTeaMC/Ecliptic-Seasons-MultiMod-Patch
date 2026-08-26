package com.teamtea.eclipticseasons_patch.modules.create;

import com.teamtea.eclipticseasons.client.core.ExtraModelManager;
import com.teamtea.eclipticseasons.client.model.SnowySeasonBakeModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public final class CreateModelBridge {
    private static final ThreadLocal<BlockPos.MutableBlockPos> CHECK_POS =
            ThreadLocal.withInitial(BlockPos.MutableBlockPos::new);
    private static final ThreadLocal<RandomSource> RANDOM =
            ThreadLocal.withInitial(RandomSource::createNewThreadLocalInstance);

    private CreateModelBridge() {
    }

    public static BakedModel resolve(
            BlockAndTintGetter view,
            BlockPos pos,
            BlockState state,
            BakedModel original
    ) {
        if (!CreatePatch.Config.enable.get()
                || !(view instanceof CreateContraptionMapSlice)) {
            return original;
        }

        long seed = state.getSeed(pos);
        RandomSource random = RANDOM.get();
        random.setSeed(seed);

        BakedModel extra = ExtraModelManager.findModel(
                view,
                pos,
                state,
                random,
                seed,
                CHECK_POS.get()
        );
        if (extra == null) {
            return original;
        }

        if (ExtraModelManager.isModelReplaceable(state, view, pos, extra)) {
            return extra;
        }

        return new SnowySeasonBakeModel<>(
                original,
                extra,
                ExtraModelManager.getRenderType(state)
        );
    }
}
