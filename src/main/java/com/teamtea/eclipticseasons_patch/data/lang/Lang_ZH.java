package com.teamtea.eclipticseasons_patch.data.lang;


import com.teamtea.eclipticseasons.data.general.lang.LangHelper;
import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


public class Lang_ZH extends LangHelper {
    public Lang_ZH(PackOutput gen, ExistingFileHelper helper) {
        super(gen, helper, EclipticSeasonsPatch.MODID, "zh_cn");
    }


    @Override
    protected void addTranslations() {
        addTouhouLittleMaid();
        addConfigLang();
        addErrorLang();
    }

    private void addTouhouLittleMaid() {
        add("task.eclipticseasons_multimodpatch.clean_snow", "扫雪");
        add("task.eclipticseasons_multimodpatch.clean_snow.desc", "适用于节气的覆雪方块");
        add("task.eclipticseasons_multimodpatch.clean_snow.condition.has_broom", "持有扫帚");
        add("task.eclipticseasons_multimodpatch.clean_snow.condition.broom_work", "雪会落到世界上");
    }


    private void addErrorLang() {
        add("error.eclipticseasons_multimodpatch.version.eclipticseasons.min",
                "§f[Ecliptic Seasons (节气)] 版本不兼容！修补模块 §e%s§f 需要 Ecliptic Seasons 版本 §a%s§f 或更高（当前为 §c%s§f）§r");

        add("error.eclipticseasons_multimodpatch.version.mods.min",
                "§f[Patch Module] %s 前置版本不兼容！需要 §a%s§f 或更高版本（当前为 §e%s§f）§r");

    }

    private void addConfigLang() {


    }


}
