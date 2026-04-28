package com.sarobidy.aetheris.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour indiquer qu'une méthode de contrôleur doit retourner une réponse JSON
 * au lieu d'une vue JSP classique.
 * Sprint 9 - API REST
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AetherJson {
    
}
