# 📋 SPRINT 3: NUMÉRISATION ET VERROUILLAGE - GUIDE COMPLET

## 🎯 Objectif
Implémenter un système simplifié d'upload de fichiers et de verrouillage des demandes dans l'application Visa.

---

## ✨ Fonctionnalités Implémentées

### 1️⃣ **Bouton "📤 Importer"**
- **Localisation**: Colonne "Fichiers & Scan" du tableau des demandes
- **Action**: Ouvre un dialogue de sélection de fichier
- **Types supportés**: PDF (.pdf) et Images (.jpg, .jpeg, .png)
- **État**: 
  - ✅ Activé pour demandes NON verrouillées
  - ❌ DÉSACTIVÉ pour demandes verrouillées

### 2️⃣ **Bouton "🔒 Terminer Scann"**
- **Localisation**: Colonne "Fichiers & Scan" du tableau des demandes
- **Action**: Verrouille la demande avec le statut "SCAN_TERMINÉ"
- **Confirmation**: Affiche une alerte de confirmation avant d'exécuter
- **État**:
  - ✅ Activé pour demandes NON verrouillées
  - ❌ DÉSACTIVÉ pour demandes verrouillées

### 3️⃣ **Verrouillage des Demandes**
- Une demande verrouillée ne peut plus être modifiée
- Le bouton "✏️ Modifier" devient gris et DÉSACTIVÉ
- Les statuts "SCAN_TERMINÉ" indiquent un verrouillage

---

## 🔧 Architecture Technique

### Frontend (JavaScript)
```javascript
// FONCTION 1: Ouvrir le dialogue de sélection de fichier
demandeManager.uploadFileDemande(demandeId)

// FONCTION 2: Traiter le fichier sélectionné et l'uploader
demandeManager.handleFileSelected()

// FONCTION 3: Verrouiller la demande (set status SCAN_TERMINÉ)
demandeManager.terminerScanDemande(demandeId)
```

### Backend APIs (à implémenter)
```
POST /demandes/{demandeId}/upload-fichier
  Body: FormData { file, idDemande }
  Response: { success: true, message: "..." }

POST /demandes/{demandeId}/terminer-scan
  Body: { idStatut: "SCAN_TERMINE", date: "YYYY-MM-DD" }
  Response: { success: true, message: "..." }
```

---

## 📊 Structure de la Base de Données

### Table `check_piece` (existante - à utiliser)
```sql
CREATE TABLE check_piece (
    id_demande VARCHAR(255),
    id_piece VARCHAR(255),
    est_fourni BOOLEAN,
    updated_at TIMESTAMP,
    file_name VARCHAR(255),           -- ⭐ À mettre à jour lors de l'upload
    file_type VARCHAR(100),           -- ⭐ À mettre à jour lors de l'upload
    FOREIGN KEY (id_demande) REFERENCES demande(id_demande),
    FOREIGN KEY (id_piece) REFERENCES piece(id_piece)
);
```

### Table `statut_demande` (existante - à utiliser)
```sql
CREATE TABLE statut_demande (
    id_statut_demande VARCHAR(255) PRIMARY KEY,
    date_ DATE,
    id_statut VARCHAR(255),           -- ⭐ 'SCAN_TERMINE' pour verrouillage
    id_demande VARCHAR(255),
    FOREIGN KEY (id_statut) REFERENCES statut(id_statut),
    FOREIGN KEY (id_demande) REFERENCES demande(id_demande)
);
```

---

## 🧪 Guide de Test

### Préparation
1. **Charger les données de test**:
   ```bash
   psql -U postgres visa_db < base/data_test_sprint_3.sql
   ```

2. **Vérifier les données**:
   ```sql
   SELECT id_demande, id_demandeur, statutDemande FROM demande ORDER BY created_at;
   ```

### Cas 1: Upload d'un fichier sur une nouvelle demande
**Demande**: `DEM_REQ001` (RAKOTO Jean - ÉTUDIANT)

**Étapes**:
1. ✅ Aller dans le menu "Demandes"
2. ✅ Localiser la ligne RAKOTO
3. ✅ Cliquer sur le bouton "📤 Importer"
4. ✅ Sélectionner un fichier PDF ou image
5. ✅ Vérifier le message ✅ "Fichier uploadé avec succès"
6. ✅ Vérifier que la base de données a mis à jour:
   ```sql
   SELECT file_name, file_type, est_fourni FROM check_piece 
   WHERE id_demande = 'DEM_REQ001' LIMIT 1;
   ```

### Cas 2: Verrouiller une demande
**Continuation du Cas 1**

**Étapes**:
1. ✅ Sur la même demande RAKOTO
2. ✅ Cliquer sur le bouton "🔒 Terminer Scann"
3. ✅ Cliquer "OK" sur l'alerte de confirmation
4. ✅ Vérifier le message ✅ "Demande verrouillée (SCAN TERMINÉ)"
5. ✅ Vérifier que:
   - Le bouton "✏️ Modifier" est GRIS et DÉSACTIVÉ
   - Le bouton "🔒 Terminer Scann" est GRIS et DÉSACTIVÉ
6. ✅ Vérifier la base de données:
   ```sql
   SELECT id_statut, date_ FROM statut_demande 
   WHERE id_demande = 'DEM_REQ001';
   ```

### Cas 3: Tentative de modification d'une demande verrouillée
**Continuation du Cas 2**

**Étapes**:
1. ✅ Sur la demande RAKOTO (maintenant verrouillée)
2. ✅ Essayer de cliquer le bouton "✏️ Modifier"
3. ✅ Vérifier que le clic n'a PAS d'effet (bouton désactivé)
4. ✅ Essayer de cliquer le bouton "🔒 Terminer Scann"
5. ✅ Vérifier que le clic n'a PAS d'effet (bouton désactivé)

### Cas 4: Tests multiples
**Demandes**: `DEM_REQ002`, `DEM_REQ003`, `DEM_REQ004`

**Étapes** (répéter pour chaque demande):
1. ✅ Upload fichier → Vérifier file_name, file_type en base
2. ✅ Terminer Scann → Vérifier statut_demande en base
3. ✅ Vérifier désactivation des boutons Modifier et Terminer Scann

---

## 💻 Implémentation Backend Requise

### Endpoint 1: Upload de fichier
**Fichier**: `DemandeController.java` ou similaire

**Pseudo-code**:
```java
@PostMapping("/demandes/{demandeId}/upload-fichier")
public ResponseEntity<?> uploadFichier(
    @PathVariable String demandeId,
    @RequestParam("file") MultipartFile file,
    @RequestParam("idDemande") String idDemande
) {
    try {
        // 1. Sauvegarder le fichier sur le serveur
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        String filePath = "/uploads/" + demandeId + "/" + fileName;
        // ... code de sauvegarde ...
        
        // 2. Mettre à jour la table check_piece
        checkPieceRepository.updateFileInfo(demandeId, fileName, fileType);
        
        // 3. Retourner succès
        return ResponseEntity.ok(Map.of("success", true, "message", "Fichier uploadé"));
    } catch (Exception e) {
        return ResponseEntity.status(500).body(Map.of("success", false, "error", e.getMessage()));
    }
}
```

### Endpoint 2: Verrouillage de demande
**Fichier**: `DemandeController.java` ou similaire

**Pseudo-code**:
```java
@PostMapping("/demandes/{demandeId}/terminer-scan")
public ResponseEntity<?> terminerScan(
    @PathVariable String demandeId,
    @RequestBody Map<String, String> payload
) {
    try {
        String idStatut = payload.get("idStatut");
        String date = payload.get("date");
        
        // 1. Insérer un nouvel enregistrement dans statut_demande
        StatutDemande sd = new StatutDemande();
        sd.setIdStatutDemande(UUID.randomUUID().toString());
        sd.setIdStatut(idStatut);
        sd.setDate(java.sql.Date.valueOf(date));
        sd.setIdDemande(demandeId);
        statutDemandaRepository.save(sd);
        
        // 2. Mettre à jour la table demande (statut_demande dans JSON ou colonne)
        Demande d = demandeRepository.findById(demandeId).get();
        d.setUpdatedAt(new Date());
        demandeRepository.save(d);
        
        // 3. Retourner succès
        return ResponseEntity.ok(Map.of("success", true, "message", "Demande verrouillée"));
    } catch (Exception e) {
        return ResponseEntity.status(500).body(Map.of("success", false, "error", e.getMessage()));
    }
}
```

---

## 📂 Structure des Fichiers

```
backoffice/src/main/resources/static/
├── demandes.html              ✅ Frontend (modifié)
├── js/
│   └── app.js                 ⏳ À vérifier (demandeManager)
└── css/
    └── style.css              ✅ CSS (nettoyé)

base/
├── create.sql                 ✅ Schéma de base
├── data_test_sprint_3.sql     ✅ Données de test (créé)
└── ...

SPRINT_3_GUIDE.md             ✅ Ce fichier
```

---

## 🚀 Plan d'Exécution

### Phase 1: Préparation ✅ COMPLÉTÉE
- [x] Créer le frontend HTML/CSS/JS
- [x] Ajouter les 3 fonctions JavaScript
- [x] Créer les données de test SQL
- [x] Nettoyer les styles CSS

### Phase 2: Implémentation Backend ⏳ À FAIRE
- [ ] Créer l'endpoint `/demandes/{id}/upload-fichier`
- [ ] Créer l'endpoint `/demandes/{id}/terminer-scan`
- [ ] Configurer le stockage des fichiers
- [ ] Tester les endpoints avec Postman ou curl

### Phase 3: Intégration et Déploiement ⏳ À FAIRE
- [ ] Charger les données de test en base
- [ ] Tester le frontend avec le backend
- [ ] Valider les cas de test
- [ ] Documenter les améliorations futures

---

## 📝 Notes Importantes

### Stockage de fichiers
- **Chemins suggérés**: `/uploads/{demandeId}/` ou `/storage/visa/{demandeId}/`
- **Format de sauvegarde**: Conserver le nom de fichier original ou utiliser UUID
- **Permissions**: Vérifier que l'application peut écrire dans le répertoire

### Base de données
- **check_piece**: Mettre à jour les colonnes `file_name` et `file_type` lors de l'upload
- **statut_demande**: Insérer un nouvel enregistrement avec `id_statut = 'SCAN_TERMINE'`
- **demande**: Mettre à jour `updated_at` et optionnellement un champ de statut

### Sécurité
- ✅ Valider les types MIME acceptés (PDF, JPEG, PNG seulement)
- ✅ Limiter la taille des fichiers (ex: 10MB max)
- ✅ Vérifier les permissions d'accès (démandeId valide, utilisateur autorisé)
- ✅ Vérifier l'existence de la demande avant d'uploader

---

## ❓ FAQ

**Q: Comment tester sans backend?**
A: Vous pouvez utiliser les DevTools du navigateur pour voir les appels Fetch et vérifier qu'ils vont aux bonnes URLs.

**Q: Peut-on uploader plusieurs fichiers?**
A: Pas dans cette version simplifiée. Chaque clic du bouton "Importer" ouvre un nouveau dialogue pour UN seul fichier.

**Q: Comment gérer plusieurs fichiers par demande?**
A: La base de données a une table `check_piece` avec plusieurs pièces par demande. Vous pouvez uploader un fichier par clic jusqu'à ce que toutes les pièces soient remplies.

**Q: Où sont stockés les fichiers uploadés?**
A: À définir dans l'implémentation du backend. Suggestions: disque local, S3, ou autre stockage.

---

## 📞 Support

En cas de problème:
1. Vérifier la console JavaScript (F12 → Console)
2. Vérifier les logs du backend (Spring Boot console)
3. Vérifier la base de données avec les requêtes SQL fournies
4. Consulter la documentation des endpoints

---

**Version**: 1.0  
**Date**: 2026-04-28  
**Auteur**: Assistant AI  
**Statut**: Prêt pour implémentation backend
