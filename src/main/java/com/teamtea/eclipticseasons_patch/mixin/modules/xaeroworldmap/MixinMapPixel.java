package com.teamtea.eclipticseasons_patch.mixin.modules.xaeroworldmap;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.common.misc.MapColorReplacer;
import com.teamtea.eclipticseasons_patch.modules.xaeroworldmap.XWM;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xaero.map.region.MapPixel;

@Mixin({MapPixel.class})
public abstract class MixinMapPixel {

    @Shadow(remap = false)
    protected BlockState state;


    @ModifyExpressionValue(at = {@At(value = "INVOKE", target = "Lxaero/map/MapWriter;loadBlockColourFromTexture(Lnet/minecraft/world/level/block/state/BlockState;ZLnet/minecraft/world/level/Level;Lnet/minecraft/core/Registry;Lnet/minecraft/core/BlockPos;)I")},
            method = {"getPixelColours"},
            remap = false)
    private int eclipticseasons$getPixelColours2(int original, @Local(argsOnly = true) Level world, @Local(argsOnly = true) BlockPos.MutableBlockPos mutableGlobalPos) {
        if (XWM.Config.enable.get()
                && MapColorReplacer.getTopSnowColor(world, state, mutableGlobalPos) instanceof MapColor mapColor) {
            return mapColor.col;
        }
        return original;
    }

    @ModifyExpressionValue(at = {@At(value = "INVOKE", target = "Lxaero/map/biome/BlockTintProvider;getBiomeColor(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;ZLxaero/map/region/MapTile;I)I")},
            method = {"getPixelColours"},
            remap = false)
    private int eclipticseasons$getPixelColours3(int original, @Local(argsOnly = true) Level world, @Local(argsOnly = true) BlockPos.MutableBlockPos mutableGlobalPos) {
        if (XWM.Config.enable.get()
                && MapColorReplacer.getTopSnowColor(world, state, mutableGlobalPos) instanceof MapColor mapColor) {
            return mapColor.col;
        }
        return original;
    }
}
