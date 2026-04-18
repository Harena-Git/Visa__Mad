package com.sarobidy.aetheris.core;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.sarobidy.aetheris.exeptions.AetherRegistryException;

public class AetherControllerRegistry {

    private final Map<Class<?>, Object> controllers = new HashMap<>();

    public void registerControllers(Collection<Class<?>> controllerClasses) throws AetherRegistryException {
        for (Class<?> cls : controllerClasses) {
            try {
                Object instance = cls.getDeclaredConstructor().newInstance();
                controllers.put(cls, instance);
                System.out.println("[AETHERIS]  Controller enregistré : " + cls.getName());
            } catch (ReflectiveOperationException e) {
                throw new AetherRegistryException("Impossible d’instancier le contrôleur " + cls.getName(), e);
            }
        }
    }

    public Optional<Object> getController(Class<?> cls) {
        return Optional.ofNullable(controllers.get(cls));
    }

    public Collection<Object> getAllInstances() {
        return Collections.unmodifiableCollection(controllers.values());
    }
}
