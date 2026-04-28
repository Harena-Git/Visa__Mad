package com.sarobidy.aetheris.core;

import com.sarobidy.aetheris.exeptions.AetherInitializationException;

/**
 * Point d’entree du framework Aetheris.
 * Orchestration du scan, initialisation et gestion d’exceptions globales.
 */
public final class AetherBootstrap {

    private AetherBootstrap() { }


    public static void start(String... packages) {
        try {
            System.out.println("───────────────────────────────");
            System.out.println(" [AETHERIS] Demarrage du Framework");
            System.out.println("───────────────────────────────");

            AetherControllerInitializer initializer = new AetherControllerInitializer(packages);
            initializer.initializeControllers();

            System.out.println(" [AETHERIS] Initialisation terminee avec succes !");
            System.out.println("───────────────────────────────");

        } catch (AetherInitializationException e) {
            System.err.println(" [AETHERIS] Erreur critique lors de l'initialisation : " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.err.println(" [AETHERIS] echec inattendu au demarrage : " + t);
            t.printStackTrace();
        }
    }
}
