package com.teamtea.eclipticseasons_patch.modules.snowyspirit;

import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.config.CommonConfig;
import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

@ESPatch(mods = "snowyspirit",esVersion = "0.12.0-pre19-1")
public class SS implements IESModPatch {

    @Override
    public void common(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> snowyspirit_winters;
        public static ForgeConfigSpec.BooleanValue specialTime;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment("Snowy Spirit").push("snowyspirit");
            enable = builder.define("Enable", true);
            specialTime = builder.comment("Enable special time with SnowySpirit.")
                    .define("SpecialTime", true);
            snowyspirit_winters = builder.comment("Solar Terms in which SnowySpirit villager AI behaviors will be active.")
                    .defineListAllowEmpty("WinterTime",
                            () -> List.of(SolarTerm.BEGINNING_OF_WINTER.toString(),
                                    SolarTerm.LIGHT_SNOW.toString(),
                                    SolarTerm.HEAVY_SNOW.toString(),
                                    SolarTerm.WINTER_SOLSTICE.toString(),
                                    SolarTerm.LESSER_COLD.toString(),
                                    SolarTerm.GREATER_COLD.toString()),
                            CommonConfig::validSolarTerm);
            builder.pop();
        }
    }
}
