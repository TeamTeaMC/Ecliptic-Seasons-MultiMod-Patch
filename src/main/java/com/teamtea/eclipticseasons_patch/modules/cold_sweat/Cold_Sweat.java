package com.teamtea.eclipticseasons_patch.modules.cold_sweat;

import com.momosoftworks.coldsweat.api.event.core.init.DefaultTempModifiersEvent;
import com.momosoftworks.coldsweat.api.event.core.registry.TempModifierRegisterEvent;
import com.momosoftworks.coldsweat.api.temperature.modifier.ElevationTempModifier;
import com.momosoftworks.coldsweat.api.util.Temperature;
import com.momosoftworks.coldsweat.api.util.placement.Matcher;
import com.momosoftworks.coldsweat.api.util.placement.Mode;
import com.momosoftworks.coldsweat.api.util.placement.Order;
import com.momosoftworks.coldsweat.api.util.placement.Placement;
import com.teamtea.eclipticseasons.EclipticSeasons;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Cold_Sweat {
    public static final Cold_Sweat INSTANCE = new Cold_Sweat();

    @SubscribeEvent
    public void registerTempModifiers(TempModifierRegisterEvent event) {
        if (CS.Config.enable.get() && CS.Config.tempModifier.get()) {
            event.register(EclipticSeasons.rl("season"), ESTempModifier::new);
        }
    }

    @SubscribeEvent
    public void defineDefaultModifiers(DefaultTempModifiersEvent event) {
        if (CS.Config.enable.get() && CS.Config.tempModifier.get()) {
            event.addModifierById(Temperature.Trait.WORLD,
                    EclipticSeasons.rl("season"),
                    mod -> mod.tickRate(60),
                    Placement.of(Mode.ADD_BEFORE, Order.FIRST, mod2 -> mod2 instanceof ElevationTempModifier).noDuplicates(Matcher.SAME_CLASS));
        }
    }
}
