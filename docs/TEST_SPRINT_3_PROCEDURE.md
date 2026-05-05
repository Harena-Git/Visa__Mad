## 🧪 PROCÉDURE COMPLÈTE DE TEST - SPRINT 3

### Étape 1: Préparer la base de données

```bash
# 1.1 Insérer les statuts manquants
psql -U postgres -d visa_db -f base/insert_statuts.sql

# 1.2 Insérer les données de test
psql -U postgres -d visa_db -f base/data_test_sprint_3.sql

# 1.3 Vérifier que les données sont présentes
psql -U postgres -d visa_db -c "SELECT id_demande, id_demandeur FROM demande LIMIT 5;"
```

### Étape 2: Démarrer le serveur Spring Boot

```bash
# 2.1 À partir du dossier backoffice
cd backoffice

# 2.2 Démarrer l'application
mvn spring-boot:run

# Vous devriez voir:
# ✅ Tomcat started on port(s): 8080 (http) with context path '/visa-backoffice'
```

### Étape 3: Tester les APIs

#### Test 3A: Vérifier que les demandes sont chargées
```bash
# Accéder à: http://localhost:8080/visa-backoffice/demandes.html
# Vous devriez voir le tableau avec les demandes
```

#### Test 3B: Tester l'upload d'un fichier
```bash
# 1. Sur une demande, cliquer le bouton "📤 Importer"
# 2. Sélectionner un fichier PDF ou image
# 3. Vérifier que:
#    - Message ✅ "Fichier uploadé avec succès" s'affiche
#    - Le fichier est présent en base:
#      SELECT file_name, file_type FROM check_piece WHERE id_demande = 'DEM_REQ001';

# Expected output:
#         file_name         |   file_type
# ---------------------- + ---------
# nom_du_fichier.pdf      | application/pdf
```

#### Test 3C: Tester le verrouillage sans fichier
```bash
# 1. Créer une nouvelle demande ou utiliser une nouvelle demande
# 2. Cliquer le bouton "🔒 Terminer Scann" SANS uploader de fichier
# 3. Vérifier que:
#    - Message ❌ "Impossible de verrouiller: aucun fichier n'a été uploadé"
#    - Le boutton ne verrouille PAS la demande
```

#### Test 3D: Tester le verrouillage avec fichier
```bash
# 1. Sur une demande qui a un fichier uploadé
# 2. Cliquer le bouton "🔒 Terminer Scann"
# 3. Confirmer dans l'alerte
# 4. Vérifier que:
#    - Message ✅ "Demande verrouillée (SCAN TERMINÉ)"
#    - Le bouton "✏️ Modifier" devient GRIS et DÉSACTIVÉ
#    - Le bouton "🔒 Terminer Scann" devient GRIS et DÉSACTIVÉ

# Vérifier en base:
#   SELECT sd.id_statut_demande, sd.id_statut, sd.date_, d.id_demande 
#   FROM statut_demande sd
#   JOIN demande d ON sd.id_demande = d.id_demande
#   WHERE d.id_demande = 'DEM_REQ001';

# Expected output:
#  id_statut_demande   | id_statut       | date_       | id_demande
# -------------------- + ----------- + ----------- + ---------
# [UUID]               | SCAN_TERMINE    | 2026-04-29  | DEM_REQ001
```

#### Test 3E: Tester que la demande verrouillée ne peut pas être modifiée
```bash
# 1. Sur la demande verrouillée (DEM_REQ001)
# 2. Essayer de cliquer le bouton "✏️ Modifier"
# 3. Vérifier que:
#    - Le clic n'a PAS d'effet (bouton désactivé)
#    - Ou afficher un message: "Cette demande est verrouillée"
```

### Étape 4: Dépannage

#### Problème: L'API retourne une erreur 404
```
Solution: Vérifier que le URL est correct:
  - Frontend appelle: /demandes/{id}/upload-fichier
  - Backend écoute sur: @RequestMapping("/demandes")
  - Donc le chemin complet: /visa-backoffice/demandes/{id}/upload-fichier
  
Mais le frontend utilise fetch('/demandes/...')
- Vérifier dans app.js: const API_BASE_URL = '/visa-backoffice/api';
- Vérifier dans demandes.html: fetch('/demandes/...')
  
⚠️ PROBLÈME: Les URLs ne correspondent pas!
```

#### Problème: Le fichier n'est pas enregistré en base
```
Solution:
  1. Vérifier les logs du serveur pour voir s'il y a une erreur
  2. Vérifier que le dossier 'uploads/demandes/' existe et est writable
  3. Vérifier que check_piece a des lignes avec est_fourni=FALSE
```

#### Problème: Terminer Scann ne verrouille pas
```
Solution:
  1. Vérifier que le statut 'SCAN_TERMINE' existe en base:
     SELECT * FROM statut WHERE id_statut = 'SCAN_TERMINE';
  2. Vérifier que check_piece a au moins UN fichier:
     SELECT * FROM check_piece WHERE id_demande = 'DEM_REQ001' AND file_name IS NOT NULL;
  3. Vérifier les logs du serveur pour voir les erreurs
```

---

## 📋 URL DE TEST

| Fonction | URL |
|----------|-----|
| Page HTML | http://localhost:8080/visa-backoffice/demandes.html |
| API GET demandes | http://localhost:8080/visa-backoffice/api/demandes |
| Upload fichier | POST /demandes/{id}/upload-fichier |
| Terminer scan | POST /demandes/{id}/terminer-scan |
| Vérifier verrouillage | GET /demandes/{id}/is-verrouille |

---

## ⚠️ ATTENTION: PROBLÈME URL DÉTECTÉ

Le frontend utilise 2 types d'URL:
```javascript
// Type 1: Direct (pour upload/terminer-scan)
fetch('/demandes/' + demandeId + '/upload-fichier')

// Type 2: Via utils.apiRequest (pour stats)
utils.apiRequest('/demandes/count')
// qui utilise: const API_BASE_URL = '/visa-backoffice/api'
// donc l'URL complète = '/visa-backoffice/api/demandes/count'
```

### Solution requise:
Le backend écoute sur `/demandes/` (sans le `/api/`)
Donc les URL hardcoded doivent être mises à jour pour utiliser la base URL correcte.
