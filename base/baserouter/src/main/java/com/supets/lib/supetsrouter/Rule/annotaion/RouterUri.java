package com.supets.lib.supetsrouter.Rule.annotaion;

import android.support.annotation.Keep;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Keep
public @interface RouterUri {
    String routerUri() default "";
}