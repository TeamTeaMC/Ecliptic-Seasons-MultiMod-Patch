package com.teamtea.eclipticseasons_patch.modules;

import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.compat.Platform;
import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.LangUtil;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LoadingFailedException;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingException;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.util.*;
import java.util.stream.Collectors;

public class PatchCore {
    public static final List<IESModPatch> MOD_PLUGINS = new ArrayList<>();
    public static final List<ModLoadingException> MOD_LOADING_EXCEPTIONS = new ArrayList<>();

    public static void run() {
        LangUtil.tryLoadLang();

        MOD_PLUGINS.clear();


        IModFileInfo coreModInfo = Platform.getModFile(EclipticSeasonsApi.MODID).getModFileInfo();
        IModInfo iModInfo = coreModInfo.getMods().get(0);

        Map<String, ArtifactVersion> modInfos = ModList.get().getMods().stream().collect(Collectors.toMap(
                IModInfo::getModId,
                IModInfo::getVersion
        ));
        Set<String> modIdSet = modInfos.keySet();
        ArtifactVersion coreModVersion = iModInfo.getVersion();
        List<String> classNames = ModList.get().getAllScanData().stream().flatMap(($) -> $.getAnnotations().stream()).filter(($) -> {
            if (!$.annotationType().getClassName().equals(ESPatch.class.getName())) {
                return false;
            } else {
                List<String> required = (List<String>) $.annotationData().getOrDefault("mods", new ArrayList<>());

                List<String> minVersions = (List<String>) $.annotationData().getOrDefault("minVersions", new ArrayList<>());
                boolean shouldTryLoad = modIdSet.containsAll(required);
                if (shouldTryLoad && !minVersions.isEmpty()
                        && required.size() == minVersions.size()) {
                    for (int i = 0; i < required.size(); i++) {
                        String sModId = required.get(i);
                        String mv = minVersions.get(i);

                        ArtifactVersion orDefault = modInfos.getOrDefault(sModId, null);
                        String modUseVersion = "none";
                        if (orDefault != null) {
                            modUseVersion = orDefault.toString();
                            if (!invalidVersion(mv, orDefault)) continue;
                        }
                        MOD_LOADING_EXCEPTIONS.add(new ModLoadingException(iModInfo, ModLoadingStage.CONSTRUCT, LangUtil.parseI18n("error.eclipticseasons_multimodpatch.version.mods.min", sModId, mv, modUseVersion), new RuntimeException()));
                    }
                }
                if(shouldTryLoad){
                    String minCoreVersion = (String) $.annotationData().getOrDefault("esVersion", "");
                    if (!minCoreVersion.isEmpty()) {
                        if (invalidVersion(minCoreVersion, coreModVersion)) {
                            MOD_LOADING_EXCEPTIONS.add(new ModLoadingException(iModInfo, ModLoadingStage.CONSTRUCT, LangUtil.parseI18n("error.eclipticseasons_multimodpatch.version.eclipticseasons.min", String.join(", ", required), minCoreVersion, coreModVersion.toString()), new RuntimeException()));
                        }
                    }
                }
                return shouldTryLoad;
            }
        }).map(ModFileScanData.AnnotationData::memberName).toList();

        if (!PatchCore.MOD_LOADING_EXCEPTIONS.isEmpty()) {
            Collections.reverse(PatchCore.MOD_LOADING_EXCEPTIONS);
            throw new LoadingFailedException(PatchCore.MOD_LOADING_EXCEPTIONS);
        }

        for (String className : classNames) {
            EclipticSeasonsPatch.logger("Found patch from " + className);
            try {
                Class<?> clazz = Class.forName(className);
                if (IESModPatch.class.isAssignableFrom(clazz)) {
                    IESModPatch plugin = (IESModPatch) clazz.getDeclaredConstructor().newInstance();
                    MOD_PLUGINS.add(plugin);
                }
            } catch (Throwable var7) {
                EclipticSeasonsPatch.logger("Failed to load patch from " + className, var7);
            }
        }
    }


    public static void register(IEventBus gameBus, IEventBus modEventBus) {
        for (IESModPatch modPlugin : MOD_PLUGINS) {
            modPlugin.init();
        }
        for (IESModPatch modPlugin : MOD_PLUGINS) {
            modPlugin.register(gameBus, modEventBus);
        }
    }

    public static boolean invalidVersion(String targetVersion, ArtifactVersion actualVersion) {
        // if(true)return true;
        return new DefaultArtifactVersion(targetVersion).compareTo(actualVersion) > 0;
    }

}
