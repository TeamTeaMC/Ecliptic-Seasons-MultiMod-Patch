package com.teamtea.eclipticseasons_patch.mixin.modules.goety;


import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons_patch.modules.goety.GOETY;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public abstract class MixinSeasonBundle {

    // Forge not support
    // @Mixin({com.Polarice3.Goety.api.magic.ISpell.class})
    // public interface ISpell {
    //     @WrapOperation(at = {@At(value = "INVOKE", target = "Lcom/Polarice3/Goety/compat/serene_seasons/SSeasonsLoaded;isLoaded()Z")},
    //             remap = false,
    //             method = {"SoulCalculation"})
    //     private boolean es_patch$SoulCalculation_isLoaded(SSeasonsLoaded instance, Operation<Boolean> original) {
    //         if (GOETY.Config.enable.get()) {
    //             return true;
    //         }
    //         return original.call(instance);
    //     }
    //
    //     @WrapOperation(at = {@At(value = "INVOKE", target = "Lcom/Polarice3/Goety/compat/serene_seasons/SSeasonsIntegration;summonSnowVariant(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z")},
    //             remap = false,method = {"SoulCalculation"})
    //     private boolean es_patch$SoulCalculation_summonSnowVariant(Level level, BlockPos pos, Operation<Boolean> original) {
    //         if (GOETY.Config.enable.get()) {
    //             return EclipticSeasonsApi.getInstance().getPrecipitationAt(level, pos) == Biome.Precipitation.SNOW;
    //         }
    //         return original.call(level, pos);
    //     }
    // }

    @Mixin(com.Polarice3.Goety.compat.serene_seasons.SSeasonsLoaded.class)
    public static abstract class SSeasonsLoadedMixin {
        @Inject(require = 0, at = {@At(value = "HEAD")},
                remap = false, method = {"isLoaded"}, cancellable = true)
        private void es_patch$isLoaded(CallbackInfoReturnable<Boolean> cir) {
            if (GOETY.Config.fakeSeason.get()) {
                cir.setReturnValue(true);
            }
        }
    }

    @Mixin(com.Polarice3.Goety.compat.serene_seasons.SSeasonsIntegration.class)
    public static abstract class SSeasonsIntegrationMixin {
        @Inject(require = 0, at = {@At(value = "HEAD")},
                remap = false, method = {"summonSnowVariant"}, cancellable = true)
        private static void es_patch$summonSnowVariant(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
            if (GOETY.Config.fakeSeason.get()) {
                cir.setReturnValue(EclipticSeasonsApi.getInstance().getPrecipitationAt(level, pos) == Biome.Precipitation.SNOW);
            }
        }
    }

    // @Mixin({com.Polarice3.Goety.common.entities.ally.undead.zombie.ZombieServant.class,
    //         com.Polarice3.Goety.common.entities.ally.undead.skeleton.AbstractSkeletonServant.class})
    // public static abstract class ZombieServant {
    //     @WrapOperation(at = {@At(value = "INVOKE", target = "Lcom/Polarice3/Goety/compat/serene_seasons/SSeasonsLoaded;isLoaded()Z")},
    //             remap = false, method = {"getVariant"})
    //     private boolean es_patch$getVariant_season(SSeasonsLoaded instance, Operation<Boolean> original) {
    //         if (GOETY.Config.enable.get()) {
    //             return true;
    //         }
    //         return original.call(instance);
    //     }
    //
    //     @WrapOperation(at = {@At(value = "INVOKE", target = "Lcom/Polarice3/Goety/compat/serene_seasons/SSeasonsIntegration;summonSnowVariant(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z")},
    //             remap = false, method = {"getVariant"})
    //     private boolean es_patch$getVariant_summonSnowVariant(Level level, BlockPos pos, Operation<Boolean> original) {
    //         if (GOETY.Config.enable.get()) {
    //             return EclipticSeasonsApi.getInstance().getPrecipitationAt(level, pos) == Biome.Precipitation.SNOW;
    //         }
    //         return original.call(level, pos);
    //     }
    // }

}
