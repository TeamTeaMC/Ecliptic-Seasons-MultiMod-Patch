package com.teamtea.eclipticseasons_patch.modules.immersive_weathering;

import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.LangUtil;
import com.teamtea.eclipticseasons_patch.config.PatchCommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@ESPatch(mods = IW.MOD_ID)
public class IW implements IESModPatch {
    public static final String MOD_ID = "immersive_weathering";

    @Override
    public void common(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    @Override
    public void register(IEventBus gameBus, IEventBus modEventBus) {
        modEventBus.register(Handler.INSTANCE);
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> growableTerms;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment(LangUtil.getModName(MOD_ID)).push(MOD_ID);
            enable = builder
                    .define("Enable", true);
            growableTerms = builder.comment(
                            "Blocks will only grow during the specified seasons.",
                            "Format: <namespace:block_growth_id>@<start_term>-<end_term>",
                            "Example: immersive_weathering:grass_block_plains@rain_water-summer_solstice",
                            "Use /ecliptic solar setTerm <term> to view available solar terms.",
                            "Note: This setting only affects configurable entries. Some built-in growth behaviors may ignore it."
                    )
                    .worldRestart()
                    .defineListAllowEmpty("SeasonalRestrictedGrowth",
                            Config::createGrowableLimited,
                            Config::validGrowableLimited);
            builder.pop();
        }

        private static boolean validGrowableLimited(Object object) {
            return createGrowableLimited(object) != null;
        }

        public static LimitRecord createGrowableLimited(Object object) {
            String s = object + "";
            String[] split = s.split("@");
            if (split.length == 2) {
                ResourceLocation parse;
                try {
                    parse = ResourceLocation.parse(split[0]);
                } catch (Exception e) {
                    return null;
                }
                String[] split1 = split[1].split("-");
                if (split1.length == 2) {
                    SolarTerm start;
                    SolarTerm end;
                    try {
                        start = SolarTerm.valueOf(split1[0].toUpperCase(Locale.ROOT));
                        end = SolarTerm.valueOf(split1[1].toUpperCase(Locale.ROOT));
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                    return new LimitRecord(parse, start, end);
                }
            }
            return null;
        }

        public static List<String> createGrowableLimited() {
            List<String> limitList = new ArrayList<>();
            String timer0 = "%s-%s".formatted(SolarTerm.BEGINNING_OF_SUMMER.getName(), SolarTerm.LESSER_HEAT.getName());
            for (String s : List.of(
                    "immersive_weathering:grass_block_taiga",
                    "immersive_weathering:grass_block_old_growth_spruce"
            )) {
                limitList.add("%s@%s".formatted(s, timer0));
            }
            String timer = "%s-%s".formatted(SolarTerm.RAIN_WATER.getName(), SolarTerm.SUMMER_SOLSTICE.getName());
            for (String s : List.of(
                    "immersive_weathering:grass_block_base",
                    "immersive_weathering:grass_block_plains",
                    "immersive_weathering:grass_block_sunflower_plains",
                    "immersive_weathering:grass_block_forest",
                    "immersive_weathering:grass_block_flower_forest",
                    "immersive_weathering:grass_block_birch_forest",
                    "immersive_weathering:grass_block_dark_forest",
                    "immersive_weathering:grass_block_cherry_grove",
                    "immersive_weathering:grass_block_swamp",
                    "immersive_weathering:grass_block_lush_caves"
            )) {
                limitList.add("%s@%s".formatted(s, timer));
            }
            return limitList;
        }

        public record LimitRecord(ResourceLocation id, SolarTerm start, SolarTerm end) {
            public boolean isValid(Level level) {
                return EclipticSeasonsApi.getInstance().getSolarTerm(level).isInTerms(start, end);
            }
        }

        public static Map<ResourceLocation, LimitRecord> recordMap = new HashMap<>();
    }

    public static class Handler {
        public static final Handler INSTANCE = new Handler();

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onModConfigEvent(ModConfigEvent event) {
            if (!(event instanceof ModConfigEvent.Unloading)
                    && event.getConfig().getSpec() == PatchCommonConfig.COMMON_CONFIG) {
                Config.recordMap = Config.growableTerms.get()
                        .stream()
                        .map(Config::createGrowableLimited)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toMap(
                                Config.LimitRecord::id,
                                Function.identity(),
                                (a, b) -> a,
                                HashMap::new
                        ));
            }
        }
    }
}
