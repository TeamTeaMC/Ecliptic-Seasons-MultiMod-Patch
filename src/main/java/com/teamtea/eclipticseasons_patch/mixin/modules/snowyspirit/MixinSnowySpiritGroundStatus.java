package com.teamtea.eclipticseasons_patch.mixin.modules.snowyspirit;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons_patch.modules.snowyspirit.SS;
import net.mehvahdjukaar.snowyspirit.common.entity.GroundStatus;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({GroundStatus.class})
public abstract class MixinSnowySpiritGroundStatus {

    @ModifyExpressionValue(at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;")},
            method = {"computeFriction"})
    private static BlockState eclipticseasons_multimodpatch$computeFriction(BlockState original,
                                                              @Local(argsOnly = true) Entity sled) {
        if (SS.Config.enable.get() && EclipticSeasonsApi.getInstance().isSnowyBlock(sled.level(), original, sled.getOnPos()))
            original = Blocks.SNOW.defaultBlockState();
        return original;
    }

}
