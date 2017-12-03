package com.github.takahirom.library.debug.alter;

import com.github.takahirom.library.debug.alter.annotation.DebugReturn;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class DebugAlterAspect {
    @Around("execution(* *.*(..)) && @annotation(com.github.takahirom.library.debug.alter.annotation.DebugReturn)")
    public Object debugReturnMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        final Method method = signature.getMethod();
        final String key;
        final String annotatedKey = method.getAnnotation(DebugReturn.class).value();

        if ("".equals(annotatedKey)) {
            key = method.getName();
        } else {
            key = annotatedKey;
        }


        final DebugAlter debugAlter = DebugAlter.getInstance();
        if (debugAlter.alter(key)) {
            return debugAlter.get(key);
        }
        return proceedingJoinPoint.proceed();
    }

    @SuppressWarnings("unused")
    public static DebugAlterAspect aspectOf() {
        return new DebugAlterAspect();
    }
}
