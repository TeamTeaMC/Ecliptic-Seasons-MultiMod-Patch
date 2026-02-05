package com.teamtea.eclipticseasons_patch.modules.dynamictrees;


import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;

@ESPatch(mods = "dynamictrees")
public class DT implements IESModPatch {

    @Override
    public void common(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    @Override
    public void register(IEventBus gameBus, IEventBus modEventBus) {
        DynamicTreeMod.init();
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment("DynamicTrees").push("dynamictrees");
            enable = builder.define("Enable", true);
            builder.pop();
        }
    }
}

