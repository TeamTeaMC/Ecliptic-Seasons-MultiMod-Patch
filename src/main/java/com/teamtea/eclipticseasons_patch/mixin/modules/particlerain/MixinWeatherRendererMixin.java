package com.teamtea.eclipticseasons_patch.mixin.modules.particlerain;


import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamtea.eclipticseasons.client.core.ClientWeatherChecker;
import com.teamtea.eclipticseasons_patch.modules.particlerain.PR;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(value = {LevelRenderer.class}, priority = 1500)
public abstract class MixinWeatherRendererMixin {


    @TargetHandler(
            mixin = "com.leclowndu93150.particlerain.mixin.LevelRendererMixin",
            name = "tickRain"
    )
    @WrapOperation(at = {@At(
            remap = false,
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V")},
            method = "@MixinSquared:Handler")
    private void eclipticseasons_multimodpatch$tickRain_fixSoundAmount(ClientLevel instance, BlockPos pos, SoundEvent sound, SoundSource category, float volume, float pitch, boolean distanceDelay, Operation<Void> original) {
        if (PR.Config.changeAmount.get()) {
            volume = ClientWeatherChecker.modifyVolume(sound, volume, instance);
        }
        original.call(instance, pos, sound, category, volume, pitch, distanceDelay);
    }

}
