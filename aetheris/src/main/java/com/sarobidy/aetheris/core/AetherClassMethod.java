package com.sarobidy.aetheris.core;

import java.lang.reflect.Method;

public class AetherClassMethod {
    private final Class<?> controllerClass;
    private final Method method;

    public AetherClassMethod(Class<?> controllerClass, Method method) {
        this.controllerClass = controllerClass;
        this.method = method;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getMethod() {
        return method;
    }
}
