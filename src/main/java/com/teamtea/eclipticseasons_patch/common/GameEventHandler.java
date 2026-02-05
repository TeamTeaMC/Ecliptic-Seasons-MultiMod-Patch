package com.teamtea.eclipticseasons_patch.common;


import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.TagsUpdatedEvent;

@EventBusSubscriber
public class GameEventHandler {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onTagsUpdatedEvent(TagsUpdatedEvent event) {
        SnowRegistryHolder.checkAndAdd(BuiltInRegistries.BLOCK);
    }

}
