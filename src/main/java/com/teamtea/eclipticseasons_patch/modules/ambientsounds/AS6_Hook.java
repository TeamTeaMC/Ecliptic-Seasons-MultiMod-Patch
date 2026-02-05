package com.teamtea.eclipticseasons_patch.modules.ambientsounds;

import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.constant.climate.ISnowTerm;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import team.creative.ambientsounds.environment.AmbientEnvironment;

public class AS6_Hook {
    public static float getTempAndFixFlag(AmbientEnvironment ae, Player player, Level level) {
        BlockPos pos = player.blockPosition();
        Holder<Biome> biomeHolder = EclipticSeasonsApi.getInstance().hasLocalWeather(level) ?
                MapChecker.getSurfaceBiome(level, pos) : level.getBiome(pos);
        Biome.Precipitation currentPrecipitationAt = EclipticSeasonsApi.getInstance().getCurrentPrecipitationAt(level, pos);
        float baseTemperature = EclipticUtil.getTemperatureFloat(level, biomeHolder.value(), pos);
        SolarTerm solarTerm = EclipticSeasonsApi.getInstance().getSolarTerm(level);
        ISnowTerm snowTerm = SolarTerm.getSnowTerm(biomeHolder.value(), false, EclipticUtil.getSnowTempChange(level));
        if (snowTerm.maySnow(solarTerm, biomeHolder.value(), pos, false)) {
            // baseTemperature = EclipticUtil.getTemperatureFloat(level, biomeHolder.value(), pos);
            if (currentPrecipitationAt == Biome.Precipitation.SNOW)
                baseTemperature = Math.min(baseTemperature,
                        solarTerm == snowTerm.getStart() ? 0.2f : 0.1f);
            else if (EclipticSeasonsApi.getInstance().getPrecipitationAt(level, pos) == Biome.Precipitation.SNOW)
                baseTemperature = Math.min(baseTemperature,
                        solarTerm == snowTerm.getStart() ? 0.3f : 0.15f);
        }
        ae.snowing = currentPrecipitationAt == Biome.Precipitation.SNOW;
        ae.thundering = EclipticSeasonsApi.getInstance().isThundering(level, pos);
        // this.raining = EclipticSeasonsApi.getInstance().isRainingOrSnowing(level, pos);
        return baseTemperature;
    }
}
