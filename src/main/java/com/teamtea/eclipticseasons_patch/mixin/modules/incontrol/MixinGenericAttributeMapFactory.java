package com.teamtea.eclipticseasons_patch.mixin.modules.incontrol;


import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import com.teamtea.eclipticseasons_patch.modules.incontrol.ICHook;
import mcjty.incontrol.rules.support.RuleKeys;
import mcjty.incontrol.tools.typed.Attribute;
import mcjty.incontrol.tools.typed.GenericAttributeMapFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({GenericAttributeMapFactory.class})
public abstract class MixinGenericAttributeMapFactory {


    @Shadow(remap = false)
    @Final
    private List<Attribute<?>> attributes;

    @Inject(at = {@At(value = "HEAD")},
            method = {"attribute"},
            remap = false)
    private void eclipticseasons$addSpringCheck(Attribute<?> a, CallbackInfoReturnable<GenericAttributeMapFactory> cir) {
        if (a.key() == RuleKeys.SPRING) {
            this.attributes.add(Attribute.createMulti(ICHook.VALID_TERMS));
            EclipticSeasonsPatch.logger("InControl add a check rule for spring, we here inject our rules for valid terms.");
        } else if (a.key() == RuleKeys.BIOMETAGS) {
            this.attributes.add(Attribute.createMulti(ICHook.SURFACE_BIOMES));
            EclipticSeasonsPatch.logger("InControl add a check rule for biome tag, we here inject our rules for surface biomes.");
        }
    }
}
