package com.teamtea.eclipticseasons_patch.mixin.modules.immersive_weathering;


import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.ordana.immersive_weathering.data.block_growths.growths.builtin.IceGrowth;
import com.ordana.immersive_weathering.data.block_growths.growths.builtin.LeavesGrowth;
import com.ordana.immersive_weathering.data.block_growths.growths.builtin.SnowGrowth;
import com.teamtea.eclipticseasons.api.constant.tag.ClimateTypeBiomeTags;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons.config.CommonConfig;
import com.teamtea.eclipticseasons_patch.modules.immersive_weathering.IW;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.IceBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

public abstract class MixinTempBundle {


    @Pseudo
    @Mixin(value = {IceBlock.class}, priority = 1500)
    public static abstract class MixinClientIceBlockOverride {
        @TargetHandler(
                mixin = "com.ordana.immersive_weathering.mixins.IceMixin",
                name = "canMelt"
        )
        @WrapOperation(at = {@At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/biome/Biome;coldEnoughToSnow(Lnet/minecraft/core/BlockPos;)Z")},
                method = "@MixinSquared:Handler")
        private static boolean eclipticseasons_multimodpatch$coldEnoughToSnow(Biome instance, BlockPos pPos, Operation<Boolean> original, @Local(argsOnly = true) Level level) {
            if (IW.Config.enable.get()) {
                return EclipticUtil.maySnow(level, pPos);
            }
            return original.call(instance, pPos);
        }
    }


    @Mixin(value = {
            SnowGrowth.class
    })
    public static abstract class SnowGrowthMixin {
        @WrapOperation(at = {@At(value = "INVOKE", target = "Lcom/ordana/immersive_weathering/util/TemperatureManager;snowGrowthCanGrowSnowyBlock(Lnet/minecraft/core/BlockPos;Ljava/util/function/Supplier;)Z")},
                method = {
                        "tryGrowing"
                }, remap = false)
        private static boolean eclipticseasons_multimodpatch$isLoaded(BlockPos pos, Supplier<Holder<Biome>> biome, Operation<Boolean> original, @Local(argsOnly = true) ServerLevel level) {
            if (IW.Config.enable.get() && CommonConfig.Temperature.snowDown.get()) {
                return EclipticUtil.maySnow(level, pos);
            }
            return original.call(pos, biome);
        }
    }

    @Mixin(value = {
            IceGrowth.class, LeavesGrowth.class
    })
    public static abstract class GrowthMixin {
        @WrapOperation(at = {@At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;coldEnoughToSnow(Lnet/minecraft/core/BlockPos;)Z")},
                method = {
                        "tryGrowing"
                })
        private static boolean eclipticseasons_multimodpatch$isLoaded(Biome instance, BlockPos pPos, Operation<Boolean> original, @Local(argsOnly = true) ServerLevel level) {
            if (IW.Config.enable.get() && CommonConfig.Temperature.snowDown.get()) {
                return EclipticUtil.maySnow(level, pPos);
            }
            return original.call(instance, pPos);
        }
    }

    @Mixin(targets = {
            "com.ordana.immersive_weathering.data.position_tests.PrecipitationTest"
            // "com.ordana.immersive_weathering.blocks.ThinIceBlock",
            // "com.ordana.immersive_weathering.blocks.IcicleBlock"
    }, value = {com.ordana.immersive_weathering.blocks.ThinIceBlock.class})
    public static abstract class ColdSnowMixin {
        @WrapOperation(at = {@At(value = "INVOKE",remap = true, target = "Lnet/minecraft/world/level/biome/Biome;coldEnoughToSnow(Lnet/minecraft/core/BlockPos;)Z"),},
                method = {"test",
                        "canExpand"
                }, remap = false)
        private static boolean eclipticseasons_multimodpatch$coldEnoughToSnow(Biome instance, BlockPos pPos, Operation<Boolean> original, @Local(argsOnly = true) Level level) {
            if (IW.Config.enable.get() && CommonConfig.Temperature.snowDown.get()) {
                return EclipticUtil.maySnow(level, pPos);
            }
            return original.call(instance, pPos);
        }
    }

    @Mixin(value = {com.ordana.immersive_weathering.blocks.IcicleBlock.class})
    public static abstract class ColdSnowMixin2 {
        @WrapOperation(at = {@At(value = "INVOKE",remap = true,  target = "Lnet/minecraft/world/level/biome/Biome;coldEnoughToSnow(Lnet/minecraft/core/BlockPos;)Z")},
                method = {
                        "lambda$animateTick$0"
                }, remap = false)
        private static boolean eclipticseasons_multimodpatch$coldEnoughToSnow(Biome instance, BlockPos pPos, Operation<Boolean> original, @Local(argsOnly = true) Level level) {
            if (IW.Config.enable.get() && CommonConfig.Temperature.snowDown.get()) {
                return EclipticUtil.maySnow(level, pPos);
            }
            return original.call(instance, pPos);
        }
    }

    @Mixin(targets = {
            "com.ordana.immersive_weathering.data.position_tests.PrecipitationTest"
            // ,"com.ordana.immersive_weathering.util.TemperatureManager"
    }, value = {com.ordana.immersive_weathering.util.TemperatureManager.class})
    public static abstract class WarmRainMixin {
        @WrapOperation(at = {@At(value = "INVOKE", remap = true, target = "Lnet/minecraft/world/level/biome/Biome;warmEnoughToRain(Lnet/minecraft/core/BlockPos;)Z")},
                method = {"test",
                        "canSnowMelt"
                }, remap = false)
        private static boolean eclipticseasons_multimodpatch$coldEnoughToSnow(Biome instance, BlockPos pPos, Operation<Boolean> original, @Local(argsOnly = true) Level level) {
            if (IW.Config.enable.get() && CommonConfig.Temperature.iceMelt.get()) {
                boolean melt = !EclipticUtil.maySnow(level, pPos);
                if (melt && CommonConfig.Temperature.snowKeepInSnowyBiomes.get()
                        && CommonConfig.Temperature.waterFreezesInFrozenBiomes.get()
                        && MapChecker.getSurfaceBiome(level, pPos).is(ClimateTypeBiomeTags.EXTREME_COLD)) {
                    return false;
                }
                return melt;
            }
            return original.call(instance, pPos);
        }
    }

    @Mixin(value = {com.ordana.immersive_weathering.blocks.IcicleBlock.class})
    public static abstract class IcicleBlockMixin {
        @WrapOperation(at = {@At(value = "INVOKE", target = "Lnet/minecraft/core/Holder;is(Lnet/minecraft/tags/TagKey;)Z")},
                method = {"randomTick"})
        private static boolean eclipticseasons_multimodpatch$randomTick_toMelt(Holder<Biome> instance, TagKey<Biome> tTagKey, Operation<Boolean> original, @Local(argsOnly = true) ServerLevel level, @Local(argsOnly = true) BlockPos pos) {
            if (IW.Config.enable.get() && CommonConfig.Temperature.iceMelt.get()) {
                if (CommonConfig.Temperature.waterFreezesInFrozenBiomes.get()
                        && MapChecker.getSurfaceBiome(level, pos).is(ClimateTypeBiomeTags.EXTREME_COLD)) {
                    return true;
                }
                return EclipticUtil.maySnow(level, pos);
            }
            return original.call(instance, tTagKey);
        }
    }
}
