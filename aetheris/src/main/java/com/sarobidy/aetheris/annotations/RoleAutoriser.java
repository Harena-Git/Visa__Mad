package com.sarobidy.aetheris.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour sécuriser l'accès à une route par rôle.
 * Le rôle de l'utilisateur stocké en session doit correspondre 
 * à l'un des rôles autorisés pour accéder à la route.
 * 
 * Exemple :
 * @RoleAutoriser({"ADMIN", "CHEF"})
 * public AetherModelView documentConfidentiel() { ... }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RoleAutoriser {
    /**
     * Liste des rôles autorisés à accéder à cette route.
     * Le rôle de l'utilisateur en session sera comparé à cette liste.
     */
    String[] value();
}
