package com.teamtea.eclipticseasons_patch.modules.feywild;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;

@ESPatch(mods = "feywild")
public class FW implements IESModPatch {

    @Override
    public void common(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    @Override
    public void register(IEventBus gameBus, IEventBus modEventBus) {
        modEventBus.register(FWEvent.INSTANCE);
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment("Fey Wild").push("feywild");
            enable = builder.define("Enable", true);
            builder.pop();
        }
    }
}
