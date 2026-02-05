package com.teamtea.eclipticseasons_patch.mixin.modules.particlerain;


import com.leclowndu93150.particlerain.WeatherParticleSpawner;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons_patch.modules.particlerain.PR;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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
            remap = false,
            method = "update")
    private static Holder<Biome> eclipticseasons$update_surfaceBiome(ClientLevel instance, BlockPos pos, Operation<Holder<Biome>> original) {
        if (PR.Config.enable.get() && EclipticSeasonsApi.getInstance().hasLocalWeather(instance))
            return MapChecker.getSurfaceBiome(instance, pos);
        return original.call(instance, pos);
    }

    @WrapOperation(at = {@At(
            remap = false,
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")},
            remap = false,
            method = "spawnParticle")
    private static Biome.Precipitation eclipticseasons$spawnParticle_fix(Biome instance, BlockPos pos, Operation<Biome.Precipitation> original, @Local(argsOnly = true) ClientLevel level, @Local(argsOnly = true) Holder<Biome> biomeHolder) {
        if (!PR.Config.enable.get()) return original.call(instance, pos);
        return PR.Hook.getPrecipitation(instance, pos, level, biomeHolder);
    }

    @WrapOperation(at = {@At(
            remap = false,
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Holder;")},
            remap = false,
            method = "getBiomeSound")
    private static Holder<Biome> eclipticseasons$getBiomeSound_surfaceBiome(ClientLevel instance, BlockPos pos, Operation<Holder<Biome>> original) {
        if (PR.Config.enable.get() && EclipticSeasonsApi.getInstance().hasLocalWeather(instance))
            return MapChecker.getSurfaceBiome(instance, pos);
        return original.call(instance, pos);
    }

    @WrapOperation(at = {@At(
            remap = false,
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")},
            remap = false,
            method = "getBiomeSound")
    private static Biome.Precipitation eclipticseasons$getBiomeSound_fix(Biome instance, BlockPos pos, Operation<Biome.Precipitation> original, @Local Holder<Biome> biomeHolder) {
        if (!PR.Config.enable.get()) return original.call(instance, pos);
        return PR.Hook.getPrecipitation(instance, pos, Minecraft.getInstance().level, biomeHolder);
    }

}
