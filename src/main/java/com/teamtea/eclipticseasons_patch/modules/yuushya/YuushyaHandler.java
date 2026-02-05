package com.teamtea.eclipticseasons_patch.modules.yuushya;

import com.teamtea.eclipticseasons.api.data.season.SnowDefinition;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons.common.core.snow.SnowChecker;
import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class YuushyaHandler {
    public static final YuushyaHandler INSTANCE = new YuushyaHandler();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onTagsUpdatedEvent(TagsUpdatedEvent tagsUpdatedEvent) {
        if (!YST.Config.enable.get()) return;
        try {
            long start = System.nanoTime();

            Class<?> clazz = Class.forName("com.yuushya.registries.YuushyaRegistries");
            Object types = clazz.getField("BLOCKS").get(null);
            Class<?> clazz2 = types.getClass();
            Field field = clazz2.getDeclaredField("OBJECT_MAP");
            field.setAccessible(true);
            var objectMap = (Map<String, Supplier<Block>>) field.get(types);

            SnowDefinition.Info info = SnowDefinition.Info.builder().snowPassable(true).flag(MapChecker.FLAG_CUSTOM).build();
            SnowDefinition.Info infoSolid = SnowDefinition.Info.builder().flag(MapChecker.FLAG_CUSTOM).build();
            SnowDefinition.Info infoSolidAO = SnowDefinition.Info.builder().flag(MapChecker.FLAG_CUSTOM_AO).build();


            for (Supplier<Block> supplier : objectMap.values()) {
                Holder.Reference<Block> holder = supplier.get().builtInRegistryHolder();
                Block block = holder.get();
                ResourceLocation location = holder.key().location();
                // if (!location.getNamespace().equals("yuushya")) continue;
                String string = location.getPath();

                if (string.contains("fence")
                        || string.contains("side_slab")
                        || string.contains("handrail")) {
                    SnowChecker.SNOW_DEFINITION_MAP.putIfAbsent(block,  List.of(SnowDefinition.builder()
                            .blocks(HolderSet.direct(holder))
                            .info(info)
                            .build())
                    );
                } else if (string.contains("half_slab")
                        || string.contains("umbrella")) {
                    SnowChecker.SNOW_DEFINITION_MAP.putIfAbsent(block, List.of( SnowDefinition.builder()
                            .blocks(HolderSet.direct(holder))
                            .info(infoSolid)
                            .build())
                    );
                } else if (string.contains("ramp")
                        || string.contains("tile")
                        // todo
                        // || string.contains("roof")
                        || string.contains("stairs")
                        || string.contains("shed")) {
                    SnowChecker.SNOW_DEFINITION_MAP.putIfAbsent(block,  List.of(SnowDefinition.builder()
                            .blocks(HolderSet.direct(holder))
                            .info(infoSolidAO)
                            .build())
                    );
                }
            }

            long end = System.nanoTime();
            long elapsedMs = (end - start) / 1_000_000;
            EclipticSeasonsPatch.logger("[Yuushya Townscape x SnowDefinition] Registry scan took " + elapsedMs + " ms");
        } catch (NullPointerException | ClassCastException | NoSuchFieldException | ClassNotFoundException |
                 IllegalAccessException e) {
            EclipticSeasonsPatch.logger(e);
        }
    }


}
