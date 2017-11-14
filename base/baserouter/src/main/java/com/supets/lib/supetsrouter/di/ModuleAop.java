package com.supets.lib.supetsrouter.di;

import android.support.annotation.Keep;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Keep
public @interface ModuleAop {
    String moduleImplName() default  "";
    Class<?> moduleImplClass() default  Object.class;

}