package com.teamtea.eclipticseasons_patch.mixin.modules.stardew_fishing;


import com.bonker.stardewfishing.common.FishingHookLogic;
import com.teamtea.eclipticseasons_patch.modules.stardew_fishing.SF;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({FishingHookLogic.class})
@Pseudo
public abstract class MixinFishingHookLogic {

    @Inject(
            require = 0,
            at = {@At(value = "HEAD")},
            method = {"lambda$startStardewMinigame$4"},
            remap = false, cancellable = true)
    private static void eclipticseasons_multimodpatch$startStardewMinigame(ServerPlayer player, FishingHookLogic cap, CallbackInfoReturnable<Boolean> cir) {
        if (SF.Hook.shouldCloseGame(player)) {
            cir.setReturnValue(true);
        }
    }

}
