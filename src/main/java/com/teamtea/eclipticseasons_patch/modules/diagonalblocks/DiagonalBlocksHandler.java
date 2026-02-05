package com.teamtea.eclipticseasons_patch.modules.diagonalblocks;

import com.teamtea.eclipticseasons.api.data.season.SnowDefinition;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons.common.core.snow.SnowChecker;
import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DiagonalBlocksHandler {
    public static final DiagonalBlocksHandler INSTANCE = new DiagonalBlocksHandler();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onTagsUpdatedEvent(TagsUpdatedEvent tagsUpdatedEvent) {
        if (!DB_FWW.Config.enable.get()) return;
        try {
            Class<?> clazz = Class.forName("fuzs.diagonalblocks.api.v2.DiagonalBlockType");
            Set<?> types = (Set<?>) clazz.getField("TYPES").get(null);

            if (types == null) {
                EclipticSeasonsPatch.logger("Diagonal Blocks Mod was loaded but we get nothing from it.");
                return;
            }

            SnowDefinition.Info info = SnowDefinition.Info.builder()
                    .snowPassable(true)
                    .flag(MapChecker.FLAG_CUSTOM)
                    .build();

            for (Object firstType : types) {
                @SuppressWarnings("unchecked")
                Map<Block, Block> biMap = (Map<Block, Block>) firstType.getClass()
                        .getMethod("getBlockConversions")
                        .invoke(firstType);

                biMap.forEach((block, block2) -> {
                    if (block instanceof WallBlock
                            && DB_FWW.Config.wall.get()) {
                        SnowChecker.SNOW_DEFINITION_MAP.putIfAbsent(
                                block2, List.of(SnowDefinition.builder()
                                        .blocks(HolderSet.direct(block2.builtInRegistryHolder()))
                                        .info(info)
                                        .build())
                        );
                    } else if (block instanceof FenceBlock
                            && DB_FWW.Config.fence.get()) {
                        SnowChecker.SNOW_DEFINITION_MAP.putIfAbsent(
                                block2, List.of(SnowDefinition.builder()
                                        .blocks(HolderSet.direct(block2.builtInRegistryHolder()))
                                        .info(info)
                                        .build())
                        );
                    }
                });
            }
        } catch (IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException
                 | NoSuchFieldException
                 | ClassNotFoundException
                 | NullPointerException
                 | ClassCastException e) {
            EclipticSeasonsPatch.logger(e);
        }

    }

}
