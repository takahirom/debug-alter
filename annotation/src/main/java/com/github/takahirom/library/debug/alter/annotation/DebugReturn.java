package com.github.takahirom.library.debug.alter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DebugReturn {
    /**
     * Key for identify debug item.
     * If you do not set , you can use method name for identifing alter item
     */
    String value() default "";
}
