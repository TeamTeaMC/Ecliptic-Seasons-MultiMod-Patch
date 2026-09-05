package com.teamtea.eclipticseasons_patch.client;

import com.teamtea.eclipticseasons.client.gui.screen.ConfigScreenContext;
import com.teamtea.eclipticseasons.client.gui.screen.config.ConfigCategory;
import com.teamtea.eclipticseasons.client.gui.screen.config.ConfigScreenDefinition;
import com.teamtea.eclipticseasons.client.gui.screen.config.ConfigScreenText;
import com.teamtea.eclipticseasons.client.gui.screen.config.session.ConfigScreenSession;
import com.teamtea.eclipticseasons.client.gui.screen.config.session.ESConfigScreenSession;
import com.teamtea.eclipticseasons.client.gui.screen.config.source.ModConfigEntrySource;
import com.teamtea.eclipticseasons.compat.Platform;
import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.config.ModConfig;

public class MultiModPatchScreenDefinition
        implements ConfigScreenDefinition {

    public static final MultiModPatchScreenDefinition INSTANCE =
            new MultiModPatchScreenDefinition();

    protected ConfigCategory all = ConfigCategory.create(
            modId(),
            "ALL",
            0
    );

    @Override
    public String modId() {
        return EclipticSeasonsPatch.MODID;
    }

    @Override
    public ConfigScreenText text() {
        return new ConfigScreenText(
                Component.translatable(modId() + ".options.title")
        );
    }

    @Override
    public void initialize(ConfigScreenContext context) {
        context.registerCategory(all);
        context.registerConfigs(modId());

        context.addSource(new ModConfigEntrySource(this.all, Component.translatable(this.modId() + ".options.all"), context.configs(), true) {
            protected Component resolveSection(ModConfig owner, String name) {
                String modName = Platform.getModName(name, 0);
                return Component.literal(modName.isEmpty() ? name : modName);
            }
        });
    }

    @Override
    public ConfigScreenSession createSession(
            ConfigScreenContext context
    ) {
        return new ESConfigScreenSession(context.configs());
    }

}