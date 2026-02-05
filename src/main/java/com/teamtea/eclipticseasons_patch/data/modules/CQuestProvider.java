package com.teamtea.eclipticseasons_patch.data.modules;

import com.feywild.feywild.FeywildMod;
import com.feywild.feywild.quest.*;
import com.feywild.feywild.quest.reward.ItemReward;
import com.feywild.feywild.quest.task.FeyGiftTask;
import com.feywild.feywild.quest.task.ItemStackTask;
import com.feywild.feywild.quest.util.FeyGift;
import com.feywild.feywild.sound.FeySound;
import com.teamtea.eclipticseasons.EclipticSeasons;
import com.teamtea.eclipticseasons.api.constant.crop.CropSeasonType;
import com.teamtea.eclipticseasons.api.constant.solar.Season;
import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.moddingx.libx.crafting.IngredientStack;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class CQuestProvider implements DataProvider {

    private final Map<Alignment, Set<Quest>> quests = new HashMap();

    private final PackOutput output;
    public final ExistingFileHelper helper;

    public CQuestProvider(PackOutput output, ExistingFileHelper helper) {
        this.output = output;
        this.helper = helper;
    }

    public void setup() {
        for (Alignment alignment : Alignment.values()) {
            Season season = Season.valueOf(alignment.id.toUpperCase(Locale.ROOT));
            Item item = BuiltInRegistries.ITEM.get(EclipticSeasons.rl(season.getName() + "_greenhouse_essence"));
            Ingredient[] array = Arrays.stream(CropSeasonType.collectValues())
                    .filter(cropSeasonType -> cropSeasonType.getInfo().isSuitable(season)
                            && cropSeasonType.ordinal() < CropSeasonType.collectValues().length - 5)
                    .map(c -> Ingredient.of(c.getTag())).toArray(Ingredient[]::new);
            this.quest(alignment, season.getName() + "_core")
                    .parent(FeywildMod.getInstance().resource("root"))
                    .icon(item)
                    .completeSound(SoundEvents.AMETHYST_BLOCK_CHIME)
                    .reward(QuestReward.of(ItemReward.INSTANCE, new ItemStack(item)))
                    .task(QuestTask.of(ItemStackTask.INSTANCE, new IngredientStack(
                            CompoundIngredient.of(array), 64)))
                    .build();
        }

    }

    public QuestBuilder quest(Alignment alignment, String name) {
        return new QuestBuilder(quests, alignment, name);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.setup();
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (Alignment alignment : this.quests.keySet()) {
            Set<ResourceLocation> ids = new HashSet<>();
            Iterator<Quest> var6 = (this.quests.get(alignment)).iterator();
            Quest quest;
            while (var6.hasNext()) {
                quest = var6.next();
                if (ids.contains(quest.id)) {
                    throw new IllegalStateException("Duplicate quest entityId: " + quest.id);
                }
                ids.add(quest.id);
            }
            var6 = (this.quests.get(alignment)).iterator();

            while (var6.hasNext()) {
                quest = var6.next();
                futures.add(DataProvider.saveStable(cache, quest.toJson(), this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(quest.id.getNamespace()).resolve("feywild_quests").resolve(alignment.id).resolve(quest.id.getPath() + ".json")));
            }
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "es_patch x fey wild";
    }

    public static class QuestBuilder {
        private final Alignment alignment;
        private final ResourceLocation id;
        private final Set<ResourceLocation> parents;
        private final List<QuestTask> tasks;
        private final List<QuestReward> rewards;
        private boolean repeatable = false;
        private int reputation;
        private Item icon;
        private QuestDisplay start;
        @Nullable
        private QuestDisplay complete;

        private Map<Alignment, Set<Quest>> quests;

        public QuestBuilder(Map<Alignment, Set<Quest>> quests, Alignment alignment, String name) {
            this.quests = quests;
            this.alignment = alignment;
            this.id = EclipticSeasonsPatch.rl(name);
            this.parents = new HashSet<>();
            this.reputation = 5;
            this.icon = null;
            this.start = new QuestDisplay(Component.translatable("quest." + EclipticSeasonsPatch.MODID + "." + alignment.id + "." + name + ".start.title"), Component.translatable("quest." + EclipticSeasonsPatch.MODID + "." + alignment.id + "." + name + ".start.description"), null);
            this.complete = new QuestDisplay(Component.translatable("quest." + EclipticSeasonsPatch.MODID + "." + alignment.id + "." + name + ".complete.title"), Component.translatable("quest." + EclipticSeasonsPatch.MODID + "." + alignment.id + "." + name + ".complete.description"), null);
            this.tasks = new ArrayList<>();
            this.rewards = new ArrayList<>();
        }

        public QuestBuilder parent(ResourceLocation... ids) {
            this.parents.addAll(Arrays.asList(ids));
            return this;
        }

        public QuestBuilder repeatable() {
            this.repeatable = true;
            return this;
        }

        public QuestBuilder reputation(int reputation) {
            this.reputation = reputation;
            return this;
        }

        public QuestBuilder icon(ItemLike icon) {
            this.icon = icon.asItem();
            return this;
        }

        public QuestBuilder start(QuestDisplay display) {
            this.start = display;
            return this;
        }

        public QuestBuilder complete(@Nullable QuestDisplay display) {
            this.complete = display;
            return this;
        }

        public QuestBuilder startSound(@Nullable FeySound sound) {
            return this.startSound(sound == null ? null : sound.getSoundEvent());
        }

        public QuestBuilder startSound(@Nullable SoundEvent sound) {
            this.start = new QuestDisplay(this.start.title, this.start.description, sound);
            return this;
        }

        public QuestBuilder completeSound(@Nullable FeySound sound) {
            return this.startSound(sound == null ? null : sound.getSoundEvent());
        }

        public QuestBuilder completeSound(@Nullable SoundEvent sound) {
            if (this.complete == null) {
                throw new IllegalStateException("Can't set sound on null completion.");
            } else {
                this.complete = new QuestDisplay(this.complete.title, this.complete.description, sound);
                return this;
            }
        }

        public QuestBuilder task(QuestTask... tasks) {
            this.tasks.addAll(Arrays.asList(tasks));
            return this;
        }

        public QuestBuilder gift(Ingredient ingredient) {
            return this.gift(ingredient, 1);
        }

        public QuestBuilder gift(Ingredient ingredient, int times) {
            this.tasks.add(QuestTask.of(FeyGiftTask.INSTANCE, new FeyGift(this.alignment, ingredient), times));
            return this;
        }

        public QuestBuilder reward(QuestReward... rewards) {
            this.rewards.addAll(Arrays.asList(rewards));
            return this;
        }

        @SuppressWarnings("removal")
        public void build() {
            Item icon = this.icon;
            if (this.icon == null && this.tasks.size() == 1) {
                icon = this.tasks.get(0).icon();
            }

            if (icon == null) {
                throw new IllegalStateException("Can't build quest without icon: " + this.id);
            } else {
                Quest quest = new Quest(this.id, parents, this.repeatable, this.reputation, icon, this.start, this.tasks.isEmpty() ? null : this.complete, this.tasks, this.rewards);
                quests.computeIfAbsent(this.alignment, (k) -> new HashSet<>()).add(quest);
            }
        }
    }
}
