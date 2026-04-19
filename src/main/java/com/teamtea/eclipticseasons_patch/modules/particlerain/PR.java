package com.teamtea.eclipticseasons_patch.modules.particlerain;

import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.LangUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.ModConfigSpec;

@ESPatch(mods = PR.MOD_ID,minVersions = "0.12.0-pre11-1",clientOnly = true)
public class PR implements IESModPatch {
    public static final String MOD_ID = "particlerain";

    @Override
    public void client(ModConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    public static class Config {

        public static ModConfigSpec.BooleanValue enable;

        public static void load(ModConfigSpec.Builder builder) {
            builder.comment(LangUtil.getModName(MOD_ID)).push(MOD_ID);
            enable = builder
                    .gameRestart().define("Enable", true);
            builder.pop();
        }
    }

    public static class Hook {

        public static Biome.Precipitation getPrecipitation(Biome instance, BlockPos pos, ClientLevel level, Holder<Biome> biomeHolder) {
            return WeatherManager.getPrecipitationAt(level, biomeHolder.value(), pos);
        }
    }

}
