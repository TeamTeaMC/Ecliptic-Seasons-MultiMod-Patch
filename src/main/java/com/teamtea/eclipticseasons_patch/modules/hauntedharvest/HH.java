package com.teamtea.eclipticseasons_patch.modules.hauntedharvest;

import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.config.CommonConfig;
import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

@ESPatch(mods = "hauntedharvest",esVersion = "0.12.0-pre19-1")
public class HH implements IESModPatch {

    @Override
    public void common(ModConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ModConfigSpec.BooleanValue enable;
        public static ModConfigSpec.ConfigValue<List<? extends String>> hauntedharvest_halloween_time;
        public static ModConfigSpec.ConfigValue<List<? extends String>> hauntedharvest_mobs_wear_pumpkins_time;

        public static void load(ModConfigSpec.Builder builder) {
            builder.comment("Haunted Harvest").push("hauntedharvest");
            enable = builder.define("Enable", true);
            hauntedharvest_halloween_time = builder.comment("Solar Terms in which Haunted Harvest villager AI behaviors will be active.")
                    .defineListAllowEmpty("Halloween Time",
                            () -> List.of(
                                    SolarTerm.COLD_DEW.toString(),
                                    SolarTerm.FIRST_FROST.toString()),
                            SolarTerm.COLD_DEW::toString,
                            CommonConfig::validSolarTerm);
            hauntedharvest_mobs_wear_pumpkins_time = builder.comment("Adds custom times in which mobs can wear pumpkins. Leave empty to ignore.")
                    .defineListAllowEmpty(" Mobs Wear Pumpkins Time",
                            () -> List.of(SolarTerm.FIRST_FROST.toString()),
                            SolarTerm.FIRST_FROST::toString,
                            CommonConfig::validSolarTerm);
            builder.pop();
        }
    }
}
