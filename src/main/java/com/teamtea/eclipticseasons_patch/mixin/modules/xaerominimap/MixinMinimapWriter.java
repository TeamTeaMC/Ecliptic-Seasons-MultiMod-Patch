package com.teamtea.eclipticseasons_patch.mixin.modules.xaerominimap;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.common.misc.MapColorReplacer;
import com.teamtea.eclipticseasons_patch.modules.xaerominimap.XMM;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xaero.common.minimap.write.MinimapWriter;

@Mixin({MinimapWriter.class})
public abstract class MixinMinimapWriter {

    @ModifyExpressionValue(at = {@At(value = "INVOKE", target = "Lxaero/common/minimap/write/MinimapWriter;addBlockColorMultipliers(ILnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)I")},
            method = {"calculateBlockColors"},
            remap = false)
    private int eclipticseasons_multimodpatch$getPixelColours3(int original, @Local(argsOnly = true) Level world, @Local BlockState state, @Local(argsOnly = true) BlockPos.MutableBlockPos mutableGlobalPos) {
        if (XMM.Config.enable.get()
                && MapColorReplacer.getTopSnowColor(world, state, mutableGlobalPos) instanceof MapColor mapColor) {
            return mapColor.col;
        }
        return original;
    }
}
