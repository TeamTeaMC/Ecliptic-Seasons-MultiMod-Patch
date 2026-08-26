package com.teamtea.eclipticseasons_patch.mixin.modules.create;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons_patch.modules.create.CreateModelBridge;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "dev.engine_room.flywheel.lib.model.baked.BakedModelBufferer", remap = false)
public abstract class MixinBakedModelBufferer {
    @WrapOperation(
            method = "bufferBlocks",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/resources/model/BakedModel;"
            )
    )
    private static BakedModel eclipticseasons$useSeasonModel(
            BlockRenderDispatcher dispatcher,
            BlockState state,
            Operation<BakedModel> original,
            @Local(argsOnly = true) BlockAndTintGetter level,
            @Local BlockPos pos
    ) {
        return CreateModelBridge.resolve(
                level,
                pos,
                state,
                original.call(dispatcher, state)
        );
    }
}
