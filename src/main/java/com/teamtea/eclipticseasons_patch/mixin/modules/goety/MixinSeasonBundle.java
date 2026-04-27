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

    @Mixin(com.Polarice3.Goety.compat.serene_seasons.SSeasonsLoaded.class)
    public static abstract class SSeasonsLoadedMixin {
        @Inject(require = 0, at = {@At(value = "HEAD")},
                remap = false, method = {"isLoaded"}, cancellable = true)
        private void eclipticseasons_multimodpatch$isLoaded(CallbackInfoReturnable<Boolean> cir) {
            if (GOETY.Config.fakeSeason.get()) {
                cir.setReturnValue(true);
            }
        }
    }

    @Mixin(com.Polarice3.Goety.compat.serene_seasons.SSeasonsIntegration.class)
    public static abstract class SSeasonsIntegrationMixin {
        @Inject(require = 0, at = {@At(value = "HEAD")},
                remap = false, method = {"summonSnowVariant"}, cancellable = true)
        private static void eclipticseasons_multimodpatch$summonSnowVariant(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
            if (GOETY.Config.fakeSeason.get()) {
                cir.setReturnValue(EclipticSeasonsApi.getInstance().getPrecipitationAt(level, pos) == Biome.Precipitation.SNOW);
            }
        }
    }

}
