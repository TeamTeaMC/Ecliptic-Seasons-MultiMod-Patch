package com.teamtea.eclipticseasons_patch.common;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class GameEventHandler {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onTagsUpdatedEvent(TagsUpdatedEvent event) {
        SnowRegistryHolder.checkAndAdd(BuiltInRegistries.BLOCK);
    }

}
