package com.sarobidy.aetheris.core;

import java.util.HashMap;
import java.util.Map;

public class AetherModelView {
    private String view;
    private HashMap<String, Object> data = new HashMap<>();

    public AetherModelView(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void addAttribute(String key, Object value) {
        data.put(key, value);
    }

    /**
     * Ajoute toutes les paires d'une Map au modèle.
     * Les clés nulles sont ignorées.
     */
    public void addAttributes(Map<?, ?> attributes) {
        if (attributes == null || attributes.isEmpty()) return;
        for (Map.Entry<?, ?> e : attributes.entrySet()) {
            Object k = e.getKey();
            if (k != null) {
                data.put(k.toString(), e.getValue());
            }
        }
    }
}
