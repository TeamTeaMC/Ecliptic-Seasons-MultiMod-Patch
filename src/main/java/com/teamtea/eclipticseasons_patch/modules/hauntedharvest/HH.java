package com.teamtea.eclipticseasons_patch.modules.hauntedharvest;

import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.config.PatchCommonConfig;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

@ESPatch(mods = "hauntedharvest",esVersion = "0.12.0-pre19-1")
public class HH implements IESModPatch {

    @Override
    public void common(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> hauntedharvest_halloween_time;
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> hauntedharvest_mobs_wear_pumpkins_time;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment("Haunted Harvest").push("hauntedharvest");
            enable = builder.define("Enable", true);
            hauntedharvest_halloween_time = builder.comment("Solar Terms in which Haunted Harvest villager AI behaviors will be active.")
                    .defineListAllowEmpty("Halloween Time",
                            () -> List.of(
                                    SolarTerm.COLD_DEW.toString(),
                                    SolarTerm.FIRST_FROST.toString()),
                            PatchCommonConfig::validSolarTerm);
            hauntedharvest_mobs_wear_pumpkins_time = builder.comment("Adds custom times in which mobs can wear pumpkins. Leave empty to ignore.")
                    .defineListAllowEmpty(" Mobs Wear Pumpkins Time",
                            () -> List.of(SolarTerm.FIRST_FROST.toString()),
                            PatchCommonConfig::validSolarTerm);
            builder.pop();
        }
    }
}
