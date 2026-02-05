package com.teamtea.eclipticseasons_patch.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ESPatch {
    String[] mods() default {};
    String[] minVersions() default {};

    String esVersion() default "";
}
