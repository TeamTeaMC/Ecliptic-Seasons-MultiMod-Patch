package com.teamtea.eclipticseasons_patch.modules.xaerominimap;

import com.teamtea.eclipticseasons_patch.api.ESPatch;
import com.teamtea.eclipticseasons_patch.api.IESModPatch;
import com.teamtea.eclipticseasons_patch.api.LangUtil;
import net.neoforged.neoforge.common.ModConfigSpec;

@ESPatch(mods = XMM.MOD_ID)
public class XMM implements IESModPatch {
   public static final String MOD_ID = "xaerominimap";

   @Override
   public void common(ModConfigSpec.Builder consumer) {
       Config.load(consumer);
   }

   public static class Config {

       public static ModConfigSpec.BooleanValue enable;

       public static void load(ModConfigSpec.Builder builder) {
           builder.comment(LangUtil.getModName(MOD_ID)).push(MOD_ID);
           enable = builder
                   .gameRestart().define("Enable", true);
           builder.pop();
       }
   }
}
