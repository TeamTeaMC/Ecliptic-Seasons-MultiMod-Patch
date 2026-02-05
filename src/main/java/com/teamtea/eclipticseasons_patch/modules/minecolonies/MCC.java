package com.teamtea.eclipticseasons_patch.modules.minecolonies;

import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ModConfigSpec;

@ESPatch(mods = "minecolonies")
public class MCC implements IESModPatch {

    @Override
    public void common(ModConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ModConfigSpec.BooleanValue enable;
        public static ModConfigSpec.IntValue averageNightTime;
        public static ModConfigSpec.IntValue checkSleepOffset;

        public static void load(ModConfigSpec.Builder builder) {
            builder.comment("MineColonies")
                    .push("minecolonies");
            enable = builder
                    .define("Enable", true);
            averageNightTime = builder
                    .defineInRange("AverageNightTime", 12000, 1, 23999);
            checkSleepOffset = builder
                    .defineInRange("CheckSleepOffset", 1000, -6000, 6000);
            builder.pop();
        }
    }

    public static class Hook {
        public static int getGoSleepOffset(Level level) {
            return EclipticSeasonsApi.getInstance().getNightTime(level) - Config.averageNightTime.get();
        }

        public static boolean isDay(Level level) {
            long l = level.getDayTime() % 24000L;
            int checkSleepTime = EclipticSeasonsApi.getInstance().getNightTime(level)
                    - Config.checkSleepOffset.get();
            return l < checkSleepTime;
        }
    }
}
