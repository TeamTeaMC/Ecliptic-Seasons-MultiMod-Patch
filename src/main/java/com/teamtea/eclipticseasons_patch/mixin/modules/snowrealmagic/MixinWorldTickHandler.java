package com.teamtea.eclipticseasons_patch.mixin.modules.snowrealmagic;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.constant.tag.ClimateTypeBiomeTags;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons.common.handler.CustomRandomTickHandler;
import com.teamtea.eclipticseasons.config.CommonConfig;
import com.teamtea.eclipticseasons_patch.modules.snowrealmagic.SRM;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.snow.WorldTickHandler;

@Mixin({WorldTickHandler.class})
public abstract class MixinWorldTickHandler {


    @WrapOperation(at = {@At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Holder;")},
            method = {"tick"})
    private static Holder<Biome> eclipticseasons_multimodpatch$tick_getESBiome(ServerLevel instance, BlockPos pos, Operation<Holder<Biome>> original) {
        if (SRM.Config.enable.get()) {
            return MapChecker.getSurfaceBiome(instance, pos);
        }
        return original.call(instance, pos);
    }

    @WrapOperation(at = {@At(value = "INVOKE", target = "Lsnownee/snow/util/CommonProxy;coldEnoughToSnow(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Holder;)Z")},
            remap = false, method = {"tick"})
    private static boolean eclipticseasons_multimodpatch$tick_coldEnoughToSnow(LevelReader level, BlockPos pos, Holder<Biome> biome, Operation<Boolean> original) {
        if (SRM.Config.enable.get() && level instanceof ServerLevel serverLevel) {
            var es_snowStatus = EclipticSeasonsApi.getInstance().getCurrentPrecipitationAt(serverLevel, pos);
            return es_snowStatus == Biome.Precipitation.SNOW
                    || (CommonConfig.Temperature.waterFreezesInFrozenBiomes.get() && CustomRandomTickHandler.isColdBiome(serverLevel,biome));
        }
        return original.call(level, pos, biome);
    }

    @Inject(at = {@At(value = "INVOKE", target = "Lsnownee/snow/WorldTickHandler;doSnow(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos$MutableBlockPos;)Z")},
            method = {"tick"},
            remap = false,
            cancellable = true)
    private static void eclipticseasons_multimodpatch$tick_shouldSkip(ServerLevel level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (SRM.Config.enable.get() && !CommonConfig.Temperature.snowDown.get()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(at = {@At(value = "INVOKE", target = "Lsnownee/snow/WorldTickHandler;doMelt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos$MutableBlockPos;)V")},
            method = {"tick"},
            remap = false,
            cancellable = true)
    private static void eclipticseasons_multimodpatch$tick_shouldSkip_melt(ServerLevel level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (SRM.Config.enable.get() && !CommonConfig.Temperature.iceMelt.get()) {
            cir.setReturnValue(false);
        }
    }


    @WrapOperation(at = {@At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isRaining()Z")},
            method = {"doSnow"})
    private static boolean eclipticseasons_multimodpatch$doSnow_checkSnow(ServerLevel instance, Operation<Boolean> original, @Local(argsOnly = true) BlockPos.MutableBlockPos pos) {
        if (SRM.Config.enable.get()) {
            var es_snowStatus = EclipticSeasonsApi.getInstance().getCurrentPrecipitationAt(instance, pos);
            return es_snowStatus == Biome.Precipitation.SNOW;
        }
        return original.call(instance);
    }

    @WrapOperation(at = {@At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Holder;")},
            method = {"doMelt"})
    private static Holder<Biome> eclipticseasons_multimodpatch$doMelt_getBiome(ServerLevel instance, BlockPos pos, Operation<Holder<Biome>> original) {
        if (SRM.Config.enable.get()) {
            return MapChecker.getSurfaceBiome(instance, pos);
        }
        return original.call(instance, pos);
    }
}
