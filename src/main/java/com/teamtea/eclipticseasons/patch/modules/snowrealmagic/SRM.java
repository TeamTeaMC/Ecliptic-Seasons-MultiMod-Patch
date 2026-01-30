package com.teamtea.eclipticseasons.patch.modules.snowrealmagic;

import com.teamtea.eclipticseasons.patch.api.ESPatch;
import com.teamtea.eclipticseasons.patch.api.IESModPatch;
import com.teamtea.eclipticseasons.patch.api.LangUtil;
import net.minecraftforge.common.ForgeConfigSpec;

@ESPatch(mods = SRM.MOD_ID, esVersion = "0.12.5.4",minVersions = "10.7.0")
public class SRM implements IESModPatch {
    public static final String MOD_ID = "snowrealmagic";

    @Override
    public void common(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment(LangUtil.getModName(MOD_ID)).push(MOD_ID);
            enable = builder
                    .define("Enable", true);
            builder.pop();
        }
    }
}
