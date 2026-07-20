package com.teamtea.eclipticseasons_patch.mixin.modules.grassiergrass;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.api.misc.client.IMapSlice;
import com.teamtea.eclipticseasons.client.core.ExtraModelManager;
import com.teamtea.eclipticseasons.client.util.ClientCon;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons_patch.modules.grassiergrass.GG;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "com.leonardoinc22.shortgrass.client.render.GrassSectionBuilder")
public abstract class MixinGrassSectionBuilder {


    @Unique
    private static ThreadLocal<RandomSource> eclipticseasons_multimodpatch$randomSourceThreadLocal = ThreadLocal.withInitial(RandomSource::create);

    @ModifyExpressionValue(at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/color/block/BlockColors;getColor(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;I)I")},
            method = {"emitSection"},
            remap = false)
    private static int eclipticseasons_multimodpatch$emitSection$getColor(int original,
                                                                          @Local(name = "tintState") BlockState state,
                                                                          @Local(argsOnly = true) BlockAndTintGetter level,
                                                                          @Local(argsOnly = true) ClientLevel climateLevel,
                                                                          @Local(name = "pos") BlockPos.MutableBlockPos pos) {
        if (GG.Config.enable.get()
                && level instanceof IMapSlice mapSlice) {
            if (ExtraModelManager.maySnowyAt(
                    climateLevel,
                    mapSlice,
                    state,
                    pos,
                    eclipticseasons_multimodpatch$randomSourceThreadLocal.get(),
                    state.getSeed(pos))) {
                original = GG.Hook.blendColor(original, GG.Hook.getSnowColor(),
                        WeatherManager.getSnowDepthAtBiome(climateLevel, MapChecker.idToBiome(climateLevel, mapSlice.getSurfaceFaceBiomeId(pos)).value()));
            }
        }
        return original;
    }

    @ModifyExpressionValue(at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/color/block/BlockColors;getColor(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;I)I")},
            method = {"emitPlantQuads"},
            remap = false)
    private static int eclipticseasons_multimodpatch$emitPlantQuads$getColor(int original,
                                                                             @Local(argsOnly = true) BlockState state,
                                                                             @Local(argsOnly = true) BlockAndTintGetter level,
                                                                             @Local(argsOnly = true) BlockPos pos) {
        if (GG.Config.enable.get()
                && level instanceof IMapSlice mapSlice) {
            Level climateLevel = ClientCon.getUseLevel();
            if (ExtraModelManager.maySnowyAt(
                    climateLevel,
                    mapSlice,
                    state,
                    pos,
                    eclipticseasons_multimodpatch$randomSourceThreadLocal.get(),
                    state.getSeed(pos))) {
                original = GG.Hook.blendColor(original, GG.Hook.getSnowColor(),
                        WeatherManager.getSnowDepthAtBiome(climateLevel, MapChecker.idToBiome(climateLevel, mapSlice.getSurfaceFaceBiomeId(pos)).value()));
            }
        }
        return original;
    }
}
