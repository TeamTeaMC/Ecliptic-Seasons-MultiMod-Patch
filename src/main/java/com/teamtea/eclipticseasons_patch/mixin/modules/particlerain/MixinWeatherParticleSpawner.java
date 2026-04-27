package com.teamtea.eclipticseasons_patch.mixin.modules.particlerain;


import com.leclowndu93150.particlerain.WeatherParticleSpawner;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.client.core.ClientWeatherChecker;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons_patch.modules.particlerain.PR;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin({WeatherParticleSpawner.class})
public abstract class MixinWeatherParticleSpawner {


    @WrapOperation(at = {@At(
            remap = false,
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Holder;")},
            method = "update")
    private static Holder<Biome> eclipticseasons_multimodpatch$update_surfaceBiome(ClientLevel instance, BlockPos pos, Operation<Holder<Biome>> original) {
        return MapChecker.getSurfaceBiome(instance, pos);
    }

    @WrapOperation(at = {@At(
            remap = false,
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")},
            method = "spawnParticle")
    private static Biome.Precipitation eclipticseasons_multimodpatch$spawnParticle_fix(Biome instance, BlockPos pos, Operation<Biome.Precipitation> original, @Local(argsOnly = true) ClientLevel level, @Local(argsOnly = true) Holder<Biome> biomeHolder) {
        return PR.Hook.getPrecipitation(instance, pos, level, biomeHolder);
    }

    @WrapOperation(at = {@At(
            remap = false,
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Holder;")},
            method = "getBiomeSound")
    private static Holder<Biome> eclipticseasons_multimodpatch$getBiomeSound_surfaceBiome(ClientLevel instance, BlockPos pos, Operation<Holder<Biome>> original) {
        return MapChecker.getSurfaceBiome(instance, pos);
    }

    @WrapOperation(at = {@At(
            remap = false,
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")},
            method = "getBiomeSound")
    private static Biome.Precipitation eclipticseasons_multimodpatch$getBiomeSound_fix(Biome instance, BlockPos pos, Operation<Biome.Precipitation> original, @Local Holder<Biome> biomeHolder) {
        return PR.Hook.getPrecipitation(instance, pos, Minecraft.getInstance().level, biomeHolder);
    }

    @WrapOperation(at = {@At(
            remap = false,
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V")},
            method = "spawnParticle")
    private static void eclipticseasons_multimodpatch$spawnParticle_fixAmount(ClientLevel instance, ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original, @Local Holder<Biome> biomeHolder) {
        if (PR.Config.changeAmount.get()) {
            float amount = ClientWeatherChecker.modifyRainAmount(1, instance);
            if (amount < 1) {
                if (instance.random.nextFloat() >= amount) return;
            }
        }
        original.call(instance, particleData, x, y, z, xSpeed, ySpeed, zSpeed);
    }

}
