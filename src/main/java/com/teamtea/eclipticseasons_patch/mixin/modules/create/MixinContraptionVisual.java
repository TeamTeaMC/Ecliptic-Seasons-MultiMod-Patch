package com.teamtea.eclipticseasons_patch.mixin.modules.create;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.render.ClientContraption;
import com.simibubi.create.content.contraptions.render.ContraptionVisual;
import com.teamtea.eclipticseasons_patch.modules.create.CreateContraptionMapSlice;
import net.minecraft.world.level.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = ContraptionVisual.class, remap = false)
public abstract class MixinContraptionVisual {
    @ModifyArg(
            method = "setupStructure",
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/engine_room/flywheel/lib/model/baked/BlockModelBuilder;<init>(Lnet/minecraft/world/level/BlockAndTintGetter;Ljava/lang/Iterable;)V"
            ),
            index = 0
    )
    private BlockAndTintGetter eclipticseasons$provideMapSlice(
            BlockAndTintGetter original,
            @Local(argsOnly = true) ClientContraption clientContraption
    ) {
        Contraption contraption = ((AccessorClientContraption) clientContraption)
                .eclipticseasons$getContraption();
        return CreateContraptionMapSlice.getOrCreate(
                original,
                clientContraption,
                contraption
        );
    }
}
