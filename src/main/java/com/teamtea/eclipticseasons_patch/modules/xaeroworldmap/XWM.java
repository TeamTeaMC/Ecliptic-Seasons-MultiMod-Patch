package com.teamtea.eclipticseasons_patch.modules.xaeroworldmap;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.LangUtil;
import net.neoforged.neoforge.common.ModConfigSpec;

 @ESPatch(mods = XWM.MOD_ID)
public class XWM implements IESModPatch {
    public static final String MOD_ID = "xaeroworldmap";

    @Override
    public void common(ModConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ModConfigSpec.BooleanValue enable;

        public static void load(ModConfigSpec.Builder builder) {
            builder.comment(LangUtil.getModName(MOD_ID)).push(MOD_ID);
            enable = builder
                    .gameRestart().define("Enable", true);
            builder.pop();
        }
    }
}
