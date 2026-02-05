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
        addFeyWild();
        addSomeInfo();
        addErrorLang();
    }

    private void addSomeInfo() {
        add("info.eclipticseasons_multimodpatch.stardew_fishing.bad_weather", "The weather is terrible, so you can't go fishing!");
        add("info.eclipticseasons_multimodpatch.stardew_fishing.not_fishing_seasons", "It's the fishing off-season!");
    }

    private void addFeyWild() {
        add("quest.eclipticseasons_multimodpatch.spring.spring_core.start.title", "Spring's Dawn");
        add("quest.eclipticseasons_multimodpatch.spring.spring_core.start.description",
                "Can you hear it? The earth whispers, the flowers dance.$(n)"
                        + "$(spring)Spring's Dawn$() is calling your hands to sow and nurture.$(n)"
                        + "$(n)"
                        + "Gather $(spring)64 spring crop seeds$() to begin the first rite of the year.$(n)"
                        + "Perhaps they will whisper the dreams winter left behind...");

        add("quest.eclipticseasons_multimodpatch.spring.spring_core.complete.title", "Spring's Waltz");
        add("quest.eclipticseasons_multimodpatch.spring.spring_core.complete.description",
                "You've done it — the rite of renewal is complete!$(n)"
                        + "The $(spring)Heart of Spring$() is your reward, gleaming with life's promise.$(n)"
                        + "$(n)"
                        + "The shoots, the breeze... they remember you.$(n)"
                        + "Let the $(spring)Spring's Waltz$() begin anew.");
        add("quest.eclipticseasons_multimodpatch.summer.summer_core.start.title", "Summer's Call");
        add("quest.eclipticseasons_multimodpatch.summer.summer_core.start.description",
                "Do you hear the seeds whispering in the soil?$(n)"
                        + "This is $(summer)Summer's Call$(), urging you to sow, to reap, to endure.$(n)"
                        + "$(n)"
                        + "Harvest $(summer)64 summer crop seeds$() and a reward awaits.$(n)"
                        + "The sun blazes high — will you answer the heat with resolve?");

        add("quest.eclipticseasons_multimodpatch.summer.summer_core.complete.title", "Summer's Requiem");
        add("quest.eclipticseasons_multimodpatch.summer.summer_core.complete.description",
                "You prevailed!$(n)"
                        + "64 seeds dance in your hands, and all of $(summer)Summer$() cheers your triumph.$(n)"
                        + "$(n)"
                        + "Your reward: the $(summer)Heart of Summer$(), a vessel of sunfire and sweat.$(n)"
                        + "This is $(summer)Summer's Requiem$() — the memory of your toil in bloom.");
        add("quest.eclipticseasons_multimodpatch.autumn.autumn_core.start.title", "Autumn's Pact");
        add("quest.eclipticseasons_multimodpatch.autumn.autumn_core.start.description",
                "Leaves fall, the wind sings of harvest.$(n)"
                        + "Will you embrace the $(autumn)Pact of Autumn$()?$(n)"
                        + "$(n)"
                        + "Gather $(autumn)64 autumn crop seeds$() and mark the golden age of the earth.$(n)"
                        + "The land waits for your touch.");

        add("quest.eclipticseasons_multimodpatch.autumn.autumn_core.complete.title", "Autumn's Yield");
        add("quest.eclipticseasons_multimodpatch.autumn.autumn_core.complete.description",
                "The fields bow in gold; the wind hums its thanks.$(n)"
                        + "You've honored $(autumn)Autumn's Trial$() and earned its richest gift.$(n)"
                        + "$(n)"
                        + "This is the $(autumn)Heart of Autumn$() — heavy with praise and plenty.$(n)"
                        + "Let the world remember what you reaped.");
        add("quest.eclipticseasons_multimodpatch.winter.winter_core.start.title", "Winter's Sleep");
        add("quest.eclipticseasons_multimodpatch.winter.winter_core.start.description",
                "Snow blankets the earth, yet life waits beneath.$(n)"
                        + "This is your quiet trial — can you awaken $(winter)Winter’s Slumber$()?$(n)"
                        + "$(n)"
                        + "Collect $(winter)64 winter crop seeds$() and plant warmth in the coldest soil.$(n)"
                        + "In stillness lies the spark of rebirth.");

        add("quest.eclipticseasons_multimodpatch.winter.winter_core.complete.title", "Winter's Pulse");
        add("quest.eclipticseasons_multimodpatch.winter.winter_core.complete.description",
                "You stood tall in frost and silence.$(n)"
                        + "The seeds answered your call, even in the hush of snow.$(n)"
                        + "$(n)"
                        + "Claim the $(winter)Heart of Winter$(), pulsing with quiet strength.$(n)"
                        + "This is $(winter)Winter’s Pulse$() — a token of your patient resilience.");

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
