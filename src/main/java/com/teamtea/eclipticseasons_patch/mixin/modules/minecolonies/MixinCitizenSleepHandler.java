package com.teamtea.eclipticseasons_patch.mixin.modules.minecolonies;


import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.entity.citizen.citizenhandlers.CitizenSleepHandler;
import com.teamtea.eclipticseasons.config.CommonConfig;
import com.teamtea.eclipticseasons_patch.modules.minecolonies.MCC;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({CitizenSleepHandler.class})
public abstract class MixinCitizenSleepHandler {


    @Shadow(remap = false)
    @Final
    private AbstractEntityCitizen citizen;

    @Expression("? <= ?")
    @Inject(at = {@At(value = "MIXINEXTRAS:EXPRESSION",
            // shift = At.Shift.AFTER,
            // by = 3,
            ordinal = 1)},
            method = {"shouldGoSleep"},
            remap = false)
    private void eclipticseasons$shouldGoSleep_checkTime(CallbackInfoReturnable<Boolean> cir,
                                             @Local(ordinal = 1) LocalDoubleRef localDoubleRef) {
        if (MCC.Config.enable.get() && CommonConfig.Season.daylightChange.get()) {
            localDoubleRef.set(localDoubleRef.get() + MCC.Hook.getGoSleepOffset(citizen.level()));
        }
    }


}
