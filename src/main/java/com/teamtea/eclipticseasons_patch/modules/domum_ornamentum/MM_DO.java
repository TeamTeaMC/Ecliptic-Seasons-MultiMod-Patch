package com.teamtea.eclipticseasons_patch.modules.domum_ornamentum;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.LangUtil;
import com.teamtea.eclipticseasons_patch.common.SnowRegistryHolder;
import net.minecraftforge.common.ForgeConfigSpec;

@ESPatch(mods = MM_DO.MOD_ID, esVersion = "0.12.0-pre15")
public class MM_DO implements IESModPatch {
    public static final String MOD_ID = "domum_ornamentum";

    @Override
    public void common(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    @Override
    public void init() {
        SnowRegistryHolder.add(SnowRegistryHolder.builder()
                .modId(MOD_ID)
                .condition(Config.enable::get)
                .nameMatcher(s -> s.contains("shingle"))
                .simple(SnowRegistryHolder::getInfoSolidAO)
        );
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment(LangUtil.getModName(MOD_ID)).push(MOD_ID);
            enable = builder.comment("Enable automatic snow-covered blocks and models.")
                    .define("Enable", true);
            builder.pop();
        }
    }
}
