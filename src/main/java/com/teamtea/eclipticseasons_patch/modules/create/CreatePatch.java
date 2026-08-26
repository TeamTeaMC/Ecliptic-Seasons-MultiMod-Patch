package com.teamtea.eclipticseasons_patch.modules.create;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.LangUtil;
import net.neoforged.neoforge.common.ModConfigSpec;

@ESPatch(mods = CreatePatch.MOD_ID, minVersions = "6.0.10", clientOnly = true)
public class CreatePatch implements IESModPatch {
    public static final String MOD_ID = "create";

    @Override
    public void client(ModConfigSpec.Builder builder) {
        Config.load(builder);
    }

    public static class Config {
        public static ModConfigSpec.BooleanValue enable;

        private static void load(ModConfigSpec.Builder builder) {
            builder.comment(LangUtil.getModName(MOD_ID)).push(MOD_ID);
            enable = builder
                    .comment("Render seasonal snow on exposed Create contraption blocks.")
                    .gameRestart()
                    .define("Enable", true);
            builder.pop();
        }
    }
}
