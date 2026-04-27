package com.teamtea.eclipticseasons_patch.mixin.modules.ambientsounds;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons_patch.modules.ambientsounds.AS6;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import team.creative.ambientsounds.environment.BiomeEnvironment;

@Mixin({BiomeEnvironment.class})
public abstract class MixinBiomeEnvironment {


    // @WrapOperation(at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isRaining()Z")},
    //         method = {"<init>(Lteam/creative/ambientsounds/engine/AmbientEngine;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lteam/creative/ambientsounds/condition/AmbientVolume;)V"}
    //         // , remap = false
    // )
    // private boolean eclipticseasons_multimodpatch$isRaining(Level instance, Operation<Boolean> original, @Local BlockPos.MutableBlockPos mutableBlockPos) {
    //     if (AS6.Config.enable.get()) {
    //         return EclipticSeasonsApi.getInstance().isRainingOrSnowing(instance,mutableBlockPos);
    //     }
    //     return original.call(instance);
    // }

    @WrapOperation(at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")},
            method = {"<init>(Lteam/creative/ambientsounds/engine/AmbientEngine;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lteam/creative/ambientsounds/condition/AmbientVolume;)V"}
            // ,remap = false
    )
    private Biome.Precipitation eclipticseasons_multimodpatch$getPrecipitationAt(Biome instance, BlockPos pPos, Operation<Biome.Precipitation> original,@Local(argsOnly = true) Level level) {
        if (AS6.Config.enable.get()) {
            return EclipticSeasonsApi.getInstance().getPrecipitationAt(level,pPos);
        }
        return original.call(instance, pPos);
    }
}
