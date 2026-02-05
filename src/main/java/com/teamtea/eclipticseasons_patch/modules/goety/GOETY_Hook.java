package com.teamtea.eclipticseasons_patch.modules.goety;

import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;

public class GOETY_Hook {

    // public static void setIfHasLocalWeather(ServerLevel instance, int pClearTime, int pWeatherTime, BlockPos pos) {
    //     ArrayList<WeatherManager.BiomeWeather> biomeList = WeatherManager.getBiomeList(instance);
    //     WeatherManager.BiomeWeather biomeWeather = WeatherManager.getBiomeWeather(instance, MapChecker.getSurfaceBiome(instance,pos));
    //     if (biomeList != null) {
    //         biomeWeather.clearTime = pClearTime / biomeList.size();
    //         biomeWeather.rainTime = pWeatherTime / biomeList.size();
    //         biomeWeather.thunderTime = pWeatherTime / biomeList.size();
    //     }
    // }
}
