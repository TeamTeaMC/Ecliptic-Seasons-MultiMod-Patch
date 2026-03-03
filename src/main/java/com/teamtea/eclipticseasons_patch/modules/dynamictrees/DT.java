package com.teamtea.eclipticseasons_patch.modules.dynamictrees;


import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.ModConfigSpec;

@ESPatch(mods = "dynamictrees")
public class DT implements IESModPatch {

    @Override
    public void common(ModConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    @Override
    public void register(IEventBus gameBus, IEventBus modEventBus) {
        DynamicTreeMod.init();
    }

    public static class Config {

        public static ModConfigSpec.BooleanValue enable;

        public static void load(ModConfigSpec.Builder builder) {
            builder.comment("DynamicTrees").push("dynamictrees");
            enable = builder.gameRestart().define("Enable", true);
            builder.pop();
        }
    }
}

