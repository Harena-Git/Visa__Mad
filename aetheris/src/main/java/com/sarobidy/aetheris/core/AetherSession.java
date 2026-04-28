package com.sarobidy.aetheris.core;

import jakarta.servlet.http.HttpSession;

public class AetherSession {

    private final HttpSession session;

    public AetherSession(HttpSession session) {
        this.session = session;
    }

    // récupérer
    public Object get(String key) {
        return session.getAttribute(key);
    }

    // ajouter / modifier
    public void set(String key, Object value) {
        session.setAttribute(key, value);
    }

    // supprimer
    public void remove(String key) {
        session.removeAttribute(key);
    }

    // vérifier existence
    public boolean has(String key) {
        return session.getAttribute(key) != null;
    }

    // invalider toute la session
    public void invalidate() {
        session.invalidate();
    }
}
