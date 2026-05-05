# 🔗 LIENS DIRECTS - SPRINT 3

## 📍 ACCÈS À L'APPLICATION

**URL Page Demandes:**
```
http://localhost:8080/backoffice/demandes.html
```

---

## 🧪 4 DEMANDES DE TEST

| # | Nom | Type Visa | Complétude | Action Directe |
|---|-----|-----------|------------|----------------|
| 1 | RAKOTO Jean | ÉTUDIANT | ✅ **100%** COMPLET | Clic "📤 Importer" → Voir bouton "✅ SCAN TERMINÉ" |
| 2 | DUPONT Marie | TRAVAILLEUR | ⏳ **50%** INCOMPLET | Clic "📤 Importer" → Uploader fichiers manquants |
| 3 | SMITH David | ÉTUDIANT | ⏳ **60%** PARTIELLEMENT | Clic "📤 Importer" → Compléter les 2 pièces |
| 4 | BERNARD Sophie | TRAVAILLEUR | ⏳ **0%** VIDE | Clic "📤 Importer" → Upload complet depuis zéro |

---

## 📂 FICHIERS MODIFIÉS/CRÉÉS

### **Frontend (HTML/JS):**
```
d:\Cours\Framework_Mr_Naina\Visa__Mad\backoffice\src\main\resources\static\demandes.html
```

**Modifications:**
- ✅ Modal piecesJointesModal (lignes ~185-230)
- ✅ 8 nouvelles fonctions JavaScript (lignes ~378-618)
- ✅ Styles CSS Sprint 3 (lignes ~660-720)

### **Données SQL de Test:**
```
d:\Cours\Framework_Mr_Naina\Visa__Mad\base\data_test_sprint_3.sql
```

**Contient:**
- 4 demandeurs de test
- 4 passeports
- 4 demandes avec check_pieces variées
- Données de référence (types visa, catégories, etc.)

### **Guide de Test Complet:**
```
d:\Cours\Framework_Mr_Naina\Visa__Mad\GUIDE_TEST_SPRINT3.md
```

---

## 🎯 FONCTIONNALITÉS À TESTER

### **Test 1: Demande Complète (RAKOTO)**
```
Étape 1: http://localhost:8080/backoffice/demandes.html
Étape 2: Chercher "RAKOTO" ou naviguer tableau
Étape 3: Clic "📤 Importer" sur ligne DEM_REQ001
Étape 4: VÉRIFIER:
  - Progress bar: 100%
  - Badge: ✅ COMPLET (vert)
  - Bouton "✅ SCAN TERMINÉ" VISIBLE
Étape 5: Clic "✅ SCAN TERMINÉ"
Résultat: Dossier marqué SCAN_TERMINE en base
```

### **Test 2: Upload Fichiers (DUPONT)**
```
Étape 1: Clic "📤 Importer" sur ligne DEM_REQ002
Étape 2: VÉRIFIER état initial:
  - Progress bar: 50%
  - Badge: ⏳ INCOMPLET (orange)
  - Bouton "✅ SCAN TERMINÉ" MASQUÉ
Étape 3: Clic "📤 Importer" sur "Contrat de travail"
Étape 4: Uploader un fichier (PDF/JPG)
Étape 5: VÉRIFIER mise à jour:
  - Progress bar: 75%
  - Ligne devient verte
  - Fichier affiché
Étape 6: Uploader "Attestation d'emploi"
Étape 7: VÉRIFIER:
  - Progress bar: 100%
  - Badge: ✅ COMPLET
  - Bouton "✅ SCAN TERMINÉ" VISIBLE
```

### **Test 3: Suppression Fichier (Tous)**
```
Dans n'importe quel modal (ex: RAKOTO):
Étape 1: Localiser ligne avec fichier
Étape 2: Clic "🗑️ Supprimer"
Étape 3: Confirmer suppression
Résultat:
  - Fichier supprimé
  - Progress bar reculée
  - Ligne redevient rouge
```

### **Test 4: Upload depuis Zéro (BERNARD)**
```
Étape 1: Clic "📤 Importer" sur ligne DEM_REQ004
Étape 2: Voir 0/4 pièces complètes
Étape 3: Uploader les 4 fichiers obligatoires
Résultat: Progress bar passe 0% → 25% → 50% → 75% → 100%
```

---

## 🔧 AVANT DE TESTER

### **1. Charger les données SQL:**
```bash
psql -U postgres -d visa_db -f d:\Cours\Framework_Mr_Naina\Visa__Mad\base\data_test_sprint_3.sql
```

Ou depuis l'interface DBeaver/PgAdmin:
```sql
-- Copier-coller le contenu de data_test_sprint_3.sql et exécuter
```

### **2. Vérifier Backend APIs:**
Ces endpoints doivent être implémentés côté Java:
```
✅ GET    /demandes/{id}/check-pieces
✅ GET    /pieces
✅ POST   /check-pieces/upload
✅ DELETE /check-pieces/{demandeId}/{pieceId}
✅ POST   /demandes/{id}/mark-scan-termine
```

### **3. Lancer l'application:**
```bash
# Depuis la racine du projet backoffice
mvn spring-boot:run
```

### **4. Accéder:**
```
http://localhost:8080/backoffice/demandes.html
```

---

## 📊 CE QUI SE PASSE EN BACKEND (À IMPLÉMENTER)

### **API 1: GET /demandes/{id}/check-pieces**
```javascript
// Retour attendu:
[
  {
    id: { idDemande: "DEM_REQ001", idPiece: "PIECE001" },
    estFourni: true,
    fileName: "passeport_jean.pdf",
    fileType: "application/pdf",
    piece: {
      libelle: "Passeport",
      estObligatoire: 1
    }
  },
  ...
]
```

### **API 3: POST /check-pieces/upload**
```javascript
// Body: FormData
{
  file: <File>,
  idDemande: "DEM_REQ001",
  idPiece: "PIECE001"
}

// Action backend:
// 1. Sauvegarder le fichier physiquement
// 2. UPDATE check_piece SET:
//    - est_fourni = TRUE
//    - file_name = nom_du_fichier
//    - file_type = type_mime
//    - updated_at = NOW()
```

### **API 5: POST /demandes/{id}/mark-scan-termine**
```javascript
// Body:
{
  idStatut: "SCAN_TERMINE",
  date: "2026-04-28"
}

// Actions backend:
// 1. Vérifier que TOUTES pièces obligatoires ont fileName + fileType
// 2. INSERT statut_demande:
//    - id_statut_demande: UUID()
//    - date_: TODAY()
//    - id_statut: 'STAT002' (SCAN_TERMINE)
//    - id_demande: 'DEM_REQ001'
// 3. UPDATE demande SET updated_at = NOW()
// 4. Verrouiller la demande (côté backend, vérifier statut avant UPDATE)
```

---

## ✅ CHECKLIST DE VALIDATION

- [ ] Tableau demandes affiche les 4 demandes
- [ ] Bouton "📤 Importer" actif pour chaque demande
- [ ] Modal piecesJointesModal s'ouvre correctement
- [ ] Pièces affichées séparées (obligatoires vs optionnelles)
- [ ] Progress bar 0-100% fonctionne
- [ ] Upload fichier fonctionne (POST /check-pieces/upload)
- [ ] Progress bar se met à jour après upload
- [ ] Bouton "✅ SCAN TERMINÉ" visible si 100%
- [ ] Bouton "✅ SCAN TERMINÉ" masqué si < 100%
- [ ] Clic "✅ SCAN TERMINÉ" appelle POST /mark-scan-termine
- [ ] Statut_demande mis à jour en base avec SCAN_TERMINE
- [ ] Suppression fichier fonctionne (DELETE)
- [ ] Progress bar reculée après suppression
- [ ] Modal se ferme correctement
- [ ] Tableau rechargé après SCAN TERMINÉ

---

## 🎬 DÉMONSTRATION RAPIDE (2 min)

1. Ouvrir: `http://localhost:8080/backoffice/demandes.html`
2. Chercher "RAKOTO" → Clic "📤 Importer"
3. Modal s'ouvre → Voir "✅ COMPLET" 100%
4. Clic "✅ SCAN TERMINÉ" → Voir alert "Dossier marqué"
5. Chercher "DUPONT" → Clic "📤 Importer"
6. Voir "⏳ INCOMPLET" 50%
7. Uploader un fichier manquant
8. Voir progress bar passer à 75%
9. Uploader deuxième fichier → 100%
10. Voir bouton "✅ SCAN TERMINÉ" apparaître

---

**Guide créé le:** 2026-04-28  
**Sprint:** 3 - Numérisation et Verrouillage  
**État:** ✅ Prêt pour test
