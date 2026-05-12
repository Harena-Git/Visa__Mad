# Sprint 4 avec Vue.js - Guide Complet

## 📌 Aperçu

Le Sprint 4 a été converti de JavaScript vanilla en **Vue.js 3** en utilisant une approche Progressive Web App.

### Fichiers créés/modifiés :

| Fichier | Type | Description |
|---------|------|-------------|
| `tracking-vue.html` | HTML | Page principale - Version Vue.js du suivi |
| `js/tracking-app.js` | JS | Application Vue.js complète |

---

## 🚀 Utilisation

### Accéder à la page de suivi Vue.js

```
URL: http://localhost:8080/visa-backoffice/tracking-vue.html
```

### Trois modalités de recherche :

#### 1️⃣ **Recherche par Numéro de Passeport**
- Saisir un numéro de passeport
- Affiche toutes les demandes associées
- Affiche les QR codes et historique pour chaque demande

#### 2️⃣ **Recherche par Token de Suivi**
- Saisir un token unique ou coller une URL de suivi
- Le système extrait automatiquement le token de l'URL
- Affiche les détails complets de la demande

#### 3️⃣ **Génération de Lien de Suivi**
- Saisir le numéro d'une demande
- Génère un token unique
- Fournit :
  - Token de suivi à copier
  - URL complète
  - QR code en Base64

---

## 🏗️ Architecture Vue.js

### Structure de l'application

```javascript
trackingApp = {
    data() {
        // État réactif de l'application
        activeTab,           // Onglet actif
        passportNumber,      // Numéro de passeport
        trackingToken,       // Token de suivi
        loading,             // État du chargement
        results,             // Résultats de recherche
        generationResult,    // Résultat de génération QR
    },
    methods: {
        // Méthodes pour interagir avec l'API
        searchByPassport(),
        searchByToken(),
        generateTrackingLink(),
        performSearch(),
        showError(),
        showSuccess(),
    }
}
```

---

## 📡 Endpoints API utilisés

### Recherche par passeport
```http
GET /api/public/tracking/passport/{numero}
```

**Réponse :**
```json
{
  "success": true,
  "demandes": [
    {
      "demandeId": "REQ-XXXXX",
      "demandeur": { "nom": "...", "prenom": "..." },
      "typeVisa": "...",
      "categorie": "...",
      "statutActuel": "SCAN TERMINÉ",
      "dateCreation": "2026-05-01",
      "qrCodeBase64": "...",
      "trackingToken": "...",
      "historique": [...]
    }
  ]
}
```

### Recherche par token
```http
GET /api/public/tracking/token/{trackingToken}
```

### Génération de QR code
```http
POST /api/demandes/{demandeId}/generate-qr
?mode=data
&baseUrl=http://localhost:8080/visa-backoffice/tracking-vue.html?token=
```

---

## 🎨 Fonctionnalités Vue.js

### 1️⃣ **Réactivité des données**
- Binding bidirectionnel avec `v-model`
- Les changements se reflètent immédiatement dans l'interface

### 2️⃣ **Gestion des onglets**
```vue
<button 
    v-for="tab in tabs" 
    :key="tab.id"
    @click="activeTab = tab.id"
    :class="['tab-button', { active: activeTab === tab.id }]"
>
```

### 3️⃣ **Rendu conditionnel**
```vue
<div v-if="results.length > 0"><!-- Afficher les résultats --></div>
<div v-else-if="!loading && searchPerformed"><!-- Aucun résultat --></div>
```

### 4️⃣ **Rendu de listes**
```vue
<div v-for="result in results" :key="result.demandeId">
    <!-- Afficher chaque résultat -->
</div>
```

### 5️⃣ **Gestion des événements**
```vue
@click="methodName()"
@keyup.enter="searchByPassport"
:disabled="loading"
```

### 6️⃣ **Transitions**
```vue
<transition name="fade">
    <div v-if="errorMessage">{{ errorMessage }}</div>
</transition>
```

---

## 💾 Gestion de l'état

### État initial
```javascript
data() {
    return {
        activeTab: 'passport',
        passportNumber: '',
        loading: false,
        results: [],
        // ...
    }
}
```

### Réactivité automatique
Vue.js détecte les changements et met à jour l'interface automatiquement :
```javascript
this.activeTab = 'token';  // Met à jour l'interface
this.results = data.demandes;  // Affiche les résultats
```

---

## 🔄 Flux d'exécution

### Scénario 1 : Recherche par passeport

```
1. Utilisateur saisit un numéro → v-model met à jour passportNumber
2. Clic sur le bouton → appelle searchByPassport()
3. Validation du champ
4. Appel API GET /api/public/tracking/passport/{numero}
5. Vue.js détecte le changement de results
6. Interface se met à jour avec v-for
7. Les transitions CSS s'appliquent
```

### Scénario 2 : Génération QR code

```
1. Utilisateur saisit un ID → v-model met à jour demandeIdInput
2. Clic sur le bouton → appelle generateTrackingLink()
3. Appel API POST /api/demandes/{id}/generate-qr
4. Réponse complète : token + QR code
5. generationResult.success = true
6. Vue.js affiche le bloc de résultats (v-if)
7. Images et champs affichent les données réactives
```

---

## 🎯 Avantages Vue.js vs Vanilla JS

| Aspect | Vanilla JS | Vue.js |
|--------|-----------|--------|
| **Binding de données** | Manuel | Automatique (v-model) |
| **Rendu conditionnel** | innerHTML | v-if / v-show natif |
| **Gestion d'événements** | addEventListener | @click / @keyup |
| **Listes dynamiques** | boucles et DOM | v-for avec tracking |
| **Maintenance** | Complexe | Simplifiée |
| **Performance** | Virtuelle | Optimisée |

---

## 🧪 Tests

### Test 1 : Recherche par passeport
```bash
1. Accéder à tracking-vue.html
2. Onglet "Par Numéro de Passeport"
3. Saisir "PAD123456789"
4. Cliquer "Rechercher"
→ Résultats affichés dynamiquement
```

### Test 2 : Génération QR code
```bash
1. Onglet "Générer un Lien"
2. Saisir "REQ-SPRINT4-001"
3. Cliquer "Générer"
→ Token, URL et QR code affichés
→ Cliquer "Copier" pour copier dans le presse-papiers
```

### Test 3 : Suivi par token
```bash
1. Onglet "Par Lien / Code"
2. Coller le token généré
3. Cliquer "Accéder à mon Suivi"
→ Détails de la demande affichés
```

---

## 📦 Dépendances

### Vue.js 3 (CDN)
```html
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
```
- Aucune dépendance supplémentaire
- Approche Progressive Enhancement
- Fonctionne en ligne ou hors ligne

---

## 🔧 Configuration

### Base URL des APIs
Modifiable dans `js/tracking-app.js` :
```javascript
apiBaseUrl: '/visa-backoffice/api/public/tracking'
```

### Format de date et localisation
La localisation française est définie dans `formatDate()` :
```javascript
date.toLocaleDateString('fr-FR', { ... })
```

---

## 🚨 Gestion des erreurs

### Messages d'erreur
```javascript
this.showError('Message d\'erreur');
// S'affiche pendant 5 secondes avec transition
```

### Messages de succès
```javascript
this.showSuccess('Message de succès');
// S'affiche pendant 3 secondes
```

### Try-catch pour les APIs
```javascript
try {
    const response = await fetch(url);
    // ...
} catch (error) {
    this.showError('Erreur lors de la recherche');
}
```

---

## 📱 Responsive Design

- Mobile : 768px et moins
- Grille adapative 1fr / 2fr → 1fr
- Flexbox pour les tabs
- Textes redimensionnés

---

## 🎓 Points clés Vue.js

### 1. Réactivité
```javascript
data() {
    return { passportNumber: '' }
}
// Modification automatique détectée
this.passportNumber = "PAD123";
```

### 2. Binding bidirectionnel
```vue
<input v-model="passportNumber">
<!-- Synchronise automatiquement -->
```

### 3. Rendu conditionnel
```vue
<div v-if="loading">Chargement...</div>
<div v-else-if="results.length > 0">Résultats</div>
```

### 4. Rendu de liste avec clé
```vue
<div v-for="result in results" :key="result.demandeId">
    {{ result.demandeId }}
</div>
```

### 5. Gestion d'événements
```vue
@click="methodName()"
@keyup.enter="searchByPassport"
:disabled="loading"
```

---

## 📈 Améliorations futures possibles

1. **Composants réutilisables**
   - `<SearchForm>`, `<ResultCard>`, `<Timeline>`

2. **Composables**
   - Logique d'API réutilisable
   - Gestion des états communs

3. **Vue Router**
   - Navigation entre pages
   - URLs dynamiques : `/tracking/:id`

4. **Pinia Store**
   - Gestion d'état centralisée
   - Partage d'état entre pages

5. **Animations avancées**
   - Transitions de page
   - Animations de liste

6. **PWA**
   - Service Worker
   - Mode hors ligne

---

## 📚 Ressources

- [Vue.js Documentation](https://vuejs.org)
- [Vue.js Guide](https://vuejs.org/guide/)
- [API Reference](https://vuejs.org/api/)

---

**Version :** 1.0  
**Date :** 2026-05-12  
**Framework :** Vue.js 3  
**État :** Production Ready ✅
