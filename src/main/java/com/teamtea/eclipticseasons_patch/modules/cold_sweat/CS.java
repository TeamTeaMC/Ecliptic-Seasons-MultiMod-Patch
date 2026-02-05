package com.teamtea.eclipticseasons_patch.modules.cold_sweat;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.List;

@ESPatch(mods = "cold_sweat",minVersions = "2.4-b06e")
public class CS implements IESModPatch {

    @Override
    public void common(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    @Override
    public void register(IEventBus gameBus, IEventBus modEventBus) {
        gameBus.register(Cold_Sweat.INSTANCE);
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;
        public static ForgeConfigSpec.ConfigValue<List<? extends Double>> cold_sweat_springs;
        public static ForgeConfigSpec.ConfigValue<List<? extends Double>> cold_sweat_summers;
        public static ForgeConfigSpec.ConfigValue<List<? extends Double>> cold_sweat_autumns;
        public static ForgeConfigSpec.ConfigValue<List<? extends Double>> cold_sweat_winters;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment("Cold Sweat").push("cold_sweat");
            enable = builder.define("Enable", true);
            cold_sweat_springs = builder.comment("Spring Temperatures, divided into six periods according to the solar term table.")
                    .defineListAllowEmpty("SpringTemps",
                            () -> List.of(-0.25d, -0.15d, -0.1d, 0d, 0d, 0.05d),
                            o -> o instanceof Double);
            cold_sweat_summers = builder.comment("Summer Temperatures divided into six periods according to the solar term table.")
                    .defineListAllowEmpty("SummerTemps",
                            () -> List.of(0.1d, 0.15d, 0.15d, 0.2d, 0.2d, 0.25d),
                            o -> o instanceof Double);
            cold_sweat_autumns = builder.comment("Autumn Temperatures divided into six periods according to the solar term table.")
                    .defineListAllowEmpty("AutumnTemps",
                            () -> List.of(0.15d, 0.1d, 0.05d, 0d, -0.1d, -0.2d),
                            o -> o instanceof Double);
            cold_sweat_winters = builder.comment("Winter Temperatures divided into six periods according to the solar term table.")
                    .defineListAllowEmpty("WinterTemps",
                            () -> List.of(-0.3d, -0.35d, -0.35d, -0.5d, -0.45d, -0.4d),
                            o -> o instanceof Double);
            builder.pop();
        }
    }
}
