package com.teamtea.eclipticseasons_patch.mixin.modules.ambientsounds;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons_patch.modules.ambientsounds.AS6;
import com.teamtea.eclipticseasons_patch.modules.ambientsounds.AS6_Hook;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import team.creative.ambientsounds.environment.AmbientEnvironment;

@Mixin({AmbientEnvironment.class})
public abstract class MixinAmbientEnvironment {


    @Shadow(remap = false)
    public boolean snowing;

    @Shadow(remap = false)
    public boolean thundering;

    @Shadow(remap = false)
    public boolean raining;

    @WrapOperation(at = {@At(value = "INVOKE", target = "Lteam/creative/ambientsounds/mod/SereneSeasonsCompat;getTemperature(Lnet/minecraft/world/entity/player/Player;)F")},
            method = {"analyzeFast"},
            remap = false)
    private float eclipticseasons_multimodpatch$SeasonsCompat(Player player, Operation<Float> original, @Local(argsOnly = true) Level level) {
        if (AS6.Config.enable.get()) {
            return AS6_Hook.getTempAndFixFlag((AmbientEnvironment) (Object) this, player, level);
        }
        return original.call(player);
    }


}
