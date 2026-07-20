package com.teamtea.eclipticseasons_patch.mixin.modules.grassiergrass;


import com.leonardoinc22.shortgrass.client.render.GrassRenderPass;
import com.teamtea.eclipticseasons.client.render.WorldRenderer;
import com.teamtea.eclipticseasons_patch.modules.grassiergrass.GG;
import net.minecraft.client.Minecraft;
import net.minecraft.core.SectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinGrassSection {

    @Inject(at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;setSectionDirty(III)V")},
            method = {"setSectionDirty"},
            remap = false)
    private static void eclipticseasons_multimodpatch$grassiergrass$setSectionDirty(SectionPos sectionPos, CallbackInfo ci) {
        if (GG.Config.enable.get()) {
            GrassRenderPass.invalidateChunk(Minecraft.getInstance().level, sectionPos.x(), sectionPos.z());
        }
    }

    @Inject(at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;setSectionDirtyWithNeighbors(III)V")},
            method = {"setSectionDirtyWithNeighbors"},
            remap = false)
    private static void eclipticseasons_multimodpatch$grassiergrass$setSectionDirtyWithNeighbors(SectionPos sectionPos, CallbackInfo ci) {
        if (GG.Config.enable.get()) {
            GrassRenderPass.invalidateChunk(Minecraft.getInstance().level, sectionPos.x(), sectionPos.z());
        }
    }

}
