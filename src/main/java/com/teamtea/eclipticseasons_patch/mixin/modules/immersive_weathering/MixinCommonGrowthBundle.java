package com.teamtea.eclipticseasons_patch.mixin.modules.immersive_weathering;


import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.ordana.immersive_weathering.data.block_growths.BlockGrowthHandler;
import com.ordana.immersive_weathering.data.block_growths.growths.ConfigurableBlockGrowth;
import com.teamtea.eclipticseasons_patch.modules.immersive_weathering.IW;
import com.teamtea.eclipticseasons_patch.modules.immersive_weathering.ESPatchIWChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Supplier;

public abstract class MixinCommonGrowthBundle {

    @Mixin(value = {BlockGrowthHandler.class})
    public static abstract class BlockGrowthHandlerMixin {
        @ModifyExpressionValue(at = {@At(value = "INVOKE", target = "Ljava/util/Optional;get()Ljava/lang/Object;")},
                method = {"parse"}, remap = false)
        private static <T> T eclipticseasons_multimodpatch$parse_set(T original, @Local(name = "e") Map.Entry<ResourceLocation, JsonElement> e) {
            if (IW.Config.enable.get() && original instanceof ESPatchIWChecker blockGrowth) {
                IW.Config.LimitRecord record = IW.Config.recordMap.get(e.getKey());
                if (record != null) {
                    blockGrowth.eclipticseasons_multimodpatch$setRecord(record);
                }
            }
            return original;
        }
    }

    @Mixin(value = {ConfigurableBlockGrowth.class})
    public static abstract class ConfigurableBlockGrowthMixin implements ESPatchIWChecker {
        @Inject(at = {@At(value = "RETURN")},
                method = {"canGrow"}, remap = false, cancellable = true)
        private void eclipticseasons_multimodpatch$canGrow(BlockPos pos, Level level, Supplier<Holder<Biome>> biome, CallbackInfoReturnable<Boolean> cir) {
            if (IW.Config.enable.get() && cir.getReturnValue()) {
                if (eclipticseasons_multimodpatch$record != null && !eclipticseasons_multimodpatch$record.isValid(level))
                    cir.setReturnValue(false);
            }
        }

        @Unique
        private IW.Config.LimitRecord eclipticseasons_multimodpatch$record;

        @Override
        public void eclipticseasons_multimodpatch$setRecord(IW.Config.LimitRecord record) {
            eclipticseasons_multimodpatch$record = record;
        }
    }

    @Mixin(value = {
            com.ordana.immersive_weathering.data.block_growths.growths.builtin.CampfireSootGrowth.class
            , com.ordana.immersive_weathering.data.block_growths.growths.builtin.FireSootGrowth.class
            , com.ordana.immersive_weathering.data.block_growths.growths.builtin.GrassGrowth.class
            , com.ordana.immersive_weathering.data.block_growths.growths.builtin.IceGrowth.class
            , com.ordana.immersive_weathering.data.block_growths.growths.builtin.LeavesGrowth.class
            , com.ordana.immersive_weathering.data.block_growths.growths.builtin.SandGrowth.class
            , com.ordana.immersive_weathering.data.block_growths.growths.builtin.SandLayerGrowth.class
            , com.ordana.immersive_weathering.data.block_growths.growths.builtin.SnowGrowth.class
            , com.ordana.immersive_weathering.data.block_growths.growths.builtin.SnowIcicleGrowth.class})
    public static abstract class BuiltinGrowthMixin implements ESPatchIWChecker {

        @Expression("? < ?")
        @ModifyExpressionValue(at = {@At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0)},
                method = {"tryGrowing"}, remap = false)
        private boolean eclipticseasons_multimodpatch$tryGrowing(boolean original, @Local(argsOnly = true) ServerLevel level) {
            if (original && IW.Config.enable.get()) {
                if (eclipticseasons_multimodpatch$record != null && !eclipticseasons_multimodpatch$record.isValid(level))
                    original = false;
            }
            return original;
        }

        @Unique
        private IW.Config.LimitRecord eclipticseasons_multimodpatch$record;

        @Override
        public void eclipticseasons_multimodpatch$setRecord(IW.Config.LimitRecord record) {
            eclipticseasons_multimodpatch$record = record;
        }
    }

    @Mixin(value = {com.ordana.immersive_weathering.data.block_growths.growths.builtin.LightningGrowth.class})
    public static abstract class LightningGrowthMixin implements ESPatchIWChecker {
        @Expression("? < ?")
        @ModifyExpressionValue(at = {@At(value = "MIXINEXTRAS:EXPRESSION")},
                method = {"onLightningHit"}, remap = false)
        private boolean eclipticseasons_multimodpatch$onLightningHit(boolean original, @Local(name = "level") Level level) {
            if (original && IW.Config.enable.get()) {
                if (eclipticseasons_multimodpatch$record != null && !eclipticseasons_multimodpatch$record.isValid(level))
                    original = false;
            }
            return original;
        }

        @Unique
        private IW.Config.LimitRecord eclipticseasons_multimodpatch$record;

        @Override
        public void eclipticseasons_multimodpatch$setRecord(IW.Config.LimitRecord record) {
            eclipticseasons_multimodpatch$record = record;
        }
    }
}
