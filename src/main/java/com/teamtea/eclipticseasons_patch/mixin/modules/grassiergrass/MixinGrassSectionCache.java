package com.teamtea.eclipticseasons_patch.mixin.modules.grassiergrass;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.teamtea.eclipticseasons.api.misc.client.IMapSlice;
import com.teamtea.eclipticseasons_patch.modules.grassiergrass.GG;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "com.leonardoinc22.shortgrass.client.render.GrassSectionCache")
public abstract class MixinGrassSectionCache {

    @ModifyExpressionValue(at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/RenderRegionCache;createRegion(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/SectionPos;)Lnet/minecraft/client/renderer/chunk/RenderChunkRegion;")},
            method = {"submitBuild"},
            remap = false)
    private static RenderChunkRegion eclipticseasons_multimodpatch$submitBuild(RenderChunkRegion original) {
        if (GG.Config.enable.get()
                && original instanceof IMapSlice mapSlice) {
            mapSlice.forceMapSliceUpdate();
        }
        return original;
    }
}
