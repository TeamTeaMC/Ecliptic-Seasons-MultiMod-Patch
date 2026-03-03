package com.teamtea.eclipticseasons_patch.mixin.modules.subtle_effects;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamtea.eclipticseasons_patch.modules.subtle_effects.SE;
import einstein.subtle_effects.ticking.FireflyManager;
import einstein.subtle_effects.ticking.tickers.LevelTicker;
import einstein.subtle_effects.ticking.tickers.entity.FrostyBreathTicker;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public abstract class MixinBundles {

    @Mixin({FireflyManager.class})
    public static abstract class ExtraFireflyManager {
        @Inject(at = {@At(value = "INVOKE", target = "Ljava/util/Optional;isEmpty()Z",ordinal = 1)},
                method = {"tick"}, cancellable = true)
        private static void eclipticseasons$isColdTime(Level level, BlockPos pos, BlockState state, RandomSource random, CallbackInfo ci) {
            if (SE.Config.enable.get() && SE.Hook.maySnow(level, pos)) {
                ci.cancel();
            }
        }
    }

    @Mixin({FrostyBreathTicker.class})
    public static abstract class ExtraFrostyBreathTicker extends LevelTicker {
        public ExtraFrostyBreathTicker(Level level, RandomSource random) {
            super(level, random);
        }

        @WrapOperation(at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;coldEnoughToSnow(Lnet/minecraft/core/BlockPos;)Z")},
                method = {"entityTick"})
        private boolean eclipticseasons$isColdTime(Biome instance, BlockPos pos, Operation<Boolean> original) {
            if (SE.Config.enable.get()) {
                return SE.Hook.maySnow(this.level, pos);
            }
            return original.call(instance, pos);
        }

    }
}