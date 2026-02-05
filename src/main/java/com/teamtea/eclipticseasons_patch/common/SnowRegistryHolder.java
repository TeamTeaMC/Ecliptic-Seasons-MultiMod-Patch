package com.teamtea.eclipticseasons_patch.common;


import com.mojang.datafixers.util.Pair;
import com.teamtea.eclipticseasons.api.data.season.SnowDefinition;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons.common.core.snow.SnowChecker;
import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collectors;

@Data
@Builder
public class SnowRegistryHolder {
    public static final List<SnowRegistryHolder> SNOW_REGISTRY_HOLDERS = new ArrayList<>();

    @Getter
    public static final SnowDefinition.Info info = SnowDefinition.Info.builder().snowPassable(true).flag(MapChecker.FLAG_CUSTOM).build();
    @Getter
    public static final SnowDefinition.Info infoAO = SnowDefinition.Info.builder().snowPassable(true).flag(MapChecker.FLAG_CUSTOM_AO).build();
    @Getter
    public static final SnowDefinition.Info infoSolid = SnowDefinition.Info.builder().flag(MapChecker.FLAG_CUSTOM).build();
    @Getter
    public static final SnowDefinition.Info infoSolidAO = SnowDefinition.Info.builder().flag(MapChecker.FLAG_CUSTOM_AO).build();

    private final BooleanSupplier condition;
    private final String modId;
    @Builder.Default
    @Nullable
    private final Predicate<Holder<Block>> matcher = null;
    @Builder.Default
    @Nullable
    private final Predicate<String> nameMatcher = null;
    @Builder.Default
    @Nullable
    private final Class<?> classMatcher = null;
    @Builder.Default
    @Nullable
    private final Function<Holder<Block>, SnowDefinition> block = null;
    @Builder.Default
    @Nullable
    private final Supplier<SnowDefinition.Info> simple = null;
    @Builder.Default
    @Nullable
    private final Supplier<Pair<SnowDefinition.Info, List<SnowDefinition.PropertyTester>>> pair = null;

    public static void checkAndAdd(Registry<Block> blocks) {
        long start = System.nanoTime();

        List<SnowRegistryHolder> list = SNOW_REGISTRY_HOLDERS.stream().filter(s -> s.condition.getAsBoolean()).toList();

        Map<String, List<SnowRegistryHolder>> holdersByNamespace = list.stream()
                .filter(s -> s.condition.getAsBoolean())
                .collect(Collectors.groupingBy(h -> h.modId));
        int blockCount = 0;
        for (Holder.Reference<Block> holder : blocks.holders().toList()) {
            ResourceLocation key = holder.key().location();
            List<SnowRegistryHolder> candidates = holdersByNamespace.get(key.getNamespace());
            if (candidates == null) continue;
            String path = key.getPath();
            Block value = holder.value();
            Class<? extends Block> blockClass = value.getClass();
            for (SnowRegistryHolder sr : candidates) {
                if (sr.matcher != null && sr.matcher.test(holder)
                        || sr.nameMatcher != null && sr.nameMatcher.test(path)
                        || sr.classMatcher != null && blockClass.isAssignableFrom(sr.classMatcher)
                        || sr.matcher == null && sr.nameMatcher == null && sr.classMatcher == null) {
                    SnowDefinition def = sr.block != null ?
                            sr.block.apply(holder) :
                            sr.simple != null ?
                                    SnowDefinition.builder().blocks(HolderSet.direct(holder)).info(sr.simple.get()).build() :
                                    sr.pair != null ?
                                            SnowDefinition.builder().blocks(HolderSet.direct(holder)).info(sr.simple.get()).build() : null;
                    if (def != null) {
                        List<SnowDefinition> snowDefinitions = SnowChecker.SNOW_DEFINITION_MAP.putIfAbsent(value, List.of(def));
                        blockCount += snowDefinitions == null ? 1 : 0;
                        break;
                    }
                }
            }
        }

        long end = System.nanoTime();
        long elapsedMs = (end - start) / 1_000_000;
        EclipticSeasonsPatch.logger("[ [%s] x SnowDefinition] Registry scan took "
                .formatted(String.join(", ", holdersByNamespace.keySet().stream().sorted().toList()))
                + elapsedMs + " ms"
                + " for %s blocks".formatted(blockCount));

    }

    public static void add(SnowRegistryHolderBuilder snowRegistryHolderBuilder) {
        SNOW_REGISTRY_HOLDERS.add(snowRegistryHolderBuilder.build());
    }

    public static void add(SnowRegistryHolderBuilder... builders) {
        for (SnowRegistryHolderBuilder registryHolderBuilder : builders) {
            SNOW_REGISTRY_HOLDERS.add(registryHolderBuilder.build());
        }
    }
}
