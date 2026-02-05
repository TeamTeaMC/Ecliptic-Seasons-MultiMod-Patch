package com.teamtea.eclipticseasons_patch.modules.yuushya;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;

@ESPatch(mods = "yuushya",esVersion = "0.12.0-pre15")
public class YST implements IESModPatch {

    @Override
    public void common(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    @Override
    public void register(IEventBus gameBus, IEventBus modEventBus) {
        gameBus.register(YuushyaHandler.INSTANCE);
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;
        // public static ModConfigSpec.BooleanValue windows;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment("Yuushya Townscape").push("yuushya");
            enable = builder
                    .comment("Here, automatic snow-covered model compatibility has been added for certain blocks from Yuushya Townscape. At the moment, this is only a compromise solution.")
                    .define("Enable", true);
            // fence = builder.define("fence", true);
            // wall = builder.define("wall", true);
            // windows = builder.define("windows", true);
            builder.pop();
        }
    }
}
