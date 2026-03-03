package com.teamtea.eclipticseasons_patch.modules.ultramarine;

import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.LangUtil;
import com.teamtea.eclipticseasons_patch.common.SnowRegistryHolder;
import net.neoforged.neoforge.common.ModConfigSpec;

// @ESPatch(mods = UM.MOD_ID, esVersion = "0.12.0-pre15")
public class UM implements IESModPatch {
    public static final String MOD_ID = "ultramarine";

    @Override
    public void common(ModConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    @Override
    public void init() {
        var condition = SnowRegistryHolder.builder()
                .modId(MOD_ID)
                .condition(Config.enable::get);
        SnowRegistryHolder.add(condition
                .simple(SnowRegistryHolder::getInfoSolid)
        );
    }

    public static class Config {

        public static ModConfigSpec.BooleanValue enable;

        public static void load(ModConfigSpec.Builder builder) {
            builder.comment(LangUtil.getModName(MOD_ID)).push(MOD_ID);
            enable = builder.comment("Enable automatic snow-covered blocks and models.")
                    .gameRestart().define("Enable", true);
            builder.pop();
        }
    }
}
