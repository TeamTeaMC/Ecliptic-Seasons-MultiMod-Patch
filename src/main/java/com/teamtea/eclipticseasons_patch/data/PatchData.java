package com.teamtea.eclipticseasons_patch.data;

import com.teamtea.eclipticseasons.data.api.MutablePackOutput;
import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import com.teamtea.eclipticseasons_patch.api.PackageUtil;
import com.teamtea.eclipticseasons_patch.data.lang.Lang_EN;
import com.teamtea.eclipticseasons_patch.data.lang.Lang_ZH;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;


public class PatchData {
    public final static String MODID = EclipticSeasonsPatch.MODID;

    public static void dataGen(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        MutablePackOutput packOutput = new MutablePackOutput(generator.getPackOutput());
        if (event.includeServer()) {

        }
        if (event.includeClient()) {
            generator.addProvider(event.includeClient(), new Lang_EN(packOutput, helper));
            generator.addProvider(event.includeClient(), new Lang_ZH(packOutput, helper));
        }

        // Fey Wild
        packOutput = packOutput.move(PackageUtil.getPackPath("feywild"));
        if (event.includeServer()) {
            // generator.addProvider(event.includeServer(), new CQuestProvider(packOutput, helper));
        }
    }
}
