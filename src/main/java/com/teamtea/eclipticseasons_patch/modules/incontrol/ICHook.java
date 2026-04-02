package com.teamtea.eclipticseasons_patch.modules.incontrol;

import com.teamtea.eclipticseasons.EclipticSeasons;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.constant.solar.Season;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.common.core.crop.CropGrowthHandler;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import mcjty.incontrol.tools.rules.IEventQuery;
import mcjty.incontrol.tools.typed.Key;
import mcjty.incontrol.tools.typed.Type;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.*;

public class ICHook {

    public static final Key<String> VALID_TERMS = Key.create(Type.STRING, "valid_terms");
    public static final Key<String> SURFACE_BIOMES = Key.create(Type.STRING, "surface_biomes");

    public static Level fetchLevel(Object event, IEventQuery query) {
        LevelAccessor world = query.getWorld(event);
        if (world instanceof Level level) return level;
        if (world instanceof ServerLevelAccessor serverLevelAccessor) return serverLevelAccessor.getLevel();
        return WeatherManager.getMainServerLevel();
    }

    public record ValidTerms(Set<SolarTerm> solarTerms) {

        public boolean matches(Level level) {
            return !solarTerms.isEmpty() && solarTerms.contains(EclipticSeasonsApi.getInstance().getSolarTerm(level));
        }

        public static ValidTerms of(List<String> strings) {
            Set<SolarTerm> solarTermSet = new HashSet<>();
            for (String string : strings) {
                try {
                    SolarTerm s = SolarTerm.valueOf(string.toUpperCase(Locale.ROOT));
                    solarTermSet.add(s);
                } catch (IllegalArgumentException e) {
                    try {
                        SolarTerm start;
                        SolarTerm end;
                        String[] split = string.split("-");
                        if (split.length == 1) {
                            Season s = Season.valueOf(string.toUpperCase(Locale.ROOT));
                            start = s.getFirstSolarTerm();
                            end = s.getEndSolarTerm().getNextSolarTerm();
                        } else if (split.length == 2) {
                            start = SolarTerm.valueOf(split[0].toUpperCase(Locale.ROOT));
                            end = SolarTerm.valueOf(split[1].toUpperCase(Locale.ROOT)).getNextSolarTerm();
                        } else {
                            throw new IllegalArgumentException(string);
                        }
                        while (start != end) {
                            solarTermSet.add(start);
                            start = start.getNextSolarTerm();
                        }
                    } catch (IllegalArgumentException exception) {
                        EclipticSeasonsPatch.logger(exception);
                    }
                }
            }
            return new ValidTerms(EnumSet.copyOf(solarTermSet));
        }
    }

    public record SurfaceBiomeSet(HolderSet<Biome> biomes) {

        public boolean matches(Level level, @Nullable BlockPos pos) {
            if (pos == null) return false;
            if (!MapChecker.isLoadNearByOnlyServer(level, pos))
                return biomes.contains(CropGrowthHandler.getCropBiome(level,pos));
            return biomes.contains(MapChecker.getSurfaceBiome(level, pos));
        }

        public static SurfaceBiomeSet of(List<String> strings) {
            if (ServerLifecycleHooks.getCurrentServer() == null) return new SurfaceBiomeSet(HolderSet.direct());
            List<Holder<Biome>> biomeSet = new ArrayList<>();
            RegistryAccess.Frozen registryAccess1 = ServerLifecycleHooks.getCurrentServer().registryAccess();
            Registry<Biome> biomeRegistry = registryAccess1.registryOrThrow(Registries.BIOME);
            for (String string : strings) {
                if (string.startsWith("#")) {
                    Optional<HolderSet.Named<Biome>> tag = biomeRegistry.getTag(create(string.split("#")[1]));
                    if (tag.isPresent() && strings.size() == 1) return new SurfaceBiomeSet(tag.get());
                    tag.ifPresent(holders -> biomeSet.addAll(holders.stream().toList()));
                } else {
                    Optional<Holder.Reference<Biome>> holder = biomeRegistry.getHolder(createKey(string));
                    holder.ifPresent(biomeSet::add);
                }
            }
            return new SurfaceBiomeSet(HolderSet.direct(biomeSet));
        }

        private static ResourceKey<Biome> createKey(String pName) {
            return ResourceKey.create(Registries.BIOME, ResourceLocation.parse(pName));
        }

        private static TagKey<Biome> create(String pName) {
            return TagKey.create(Registries.BIOME, ResourceLocation.parse(pName));
        }
    }

    public static boolean validSeasonOrLocal(Level level, @Nullable BlockPos pos, Season season, boolean in) {
        Season now = pos == null ?
                EclipticSeasonsApi.getInstance().getSolarTerm(level).getSeason() :
                EclipticSeasonsApi.getInstance().getAgroSeason(level, pos);
        return in == (now == season);
    }
}
