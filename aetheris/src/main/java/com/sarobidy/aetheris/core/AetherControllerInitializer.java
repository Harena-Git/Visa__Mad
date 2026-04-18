package com.sarobidy.aetheris.core;

import java.util.Set;

import com.sarobidy.aetheris.annotations.AetherController;
import com.sarobidy.aetheris.exeptions.AetherInitializationException;
import com.sarobidy.aetheris.exeptions.AetherScanException;

/**
 * Initialise et enregistre les classes annotées avec @AetherController.
 * Peut scanner un ou plusieurs packages.
 */
public final class AetherControllerInitializer {

    private final String[] packagesToScan;

    public AetherControllerInitializer(String... packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    /**
     * Lance le scan et retourne les classes trouvées.
     * @return Set de classes trouvées
     * @throws AetherInitializationException en cas d'erreur lors de l'initialisation
     */
    public Set<Class<?>> initializeControllers() throws AetherInitializationException {
        try {
            System.out.println("[AETHERIS] Initialisation des contrôleurs...");
            Set<Class<?>> controllers = AetherClassScanner.findClassesAnnotatedWith(AetherController.class, packagesToScan);

            if (controllers.isEmpty()) {
                System.out.println("[AETHERIS][INFO] Aucun contrôleur trouvé.");
            } else {
                controllers.forEach(c -> System.out.println("[AETHERIS][FOUND] Controller détecté : " + c.getName()));
            }

            return controllers;

        } catch (AetherScanException e) {
            throw new AetherInitializationException("Erreur lors de l'initialisation des contrôleurs", e);
        } catch (Exception e) {
            throw new AetherInitializationException("Erreur inattendue lors du scan des contrôleurs", e);
        }
    }
}
