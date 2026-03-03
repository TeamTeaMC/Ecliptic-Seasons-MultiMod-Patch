package com.teamtea.eclipticseasons_patch.modules;

import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.compat.Platform;
import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.LangUtil;
import com.teamtea.eclipticseasons_patch.api.PreloadedConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingException;
import net.neoforged.fml.ModLoadingIssue;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.util.*;
import java.util.stream.Collectors;

public class PatchCore {
    public static final List<IESModPatch> MOD_PLUGINS = new ArrayList<>();
    public static final List<ModLoadingIssue> MOD_LOADING_EXCEPTIONS = new ArrayList<>();

    public static final List<String> MOD_DISABLED = new ArrayList<>();
    public static final List<String> CLIENT_MOD_DISABLED = new ArrayList<>();

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
                boolean disabledPlugin = !required.isEmpty() && !PreloadedConfig.shouldApply(required.get(0));
                shouldTryLoad &= !disabledPlugin;
                if (disabledPlugin) {
                    boolean clientOnly = (Boolean) $.annotationData().getOrDefault("clientOnly", false);
                    (clientOnly ? CLIENT_MOD_DISABLED : MOD_DISABLED).add(required.get(0));
                }
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
                        MOD_LOADING_EXCEPTIONS.add(new ModLoadingIssue(
                                ModLoadingIssue.Severity.ERROR,
                                LangUtil.parseI18n("error.eclipticseasons_multimodpatch.version.mods.min",
                                        sModId, mv, modUseVersion),
                                List.of()).withAffectedModFile(
                                Platform.getModFile(sModId)
                        ));
                    }
                }
                if (shouldTryLoad) {
                    String minCoreVersion = (String) $.annotationData().getOrDefault("esVersion", "");
                    if (!minCoreVersion.isEmpty()) {
                        if (invalidVersion(minCoreVersion, coreModVersion)) {
                            MOD_LOADING_EXCEPTIONS.add(new ModLoadingIssue(
                                    ModLoadingIssue.Severity.ERROR,
                                    LangUtil.parseI18n("error.eclipticseasons_multimodpatch.version.eclipticseasons.min",
                                            String.join(", ", required), minCoreVersion, coreModVersion.toString()),
                                    List.of())
                                    .withAffectedMod(iModInfo));
                        }
                    }
                }
                return shouldTryLoad;
            }
        }).map(ModFileScanData.AnnotationData::memberName).toList();

        if (!PatchCore.MOD_LOADING_EXCEPTIONS.isEmpty()) {
            Collections.reverse(PatchCore.MOD_LOADING_EXCEPTIONS);
            throw new ModLoadingException(PatchCore.MOD_LOADING_EXCEPTIONS);
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
