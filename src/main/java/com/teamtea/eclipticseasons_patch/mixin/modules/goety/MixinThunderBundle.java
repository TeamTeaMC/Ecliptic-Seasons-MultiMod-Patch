package com.teamtea.eclipticseasons_patch.mixin.modules.goety;


import com.Polarice3.Goety.common.entities.hostile.cultists.SpellCastingCultist;
import com.Polarice3.Goety.common.entities.projectiles.AbstractSpellCloud;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons_patch.modules.goety.GOETY;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

public abstract class MixinThunderBundle {

    // Forge not support
    // @Mixin({com.Polarice3.Goety.api.magic.ISpell.class})
    // public interface ISpell {
    //      @WrapOperation(require = 0,at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isThundering()Z")},
    //             method = {"SoulCalculation"})
    //     private boolean es_patch$SoulCalculation_isThundering(Level instance, Operation<Boolean> original, @Local(argsOnly = true) LivingEntity livingEntity) {
    //         if (GOETY.Config.enable.get()) {
    //             return EclipticSeasonsApi.getInstance().isThundering(instance, livingEntity.blockPosition());
    //         }
    //         return original.call(instance);
    //     }
    // }

    @Mixin({com.Polarice3.Goety.common.entities.util.StormEntity.class})
    public static abstract class I_StormEntity extends Entity {
        public I_StormEntity(EntityType<?> pEntityType, Level pLevel) {
            super(pEntityType, pLevel);
        }

        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isThundering()Z")},
                method = {"tick"})
        private boolean es_patch$tick_isThundering(ServerLevel instance, Operation<Boolean> original) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isThundering(instance, blockPosition());
            }
            return original.call(instance);
        }

        //  @WrapOperation(require = 0,at = {@At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setWeatherParameters(IIZZ)V")},
        //         method = {"tick"})
        // private void es_patch$tick_setWeatherParameters(ServerLevel instance, int pClearTime, int pWeatherTime, boolean pIsRaining, boolean pIsThundering, Operation<Void> original) {
        //     if (GOETY.Config.enable.get() && EclipticSeasonsApi.getInstance().hasLocalWeather(instance)) {
        //         GOETY_Hook.setIfHasLocalWeather(instance, pClearTime, pWeatherTime, blockPosition());
        //         return;
        //     }
        //     original.call(instance, pClearTime, pWeatherTime, pIsRaining, pIsThundering);
        // }
    }

    @Mixin({com.Polarice3.Goety.common.effects.ElectrifiedEffect.class})
    public static abstract class ElectrifiedEffect {
        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isThundering()Z")},
                method = {"applyEffectTick"})
        private boolean es_patch$applyEffectTick_isThundering(ServerLevel instance, Operation<Boolean> original, @Local(ordinal = 1) LivingEntity livingEntity) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isThundering(instance, livingEntity.blockPosition());
            }
            return original.call(instance);
        }
    }

    @Mixin({com.Polarice3.Goety.utils.WandUtil.class})
    public static abstract class WandUtil {
        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isThundering()Z")},
                method = {"chainLightning(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;DFZ)V"})
        private static boolean es_patch$chainLightning_isThundering(Level instance, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) LivingEntity livingEntity) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isThundering(instance, livingEntity.blockPosition());
            }
            return original.call(instance);
        }
    }

    @Mixin({com.Polarice3.Goety.common.entities.projectiles.ElectroOrb.class,
            com.Polarice3.Goety.common.entities.projectiles.MiniElectroOrb.class})
    public static abstract class projectiles {
        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isThundering()Z")},
                method = {"onHit"})
        private boolean es_patch$onHit_isThundering(Level instance, Operation<Boolean> original, @Local Entity entity) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isThundering(instance, entity.blockPosition());
            }
            return original.call(instance);
        }
    }

    @Mixin({com.Polarice3.Goety.common.effects.GoetyBaseEffect.class})
    public static abstract class GoetyBaseEffect {
        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isThundering()Z")},
                method = {"applyEffectTick"})
        private boolean es_patch$applyEffectTick_isThundering(Level instance, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) LivingEntity livingEntity) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isThundering(instance, livingEntity.blockPosition());
            }
            return original.call(instance);
        }
    }

    @Mixin(targets = {"com.Polarice3.Goety.common.entities.hostile.illagers.Crusher$MeleeGoal",
            "com.Polarice3.Goety.common.entities.ally.illager.CrusherServant$MeleeGoal"})
    public static abstract class MeleeGoal {
        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isThundering()Z")},
                method = {"chain"})
        private boolean es_patch$chain_isThundering(Level instance, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) LivingEntity livingEntity) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isThundering(instance, livingEntity.blockPosition());
            }
            return original.call(instance);
        }
    }

    @Mixin({com.Polarice3.Goety.common.items.equipment.StormlanderItem.class})
    public static abstract class StormlanderItem {
        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isThundering()Z")},
                method = {"chain"})
        private boolean es_patch$chain_isThundering(Level instance, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) LivingEntity livingEntity) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isThundering(instance, livingEntity.blockPosition());
            }
            return original.call(instance);
        }
    }

    @Mixin({com.Polarice3.Goety.common.entities.projectiles.MonsoonCloud.class})
    public static abstract class MonsoonCloud extends AbstractSpellCloud {
        public MonsoonCloud(EntityType<?> p_19870_, Level p_19871_) {
            super(p_19870_, p_19871_);
        }

        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isThundering()Z")},
                method = {"hurtEntities"})
        private boolean es_patch$hurtEntities_isThundering(ServerLevel instance, Operation<Boolean> original) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isThundering(instance, blockPosition());
            }
            return original.call(instance);
        }
    }

    @Mixin({com.Polarice3.Goety.common.entities.boss.Apostle.class})
    public static abstract class Apostle extends SpellCastingCultist {
        protected Apostle(EntityType<? extends SpellCastingCultist> type, Level p_i48551_2_) {
            super(type, p_i48551_2_);
        }

        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/LevelData;isThundering()Z")},
                method = {"tickDeath", "remove"})
        private boolean es_patch$isThundering(LevelData instance, Operation<Boolean> original, @Local ServerLevel serverLevel) {
            if (GOETY.Config.enable.get() && EclipticSeasonsApi.getInstance().hasLocalWeather(serverLevel)) {
                return EclipticSeasonsApi.getInstance().isThundering(serverLevel, blockPosition());
            }
            return original.call(instance);
        }

        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isThundering()Z")},
                method = {"aiStep"})
        private boolean es_patch$aiStep_isThundering(ServerLevel instance, Operation<Boolean> original) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isThundering(instance, blockPosition());
            }
            return original.call(instance);
        }

        //  @WrapOperation(require = 0,at = {@At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setWeatherParameters(IIZZ)V")},
        //         method = {"tickDeath", "remove", "aiStep"})
        // private void es_patch$setWeatherParameters(ServerLevel instance, int pClearTime, int pWeatherTime, boolean pIsRaining, boolean pIsThundering, Operation<Void> original) {
        //     if (GOETY.Config.enable.get() && EclipticSeasonsApi.getInstance().hasLocalWeather(instance)) {
        //         GOETY_Hook.setIfHasLocalWeather(instance, pClearTime, pWeatherTime, blockPosition());
        //         return;
        //     }
        //     original.call(instance, pClearTime, pWeatherTime, pIsRaining, pIsThundering);
        // }
    }

    @Mixin({com.Polarice3.Goety.common.entities.ally.illager.train.ModIllagerType.class})
    public static abstract class ModIllagerType {
        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isThundering()Z")},
                method = {"getIllager"})
        private boolean es_patch$applyEffectTick_isThundering(Level instance, Operation<Boolean> original, @Local(argsOnly = true) BlockPos pos) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isThundering(instance, pos);
            }
            return original.call(instance);
        }
    }

    @Mixin({com.Polarice3.Goety.common.ritual.type.StormRitualType.class})
    public static abstract class StormRitualType {
        @WrapOperation(require = 0,
                at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isThundering()Z")},
                method = {"getRequirement"})
        private static boolean es_patch$getRequirement_isThundering(Level instance, Operation<Boolean> original, @Local(argsOnly = true) BlockPos pos) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isThundering(instance, pos);
            }
            return original.call(instance);
        }
    }

    @Mixin({com.Polarice3.Goety.common.magic.spells.storm.BoltingSpell.BoltingDashTask.class})
    public static abstract class BoltingSpell {
        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isThundering()Z")},
                method = {"tickTask"})
        private boolean es_patch$getProperStructure_isThundering(ServerLevel instance, Operation<Boolean> original, @Local(ordinal = 1) LivingEntity entity) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isThundering(instance, entity.blockPosition());
            }
            return original.call(instance);
        }
    }

    @Mixin({com.Polarice3.Goety.common.magic.spells.storm.ShockingSpell.class,
            com.Polarice3.Goety.common.magic.spells.storm.ThunderboltSpell.class})
    public static abstract class ShockingSpell_ThunderboltSpell {
        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isThundering()Z")},
                method = {"SpellResult"})
        private boolean es_patch$getProperStructure_isThundering(ServerLevel instance, Operation<Boolean> original, @Local(ordinal = 1) LivingEntity entity) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isThundering(instance, entity.blockPosition());
            }
            return original.call(instance);
        }
    }

    @Mixin(targets = {"com.Polarice3.Goety.common.magic.spells.storm.DischargeSpell$1"})
    public static abstract class DischargeSpell {
        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isThundering()Z")},
                method = {"explodeHurt"})
        private boolean es_patch$explodeHurt_isThundering(ServerLevel instance, Operation<Boolean> original, @Local(ordinal = 0) LivingEntity entity) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isThundering(instance, entity.blockPosition());
            }
            return original.call(instance);
        }
    }
}
