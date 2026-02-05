package com.teamtea.eclipticseasons_patch.modules.fetzisasiandeco;

import com.teamtea.eclipticseasons.api.data.season.SnowDefinition;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons.common.core.snow.SnowChecker;
import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class FetzisHandler {
    public static final FetzisHandler INSTANCE = new FetzisHandler();

    @SubscribeEvent(priority = EventPriority.LOW)
    @SuppressWarnings("unchecked")
    public void onTagsUpdatedEvent(TagsUpdatedEvent tagsUpdatedEvent) {
        if(!FAD.Config.enable.get())return;
        try {
            long start = System.nanoTime();
            String packageName = "io.github.lordfetzi.blocks";

            Set<Class<?>> classes = getClassType(packageName, List.of("HandRail", "Panel", "VerticalSlab"));
            Set<Class<?>> classesSolid = getClassType(packageName, List.of("RoofTop", "TallRoofTop", "ZenBlock"));

            Class<?> initClass = Class.forName("io.github.lordfetzi.init.blockInit");
            Field blocksField = initClass.getDeclaredField("BLOCKS");
            blocksField.setAccessible(true);
            Object deferredRegister = blocksField.get(null);
            Method iteratorMethod = deferredRegister.getClass().getMethod("iterator");
            Iterator<Holder<Block>> iterator = (Iterator<Holder<Block>>) iteratorMethod.invoke(deferredRegister);

            SnowDefinition.Info info = SnowDefinition.Info.builder().snowPassable(true).flag(MapChecker.FLAG_CUSTOM).build();
            SnowDefinition.Info infoSolid = SnowDefinition.Info.builder().flag(MapChecker.FLAG_CUSTOM_AO).build();

            while (iterator.hasNext()) {
                var holder = iterator.next();
                Block block = holder.value();
                if (!checkif(classes, block, holder, info)) {
                    checkif(classesSolid, block, holder, infoSolid);
                }
            }
            long end = System.nanoTime();
            long elapsedMs = (end - start) / 1_000_000;
            EclipticSeasonsPatch.logger("[FetzisHandler x SnowDefinition] Registry scan took " + elapsedMs + " ms");
        } catch (NullPointerException | ClassCastException | NoSuchFieldException | IllegalAccessException |
                 ClassNotFoundException
                 | InvocationTargetException | NoSuchMethodException e) {
            EclipticSeasonsPatch.logger(e);
        }
    }

    private static boolean checkif(Set<Class<?>> classesSolid, Block block, Holder<Block> holder, SnowDefinition.Info infoSolid) {
        for (Class<?> aClass : classesSolid) {
            if (aClass.isInstance(block)) {
                SnowChecker.SNOW_DEFINITION_MAP.putIfAbsent(block, List.of(SnowDefinition.builder()
                        .blocks(HolderSet.direct(holder))
                        .info(infoSolid)
                        .build())
                );
                return true;
            }
        }
        return false;
    }

    private static HashSet<Class<?>> getClassType(String packageName, List<String> stringsSolid) {
        var classesSolid = new HashSet<Class<?>>();
        for (String s : stringsSolid) {
            try {
                Class<?> clazz = Class.forName(packageName + "." + s);
                classesSolid.add(clazz);
            } catch (ClassNotFoundException ignored) {
            }
        }
        return classesSolid;
    }

}
