package com.teamtea.eclipticseasons_patch.modules.simpleclouds;

import com.teamtea.eclipticseasons.api.event.BeforeCheckSnowStatusEvent;
import com.teamtea.eclipticseasons.config.CommonConfig;
import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import dev.nonamecrackers2.simpleclouds.common.api.SimpleCloudsAPIImpl;
import dev.nonamecrackers2.simpleclouds.common.world.CloudManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.lang.reflect.Field;


@ESPatch(mods = "simpleclouds", esVersion = "0.12.3.6")
public class SC implements IESModPatch {

    @Override
    public void common(ModConfigSpec.Builder consumer) {
        Config.load(consumer);
    }


    @Override
    public void register(IEventBus gameBus, IEventBus modEventBus) {
        gameBus.register(Handler.INSTANCE);
    }

    public static class Config {

        public static ModConfigSpec.BooleanValue enable;

        public static void load(ModConfigSpec.Builder builder) {
            builder.comment("Simple Clouds", "This is just a simple processing routine and is not fully compatible.").push("simpleclouds");
            enable = builder.define("Enable", true);
            builder.pop();
        }
    }

    public static class Handler {
        public static final Handler INSTANCE = new Handler();

        @SubscribeEvent
        public void onChunkEvent(BeforeCheckSnowStatusEvent event) {
            if (!Config.enable.get()) return;
            ServerLevel level = event.getLevel();
            BlockPos pos = event.getPos();
            if ((Object) SimpleCloudsAPIImpl.INSTANCE.getCloudManager(level) instanceof CloudManager<?> cloudManager) {
                if (CommonConfig.Snow.forceChunkUpdate.get())
                    CommonConfig.Snow.forceChunkUpdate.set(false);
                if (!CommonConfig.Snow.forceChunkUpdateOnlyWhenMelt.get())
                    CommonConfig.Snow.forceChunkUpdateOnlyWhenMelt.set(true);
                if (!CommonConfig.isSnowInWorld()) {
                    try {
                        Class<?> clazz = CommonConfig.class;
                        Field field = clazz.getDeclaredField("snowInWorld");
                        field.setAccessible(true);
                        field.setBoolean(null, true);
                    } catch (Exception ignore) {
                    }
                }
                if (pos != BlockPos.ZERO) {
                    boolean hasPrecipitationAt = cloudManager.hasPrecipitationAt(pos.above());
                    event.setRain(hasPrecipitationAt);
                }
            }
        }

    }
}