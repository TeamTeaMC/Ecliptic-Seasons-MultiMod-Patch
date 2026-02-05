package com.teamtea.eclipticseasons_patch.modules.fetzisasiandeco;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.ModConfigSpec;

@ESPatch(mods = "fetzisasiandeco",esVersion = "0.12.0-pre15")
public class FAD implements IESModPatch {

    @Override
    public void common(ModConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    @Override
    public void register(IEventBus gameBus, IEventBus modEventBus) {
        gameBus.register(FetzisHandler.INSTANCE);
    }

    public static class Config {

        public static ModConfigSpec.BooleanValue enable;
        public static ModConfigSpec.BooleanValue fence;
        public static ModConfigSpec.BooleanValue wall;
        // public static ModConfigSpec.BooleanValue windows;

        public static void load(ModConfigSpec.Builder builder) {
            builder.comment("Fetzi's Asian Decoration").push("fetzisasiandeco");
            enable = builder
                    .comment("Here, automatic snow-covered model compatibility has been added for certain blocks from Fetzi's Asian Decoration. At the moment, this is only a compromise solution.")
                    .define("Enable", true);
            // fence = builder.define("fence", true);
            // wall = builder.define("wall", true);
            // windows = builder.define("windows", true);
            builder.pop();
        }
    }
}
