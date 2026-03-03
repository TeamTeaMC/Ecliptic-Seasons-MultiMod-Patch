package com.teamtea.eclipticseasons_patch.modules.journeymap;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.minecraftforge.common.ForgeConfigSpec;

@ESPatch(mods = "journeymap", clientOnly = true)
public class JM implements IESModPatch {

    @Override
    public void client(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment("Journey Map").push("journeymap");
            enable = builder
                    .define("Enable", true);
            builder.pop();
        }
    }
}
