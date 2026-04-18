package com.sarobidy.aetheris.exeptions;

/**
 * Exception levée lorsqu'un utilisateur tente d'accéder à une route
 * protégée par @RoleAutoriser sans avoir le rôle requis.
 */
public class AetherUnauthorizedException extends AetherException {
    
    private final String requiredRole;
    private final String userRole;
    
    public AetherUnauthorizedException(String message, String userRole, String requiredRole) {
        super(message);
        this.userRole = userRole;
        this.requiredRole = requiredRole;
    }
    
    public String getRequiredRole() {
        return requiredRole;
    }
    
    public String getUserRole() {
        return userRole;
    }
}
