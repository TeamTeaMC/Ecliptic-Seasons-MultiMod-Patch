package com.teamtea.eclipticseasons_patch.mixin.modules.simpleclouds;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons_patch.modules.simpleclouds.SC;
import dev.nonamecrackers2.simpleclouds.client.renderer.WorldEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({WorldEffects.class})
public class MixinSimpleCloudWorldEffects {

    @WrapOperation(
            method = {"tick"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Holder;")
    )
    private Holder<Biome> eclipticseasons_multimodpatch$tick_getBiome(ClientLevel instance, BlockPos pos, Operation<Holder<Biome>> original) {
        return SC.Config.enable.get() ?
                MapChecker.getSurfaceBiome(instance, pos) :
                original.call(instance, pos);
    }

    @WrapOperation(
            method = {"tick"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")
    )
    private Biome.Precipitation eclipticseasons_multimodpatch$tick_getPrecipitationAt(Biome biome, BlockPos pos, Operation<Biome.Precipitation> original) {
        return SC.Config.enable.get() ?
                EclipticSeasonsApi.getInstance().getCurrentPrecipitationAt(Minecraft.getInstance().level, pos) :
                original.call(biome, pos);
    }
}
