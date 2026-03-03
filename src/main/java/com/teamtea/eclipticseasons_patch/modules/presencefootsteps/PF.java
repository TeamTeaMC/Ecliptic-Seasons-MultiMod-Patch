package com.teamtea.eclipticseasons_patch.modules.presencefootsteps;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.minecraftforge.common.ForgeConfigSpec;

@ESPatch(mods = "presencefootsteps", clientOnly = true)
public class PF implements IESModPatch {

    @Override
    public void client(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment("Presence Footsteps").push("presencefootsteps");
            enable = builder
                    .define("Enable", true);
            builder.pop();
        }
    }
}
