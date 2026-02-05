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

    //@Shadow(remap = false)
    //protected BlockState state;

    //@Definition(id = "colours", field = "Lxaero/map/settings/ModSettings;colours:I")
    //@Expression("? == 0")
    //@ModifyExpressionValue(at = {@At(value = "MIXINEXTRAS:EXPRESSION")},
    //        method = {"getPixelColours"},
    //        remap = false)
    //private boolean eclipticseasons$getPixelColours(boolean original, @Local(argsOnly = true) Level world, @Local(argsOnly = true) BlockPos.MutableBlockPos mutableGlobalPos) {
    //    if (XWM.Config.enable.get()
    //            && EclipticSeasonsApi.getInstance().isSnowyBlock(world, state, mutableGlobalPos)) {
    //        return false;
    //    }
    //    return original;
    //}

    //@ModifyExpressionValue(at = {@At(value = "INVOKE", target = "Lxaero/common/minimap/write/MinimapWriter;loadBlockColourFromTexture(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/BlockPos;Z)I")},
    //        method = {"calculateBlockColors"},
    //        remap = false)
    //private int eclipticseasons$getPixelColours2(int original, @Local(argsOnly = true) Level world, @Local(argsOnly = true) BlockPos.MutableBlockPos mutableGlobalPos) {
    //    if (XWM.Config.enable.get()
    //            && MapColorReplacer.getTopSnowColor(world, state, mutableGlobalPos) instanceof MapColor mapColor) {
    //        return mapColor.col;
    //    }
    //    return original;
    //}

    @ModifyExpressionValue(at = {@At(value = "INVOKE", target = "Lxaero/common/minimap/write/MinimapWriter;addBlockColorMultipliers(ILnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)I")},
            method = {"calculateBlockColors"},
            remap = false)
    private int eclipticseasons$getPixelColours3(int original, @Local(argsOnly = true) Level world, @Local BlockState state, @Local(argsOnly = true) BlockPos.MutableBlockPos mutableGlobalPos) {
        if (XMM.Config.enable.get()
                && MapColorReplacer.getTopSnowColor(world, state, mutableGlobalPos) instanceof MapColor mapColor) {
            return mapColor.col;
        }
        return original;
    }
}
