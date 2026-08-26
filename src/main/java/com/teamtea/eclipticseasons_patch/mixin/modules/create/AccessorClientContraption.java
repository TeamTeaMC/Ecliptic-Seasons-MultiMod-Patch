package com.teamtea.eclipticseasons_patch.mixin.modules.create;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.render.ClientContraption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ClientContraption.class, remap = false)
public interface AccessorClientContraption {
    @Accessor("contraption")
    Contraption eclipticseasons$getContraption();
}
