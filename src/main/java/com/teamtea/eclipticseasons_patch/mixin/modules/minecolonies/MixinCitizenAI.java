package com.teamtea.eclipticseasons_patch.mixin.modules.minecolonies;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.minecolonies.core.entity.ai.workers.CitizenAI;
import com.teamtea.eclipticseasons.config.CommonConfig;
import com.teamtea.eclipticseasons_patch.modules.minecolonies.MCC;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({CitizenAI.class})
public abstract class MixinCitizenAI {


    @WrapOperation(at = {@At(value = "INVOKE", target = "Lcom/minecolonies/api/util/WorldUtil;isPastTime(Lnet/minecraft/world/level/Level;I)Z")},
            method = {"calculateNextState"},
            remap = false)
    private boolean eclipticseasons_multimodpatch$calculateNextState_isPastTime(Level world, int pastTime, Operation<Boolean> original) {
        if (MCC.Config.enable.get() && CommonConfig.Season.daylightChange.get()) {
            return MCC.Hook.isDay(world);
        }
        return original.call(world, pastTime);
    }


}
