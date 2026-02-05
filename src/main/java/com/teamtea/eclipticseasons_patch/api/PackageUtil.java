package com.teamtea.eclipticseasons_patch.api;

import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;

public class PackageUtil {

    public static ResourceLocation getPack(String modid) {
        return EclipticSeasonsPatch.rl(getPackID(modid));
    }

    public static String getPackID(String modid) {
        return "es_patch_" + modid;
    }

    public static Path getPackPath(String modid) {
        return Path.of("resourcepacks", PackageUtil.getPackID(modid));
    }
}
