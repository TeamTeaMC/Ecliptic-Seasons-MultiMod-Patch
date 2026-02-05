package com.teamtea.eclipticseasons_patch.modules.incontrol;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.minecraftforge.common.ForgeConfigSpec;

@ESPatch(mods = "incontrol",minVersions = "1.20-9.4.0", esVersion = "0.12.0-pre4-1")
public class IC implements IESModPatch {

    @Override
    public void common(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment("In Control").push("incontrol");
            enable = builder.define("Enable", true);
            builder.pop();
        }
    }

}
