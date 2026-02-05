package com.teamtea.eclipticseasons_patch.config;

import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.modules.PatchCore;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class PatchClientConfig {

    public static final ModConfigSpec CLIENT_CONFIG = new ModConfigSpec.Builder().configure(PatchClientConfig::new).getRight();

    protected PatchClientConfig(ModConfigSpec.Builder builder) {
        for (IESModPatch modPlugin : PatchCore.MOD_PLUGINS) {
            modPlugin.client(builder);
        }
    }

    public static void UpdateConfig(ModConfigEvent modConfigEvent) {
        if (!(modConfigEvent instanceof ModConfigEvent.Unloading)
                && modConfigEvent.getConfig().getSpec() == CLIENT_CONFIG) {
        }
    }
}
