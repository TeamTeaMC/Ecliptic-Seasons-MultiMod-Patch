package com.teamtea.eclipticseasons_patch.mixin.modules.yuushya;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamtea.eclipticseasons.client.core.ExtraModelManager;
import com.teamtea.eclipticseasons.client.model.bakequad.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

public abstract class MixinBundle {

    @Mixin({ExtraModelManager.class})
    public static abstract class ExtraModelManagers {

        @Shadow(remap = false)
        public static ResourceLocation snow;


        // @WrapOperation(at = {@At(value = "INVOKE",
        //         target = "Lnet/minecraft/client/renderer/block/model/BakedQuad;getDirection()Lnet/minecraft/core/Direction;")},
        //          method = {"makeSnowyBakedQuads"})
        // private static Direction eclipticseasons_multimodpatch$makeSnowyBakedQuads2
        //         (BakedQuad instance, Operation<Direction> original) {
        //     int[] vertices = instance.getVertices();
        //
        //     // 基准顶点
        //     float x0 = Float.intBitsToFloat(vertices[0]);
        //     float y0 = Float.intBitsToFloat(vertices[1]);
        //     float z0 = Float.intBitsToFloat(vertices[2]);
        //
        //     float x1 = Float.intBitsToFloat(vertices[8]);
        //     float y1 = Float.intBitsToFloat(vertices[9]);
        //     float z1 = Float.intBitsToFloat(vertices[10]);
        //
        //     float x3 = Float.intBitsToFloat(vertices[24]);
        //     float y3 = Float.intBitsToFloat(vertices[25]);
        //     float z3 = Float.intBitsToFloat(vertices[26]);
        //
        //     // 边向量
        //     float edge1X = x1 - x0;
        //     float edge1Y = y1 - y0;
        //     float edge1Z = z1 - z0;
        //
        //     float edge2X = x3 - x0;
        //     float edge2Y = y3 - y0;
        //     float edge2Z = z3 - z0;
        //
        //     // 法线
        //     float nx = edge1Y * edge2Z - edge1Z * edge2Y;
        //     float ny = edge1Z * edge2X - edge1X * edge2Z;
        //     float nz = edge1X * edge2Y - edge1Y * edge2X;
        //
        //     float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        //     if (len > 1e-6f) {
        //         nx /= len;
        //         ny /= len;
        //         nz /= len;
        //     } else {
        //         // 退化，交给原逻辑
        //         return instance.getDirection();
        //     }
        //
        //     // 角度阈值：cos(75°) ≈ 0.2588
        //     if (Math.abs(ny) >= 0.2588f) {
        //         // 小于75° → 算成UP
        //         return Direction.UP;
        //     } else {
        //         // 大于等于75° → 基本竖直，按XZ选择方向
        //         return Math.abs(nx) > Math.abs(nz)
        //                 ? (nx > 0 ? Direction.EAST : Direction.WEST)
        //                 : (nz > 0 ? Direction.SOUTH : Direction.NORTH);
        //     }
        // }

    }

}
