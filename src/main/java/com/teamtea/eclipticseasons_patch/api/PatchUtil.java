package com.teamtea.eclipticseasons_patch.api;

public class PatchUtil {

    public static boolean isBridgeModLoaded() {
        return "Serene Seasons API Stub (Ecliptic Seasons Bridge)".equals(
                LangUtil.getModName("sereneseasons"));
    }
}
