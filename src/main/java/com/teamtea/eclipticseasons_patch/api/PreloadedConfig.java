package com.teamtea.eclipticseasons_patch.api;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.teamtea.eclipticseasons.EclipticSeasons;
import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PreloadedConfig {
    private static CommentedFileConfig commonConfig;
    private static CommentedFileConfig clientConfig;

    public static void onLoad(String mixinPackage) {
        commonConfig = getCommentedFileConfig(ModConfig.Type.COMMON);
        clientConfig = getCommentedFileConfig(ModConfig.Type.CLIENT);
    }

    private static @NotNull CommentedFileConfig getCommentedFileConfig(ModConfig.Type type) {
        CommentedFileConfig oldConfig = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve(EclipticSeasons.defaultConfigName(type, EclipticSeasonsPatch.MODID)))
                .preserveInsertionOrder().build();
        oldConfig.load();
        oldConfig.close();
        return oldConfig;
    }

    public static boolean shouldApply(String modid) {
        if (commonConfig.get(List.of(modid, "Enable")) instanceof Boolean b2) return b2;
        if (clientConfig.get(List.of(modid, "Enable")) instanceof Boolean b2) return b2;
        return true;
    }
}
