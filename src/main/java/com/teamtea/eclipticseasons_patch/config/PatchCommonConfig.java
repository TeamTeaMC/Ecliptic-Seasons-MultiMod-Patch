package com.teamtea.eclipticseasons_patch.config;

import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.modules.PatchCore;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class PatchCommonConfig {
    public static final ForgeConfigSpec COMMON_CONFIG = new ForgeConfigSpec.Builder().configure(PatchCommonConfig::new).getRight();

    protected PatchCommonConfig(ForgeConfigSpec.Builder builder) {
        for (IESModPatch modPlugin : PatchCore.MOD_PLUGINS) {
            modPlugin.common(builder);
        }
    }

    public static void UpdateConfig(ModConfigEvent modConfigEvent) {
        if (!(modConfigEvent instanceof ModConfigEvent.Unloading)
                && modConfigEvent.getConfig().getSpec() == COMMON_CONFIG) {
        }
    }

    public static boolean validSeason(Object o) {
        if (o instanceof String s) {
            try {
                com.teamtea.eclipticseasons.api.constant.solar.Season.valueOf(s);
                return true;
            } catch (IllegalArgumentException ignored) {
            }
        }
        return o instanceof com.teamtea.eclipticseasons.api.constant.solar.Season;
    }

    public static boolean validSolarTerm(Object o) {
        if (o instanceof String s) {
            try {
                com.teamtea.eclipticseasons.api.constant.solar.SolarTerm.valueOf(s);
                return true;
            } catch (IllegalArgumentException ignored) {
            }
        }
        return o instanceof com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
    }
}

