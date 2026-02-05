package com.teamtea.eclipticseasons_patch.modules.journeymap;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.neoforged.neoforge.common.ModConfigSpec;

@ESPatch(mods = "journeymap", minVersions = "1.21.1-6.0.0-beta.52")
public class JM implements IESModPatch {

    @Override
    public void client(ModConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ModConfigSpec.BooleanValue enable;

        public static void load(ModConfigSpec.Builder builder) {
            builder.comment("Journey Map").push("journeymap");
            enable = builder
                    .define("Enable", true);
            builder.pop();
        }
    }
}
