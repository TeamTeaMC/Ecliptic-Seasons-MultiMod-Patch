package com.teamtea.eclipticseasons_patch.modules.snowrealmagic;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.LangUtil;
import net.neoforged.neoforge.common.ModConfigSpec;

@ESPatch(mods = SRM.MOD_ID, esVersion = "0.12.5.4")
public class SRM implements IESModPatch {
    public static final String MOD_ID = "snowrealmagic";

    @Override
    public void common(ModConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ModConfigSpec.BooleanValue enable;

        public static void load(ModConfigSpec.Builder builder) {
            builder.comment(LangUtil.getModName(MOD_ID)).push(MOD_ID);
            enable = builder
                    .define("Enable", true);
            builder.pop();
        }
    }
}
