package com.sarobidy.aetheris.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sarobidy.aetheris.core.AetherRouteMapping;

public class AetherPathUtils {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{([^/]+)\\}");

    /** Normalise un URI : trim, collapse '//' et supprime trailing slash (sauf racine) */
    public static String normalize(String uri) {
        if (uri == null) return "/";
        String s = uri.trim();
        if (s.isEmpty()) return "/";
        // ensure leading slash
        if (!s.startsWith("/")) s = "/" + s;
        // collapse multiple slashes
        s = s.replaceAll("/+", "/");
        // remove trailing slash except root
        if (s.length() > 1 && s.endsWith("/")) s = s.substring(0, s.length() - 1);
        return s;
    }

    /** Détecte si route contient des variables {var} */
    public static boolean isDynamic(String uri) {
        if (uri == null) return false;
        return VARIABLE_PATTERN.matcher(uri).find();
    }

    /** Transforme "/items/{id}" -> regex avec groupes nommés (?<id>[^/]+) */
    public static Pattern toRegex(String uri) {
        String normalized = normalize(uri);
        String regex = VARIABLE_PATTERN.matcher(normalized).replaceAll("(?<$1>[^/]+)");
        return Pattern.compile("^" + regex + "$");
    }

    /** Extrait noms de variables dans l'ordre d'apparition */
    public static List<String> extractVariables(String uri) {
        String normalized = normalize(uri);
        List<String> list = new ArrayList<>();
        Matcher m = VARIABLE_PATTERN.matcher(normalized);
        while (m.find()) list.add(m.group(1));
        return list;
    }

    /**
     * Match d'une requestUri sur un template (template peut être normalisé ou non).
     * Si mismatch -> null
     * Sinon -> Map variableName -> valeur (LinkedHashMap pour garder l'ordre)
     */
    public static Map<String, String> match(String routeTemplate, String requestUri) {
        String tpl = normalize(routeTemplate);
        String req = normalize(requestUri);

        if (!isDynamic(tpl)) return Map.of();

        Pattern p = toRegex(tpl);
        Matcher m = p.matcher(req);
        if (!m.matches()) return null;

        List<String> vars = extractVariables(tpl);
        Map<String, String> map = new LinkedHashMap<>();
        for (String v : vars) {
            // group by name (works with Java regex named groups)
            try {
                String value = m.group(v);
                map.put(v, value);
            } catch (IllegalArgumentException ex) {
                // fallback: use numeric group index (shouldn't happen normally)
                // compute index by order:
                int idx = vars.indexOf(v) + 1;
                map.put(v, m.group(idx));
            }
        }
        return map;
    }

    /** Conversion simple String -> type primitif / wrapper / String */
    public static Object convert(String value, Class<?> type) {
        if (value == null) return defaultPrimitiveValue(type);

        if (type == String.class) return value;
        if (type == int.class || type == Integer.class) return Integer.parseInt(value);
        if (type == long.class || type == Long.class) return Long.parseLong(value);
        if (type == double.class || type == Double.class) return Double.parseDouble(value);
        if (type == float.class || type == Float.class) return Float.parseFloat(value);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
        if (type == char.class || type == Character.class) {
            return value.isEmpty() ? '\0' : value.charAt(0);
        }

        // fallback: return the raw string
        return value;
    }

    private static Object defaultPrimitiveValue(Class<?> type) {
        if (!type.isPrimitive()) return null;
        if (type == boolean.class) return false;
        if (type == byte.class) return (byte) 0;
        if (type == short.class) return (short) 0;
        if (type == int.class) return 0;
        if (type == long.class) return 0L;
        if (type == float.class) return 0f;
        if (type == double.class) return 0d;
        if (type == char.class) return '\0';
        return null;
    }

    /* ===================== ROUTE EXTRACTION ===================== */

    /** Extrait routes statiques depuis une map de mappings (clé = uri déclarée / normalisée) */
    public static Map<String, AetherRouteMapping> extractStaticRoutes(Map<String, AetherRouteMapping> allRoutes) {
        Map<String, AetherRouteMapping> staticRoutes = new HashMap<>();
        for (var entry : allRoutes.entrySet()) {
            String key = normalize(entry.getKey());
            if (!isDynamic(key)) {
                staticRoutes.put(key, entry.getValue());
            }
        }
        return staticRoutes;
    }

    /** Extrait routes dynamiques triées par spécificité (plus de segments d'abord puis longueur) */
    public static List<AetherRouteMapping> extractDynamicRoutes(Map<String, AetherRouteMapping> allRoutes) {
        List<AetherRouteMapping> dynamic = new ArrayList<>();
        for (var entry : allRoutes.entrySet()) {
            String key = normalize(entry.getKey());
            if (isDynamic(key)) dynamic.add(entry.getValue());
        }

        dynamic.sort((a, b) -> {
            int sa = a.getUri().split("/").length;
            int sb = b.getUri().split("/").length;
            if (sa != sb) return Integer.compare(sb, sa);
            return Integer.compare(b.getUri().length(), a.getUri().length());
        });

        return dynamic;
    }
}
