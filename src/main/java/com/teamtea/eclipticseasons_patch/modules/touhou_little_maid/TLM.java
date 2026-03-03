package com.teamtea.eclipticseasons_patch.modules.touhou_little_maid;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.neoforged.neoforge.common.ModConfigSpec;

@ESPatch(mods = "touhou_little_maid", esVersion = "0.12.0-pre13")
public class TLM implements IESModPatch {
    public static final String MOD_ID = "touhou_little_maid";

    @Override
    public void common(ModConfigSpec.Builder consumer) {
        Config.load(consumer);
    }


    public static class Config {

        public static ModConfigSpec.BooleanValue enable;

        public static void load(ModConfigSpec.Builder builder) {
            builder.comment("touhou_little_maid").push("touhou_little_maid");
            enable = builder.gameRestart().define("Enable", true);
            builder.pop();
        }
    }
}
