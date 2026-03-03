package com.teamtea.eclipticseasons_patch.modules.particlerain;

import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import com.teamtea.eclipticseasons.compat.vanilla.VanillaWeather;
import com.teamtea.eclipticseasons.config.CommonConfig;
import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.LangUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.Tags;

@ESPatch(mods = PR.MOD_ID, minVersions = "0.12.0-pre11-1", clientOnly = true)
public class PR implements IESModPatch {
    public static final String MOD_ID = "particlerain";

    @Override
    public void client(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;
        public static ForgeConfigSpec.BooleanValue fixSand;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment(LangUtil.getModName(MOD_ID)).push(MOD_ID);
            enable = builder
                    .define("Enable", true);
            fixSand = builder
                    .comment("When it rains in desert biomes, replace it with a sandstorm.")
                    .define("FixSand", true);
            builder.pop();
        }
    }

    public static class Hook {

        public static Biome.Precipitation getPrecipitation(Biome instance, BlockPos pos, ClientLevel level, Holder<Biome> biomeHolder) {
            boolean hasLocalWeather = EclipticSeasonsApi.getInstance().hasLocalWeather(level);
            Biome.Precipitation precipitationAt =
                    hasLocalWeather ?
                            EclipticUtil.getRainOrSnow(level, instance, pos) :
                            VanillaWeather.getRainOrSnow(level, instance, pos);
            if (!CommonConfig.Weather.notRainInDesert.get()
                    && Config.fixSand.get() && precipitationAt == Biome.Precipitation.RAIN && hasLocalWeather) {
                if (instance.getModifiedClimateSettings().downfall() == 0 && (biomeHolder.is(Tags.Biomes.IS_DESERT) || biomeHolder.is(BiomeTags.IS_BADLANDS)))
                    precipitationAt = Biome.Precipitation.NONE;
            }
            return precipitationAt;
        }
    }

}
