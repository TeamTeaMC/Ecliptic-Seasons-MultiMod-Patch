package com.teamtea.eclipticseasons_patch.modules.mcwroofs;

import com.mojang.datafixers.util.Pair;
import com.teamtea.eclipticseasons.api.data.season.SnowDefinition;
import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.LangUtil;
import com.teamtea.eclipticseasons_patch.common.SnowRegistryHolder;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

@ESPatch(mods = MCW_R.MOD_ID, esVersion = "0.12.0-pre15")
public class MCW_R implements IESModPatch {
    public static final String MOD_ID = "mcwroofs";

    @Override
    public void common(ModConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    @Override
    public void init() {
        var matcher = List.of(SnowDefinition.PropertyTester.builder().name("water")
                .matcher(SnowDefinition.ExactMatcher.builder().value("false").build()).build());
        var condition = SnowRegistryHolder.builder()
                .modId(MOD_ID)
                .condition(Config.enable::get);
        SnowRegistryHolder.add(condition.nameMatcher(string -> string.contains("roof")
                        && !string.contains("attic")
                        && !string.contains("grass"))
                .simple(SnowRegistryHolder::getInfoSolid)
        );
        SnowRegistryHolder.add(condition.nameMatcher(string -> string.contains("awning"))
                .simple(SnowRegistryHolder::getInfoSolid)
        );

        SnowRegistryHolder.add(condition.nameMatcher(string -> string.contains("gutter"))
                .pair(() -> Pair.of(SnowRegistryHolder.getInfo(), matcher))
        );
    }

    public static class Config {

        public static ModConfigSpec.BooleanValue enable;

        public static void load(ModConfigSpec.Builder builder) {
            builder.comment(LangUtil.getModName(MOD_ID)).push(MOD_ID);
            enable = builder.comment("Enable automatic snow-covered blocks and models.")
                    .define("Enable", true);
            builder.pop();
        }
    }
}
