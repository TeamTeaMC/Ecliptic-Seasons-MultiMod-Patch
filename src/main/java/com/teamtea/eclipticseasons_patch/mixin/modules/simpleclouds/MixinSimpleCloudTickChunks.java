package com.teamtea.eclipticseasons_patch.mixin.modules.simpleclouds;


import com.teamtea.eclipticseasons.config.CommonConfig;
import com.teamtea.eclipticseasons_patch.modules.simpleclouds.SC;
import dev.nonamecrackers2.simpleclouds.common.event.TickChunks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TickChunks.class})
public class MixinSimpleCloudTickChunks {

    @Inject(
            remap = false,
            method = {"rainAndSnowVanillaCompatibility"},
            at = @At(value = "HEAD"),
            cancellable = true)
    private static void eclipticseasons_multimodpatch$rainAndSnowVanillaCompatibility(CallbackInfo ci) {
        if (SC.Config.enable.get() && !CommonConfig.Temperature.snowDown.get()) {
            ci.cancel();
        }
    }
}
