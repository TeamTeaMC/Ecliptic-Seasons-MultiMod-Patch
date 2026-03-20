package com.teamtea.eclipticseasons_patch.modules.dynamictrees;

import com.dtteam.dynamictrees.api.season.SeasonProvider;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class EclipticSeasonProvider implements SeasonProvider {

    private float seasonValue = 1.0f;

    @Override
    public Float getSeasonValue(Level level, BlockPos pos) {
        return seasonValue;
    }

    @Override
    public void updateTick(Level level, long dayTime) {
        seasonValue = DT.Config.enable.get() ?
                EclipticSeasonsApi.getInstance().getSolarDays(level) / (6f * EclipticSeasonsApi.getInstance().getLastingDaysOfEachTerm(level)) :
                1f;
    }

    @Override
    public boolean shouldSnowMelt(Level level, BlockPos pos) {
        return false;
    }
}
