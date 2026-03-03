package com.teamtea.eclipticseasons_patch.modules.subtle_effects;

import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.constant.climate.ISnowTerm;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.LangUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;

@ESPatch(mods = SE.MOD_ID)
public class SE implements IESModPatch {
    public static final String MOD_ID = "subtle_effects";

    @Override
    public void common(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment(LangUtil.getModName(MOD_ID)).push(MOD_ID);
            enable = builder
                    .define("Enable", true);
            builder.pop();
        }
    }

    public static class Hook {
        public static boolean maySnow(Level level, BlockPos pos) {
            Biome biome = MapChecker.getSurfaceBiome(level, pos).value();
            boolean server = level instanceof ServerLevel;
            ISnowTerm snowTerm = SolarTerm.getSnowTerm(biome, server, EclipticUtil.getSnowTempChange(level));
            SolarTerm solarTerm = EclipticSeasonsApi.getInstance().getSolarTerm(level);
            return snowTerm.maySnow(solarTerm, biome, pos, server);
        }
    }
}
