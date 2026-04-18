package com.sarobidy.aetheris.core;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarobidy.aetheris.annotations.AetherJson;
import com.sarobidy.aetheris.annotations.RoleAutoriser;
import com.sarobidy.aetheris.exeptions.AetherUnauthorizedException;
import com.sarobidy.aetheris.utils.AetherPathUtils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;


/**
 * Exécute la méthode mappée :
 * - construit les arguments à partir : path variables, request parameters (GET/POST),
 *   injection possible de HttpServletRequest et HttpServletResponse.
 * - appelle la méthode et rend la réponse (String, AetherModelView, JSON, etc.)
 */
public class AetherRouteExecutor {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void execute(
            AetherRouteMapping mapping,
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws Exception {

        Method method = mapping.getMethod();

        // Vérification des rôles AVANT d'exécuter la méthode
        checkRoleAuthorization(method, req);

        Object controller = mapping.getControllerClass()
                                   .getDeclaredConstructor()
                                   .newInstance();

        Object[] args = buildMethodArguments(mapping, method, req, resp);

        Object result = method.invoke(controller, args);

        if (result == null) {
            resp.setContentType("text/plain;charset=UTF-8");
            resp.getWriter().write("[AETHERIS] Aucune réponse de la méthode.");
            return;
        }

        // Vérifier si la méthode est annotée @AetherJson
        if (method.isAnnotationPresent(AetherJson.class)) {
            renderJson(result, resp);
            return;
        }

        if (result instanceof String content) {
            renderString(content, resp);
            return;
        }

        if (result instanceof AetherModelView mv) {
            renderModelView(mv, req, resp);
            return;
        }

        renderGeneric(result, resp);
    }



    /* ============================================================================================ */
    /*                                 BUILD ARGUMENTS                                               */
    /* ============================================================================================ */

    private static Object[] buildMethodArguments(
            AetherRouteMapping mapping,
            Method method,
            HttpServletRequest req,
            HttpServletResponse resp
    ) {
        Parameter[] params = method.getParameters();
        Object[] args = new Object[params.length];

        Map<String, List<AetherUploadedFile>> uploadedFiles = collectAndSaveUploadedFiles(req);

        Map<String, String> pathValues = AetherPathUtils.match(
                mapping.getOriginalUri(),
                req.getRequestURI().substring(req.getContextPath().length())
        );
        if (pathValues == null) pathValues = Map.of();

        Map<String, String[]> requestParams = req.getParameterMap();

        for (int i = 0; i < params.length; i++) {
            Parameter p = params[i];
            String name = p.getName();
            Class<?> type = p.getType();

            /* ---------------------------------------------------------- */
            /*           INJECTION HttpServletRequest / HttpServletResponse */
            /* ---------------------------------------------------------- */
            if (HttpServletRequest.class.isAssignableFrom(type)) {
                args[i] = req;
                continue;
            }
            if (HttpServletResponse.class.isAssignableFrom(type)) {
                args[i] = resp;
                continue;
            }

            /* ---------------------------------------------------------- */
            /*                     UPLOAD : Part                          */
            /* ---------------------------------------------------------- */
            if (Part.class.isAssignableFrom(type)) {
                try {
                    args[i] = req.getPart(name);
                } catch (jakarta.servlet.ServletException | java.io.IOException | IllegalStateException e) {
                    args[i] = null;
                }
                continue;
            }

            /* ---------------------------------------------------------- */
            /*              UPLOAD : AetherUploadedFile                    */
            /* ---------------------------------------------------------- */
            if (AetherUploadedFile.class.isAssignableFrom(type)) {
                List<AetherUploadedFile> files = uploadedFiles.get(name);
                args[i] = (files != null && !files.isEmpty()) ? files.get(0) : null;
                continue;
            }

            /* ---------------------------------------------------------- */
            /*         UPLOAD : List<AetherUploadedFile>                   */
            /* ---------------------------------------------------------- */
            if (List.class.isAssignableFrom(type)) {
                if (p.getParameterizedType() instanceof java.lang.reflect.ParameterizedType par) {
                    Type[] actual = par.getActualTypeArguments();
                    if (actual.length == 1 && actual[0] == AetherUploadedFile.class) {
                        args[i] = uploadedFiles.getOrDefault(name, List.of());
                        continue;
                    }
                }
            }

            /* ---------------------------------------------------------- */
            /*     UPLOAD : Map<String, AetherUploadedFile | List<...>>    */
            /* ---------------------------------------------------------- */
            if (Map.class.isAssignableFrom(type)) {
                if (p.getParameterizedType() instanceof java.lang.reflect.ParameterizedType par) {
                    Type[] actual = par.getActualTypeArguments();
                    if (actual.length == 2 && actual[0] == String.class) {
                        // Map<String, AetherUploadedFile>
                        if (actual[1] == AetherUploadedFile.class) {
                            java.util.LinkedHashMap<String, AetherUploadedFile> map = new java.util.LinkedHashMap<>();
                            for (var entry : uploadedFiles.entrySet()) {
                                List<AetherUploadedFile> files = entry.getValue();
                                map.put(entry.getKey(), (files != null && !files.isEmpty()) ? files.get(0) : null);
                            }
                            args[i] = map;
                            continue;
                        }

                        // Map<String, List<AetherUploadedFile>>
                        if (actual[1] instanceof java.lang.reflect.ParameterizedType listType
                                && listType.getRawType() == List.class) {
                            Type[] inner = listType.getActualTypeArguments();
                            if (inner.length == 1 && inner[0] == AetherUploadedFile.class) {
                                args[i] = uploadedFiles;
                                continue;
                            }
                        }
                    }
                }
            }

            /* ---------------------------------------------------------- */
            /*              INJECTION STRICTE Map<String,Object>           */
            /* ---------------------------------------------------------- */
            if (java.util.Map.class.isAssignableFrom(type)) {
                if (p.getParameterizedType() instanceof java.lang.reflect.ParameterizedType par) {
                    Type[] actual = par.getActualTypeArguments();
                    if (actual.length == 2 && actual[0] == String.class && actual[1] == Object.class) {
                        args[i] = buildStrictObjectMap(requestParams);
                        continue;
                    }
                }
            }

            /* ---------------------------------------------------------- */
            /*                   PATH VARIABLES                           */
            /* ---------------------------------------------------------- */
            if (pathValues.containsKey(name)) {
                args[i] = AetherPathUtils.convert(pathValues.get(name), type);
                continue;
            }

            /* ---------------------------------------------------------- */
            /*             @AetherReqParam (annotation explicite)          */
            /* ---------------------------------------------------------- */
            if (p.isAnnotationPresent(com.sarobidy.aetheris.annotations.AetherReqParam.class)) {
                String paramName = p.getAnnotation(com.sarobidy.aetheris.annotations.AetherReqParam.class).value();
                if (requestParams.containsKey(paramName)) {
                    String raw = requestParams.get(paramName)[0];
                    args[i] = AetherPathUtils.convert(raw, type);
                }
                continue;
            }

            /* ---------------------------------------------------------- */
            /*        REQUEST PARAMS CLASSIQUES : match par nom            */
            /* ---------------------------------------------------------- */
            if (requestParams.containsKey(name)) {
                String raw = requestParams.get(name)[0];
                args[i] = AetherPathUtils.convert(raw, type);
                continue;
            }

            /* ---------------------------------------------------------- */
            /*                 SESSION MANAGEMENT                         */
            /* ---------------------------------------------------------- */
            if (AetherSession.class.isAssignableFrom(type)) {
                HttpSession httpSession = req.getSession();
                args[i] = new AetherSession(httpSession);
                continue;
            }

            /* ---------------------------------------------------------- */
            /*     AUTO-BINDING D'OBJET (POJO)        */
            /* ---------------------------------------------------------- */
            if (isPojo(type)) {
                args[i] = buildPojoFromParams(type, requestParams);
                continue;
            }

            /* ---------------------------------------------------------- */
            /*                 VALEURS PAR DÉFAUT                         */
            /* ---------------------------------------------------------- */
            args[i] = AetherPathUtils.convert(null, type);
        }

        return args;
    }


    /* ============================================================================================ */
    /*                        OBJECT BINDING : Form → POJO                                           */
    /* ============================================================================================ */

    private static boolean isPojo(Class<?> type) {
        return !type.isPrimitive()
                && !type.isEnum()
                && !type.getName().startsWith("java.")
                && !type.getName().startsWith("jakarta.")
                && !Map.class.isAssignableFrom(type);
    }

    private static Object buildPojoFromParams(Class<?> type, Map<String,String[]> params) {
        try {
            Object instance = type.getDeclaredConstructor().newInstance();

            for (var field : type.getDeclaredFields()) {
                String fieldName = field.getName();

                if (!params.containsKey(fieldName)) continue;

                String raw = params.get(fieldName)[0];

                field.setAccessible(true);
                Object converted = AetherPathUtils.convert(raw, field.getType());
                field.set(instance, converted);
            }

            return instance;
        } catch (ReflectiveOperationException | IllegalArgumentException | SecurityException e) {
            throw new RuntimeException("[AETHERIS] Erreur de POJO binding pour " + type.getName(), e);
        }
    }



    /* ============================================================================================ */
    /*                               MAP BUILDER                                                     */
    /* ============================================================================================ */

    private static Object buildStrictObjectMap(Map<String,String[]> requestParams) {
        java.util.LinkedHashMap<String,Object> map = new java.util.LinkedHashMap<>();
        if (requestParams == null || requestParams.isEmpty()) return map;

        for (Map.Entry<String,String[]> e : requestParams.entrySet()) {
            String[] vals = e.getValue();
            if (vals == null) map.put(e.getKey(), null);
            else if (vals.length == 1) map.put(e.getKey(), vals[0]);
            else map.put(e.getKey(), vals);
        }
        return map;
    }


    /* ============================================================================================ */
    /*                                FILE UPLOAD                                                   */
    /* ============================================================================================ */

    private static Map<String, List<AetherUploadedFile>> collectAndSaveUploadedFiles(HttpServletRequest req) {
        Map<String, List<AetherUploadedFile>> uploadedFiles = new HashMap<>();

        String contentType = req.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("multipart/")) {
            return uploadedFiles;
        }

        Path uploadBasePath = resolveUploadBasePath(req.getServletContext());

        try {
            for (Part part : req.getParts()) {
                String submitted = part.getSubmittedFileName();
                if (submitted == null || submitted.isBlank()) {
                    continue; // champ non-fichier
                }

                Files.createDirectories(uploadBasePath);

                String safeName = sanitizeFilename(submitted);
                Path destination = uniqueDestination(uploadBasePath, safeName);

                try (var in = part.getInputStream()) {
                    Files.copy(in, destination);
                }

                AetherUploadedFile file = new AetherUploadedFile(
                        part.getName(),
                        submitted,
                        part.getContentType(),
                        part.getSize(),
                        destination
                );

                uploadedFiles.computeIfAbsent(part.getName(), k -> new ArrayList<>()).add(file);
            }
        } catch (jakarta.servlet.ServletException | java.io.IOException | IllegalStateException ignored) {
            // best-effort: pas d'upload ou container non configuré
        }

        return uploadedFiles;
    }

    private static Path resolveUploadBasePath(ServletContext context) {
        // Optionnel: configuré via context-param "upload-dir" (sinon "uploads")
        String uploadDir = context.getInitParameter("upload-dir");
        if (uploadDir == null || uploadDir.isBlank()) uploadDir = "uploads";

        // Tenter sous le webapp (Tomcat), sinon fallback temp
        String webappPath = context.getRealPath("/" + uploadDir);
        if (webappPath != null && !webappPath.isBlank()) {
            return Path.of(webappPath);
        }

        return Path.of(System.getProperty("java.io.tmpdir"), "aetheris-uploads");
    }

    private static String sanitizeFilename(String name) {
        String cleaned = name.replace("\\", "/");
        int lastSlash = cleaned.lastIndexOf('/');
        if (lastSlash >= 0) cleaned = cleaned.substring(lastSlash + 1);
        cleaned = cleaned.replaceAll("[^a-zA-Z0-9._-]", "_");
        if (cleaned.isBlank()) cleaned = "upload.bin";
        return cleaned;
    }

    private static Path uniqueDestination(Path baseDir, String filename) {
        Path dest = baseDir.resolve(filename);
        if (!Files.exists(dest)) return dest;

        String base = filename;
        String ext = "";
        int dot = filename.lastIndexOf('.');
        if (dot > 0) {
            base = filename.substring(0, dot);
            ext = filename.substring(dot);
        }

        int i = 1;
        while (true) {
            Path candidate = baseDir.resolve(base + "_" + i + ext);
            if (!Files.exists(candidate)) return candidate;
            i++;
        }
    }


    /* ============================================================================================ */
    /*                                RENDERERS                                                      */
    /* ============================================================================================ */

    private static void renderString(String content, HttpServletResponse resp) throws java.io.IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(content);
    }

    private static void renderModelView(AetherModelView mv, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (mv.getData() != null) mv.getData().forEach(req::setAttribute);

        ServletContext context = req.getServletContext();
        String basePath = context.getInitParameter("view-base-path");
        String extension = context.getInitParameter("view-extension");

        if (basePath == null || basePath.isBlank()) basePath = "/WEB-INF/views/";
        if (extension == null || extension.isBlank()) extension = "jsp";

        String jspPath = basePath + mv.getView() + "." + extension;
        RequestDispatcher dispatcher = context.getRequestDispatcher(jspPath);

        if (dispatcher == null) {
            resp.setContentType("text/plain;charset=UTF-8");
            resp.getWriter().write("[AETHERIS][ERREUR] Vue introuvable : " + jspPath);
            return;
        }

        dispatcher.forward(req, resp);
    }

    /**
     * Rend la réponse en format JSON.
     * Utilise Jackson pour convertir l'objet Java en JSON.
     */
    private static void renderJson(Object result, HttpServletResponse resp) throws java.io.IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json;charset=UTF-8");

        java.util.LinkedHashMap<String, Object> payload = new java.util.LinkedHashMap<>();
        payload.put("status", "success");
        payload.put("code", HttpServletResponse.SC_OK);
        payload.put("data", result);

        String json = objectMapper.writeValueAsString(payload);
        resp.getWriter().write(json);
    }

    private static void renderGeneric(Object result, HttpServletResponse resp) throws java.io.IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().write("[AETHERIS] Type de retour non géré : " + result.getClass().getName());
        resp.getWriter().write("\nContenu : " + result);
    }

}
