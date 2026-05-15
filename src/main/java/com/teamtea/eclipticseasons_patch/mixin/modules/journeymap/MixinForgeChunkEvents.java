package com.teamtea.eclipticseasons_patch.mixin.modules.journeymap;


import com.teamtea.eclipticseasons.api.event.CanPlantGrowEvent;
import com.teamtea.eclipticseasons_patch.modules.journeymap.JM;
import journeymap.client.event.forge.ForgeChunkEvents;
import net.minecraftforge.event.level.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ForgeChunkEvents.class})
public abstract class MixinForgeChunkEvents {

    @Inject(at = {@At(value = "HEAD")},
            method = {"onBlockUpdate"},
            cancellable = true,
            remap = false)
    private void eclipticseasons_multimodpatch$onBlockUpdate(BlockEvent event, CallbackInfo ci) {
        if (JM.Config.enable.get()) {
            if (event instanceof CanPlantGrowEvent)
                ci.cancel();
        }
    }

}
