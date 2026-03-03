package com.teamtea.eclipticseasons_patch.modules.ambientsounds;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.neoforged.neoforge.common.ModConfigSpec;

@ESPatch(mods = "ambientsounds",clientOnly = true)
public class AS6 implements IESModPatch {

    @Override
    public void client(ModConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ModConfigSpec.BooleanValue enable;

        public static void load(ModConfigSpec.Builder builder) {
            builder.comment("Ambient Sounds",
                            "The mod is no longer maintained for version 1.20, so we need a mixin to make the sounds work properly during snowfall.")
                    .push("ambientsounds");
            enable = builder
                    .gameRestart().define("Enable", true);
            builder.pop();
        }
    }
}
