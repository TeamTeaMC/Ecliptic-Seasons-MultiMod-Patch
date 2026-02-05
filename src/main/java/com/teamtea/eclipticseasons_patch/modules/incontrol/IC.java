package com.teamtea.eclipticseasons_patch.modules.incontrol;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.neoforged.neoforge.common.ModConfigSpec;

@ESPatch(mods = "incontrol", esVersion = "0.12.0-pre4-1")
public class IC implements IESModPatch {

    @Override
    public void common(ModConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ModConfigSpec.BooleanValue enable;

        public static void load(ModConfigSpec.Builder builder) {
            builder.comment("InControl").push("incontrol");
            enable = builder.define("Enable", true);
            builder.pop();
        }
    }
}
