package com.teamtea.eclipticseasons_patch.mixin.modules.incontrol;


import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.constant.solar.Season;
import com.teamtea.eclipticseasons_patch.modules.incontrol.IC;
import com.teamtea.eclipticseasons_patch.modules.incontrol.ICHook;
import mcjty.incontrol.rules.support.GenericRuleEvaluator;
import mcjty.incontrol.tools.rules.IEventQuery;
import mcjty.incontrol.tools.typed.AttributeMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.BiFunction;

@Mixin({GenericRuleEvaluator.class})
public abstract class MixinGenericRuleEvaluator {

    @Shadow(remap = false)
    @Final
    private List<BiFunction<Object, IEventQuery, Boolean>> checks;


    @Inject(at = {@At(value = "HEAD")},
            method = {"addSpringCheck"},
            remap = false, cancellable = true)
    private void eclipticseasons_multimodpatch$addSpringCheck(Boolean s, CallbackInfo ci) {
        if (IC.Config.enable.get()) {
            this.checks.add((event, query) ->
                    ICHook.validSeasonOrLocal(ICHook.fetchLevel(event, query), query.getPos(event), Season.SPRING, s));
            ci.cancel();
        }
    }

    @Inject(at = {@At(value = "HEAD")},
            method = {"addSummerCheck"},
            remap = false, cancellable = true)
    private void eclipticseasons_multimodpatch$addSummerCheck(Boolean s, CallbackInfo ci) {
        if (IC.Config.enable.get()) {
            this.checks.add((event, query) ->
                    ICHook.validSeasonOrLocal(ICHook.fetchLevel(event, query), query.getPos(event), Season.SUMMER, s));
            ci.cancel();
        }
    }

    @Inject(at = {@At(value = "HEAD")},
            method = {"addAutumnCheck"},
            remap = false, cancellable = true)
    private void eclipticseasons_multimodpatch$addAutumnCheck(Boolean s, CallbackInfo ci) {
        if (IC.Config.enable.get()) {
            this.checks.add((event, query) ->
                    ICHook.validSeasonOrLocal(ICHook.fetchLevel(event, query), query.getPos(event), Season.AUTUMN, s));
            ci.cancel();
        }
    }

    @Inject(at = {@At(value = "HEAD")},
            method = {"addWinterCheck"},
            remap = false, cancellable = true)
    private void eclipticseasons_multimodpatch$addWinterCheck(Boolean s, CallbackInfo ci) {
        if (IC.Config.enable.get()) {
            this.checks.add((event, query) ->
                    ICHook.validSeasonOrLocal(ICHook.fetchLevel(event, query), query.getPos(event), Season.WINTER, s));
            ci.cancel();
        }
    }


    @Inject(at = {@At(value = "HEAD")},
            method = {"addChecks"},
            remap = false)
    private void eclipticseasons_multimodpatch$addChecks(AttributeMap map, CallbackInfo ci) {
        if (IC.Config.enable.get()) {
            map.consumeAsList(ICHook.VALID_TERMS, (sl) -> {
                ICHook.ValidTerms validTerms = ICHook.ValidTerms.of(sl);
                this.checks.add((event, query) ->
                        validTerms.matches(ICHook.fetchLevel(event, query)));
            });
            map.consumeAsList(ICHook.SURFACE_BIOMES, (sl) -> {
                ICHook.SurfaceBiomeSet validTerms = ICHook.SurfaceBiomeSet.of(sl);
                this.checks.add((event, query) ->
                        validTerms.matches(ICHook.fetchLevel(event, query), query.getPos(event)));
            });
        }
    }

}
