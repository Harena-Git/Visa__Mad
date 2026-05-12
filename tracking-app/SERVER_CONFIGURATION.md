# Configuration du Serveur pour Tracking-App

## Vue d'ensemble

La structure de projet Vue.js `tracking-app` a été créée séparément du dossier backoffice pour une meilleure modularité. Pour que l'application fonctionne correctement, le serveur Spring Boot doit être configuré pour router les requêtes vers les fichiers appropriés.

## Structure du Projet

```
tracking-app/
├── index.html                    # Page d'accueil
├── tracking-vue.html             # Version simple
├── tracking-composants.html      # Version composants
├── css/
│   └── tracking.css             # Styles partagés
└── js/
    ├── tracking-app.js          # Logique version simple
    └── tracking-composants.js   # Logique version composants
```

## URLs Cibles

Après configuration du serveur:

- **Accueil:** `http://localhost:8080/visa-backoffice/tracking`
- **Version simple:** `http://localhost:8080/visa-backoffice/tracking-vue.html`
- **Version composants:** `http://localhost:8080/visa-backoffice/tracking-composants.html`

## Options de Configuration

### Option 1: Configuration Spring Boot (Recommandée)

Modifiez `backoffice/src/main/webapp/WEB-INF/web.xml`:

```xml
<!-- Ajouter une nouvelle servlet pour servir les fichiers statiques de tracking-app -->
<servlet>
    <servlet-name>trackingServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/config/tracking-servlet.xml</param-value>
    </init-param>
</servlet>

<servlet-mapping>
    <servlet-name>trackingServlet</servlet-name>
    <url-pattern>/tracking*</url-pattern>
</servlet-mapping>
```

### Option 2: Configuration via Contrôleur Spring (Plus Flexible)

Créez un nouveau contrôleur `TrackingController.java`:

```java
package com.visa.tracking;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/visa-backoffice/tracking")
public class TrackingController {
    
    @GetMapping("")
    public String index() {
        return "redirect:/visa-backoffice/tracking/index.html";
    }
    
    @GetMapping("/tracking-vue.html")
    public String trackingVue() {
        return "tracking-vue";
    }
    
    @GetMapping("/tracking-composants.html")
    public String trackingComposants() {
        return "tracking-composants";
    }
}
```

Puis placez les fichiers HTML dans `src/main/resources/templates/tracking/`.

### Option 3: Configuration via ResourceHandler Spring

Ajoutez cette configuration dans votre classe `WebConfig`:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/visa-backoffice/tracking/**")
            .addResourceLocations("classpath:/tracking-app/");
    }
}
```

Puis copiez les fichiers du dossier `tracking-app` dans `src/main/resources/tracking-app/`.

### Option 4: Configuration Apache (Alternative)

Si vous utilisez Apache en reverse proxy:

```apache
<Location /visa-backoffice/tracking>
    ProxyPass http://localhost:8080/tracking-app
</Location>
```

## Migration des Fichiers

### Étape 1: Copier les fichiers dans le projet Maven

```bash
# Depuis le dossier racine du projet
xcopy tracking-app\*.html backoffice\src\main\resources\static\tracking\ /Y
xcopy tracking-app\css backoffice\src\main\resources\static\tracking\css\ /Y
xcopy tracking-app\js backoffice\src\main\resources\static\tracking\js\ /Y
```

### Étape 2: Mettez à jour les références de fichiers

Dans les fichiers HTML, assurez-vous que les chemins sont corrects:

```html
<!-- CSS -->
<link rel="stylesheet" href="/visa-backoffice/api/static/tracking/css/tracking.css">

<!-- JS -->
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
<script src="/visa-backoffice/api/static/tracking/js/tracking-app.js"></script>
```

## Tester la Configuration

### Test Local (Avant Déploiement)

1. Lancez le serveur Spring Boot:
```bash
cd backoffice
mvn spring-boot:run
```

2. Accédez aux URLs:
```
http://localhost:8080/visa-backoffice/tracking
http://localhost:8080/visa-backoffice/tracking-vue.html
http://localhost:8080/visa-backoffice/tracking-composants.html
```

3. Vérifiez la console du navigateur pour les erreurs:
```
F12 → Console → Vérifier les erreurs 404 ou CORS
```

### Checklist de Vérification

- [ ] Les fichiers HTML se chargent correctement
- [ ] Les fichiers CSS s'appliquent (la page a du style)
- [ ] Vue.js se charge depuis le CDN (pas d'erreur Vue.js undefined)
- [ ] Les fichiers JS se chargent (vérifier Network tab)
- [ ] Au moins une recherche fonctionne

## Troubleshooting

### Erreur 404 sur les fichiers CSS/JS

**Problème:** Les fichiers CSS ou JS ne se chargent pas.

**Solution:**
```javascript
// Dans les fichiers HTML, vérifiez les chemins des includes
<link href="/visa-backoffice/api/static/tracking/css/tracking.css">
<script src="/visa-backoffice/api/static/tracking/js/tracking-app.js"></script>
```

### Vue.js non défini

**Problème:** `Vue is not defined`

**Solution:** Assurez-vous que le CDN Vue est chargé avant vos scripts:
```html
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
<script src="/path/to/tracking-app.js"></script>
```

### CORS Errors sur les API

**Problème:** `Access-Control-Allow-Origin` error

**Solution:** Le fichier `backoffice/pom.xml` doit avoir la configuration CORS:
```xml
<!-- Dans les dépendances -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

## Déploiement en Production

1. **Empaquetez le JAR:**
```bash
cd backoffice
mvn clean package
```

2. **Lancez l'application:**
```bash
java -jar target/backoffice-0.0.1-SNAPSHOT.jar
```

3. **Vérifiez le WAR (alternative):**
```bash
mvn clean package -Pwar
# Déployez le WAR sur Tomcat
```

## Prochaines Étapes Recommandées

1. **Mettre en cache les ressources statiques:**
```java
registry.addResourceHandler("/tracking/**")
    .addResourceLocations("classpath:/tracking-app/")
    .setCachePeriod(3600); // 1 heure
```

2. **Minifier les fichiers JavaScript/CSS** pour la production

3. **Ajouter une authentification** si nécessaire

4. **Configurer HTTPS** pour les requêtes API sensibles

---

## Support

Pour plus d'aide sur la configuration Spring Boot, consultez:
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Vue.js 3 Guide](https://vuejs.org/)
