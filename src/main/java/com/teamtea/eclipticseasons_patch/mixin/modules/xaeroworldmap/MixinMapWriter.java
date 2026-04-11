package com.teamtea.eclipticseasons_patch.mixin.modules.xaeroworldmap;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamtea.eclipticseasons_patch.modules.xaeroworldmap.XWM;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xaero.map.MapWriter;

@Mixin({MapWriter.class})
public abstract class MixinMapWriter {


    @WrapOperation(at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getMapColor(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/MapColor;")},
            method = {"hasVanillaColor"},
            remap = false)
    private MapColor eclipticseasons$hasVanillaColor(BlockState instance, BlockGetter blockGetter, BlockPos blockPos, Operation<MapColor> original) {
        if (XWM.Config.skipVanillaColorCheck.get()
                && blockGetter instanceof ServerLevel) {
            return instance.getMapColor(EmptyBlockGetter.INSTANCE, blockPos);
        }
        return original.call(instance, blockGetter, blockPos);
    }
}
