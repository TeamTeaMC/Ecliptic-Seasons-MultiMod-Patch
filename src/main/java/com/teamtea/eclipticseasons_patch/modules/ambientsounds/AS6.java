package com.teamtea.eclipticseasons_patch.modules.ambientsounds;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.minecraftforge.common.ForgeConfigSpec;

@ESPatch(mods = "ambientsounds",esVersion = "0.12.0-pre16")
public class AS6 implements IESModPatch {

    @Override
    public void client(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment("Ambient Sounds",
                            "The mod is no longer maintained for version 1.20, so we need a mixin to make the sounds work properly during snowfall.")
                    .push("ambientsounds");
            enable = builder
                    .define("Enable", true);
            builder.pop();
        }
    }
}
