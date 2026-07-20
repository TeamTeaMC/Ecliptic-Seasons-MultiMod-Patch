package com.teamtea.eclipticseasons_patch.modules.grassiergrass;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.LangUtil;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.ModConfigSpec;


@ESPatch(mods = GG.MOD_ID)
public class GG implements IESModPatch {
    public static final String MOD_ID = "grassiergrass";


    @Override
    public void common(ModConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    // @Override
    // public void register(IEventBus gameBus, IEventBus modEventBus) {
    //     if (FMLEnvironment.dist == Dist.CLIENT)
    //         gameBus.register(GrassierGrassHandler.INSTANCE);
    // }

    public static class Config {

        public static ModConfigSpec.BooleanValue enable;
        public static ModConfigSpec.IntValue snowyGrassColor;


        public static void load(ModConfigSpec.Builder builder) {
            builder.comment(LangUtil.getModName(MOD_ID)).push(MOD_ID);
            enable = builder
                    .define("Enable", true);
            snowyGrassColor = builder
                    .defineInRange("SnowyGrassColor", 0xB9EBFF, Integer.MIN_VALUE, Integer.MAX_VALUE);
            builder.pop();
        }
    }

    public static class Hook {
        public static int blendColor(int color1, int color2, int ratio) {
            // ratio: 0-100

            int r = (color1 >> 16) & 0xFF;
            int g = (color1 >> 8) & 0xFF;
            int b = color1 & 0xFF;

            int gray = (r * 30 + g * 59 + b * 11) / 100;

            int snowR = (color2 >> 16) & 0xFF;
            int snowG = (color2 >> 8) & 0xFF;
            int snowB = color2 & 0xFF;

            int baseR = (r * 30 + gray * 70) / 100;
            int baseG = (g * 30 + gray * 70) / 100;
            int baseB = (b * 30 + gray * 70) / 100;

            r = (baseR * (100 - ratio) + snowR * ratio) / 100;
            g = (baseG * (100 - ratio) + snowG * ratio) / 100;
            b = (baseB * (100 - ratio) + snowB * ratio) / 100;

            return (r << 16) | (g << 8) | b;
        }

        public static int getSnowColor() {
            return Config.snowyGrassColor.getAsInt();
        }
    }
}
