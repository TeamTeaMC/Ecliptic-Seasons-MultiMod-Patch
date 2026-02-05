package com.teamtea.eclipticseasons_patch;


import com.teamtea.eclipticseasons_patch.config.PatchClientConfig;
import com.teamtea.eclipticseasons_patch.config.PatchCommonConfig;
import com.teamtea.eclipticseasons_patch.data.PatchData;
import com.teamtea.eclipticseasons_patch.modules.PatchCore;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EclipticSeasonsPatch.MODID)
public class EclipticSeasonsPatch {
    public static final String MODID = "eclipticseasons_multimodpatch";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger(EclipticSeasonsPatch.MODID);
    public static final String NETWORK_VERSION = "1.0";

    public static void logger(String x) {
        // 通过它可以判断是否在哪个服务器
        // ServerLifecycleHooks.getCurrentServer()
        // if (!FMLEnvironment.production||General.bool.get())
        {
//            LOGGER.debug(x);
            LOGGER.info(x);
        }
    }

    public static void logger(Object... x) {
        extraLogger(false, x);
    }

    public static void extraLogger(boolean debug, Object... x) {

        // if (!FMLEnvironment.production||General.bool.get())
        {
            StringBuilder output = new StringBuilder();

            for (Object i : x) {
                if (i == null) output.append(", ").append("null");
                else if (i.getClass().isArray()) {
                    output.append(", [");
                    if (i instanceof Object[] objects) {
                        for (Object c : objects) {
                            output.append(c).append(",");
                        }
                    } else if (i instanceof float[] objects) {
                        for (float c : objects) {
                            output.append(c).append(",");
                        }
                    } else if (i instanceof int[] objects) {
                        for (int c : objects) {
                            output.append(c).append(",");
                        }
                    } else if (i instanceof double[] objects) {
                        for (double c : objects) {
                            output.append(c).append(",");
                        }
                    } else if (i instanceof long[] objects) {
                        for (long c : objects) {
                            output.append(c).append(",");
                        }
                    } else if (i instanceof boolean[] objects) {
                        for (boolean c : objects) {
                            output.append(c).append(",");
                        }
                    }
                    output.append("]");
                } else if (i instanceof List list) {
                    output.append(", [");
                    for (Object c : list) {
                        output.append(c);
                    }
                    output.append("]");
                } else
                    output.append(", ").append(i);
            }
            if (debug) {
                LOGGER.debug(output.substring(1));
            } else {
                LOGGER.info(output.substring(1));
            }
        }

    }


    public EclipticSeasonsPatch(IEventBus modEventBus, ModContainer modContainer) {
        PatchCore.run();
        // if(false)
        // throw new ModLoadingException(List.of(ModLoadingIssue.error().withCause()));
        modEventBus.addListener(this::gatherData);
        modEventBus.addListener(this::FMLCommonSetup);

        modEventBus.addListener(PatchCommonConfig::UpdateConfig);
        modEventBus.addListener(PatchClientConfig::UpdateConfig);

        modContainer.registerConfig(ModConfig.Type.COMMON, PatchCommonConfig.COMMON_CONFIG);
        modContainer.registerConfig(ModConfig.Type.CLIENT, PatchClientConfig.CLIENT_CONFIG);

        if (FMLLoader.getDist() == Dist.CLIENT)
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);


        PatchCore.register(NeoForge.EVENT_BUS, modEventBus);
    }

    @SuppressWarnings("removal")
    public static ResourceLocation rl(String id) {
        return ResourceLocation.fromNamespaceAndPath(MODID, id);
    }

    public void FMLCommonSetup(final FMLCommonSetupEvent event) {
    }

    public void gatherData(final GatherDataEvent event) {
        PatchData.dataGen(event);
    }

}
