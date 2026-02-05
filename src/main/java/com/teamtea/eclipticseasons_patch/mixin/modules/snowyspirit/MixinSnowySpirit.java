package com.teamtea.eclipticseasons_patch.mixin.modules.snowyspirit;


import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.config.CommonConfig;
import com.teamtea.eclipticseasons_patch.modules.snowyspirit.SS;
import net.mehvahdjukaar.snowyspirit.SnowySpirit;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({SnowySpirit.class})
public abstract class MixinSnowySpirit {

    @Inject(at = {@At(value = "HEAD")},
            method = {"isChristmasSeason"},
            remap = false, cancellable = true)
    private static void eclipticseasons$isChristmasSeason(Level level, CallbackInfoReturnable<Boolean> cir) {
        if (SS.Config.enable.get() && SS.Config.specialTime.get())
            cir.setReturnValue(CommonConfig.castSolarTerms(SS.Config.snowyspirit_winters.get()).contains(EclipticSeasonsApi.getInstance().getSolarTerm(level)));
    }

}
