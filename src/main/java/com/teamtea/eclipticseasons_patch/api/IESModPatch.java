package com.teamtea.eclipticseasons_patch.api;

import com.teamtea.eclipticseasons.compat.Platform;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.ModConfigSpec;

public interface IESModPatch {

    default void client(ModConfigSpec.Builder consumer) {
    }


    default void common(ModConfigSpec.Builder consumer) {
    }


    default void register(IEventBus gameBus, IEventBus modEventBus) {
    }


    default void init() {
    }



}
