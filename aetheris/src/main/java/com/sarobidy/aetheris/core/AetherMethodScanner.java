package com.sarobidy.aetheris.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sarobidy.aetheris.annotations.AetherPath;
import com.sarobidy.aetheris.exeptions.AetherScanException;

/**
 * Scanne les classes pour les méthodes annotées @AetherPath et
 * crée AetherRouteMapping (clé normalisée = mapping.getUri()).
 */
public final class AetherMethodScanner {

    private AetherMethodScanner() {}

    public static Map<String, AetherRouteMapping> findAnnotatedRouteMethods(Set<Class<?>> controllerClasses)
            throws AetherScanException {

        Map<String, AetherRouteMapping> result = new HashMap<>();

        for (Class<?> controller : controllerClasses) {
            for (Method method : controller.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(AetherPath.class)) continue;

                AetherPath route = method.getAnnotation(AetherPath.class);
                String rawPath = route.value();

                AetherRouteMapping mapping = new AetherRouteMapping(controller, method, rawPath);
                String httpKey = String.join(",",
                        mapping.getHttpMethods().stream()
                                .map(Enum::name)
                                .sorted()
                                .toList()
                );
                
                String key = mapping.getUri() + "::" + httpKey;

                

                System.out.println("[AETHERIS][FOUND] " +
                        (mapping.isDynamic() ? "Dynamique" : "Statique") +
                        " : " + mapping.getUri() +
                        " -> " + controller.getSimpleName() + "." + method.getName()
                );
                System.out.println("key : " + key);
                // Collision (même clé normalisée)
                if (result.containsKey(key)) {
                    throw new AetherScanException(
                            "[AETHERIS][ERREUR] Route dupliquée (URI + HTTP): " + key +
                            "\nDéjà mappée sur : " + result.get(key).getMethod() +
                            "\nConflit avec     : " + method
                    );
                }


                result.put(key, mapping);
            }
        }

        return result;
    }
}
