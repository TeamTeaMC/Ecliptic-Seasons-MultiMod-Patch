package com.teamtea.eclipticseasons_patch.mixin.modules.xaeroworldmap;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons.common.misc.MapColorReplacer;
import com.teamtea.eclipticseasons.config.CommonConfig;
import com.teamtea.eclipticseasons_patch.modules.xaeroworldmap.XWM;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xaero.map.region.MapBlock;
import xaero.map.region.MapPixel;

@Mixin({MapPixel.class})
public abstract class MixinMapPixel {

    @Shadow(remap = false)
    protected BlockState state;

    @Shadow(remap = false)
    protected byte light;


    @ModifyExpressionValue(at = {@At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;")},
            method = {"getPixelColours"})
    private Block eclipticseasons_multimodpatch$getPixelColours_init(
            Block original,
            @Share("eclipticseasons_multimodpatch$isSnowy") LocalRef<MapColor> ref) {
        ref.set(null);
        return original;
    }

    @ModifyExpressionValue(at = {@At(value = "INVOKE", target = "Lxaero/map/MapWriter;loadBlockColourFromTexture(Lnet/minecraft/world/level/block/state/BlockState;ZLnet/minecraft/world/level/Level;Lnet/minecraft/core/Registry;Lnet/minecraft/core/BlockPos;)I")},
            method = {"getPixelColours"},
            remap = false)
    private int eclipticseasons_multimodpatch$getPixelColours_base(
            int original, @Local(argsOnly = true) Level world,
            @Local(argsOnly = true) BlockPos.MutableBlockPos mutableGlobalPos,
            @Local(argsOnly = true) MapBlock block,
            @Local(ordinal = 1, argsOnly = true) Registry<Biome> biomeRegistry,
            @Share("eclipticseasons_multimodpatch$isSnowy") LocalRef<MapColor> ref) {
        if (XWM.Config.enable.get()) {
            if (MapChecker.isLoadNearBy(world, mutableGlobalPos)) {
                MapColor mapColor = MapColorReplacer.getTopSnowColor(world, state, mutableGlobalPos);
                if (mapColor != null) {
                    ref.set(mapColor);
                    return mapColor.col;
                }
            } else if (block.getHeight() >= block.getTopHeight()
                    && (!CommonConfig.Snow.notSnowyNearGlowingBlock.get()
                    || EclipticUtil.canSnowyBlockInteract()
                    || light < CommonConfig.Snow.notSnowyNearGlowingBlockLevel.get())
                    && MapChecker.getDefaultBlockTypeFlag(state) > MapChecker.FLAG_NONE) {
                ResourceKey<Biome> resourceKey = block.getBiome();
                Biome biome1 = biomeRegistry.get(resourceKey);
                boolean b = MapChecker.shouldSnowAtBiome(world, biome1, state, null, state.getSeed(mutableGlobalPos), mutableGlobalPos);
                ref.set(MapColor.SNOW);
                if (b) return MapColor.SNOW.col;
            }
        }
        ref.set(MapColor.COLOR_BLACK);
        return original;
    }

    @ModifyExpressionValue(at = {@At(value = "INVOKE", target = "Lxaero/map/biome/BlockTintProvider;getBiomeColor(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;ZLxaero/map/region/MapTile;I)I")},
            method = {"getPixelColours"},
            remap = false)
    private int eclipticseasons_multimodpatch$getPixelColours_Tint(int original, @Local(argsOnly = true) Level world, @Local(argsOnly = true) BlockPos.MutableBlockPos mutableGlobalPos,
                                                                   @Local(argsOnly = true) MapBlock block,
                                                                   @Local(ordinal = 1, argsOnly = true) Registry<Biome> biomeRegistry,
                                                                   @Share("eclipticseasons_multimodpatch$isSnowy") LocalRef<MapColor> ref) {
        if (ref.get() == null) {
            if (XWM.Config.enable.get()) {
                if (MapChecker.isLoadNearBy(world, mutableGlobalPos)) {
                    MapColor mapColor = MapColorReplacer.getTopSnowColor(world, state, mutableGlobalPos);
                    if (mapColor != null) {
                        return mapColor.col;
                    }
                } else if (block.getHeight() >= block.getTopHeight()
                        && (!CommonConfig.Snow.notSnowyNearGlowingBlock.get()
                        || EclipticUtil.canSnowyBlockInteract()
                        || light < CommonConfig.Snow.notSnowyNearGlowingBlockLevel.get())
                        && MapChecker.getDefaultBlockTypeFlag(state) > MapChecker.FLAG_NONE) {
                    ResourceKey<Biome> resourceKey = block.getBiome();
                    Biome biome1 = biomeRegistry.get(resourceKey);
                    boolean b = MapChecker.shouldSnowAtBiome(world, biome1, state, null, state.getSeed(mutableGlobalPos), mutableGlobalPos);
                    if (b) return MapColor.SNOW.col;
                }
            }
        } else if (ref.get() != MapColor.COLOR_BLACK) {
            original = ref.get().col;
        }
        return original;
    }
}
