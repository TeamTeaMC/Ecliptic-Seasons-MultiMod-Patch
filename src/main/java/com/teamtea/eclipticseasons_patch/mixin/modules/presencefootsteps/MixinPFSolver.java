package com.teamtea.eclipticseasons_patch.mixin.modules.presencefootsteps;


import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons_patch.modules.presencefootsteps.PF;
import eu.ha3.presencefootsteps.world.Association;
import eu.ha3.presencefootsteps.world.PFSolver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PFSolver.class})
public abstract class MixinPFSolver {

    /**
     *
     **/
    @Inject(at = {@At(
            remap = false,
            value = "INVOKE",
            ordinal = 1,
            target = "Leu/ha3/presencefootsteps/world/Emitter;isResult(Ljava/lang/String;)Z")},
            remap = false,
            require = 0,
            method = {
                    "findAssociation(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/AABB;)Leu/ha3/presencefootsteps/world/Association;"})
    public void eclipticseasons_multimodpatch$findAssociation_check_if_snow(Entity entity, BlockPos pos, AABB collider, CallbackInfoReturnable<Association> cir, @Local(ordinal = 0) LocalRef<BlockState> stateLocalRef) {
        if (PF.Config.enable.get() && stateLocalRef.get().blocksMotion()
                && EclipticSeasonsApi.getInstance().isSnowyBlock(entity.level(), stateLocalRef.get(), pos))
            stateLocalRef.set(Blocks.SNOW.defaultBlockState());
    }


}
