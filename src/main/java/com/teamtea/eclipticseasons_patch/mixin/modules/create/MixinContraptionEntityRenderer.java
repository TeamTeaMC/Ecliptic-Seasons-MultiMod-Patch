package com.teamtea.eclipticseasons_patch.mixin.modules.create;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.render.ClientContraption;
import com.simibubi.create.content.contraptions.render.ContraptionEntityRenderer;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import com.teamtea.eclipticseasons_patch.modules.create.CreateContraptionMapSlice;
import com.teamtea.eclipticseasons_patch.modules.create.CreateModelBridge;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ContraptionEntityRenderer.class, remap = false)
public abstract class MixinContraptionEntityRenderer {
    @WrapOperation(
            method = "buildStructureBuffer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/resources/model/BakedModel;"
            )
    )
    private static BakedModel eclipticseasons$useSeasonModel(
            BlockRenderDispatcher dispatcher,
            BlockState state,
            Operation<BakedModel> original,
            @Local(argsOnly = true) Contraption contraption,
            @Local(argsOnly = true) VirtualRenderWorld renderWorld,
            @Local BlockPos pos
    ) {
        BakedModel originalModel = original.call(dispatcher, state);
        ClientContraption clientContraption = contraption.getOrCreateClientContraptionLazy();
        CreateContraptionMapSlice slice = CreateContraptionMapSlice.getOrCreate(
                renderWorld,
                clientContraption,
                contraption
        );
        return CreateModelBridge.resolve(slice, pos, state, originalModel);
    }
}
