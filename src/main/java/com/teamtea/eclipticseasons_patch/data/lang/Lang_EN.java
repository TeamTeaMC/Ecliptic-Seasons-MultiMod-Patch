package com.teamtea.eclipticseasons_patch.data.lang;

import com.teamtea.eclipticseasons.data.general.lang.LangHelper;
import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;


public class Lang_EN extends LangHelper {
    public Lang_EN(PackOutput gen, ExistingFileHelper helper) {
        super(gen, helper, EclipticSeasonsPatch.MODID, "en_us");
    }


    @Override
    protected void addTranslations() {
        addTouhouLittleMaid();
        addConfigLang();
        addSomeInfo();
        addErrorLang();
    }

    private void addSomeInfo() {
        add("info.eclipticseasons_multimodpatch.stardew_fishing.bad_weather", "The weather is terrible, so you can't go fishing!");
        add("info.eclipticseasons_multimodpatch.stardew_fishing.not_fishing_seasons", "It's the fishing off-season!");
    }


    private void addTouhouLittleMaid() {
        add("task.eclipticseasons_multimodpatch.clean_snow", "Clean Snow");
        add("task.eclipticseasons_multimodpatch.clean_snow.desc", "Applied to snow-covered blocks from Ecliptic Season");
        add("task.eclipticseasons_multimodpatch.clean_snow.condition.has_broom", "Has Broom");
        add("task.eclipticseasons_multimodpatch.clean_snow.condition.broom_work", "Snow would fall in world.");
    }

    private void addConfigLang() {

    }

    private void addErrorLang() {
        add("error.eclipticseasons_multimodpatch.version.eclipticseasons.min",
                "§f[Ecliptic Seasons] Incompatible version! Patch module §e%s§f requires Ecliptic Seasons §a%s§f or higher (current: §c%s§f)§r");

        add("error.eclipticseasons_multimodpatch.version.mods.min",
                "§f[Patch Module] %s dependency is incompatible! Requires §a%s§f or higher (current: §c%s§f)§r");

    }
}
