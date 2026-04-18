package com.sarobidy.aetheris.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sarobidy.aetheris.annotations.AetherController;
import com.sarobidy.aetheris.enums.HttpMethod;
import com.sarobidy.aetheris.exeptions.AetherScanException;
import com.sarobidy.aetheris.utils.AetherPathUtils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "FrontServlet", urlPatterns = "/*", loadOnStartup = 1)
@MultipartConfig
public class FrontServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();

        try {
            Object existingStatic = context.getAttribute("AETHER_STATIC_ROUTES");
            Object existingDynamic = context.getAttribute("AETHER_DYNAMIC_ROUTES");
            if (existingStatic != null && existingDynamic != null) {
                System.out.println("[AETHERIS] Routes déjà présentes dans le ServletContext.");
                return;
            }

            System.out.println("[AETHERIS] Initialisation du scanner de contrôleurs...");

            String packageParam = config.getInitParameter("controller-scan-packages");
            String[] packagesToScan = null;
            if (packageParam != null && !packageParam.isBlank()) {
                packagesToScan = Arrays.stream(packageParam.split(","))
                        .map(String::trim)
                        .toArray(String[]::new);
                System.out.println("[AETHERIS] Packages à scanner : " + Arrays.toString(packagesToScan));
            } else {
                System.out.println("[AETHERIS] Aucun package spécifié, scan global.");
            }

            // Scan des contrôleurs
            Set<Class<?>> controllers = AetherClassScanner.findClassesAnnotatedWith(
                    AetherController.class,
                    packagesToScan
            );

            // Scan des méthodes et création des mappings
            Map<String, AetherRouteMapping> annotatedRoutes =
                    AetherMethodScanner.findAnnotatedRouteMethods(controllers);

            // Séparer static et dynamic
            Map<String, AetherRouteMapping> staticRoutes = AetherPathUtils.extractStaticRoutes(annotatedRoutes);
            List<AetherRouteMapping> dynamicRoutes = AetherPathUtils.extractDynamicRoutes(annotatedRoutes);

            context.setAttribute("AETHER_STATIC_ROUTES", staticRoutes);
            context.setAttribute("AETHER_DYNAMIC_ROUTES", dynamicRoutes);

            System.out.println("[AETHERIS] Routes chargées : static=" + staticRoutes.size() +
                    ", dynamic=" + dynamicRoutes.size());

            
            

        } catch (AetherScanException e) {
            throw new ServletException("[AETHERIS][ERREUR INIT] Impossible de scanner les classes.", e);
        }
    }

    // GET -> same pipeline
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String uri = AetherPathUtils.normalize(req.getRequestURI().substring(req.getContextPath().length()));
        if (serveStatic(resp, uri)) return;

        handleFrameworkRoute(req, resp, uri);
    }

    // POST -> treat like GET, ensure character encoding for form body
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (java.io.UnsupportedEncodingException ignored) { /* best-effort */ }

        String uri = AetherPathUtils.normalize(req.getRequestURI().substring(req.getContextPath().length()));
        if (serveStatic(resp, uri)) return;

        handleFrameworkRoute(req, resp, uri);
    }

    private boolean serveStatic(HttpServletResponse resp, String uri) throws IOException {
        String realPath = getServletContext().getRealPath(uri);
        File file = new File(realPath);
        if (file.exists() && file.isFile()) {
            String mimeType = getServletContext().getMimeType(uri);
            if (mimeType == null) mimeType = "application/octet-stream";
            resp.setContentType(mimeType);
            resp.setContentLengthLong(file.length());
            try (InputStream in = new FileInputStream(file);
                 OutputStream out = resp.getOutputStream()) {
                in.transferTo(out);
            }
            return true;
        }
        return false;
    }

    private void handleFrameworkRoute(HttpServletRequest req, HttpServletResponse resp, String uri)
            throws IOException, ServletException {

        AetherRouteMapping matchedRoute = findRoute(req, uri);

        if (matchedRoute != null) {
            try {
                // Optionnel : extraire les valeurs des variables et les mettre en attribut request
                if (matchedRoute.isDynamic()) {
                    Map<String, String> pathVars = AetherPathUtils.match(matchedRoute.getOriginalUri(), uri);
                    if (pathVars != null) req.setAttribute("AETHER_PATH_VARS", pathVars);
                }

                // Execute (AetherRouteExecutor gère conversion/injection)
                AetherRouteExecutor.execute(matchedRoute, req, resp);
                System.out.println("[AETHERIS] Route exécutée : " + uri);


            } catch (com.sarobidy.aetheris.exeptions.AetherUnauthorizedException e) {
                // Gestion spécifique pour les erreurs d'autorisation
                System.err.println("[AETHERIS][SÉCURITÉ] Accès refusé : " + uri);
                System.err.println("[AETHERIS][SÉCURITÉ] Rôle utilisateur : " + e.getUserRole());
                System.err.println("[AETHERIS][SÉCURITÉ] Rôles requis : " + e.getRequiredRole());
                
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                req.setAttribute("errorMessage", e.getMessage());
                req.setAttribute("userRole", e.getUserRole());
                req.setAttribute("requiredRole", e.getRequiredRole());
                req.setAttribute("requestedUri", uri);
                
                RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/error-unauthorized.jsp");
                if (dispatcher != null) {
                    dispatcher.forward(req, resp);
                } else {
                    resp.setContentType("text/plain;charset=UTF-8");
                    resp.getWriter().write(e.getMessage());
                }
                
            } catch (Exception e) {
                System.err.println("[AETHERIS][ERREUR] Impossible d'exécuter la route : " + uri);
                resp.setContentType("text/plain;charset=UTF-8");
                resp.getWriter().write("[AETHERIS][ERREUR] Impossible d'exécuter la route : " + uri + "\n");
                resp.getWriter().write(e.getClass().getName() + ": " + e.getMessage() + "\n");
                e.printStackTrace(resp.getWriter());
            }


        } else {
            resp.setContentType("text/plain;charset=UTF-8");
            resp.getWriter().write("[AETHERIS] L'URI " + uri + " n'est pas pris en charge.\n");
        }
    }

    @SuppressWarnings("unchecked")
    private AetherRouteMapping findRoute(HttpServletRequest req, String uri) {
        ServletContext context = req.getServletContext();
    
        Map<String, AetherRouteMapping> staticRoutes =
                (Map<String, AetherRouteMapping>) context.getAttribute("AETHER_STATIC_ROUTES");
        List<AetherRouteMapping> dynamicRoutes =
                (List<AetherRouteMapping>) context.getAttribute("AETHER_DYNAMIC_ROUTES");
    
        HttpMethod http = HttpMethod.valueOf(req.getMethod()); 
    
     
        String staticKey = AetherPathUtils.normalize(uri) + "::" + http.name();
        if (staticRoutes.containsKey(staticKey)) {
            return staticRoutes.get(staticKey);
        }
    
        
        if (dynamicRoutes != null) {
            for (AetherRouteMapping mapping : dynamicRoutes) {
                if (mapping.getHttpMethods().contains(http) &&
                        mapping.getRegex().matcher(AetherPathUtils.normalize(uri)).matches()) {
                    return mapping;
                }
            }
        }
    
        return null;
    }

}
