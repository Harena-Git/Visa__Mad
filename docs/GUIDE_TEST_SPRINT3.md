# 📋 SPRINT 3 - GUIDE DE TEST
## Liens directs et cas de test

---

## 🎯 ACCÈS À LA PAGE DE TEST

**URL Principale:**
```
http://localhost:8080/backoffice/demandes.html
```

---

## 📊 4 CAS DE TEST INCLUS

Chaque cas teste un scénario différent du Sprint 3.

---

## 🧪 CAS DE TEST 1: DEMANDE COMPLÈTE (100%)
### **Demandeur:** RAKOTO Jean  
### **Visa:** ÉTUDIANT (Nouveau)  
### **État:** ✅ COMPLET - Bouton "SCAN TERMINÉ" VISIBLE

**ID Demande:** `DEM_REQ001`  
**Données:**
- Demandeur: Jean RAKOTO (né 1995-05-15)
- Passeport: A123456789 (Valide jusqu'à 2030)
- Pièces: 5/5 obligatoires COMPLÈTES + 1/2 optionnelles
  - ✅ Passeport: `passeport_jean.pdf`
  - ✅ Photo: `photo_jean.jpg`
  - ✅ Baccalauréat: `baccalaureat_jean.pdf`
  - ✅ Acceptation: `acceptation_universite.pdf`
  - ✅ Finances: `attestation_financiere.pdf`

### **Étapes de test:**

1. **Accéder au tableau des demandes**
   ```
   URL: http://localhost:8080/backoffice/demandes.html
   ```

2. **Rechercher RAKOTO Jean dans le tableau**
   - Utilisez le champ "Rechercher par demandeur" 
   - Ou naviguez dans la pagination

3. **Cliquer sur le bouton "📤 Importer"**
   ```
   Ligne: Demande DEM_REQ001
   Bouton: "📤 Importer"
   ```

4. **Le modal s'ouvre - Vérifier:**
   - ✅ Progress bar à 100%
   - ✅ Text: "5/5 pièces obligatoires complètes"
   - ✅ Badge: "✅ COMPLET" (en vert)
   - ✅ Bouton "✅ SCAN TERMINÉ" VISIBLE

5. **Cliquer sur "✅ SCAN TERMINÉ"**
   - Une alerte confirme: "✅ Dossier marqué comme SCAN TERMINÉ"
   - Le modal se ferme
   - Les demandes se rechargent
   - Statut enregistré dans table `statut_demande` avec id_statut = 'SCAN_TERMINE'

---

## 🧪 CAS DE TEST 2: DEMANDE INCOMPLÈTE (50%)
### **Demandeur:** DUPONT Marie  
### **Visa:** TRAVAILLEUR (Nouveau)  
### **État:** ⏳ INCOMPLÈTE - Bouton "SCAN TERMINÉ" MASQUÉ

**ID Demande:** `DEM_REQ002`  
**Données:**
- Demandeur: Marie DUPONT (née 1992-08-22)
- Passeport: B987654321 (Valide jusqu'à 2029)
- Pièces: 2/4 obligatoires COMPLÈTES
  - ✅ Passeport: `passeport_marie.pdf`
  - ✅ Photo: `photo_marie.jpg`
  - ❌ Contrat travail: VIDE
  - ❌ Attestation emploi: VIDE

### **Étapes de test:**

1. **Rechercher DUPONT Marie**
   ```
   URL: http://localhost:8080/backoffice/demandes.html
   ```

2. **Cliquer "📤 Importer" pour DEM_REQ002**

3. **Le modal s'ouvre - Vérifier:**
   - ✅ Progress bar à 50%
   - ✅ Text: "2/4 pièces obligatoires complètes"
   - ✅ Badge: "⏳ INCOMPLET" (en orange)
   - ❌ Bouton "✅ SCAN TERMINÉ" MASQUÉ (display: none)

4. **Tester l'upload manquant:**

   a) Cliquer sur "📤 Importer" pour "Contrat de travail"
   ```
   Ligne: ❌ Contrat de travail
   Bouton: "📤 Importer"
   ```

   b) Sélectionner un fichier PDF (ou image)
   ```
   Exemple: contrat_marie.pdf
   ```

   c) Après upload:
   - ✅ Progress bar passe à 75% (3/4)
   - ✅ Ligne "Contrat de travail" devient verte
   - ✅ Affiche le nom du fichier et type MIME

   d) Uploader "Attestation d'emploi"
   ```
   Cliquer "📤 Importer"
   Sélectionner fichier: attestation_marie.pdf
   ```

5. **Après complétude:**
   - ✅ Progress bar à 100%
   - ✅ Badge change en "✅ COMPLET"
   - ✅ Bouton "✅ SCAN TERMINÉ" DEVIENT VISIBLE
   - 🎯 Cliquer le bouton pour finaliser

---

## 🧪 CAS DE TEST 3: DEMANDE PARTIELLEMENT COMPLÈTE (60%)
### **Demandeur:** SMITH David  
### **Visa:** ÉTUDIANT (Nouveau)  
### **État:** ⏳ PARTIELLEMENT COMPLÈTE

**ID Demande:** `DEM_REQ003`  
**Données:**
- Demandeur: David SMITH (né 1998-03-30)
- Passeport: C555666777 (Valide jusqu'à 2031)
- Pièces: 3/5 obligatoires COMPLÈTES (60%)
  - ✅ Passeport: `passeport_david.pdf`
  - ✅ Photo: `photo_david.jpg`
  - ✅ Baccalauréat: `baccalaureat_david.pdf`
  - ❌ Acceptation université: VIDE
  - ❌ Attestation financière: VIDE

### **Étapes de test:**

1. **Rechercher SMITH David**
   ```
   URL: http://localhost:8080/backoffice/demandes.html
   ```

2. **Cliquer "📤 Importer" pour DEM_REQ003**

3. **Vérifier l'état initial:**
   - ✅ Progress bar à 60%
   - ✅ Text: "3/5 pièces obligatoires complètes"
   - ✅ Badge: "⏳ INCOMPLET"
   - ❌ Bouton "✅ SCAN TERMINÉ" MASQUÉ

4. **Compléter les fichiers manquants:**

   a) Upload "Lettre d'acceptation université"
   ```
   Cliquer "📤 Importer" sur la ligne ❌
   Sélectionner: acceptation_universite_david.pdf
   ```

   b) Upload "Attestation financière"
   ```
   Cliquer "📤 Importer"
   Sélectionner: attestation_financiere_david.pdf
   ```

5. **Vérifier la complétude:**
   - ✅ Progress bar à 100%
   - ✅ Badge: "✅ COMPLET"
   - ✅ Bouton "✅ SCAN TERMINÉ" VISIBLE
   - 🎯 Marquer comme SCAN TERMINÉ

---

## 🧪 CAS DE TEST 4: DEMANDE VIDE (0%)
### **Demandeur:** BERNARD Sophie  
### **Visa:** TRAVAILLEUR (Nouveau)  
### **État:** ⏳ VIDE - Aucun fichier

**ID Demande:** `DEM_REQ004`  
**Données:**
- Demandeur: Sophie BERNARD (née 1994-12-08)
- Passeport: D444888999 (Valide jusqu'à 2032)
- Pièces: 0/4 obligatoires COMPLÈTES
  - ❌ Passeport: VIDE
  - ❌ Photo: VIDE
  - ❌ Contrat travail: VIDE
  - ❌ Attestation emploi: VIDE

### **Étapes de test:**

1. **Rechercher BERNARD Sophie**
   ```
   URL: http://localhost:8080/backoffice/demandes.html
   ```

2. **Cliquer "📤 Importer" pour DEM_REQ004**

3. **État initial:**
   - ✅ Progress bar à 0%
   - ✅ Text: "0/4 pièces obligatoires complètes"
   - ✅ Badge: "⏳ INCOMPLET" (entièrement rouge)
   - ❌ Bouton "✅ SCAN TERMINÉ" MASQUÉ
   - Toutes les lignes sont rouges avec ❌

4. **Test d'upload complet - Uploader tous les fichiers:**

   ```
   Uploader dans cet ordre:
   1. 📤 Importer → passeport_sophie.pdf
   2. 📤 Importer → photo_sophie.jpg
   3. 📤 Importer → contrat_sophie.pdf
   4. 📤 Importer → attestation_sophie.pdf
   ```

5. **Vérifier après chaque upload:**
   - Progress bar augmente (25% → 50% → 75% → 100%)
   - Lignes deviennent vertes progressivement
   - Fichiers s'affichent

6. **À 100% - Finaliser:**
   - ✅ Badge: "✅ COMPLET"
   - ✅ Bouton "✅ SCAN TERMINÉ" VISIBLE
   - 🎯 Cliquer pour marquer SCAN TERMINÉ

---

## 🧪 CAS DE TEST BONUS: SUPPRESSION DE FICHIER

### **Où tester:**

1. Ouvrir n'importe quel modal avec fichiers (ex: DEM_REQ001)

2. Localiser une pièce avec fichier:
   ```
   Ex: ✅ Passeport - passeport_jean.pdf
   ```

3. Cliquer le bouton "🗑️ Supprimer"

4. Confirmer la suppression

5. **Vérifier:**
   - Fichier supprimé
   - Progress bar reculée
   - Ligne redevient rouge (❌)
   - Bouton "✅ SCAN TERMINÉ" redevient masqué (si n'était pas à 100%)

---

## 📱 ÉLÉMENTS DE L'INTERFACE À TESTER

### **Modal des Pièces Jointes**
```
Ligne HTML: demandes.html, lignes ~185-230
ID: piecesJointesModal
```

### **Zone de Statut**
```
Affiche:
- Indicateur: "Statut du dossier: [✅ COMPLET] ou [⏳ INCOMPLET]"
- Barre de progression: [████░░░░░░░░░░░░░░] X%
- Texte: "X/Y pièces obligatoires complètes"
```

### **Liste des Pièces**
```
Deux sections:
1. 📌 Pièces Obligatoires (obligatoires)
2. 📄 Pièces Optionnelles (optionnels)

Chaque pièce affiche:
- Status icon (✅ ou ❌)
- Nom de la pièce
- Nom du fichier (si présent)
- Type MIME (si présent)
- Boutons: 📤 Importer, 🗑️ Supprimer (si fichier)
```

### **Boutons d'Action**
```
1. 📤 Importer - Upload un fichier (toujours visible)
2. 🗑️ Supprimer - Supprimer un fichier (visible si fichier présent)
3. ✅ SCAN TERMINÉ - Valider le dossier (VISIBLE UNIQUEMENT SI 100%)
4. ❌ Fermer - Fermer le modal (toujours visible)
```

---

## 🔗 LIENS DE NAVIGATION DIRECT

### **Page Demandes:**
```
http://localhost:8080/backoffice/demandes.html
```

### **Accès rapide aux demandes test:**

| Demandeur | Visa | Complétude | Lien Bouton |
|-----------|------|------------|------------|
| RAKOTO Jean | ÉTUDIANT | ✅ 100% | Ligne DEM_REQ001 → "📤 Importer" |
| DUPONT Marie | TRAVAILLEUR | ⏳ 50% | Ligne DEM_REQ002 → "📤 Importer" |
| SMITH David | ÉTUDIANT | ⏳ 60% | Ligne DEM_REQ003 → "📤 Importer" |
| BERNARD Sophie | TRAVAILLEUR | ⏳ 0% | Ligne DEM_REQ004 → "📤 Importer" |

---

## 🔍 POINTS DE CONTRÔLE SPRINT 3

- [ ] **Upload fonctionnel** - Fichiers uploadés et sauvegardés
- [ ] **Progress bar** - Augmente avec chaque fichier
- [ ] **Pièces obligatoires vs optionnelles** - Séparées visuellement
- [ ] **Bouton SCAN TERMINÉ** - Visible seulement si 100%
- [ ] **Validation finale** - Statut SCAN_TERMINE enregistré en base
- [ ] **Suppression fichiers** - Revert la progression
- [ ] **Tooltip** - Affiche status au survol
- [ ] **Mobile responsif** - Affichage sur petit écran

---

## ⚠️ PRÉREQUIS AVANT TEST

1. **Base de données:**
   ```bash
   # Exécuter le script SQL de test
   psql -U username -d visa_db -f data_test_sprint_3.sql
   ```

2. **Backend APIs implémentées:**
   - `GET /demandes/{id}/check-pieces`
   - `GET /pieces`
   - `POST /check-pieces/upload`
   - `DELETE /check-pieces/{demandeId}/{pieceId}`
   - `POST /demandes/{id}/mark-scan-termine`

3. **Serveur lancé:**
   ```bash
   # Maven ou IDE
   mvn spring-boot:run
   ```

4. **Frontend accessible:**
   ```
   http://localhost:8080/backoffice/
   ```

---

## 📝 NOTES

- Les fichiers uploadés sont **stockés physiquement** sur le serveur
- Les métadonnées (file_name, file_type) sont **enregistrées en base**
- La complétude est calculée **côté frontend** (voir fonction `checkDossierComplet()`)
- Après SCAN TERMINÉ, le dossier devient **immodifiable** (côté backend)

---

**FIN DU GUIDE DE TEST**
