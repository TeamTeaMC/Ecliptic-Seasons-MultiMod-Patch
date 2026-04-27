package com.teamtea.eclipticseasons_patch.mixin.modules.presencefootsteps;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons_patch.modules.presencefootsteps.PF;
import eu.ha3.presencefootsteps.world.Association;
import eu.ha3.presencefootsteps.world.AssociationPool;
import eu.ha3.presencefootsteps.world.PFSolver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
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
            // ordinal = 1,
            target = "Lnet/minecraft/world/level/Level;isRainingAt(Lnet/minecraft/core/BlockPos;)Z")},
            remap = false,
            method = {
                    "findAssociation(Leu/ha3/presencefootsteps/world/AssociationPool;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/core/BlockPos$MutableBlockPos;Lnet/minecraft/world/phys/AABB;)Leu/ha3/presencefootsteps/world/Association;"})
    public void eclipticseasons_multimodpatch$findAssociation_check_if_snow(AssociationPool associations, LivingEntity entity, BlockPos.MutableBlockPos pos, AABB collider, CallbackInfoReturnable<Association> cir, @Local(ordinal = 0) LocalRef<BlockState> stateLocalRef, @Share("skipCollisionCheck") LocalBooleanRef ref) {
        if (PF.Config.enable.get() && stateLocalRef.get().blocksMotion()
                && EclipticSeasonsApi.getInstance().isSnowyBlock(entity.level(), stateLocalRef.get(), pos)) {
            stateLocalRef.set(Blocks.SNOW.defaultBlockState());
            ref.set(true);
        }
    }


    @Inject(at = {@At(
            remap = false,
            value = "HEAD")},
            remap = false,
            method = {
                    "findAssociation(Leu/ha3/presencefootsteps/world/AssociationPool;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/core/BlockPos$MutableBlockPos;Lnet/minecraft/world/phys/AABB;)Leu/ha3/presencefootsteps/world/Association;"})
    public void eclipticseasons_multimodpatch$findAssociation_add_field(AssociationPool associations, LivingEntity entity, BlockPos.MutableBlockPos pos, AABB collider, CallbackInfoReturnable<Association> cir, @Share("skipCollisionCheck") LocalBooleanRef ref) {
        ref.set(false);
    }


    @WrapOperation(at = {@At(
            remap = false,
            value = "INVOKE", target = "Leu/ha3/presencefootsteps/world/PFSolver;checkCollision(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/AABB;)Z")},
            remap = false,
            method = {
                    "findAssociation(Leu/ha3/presencefootsteps/world/AssociationPool;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/core/BlockPos$MutableBlockPos;Lnet/minecraft/world/phys/AABB;)Leu/ha3/presencefootsteps/world/Association;"})
    public boolean eclipticseasons_multimodpatch$findAssociation_skip_checkCollision(PFSolver instance, Level world, BlockState state, BlockPos pos, AABB collider, Operation<Boolean> original, @Share("skipCollisionCheck") LocalBooleanRef ref) {
        if (ref.get()) return true;
        return original.call(instance, world, state, pos, collider);
    }

}
