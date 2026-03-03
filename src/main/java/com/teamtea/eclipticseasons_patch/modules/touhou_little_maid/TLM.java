package com.teamtea.eclipticseasons_patch.modules.touhou_little_maid;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.minecraftforge.common.ForgeConfigSpec;

@ESPatch(mods = "touhou_little_maid", esVersion = "0.12.0-pre13")
public class TLM implements IESModPatch {
    public static final String MOD_ID = "touhou_little_maid";

    @Override
    public void common(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }


    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment("Touhou Little Maid").push("touhou_little_maid");
            enable = builder.define("Enable", true);
            builder.pop();
        }
    }
}
