package com.teamtea.eclipticseasons_patch.modules.stardew_fishing;

import com.bonker.stardewfishing.server.event.StardewMinigameModifyRewardsEvent;
import com.teamtea.eclipticseasons.common.game.SeasonFishingHooks;
import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.LangUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

@ESPatch(mods = SF.MOD_ID, esVersion = "0.12.1.1")
public class SF implements IESModPatch {
    public static final String MOD_ID = "stardew_fishing";

    @Override
    public void common(ForgeConfigSpec.Builder consumer) {
        Config.load(consumer);
    }

    @Override
    public void register(IEventBus gameBus, IEventBus modEventBus) {
        gameBus.register(Handler.INSTANCE);
    }

    public static class Handler {
        public static final Handler INSTANCE = new Handler();

        @SubscribeEvent
        public void onStardewMinigameModifyRewardsEvent(StardewMinigameModifyRewardsEvent event) {
            if (!Config.enable.get()) return;
            List<ItemStack> original = event.getRewards();
            Level level = event.getPlayer().level();
            boolean badWeather = SeasonFishingHooks.isBadWeatherNow(level, event.getPlayer().getOnPos());
            boolean notFishingSeason = !SeasonFishingHooks.isFishingSeason(level, event.getPlayer().getOnPos());
            if (badWeather || notFishingSeason) {
                original.removeIf(stack ->
                        stack.is(ItemTags.FISHES) &&
                                (badWeather || level.getRandom().nextInt(2) == 0)
                );
            }
        }
    }

    public static class Hook {
        public static boolean shouldCloseGame(ServerPlayer serverPlayer) {
            if (!Config.enable.get()) return false;
            if (serverPlayer == null || !Config.disableMiniGame.get()) return false;
            Level level = serverPlayer.level();
            boolean badWeather = SeasonFishingHooks.isBadWeatherNow(level, serverPlayer.getOnPos());
            boolean notFishingSeason = !SeasonFishingHooks.isFishingSeason(level, serverPlayer.getOnPos());
            if (badWeather)
                serverPlayer.sendSystemMessage(Component.translatable("info.eclipticseasons_multimodpatch.stardew_fishing.bad_weather"));
            if (notFishingSeason)
                serverPlayer.sendSystemMessage(Component.translatable("info.eclipticseasons_multimodpatch.stardew_fishing.not_fishing_seasons"));
            return badWeather || notFishingSeason;
        }
    }

    public static class Config {

        public static ForgeConfigSpec.BooleanValue enable;
        public static ForgeConfigSpec.BooleanValue disableMiniGame;

        public static void load(ForgeConfigSpec.Builder builder) {
            builder.comment(LangUtil.getModName(MOD_ID)).push(MOD_ID);
            enable = builder
                    .define("Enable", true);
            disableMiniGame = builder
                    .define("DisableMiniGameIfNotPassCheck", true);
            builder.pop();
        }
    }
}
