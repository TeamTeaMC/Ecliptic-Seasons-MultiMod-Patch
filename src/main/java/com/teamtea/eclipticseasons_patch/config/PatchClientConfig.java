package com.teamtea.eclipticseasons_patch.config;

import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.modules.PatchCore;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class PatchClientConfig {

    public static final ForgeConfigSpec CLIENT_CONFIG = new ForgeConfigSpec.Builder().configure(PatchClientConfig::new).getRight();

    protected PatchClientConfig(ForgeConfigSpec.Builder builder) {
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
