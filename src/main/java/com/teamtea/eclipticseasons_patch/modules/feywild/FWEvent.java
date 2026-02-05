package com.teamtea.eclipticseasons_patch.modules.feywild;

import com.teamtea.eclipticseasons.common.resource.FakeResourceManagerHelperUtil;
import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.PackageUtil;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;

public class FWEvent implements IESModPatch {

    public static final FWEvent INSTANCE = new FWEvent();

    @SubscribeEvent
    public void registerBuiltinResourcePacks(AddPackFindersEvent event) {
        ModFile modFile = FMLLoader.getLoadingModList().getModFileById(EclipticSeasonsPatch.MODID).getFile();
        if (modFile != null) {
            if (event.getPackType() == PackType.SERVER_DATA && FW.Config.enable.get()) {
                FakeResourceManagerHelperUtil.registerBuiltinResourcePack(
                        event,
                        PackageUtil.getPack("feywild"),
                        modFile, PackSource.BUILT_IN);
            }
        }
    }
}
