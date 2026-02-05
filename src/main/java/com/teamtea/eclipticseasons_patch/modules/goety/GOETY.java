package com.teamtea.eclipticseasons_patch.modules.goety;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.minecraftforge.common.ForgeConfigSpec;

@ESPatch(mods = "goety",minVersions = "2.5.37.5")
public class GOETY implements IESModPatch {

    @Override
    public void common(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;
        public static ForgeConfigSpec.BooleanValue fakeSeason;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment("Goety").push("goety");
            enable = builder.define("Enable", true);
            fakeSeason = builder.define("FakeSeasonsLoaded", true);
            builder.pop();
        }
    }
}
