package com.teamtea.eclipticseasons_patch.mixin.modules.goety;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons_patch.modules.goety.GOETY;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

public abstract class MixinBundle {

    @Mixin({com.Polarice3.Goety.utils.MobUtil.class})
    public static abstract class MobUtil {
        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isRaining()Z")},
                method = {"isInSunlightNoRain"})
        private static boolean es_patch$isInSunlightNoRain_isRaining(Level instance, Operation<Boolean> original,
                                                                     @Local(argsOnly = true) LivingEntity livingEntity) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isRainingOrSnowing(instance, livingEntity.blockPosition());
            }
            return original.call(instance);
        }
    }

    @Mixin({com.Polarice3.Goety.common.events.LichEvents.class})
    public static abstract class LichEvents {
        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isRaining()Z")},
                method = {"onPlayerLichdom"})
        private static boolean es_patch$onPlayerLichdom_isRaining(Level instance, Operation<Boolean> original,
                                                                  @Local Player livingEntity) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isRainingOrSnowing(instance, livingEntity.blockPosition());
            }
            return original.call(instance);
        }
    }


    @Mixin({com.Polarice3.Goety.common.inventory.WitchRobeInventory.class})
    public static abstract class WitchRobeInventory {
        @Shadow(remap = false)
        public LivingEntity getLivingEntity() {
            return null;
        }

        @WrapOperation(require = 0, at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isRaining()Z")},
                method = {"tick"})
        private boolean es_patch$tick_isRaining(Level instance, Operation<Boolean> original) {
            if (GOETY.Config.enable.get()) {
                return EclipticSeasonsApi.getInstance().isRainingOrSnowing(instance, getLivingEntity().blockPosition());
            }
            return original.call(instance);
        }
    }

}
