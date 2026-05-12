# Guide Complet - Sprint 4 en Vue.js

## 📌 Vue d'ensemble

Le Sprint 4 a été complètement converti en Vue.js avec **deux approches** :

1. **App Simple** - Une application monolithique (tracking-vue.html)
2. **App Composants** - Architecture basée sur composants réutilisables (tracking-composants.html)

---

## 📁 Structure des fichiers

```
backoffice/src/main/resources/static/
├── tracking-vue.html                  # Version simple Vue.js
├── tracking-composants.html           # Version avec composants
└── js/
    ├── tracking-app.js               # App simple
    └── tracking-composants.js        # App avec composants
```

---

## 🎯 Comparaison des deux approches

### Approche 1 : Application Simple (tracking-vue.html)

#### Caractéristiques :
- ✅ **Simple et directe**
- ✅ **Parfait pour les petits projets**
- ✅ **Apprentissage rapide de Vue.js**
- ❌ Code moins réutilisable
- ❌ Moins de séparation des préoccupations

#### Cas d'usage :
- Pages simples
- Prototypes rapides
- Applications MVP

#### Taille du code :
- `tracking-app.js` : ~300 lignes

---

### Approche 2 : Architecture Composants (tracking-composants.html)

#### Caractéristiques :
- ✅ **Code réutilisable**
- ✅ **Meilleure maintenabilité**
- ✅ **Séparation des préoccupations**
- ✅ **Scalable**
- ✅ **Testable**
- ❌ Code plus verbeux au départ

#### Cas d'usage :
- Applications complexes
- Équipes de développement
- Applications long terme
- Réutilisation de code

#### Taille du code :
- `tracking-composants.js` : ~450 lignes (mais beaucoup plus modulaire)

---

## 🔄 Architecture Détaillée

### Approche Simple (tracking-vue.html)

```
Vue Application
│
├── Data (état central)
│   ├── activeTab
│   ├── passportNumber
│   ├── loading
│   ├── results
│   └── messages
│
├── Methods (logique)
│   ├── searchByPassport()
│   ├── searchByToken()
│   ├── generateTrackingLink()
│   └── showMessage()
│
└── Template (interface)
    ├── Recherche Passeport
    ├── Recherche Token
    ├── Génération QR
    ├── Résultats
    └── Messages
```

---

### Approche Composants (tracking-composants.html)

```
Vue Application
│
├── SearchPassportComponent
│   ├── Data
│   │   └── passportNumber
│   └── Methods
│       └── search()
│
├── SearchTokenComponent
│   ├── Data
│   │   └── trackingToken
│   └── Methods
│       └── search()
│
├── GenerateQRComponent
│   ├── Data
│   │   └── demandeId
│   └── Methods
│       └── generate()
│
├── SearchResultCardComponent
│   └── Props
│       └── result
│
└── Application Principale
    ├── Gère l'état global
    ├── Coordonne les événements
    └── Gère les API calls
```

---

## 🚀 Utilisation

### Version Simple

```
http://localhost:8080/visa-backoffice/tracking-vue.html
```

**Code minimal :**
```javascript
const trackingApp = {
    data() {
        return { /* état */ };
    },
    methods: {
        async searchByPassport() { /* logique */ },
        // ...
    }
};

createApp(trackingApp).mount('#app');
```

---

### Version Composants

```
http://localhost:8080/visa-backoffice/tracking-composants.html
```

**Code réutilisable :**
```javascript
// 1. Définir des composants réutilisables
const SearchPassportComponent = defineComponent({/* ... */});

// 2. Utiliser dans l'app
const app = { components: { SearchPassportComponent } };

// 3. Dans le template
<component :is="currentTabComponent" @search="handleSearch" />
```

---

## 📊 Flux d'exécution

### Scenario : Recherche par Passeport

#### Approche Simple
```
1. Utilisateur tape un passeport
   └─> v-model met à jour passportNumber
   
2. Clic sur le bouton
   └─> @click="searchByPassport"
   
3. Validation + Appel API
   └─> performSearch(url, title)
   
4. API répond
   └─> this.results = data
   
5. Vue.js détecte le changement
   └─> Template se met à jour via v-for
   
6. Affichage des résultats
   └─> Les demandes s'affichent
```

#### Approche Composants
```
1. Utilisateur tape un passeport
   └─> v-model dans SearchPassportComponent
   
2. Clic sur le bouton
   └─> emit('search', data)
   
3. Parent reçoit l'événement
   └─> @search="handleSearch"
   
4. Parent appelle performSearch()
   └─> this.results = data
   
5. Parent met à jour currentTabComponent
   └─> SearchResultCardComponent affiche les résultats
   
6. Les cartes se mettent à jour
   └─> Via les props du composant enfant
```

---

## 💡 Concepts Vue.js Utilisés

### 1. **Data Binding**

#### Simple
```javascript
data() { return { passportNumber: '' } }
```

```html
<input v-model="passportNumber">
```

#### Composants
```javascript
// Dans le composant enfant
props: ['state'],
// Dans le parent
:state="{ loading: this.loading }"
```

---

### 2. **Rendu Conditionnel**

#### Simple
```html
<div v-if="results.length > 0">Résultats</div>
<div v-else-if="!loading && searchPerformed">Aucun résultat</div>
```

#### Composants
```javascript
computed: {
    currentTabComponent() {
        return components[this.activeTab];
    }
}
```

```html
<component :is="currentTabComponent" />
```

---

### 3. **Listes Dynamiques**

#### Simple
```html
<div v-for="result in results" :key="result.demandeId">
    {{ result.demandeId }}
</div>
```

#### Composants
```html
<transition-group name="list" tag="div">
    <search-result-card 
        v-for="result in results"
        :key="result.demandeId"
        :result="result"
    />
</transition-group>
```

---

### 4. **Gestion d'Événements**

#### Simple
```html
@click="searchByPassport"
@keyup.enter="search"
```

#### Composants
```html
<!-- Enfant -->
@click="$emit('search', data)"

<!-- Parent -->
@search="handleSearch"
```

---

### 5. **Transitions**

#### Simple
```html
<transition name="fade">
    <div v-if="errorMessage">{{ errorMessage }}</div>
</transition>
```

#### Composants
```html
<transition-group name="list" tag="div">
    <!-- Éléments animés -->
</transition-group>
```

---

## 🎨 Styles

Les deux versions utilisent les mêmes styles CSS. Ils covering :

- **Responsive Design** : Mobile-first
- **Animations** : Transitions fluides
- **Accessibilité** : Contraste de couleurs élevé
- **Thème** : Cohérent avec le design système

---

## 🔐 Gestion de la Sécurité

### API Publique
```javascript
apiBaseUrl: '/visa-backoffice/api/public/tracking'
```

Les endpoints sont marqués `@CrossOrigin` et ne nécessitent pas d'authentification.

### Validation
```javascript
if (!this.passportNumber.trim()) {
    this.showMessage('Veuillez entrer un numéro', 'error');
    return;
}
```

### Gestion des Erreurs
```javascript
try {
    const response = await fetch(url);
    // ...
} catch (error) {
    this.showMessage('Erreur lors de la recherche', 'error');
}
```

---

## 📈 Performance

### Optimisations Vue.js

1. **v-show vs v-if**
   - Tabs : v-show (peu de changements DOM)
   - Messages : v-if (moins de DOM côté)

2. **Keys dans v-for**
   ```html
   v-for="result in results" :key="result.demandeId"
   ```

3. **Memoization avec computed** (dans composants)
   ```javascript
   computed: {
       currentTabComponent() { /* ... */ }
   }
   ```

4. **Lazy loading** (possible futur)
   ```javascript
   const SearchPassportComponent = defineAsyncComponent(() =>
       import('./SearchPassport.vue')
   );
   ```

---

## 🧪 Tests

### Test Unitaire Possible (Composants)

```javascript
import { shallowMount } from '@vue/test-utils';
import SearchPassportComponent from './SearchPassportComponent.js';

describe('SearchPassportComponent', () => {
    it('émet search au clic', () => {
        const wrapper = shallowMount(SearchPassportComponent);
        wrapper.vm.$emit('search');
        expect(wrapper.emitted('search')).toBeTruthy();
    });
});
```

---

## 🚀 Migration Futures

### De Simple vers Composants

Si vous commencez par la **version simple**, vous pouvez migrer progressivement :

```javascript
// Étape 1 : Identifier les composants
// - SearchForm
// - ResultCard
// - Timeline

// Étape 2 : Extraire le code
const SearchFormComponent = defineComponent({/* ... */});

// Étape 3 : Utiliser dans l'app
const app = {
    components: { SearchFormComponent }
};

// Étape 4 : Remplacer le template
<search-form-component @search="handleSearch" />
```

---

## 🎓 Points Clés à Retenir

### ✅ Vue.js excelle à :
- Gestion d'état réactif
- Rendu dynamique
- Gestion d'événements
- Animations
- Composants réutilisables

### ⚠️ Alternative à considérer :
- **React** : Plus populaire, mais setup plus complexe
- **Svelte** : Plus léger, syntaxe plus simple
- **Vanilla JS** : Simple pour cas très spécifiques

---

## 📚 Ressources

### Documentation
- [Vue.js Guide](https://vuejs.org/guide/)
- [API Reference](https://vuejs.org/api/)
- [Examples](https://vuejs.org/examples/)

### Tutoriels
- [Vue Mastery](https://www.vuemastery.com)
- [Official Tutorial](https://vuejs.org/tutorial/)

### Communauté
- [Vue Forum](https://forum.vuejs.org)
- [Discord Vue.js](https://discord.com/invite/vue)

---

## ✨ Résumé

| Aspect | Simple | Composants |
|--------|--------|-----------|
| **Courbe d'apprentissage** | Facile | Modérée |
| **Temps de développement** | Rapide | Plus long |
| **Maintenabilité** | Moyenne | Excellente |
| **Réutilisabilité** | Faible | Excellente |
| **Scalabilité** | Limitée | Excellente |
| **Taille du bundle** | Plus petit | Plus grand |
| **Pour commencer** | ✅ Recommandé | ✅ Après Simple |

---

**Recommandation** : Commencez par la **version simple** pour apprendre Vue.js, puis migrez vers la **version composants** quand la complexité augmente.

**Version :** 1.0  
**Date :** 2026-05-12  
**Auteur :** Sprint 4 Vue.js Team  
**État :** Production Ready ✅
