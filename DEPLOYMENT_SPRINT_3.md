# ✅ SPRINT 3 - CHANGEMENTS FINALISÉS

## 📋 Résumé des corrections apportées

### 1. **Backend - DemandeSpring3Controller.java** (CRÉÉ)
- ✅ Endpoint `GET /api/demandes/{id}/is-verrouille` - Vérifier si demande est verrouillée
- ✅ Endpoint `POST /api/demandes/{id}/upload-fichier` - Upload avec validation
- ✅ Endpoint `POST /api/demandes/{id}/terminer-scan` - Verrouiller avec validation backend
- ✅ **Validation Backend Critique**: Vérifier qu'au MOINS UN fichier existe avant verrouillage
- ✅ Vérification que demande n'est pas déjà verrouillée
- ✅ Validation du type MIME (PDF, JPEG, PNG, GIF, WebP)
- ✅ Limite de taille fichier (10 MB)

### 2. **Frontend - demandes.html** (MODIFIÉ)
- ✅ Correction des URLs: `/demandes/` → `/visa-backoffice/api/demandes/`
- ✅ Amélioration gestion d'erreurs avec messages détaillés
- ✅ Meilleure gestion du JSON response

### 3. **Repository - StatutDemandeRepository.java** (MODIFIÉ)
- ✅ Ajout de la méthode `findByDemandeId()` pour chercher les statuts d'une demande

### 4. **SQL - insert_statuts.sql** (CRÉÉ)
- ✅ Script pour insérer les statuts manquants dont 'SCAN_TERMINE'

---

## 🚀 PROCÉDURE DE DÉPLOIEMENT

### Étape 1: Insérer les statuts en base de données
```bash
psql -U postgres -d visa_db -f base/insert_statuts.sql
```

### Étape 2: Insérer les données de test
```bash
psql -U postgres -d visa_db -f base/data_test_sprint_3.sql
```

### Étape 3: Redémarrer le serveur Spring Boot
```bash
cd backoffice
mvn clean spring-boot:run
```

Vous devriez voir:
```
✅ Tomcat started on port(s): 8080 (http) with context path '/visa-backoffice'
✅ Application started successfully
```

### Étape 4: Accéder à l'application
```
http://localhost:8080/visa-backoffice/demandes.html
```

---

## 🧪 SCÉNARIOS DE TEST

### Test 1: Upload d'un fichier ✅
1. Cliquer le bouton "📤 Importer" sur une demande
2. Sélectionner un fichier PDF ou image
3. Vérifier: Message ✅ "Fichier uploadé avec succès"
4. Vérifier en base:
   ```sql
   SELECT file_name, file_type, est_fourni FROM check_piece 
   WHERE id_demande = 'DEM_REQ001' LIMIT 1;
   ```

### Test 2: Verrouillage SANS fichier ❌
1. Prendre une demande SANS fichier
2. Cliquer le bouton "🔒 Terminer Scann"
3. Vérifier: Message ❌ "Impossible de verrouiller: aucun fichier n'a été uploadé"
4. Vérifier que rien n'a changé en base

### Test 3: Verrouillage AVEC fichier ✅
1. Prendre une demande AVEC fichier
2. Cliquer le bouton "🔒 Terminer Scann"
3. Confirmer dans l'alerte
4. Vérifier: Message ✅ "Demande verrouillée (SCAN TERMINÉ)"
5. Vérifier que le bouton "✏️ Modifier" devient GRIS
6. Vérifier en base:
   ```sql
   SELECT id_statut, date_ FROM statut_demande 
   WHERE id_demande = 'DEM_REQ001';
   ```

### Test 4: Demande verrouillée - Non modifiable ❌
1. Sur une demande verrouillée
2. Essayer de cliquer "✏️ Modifier"
3. Vérifier: Le bouton est DÉSACTIVÉ (gris) et ne répond pas

### Test 5: Rechargement de page
1. Verrouiller une demande
2. Recharger la page (F5)
3. Vérifier: Les boutons restent DÉSACTIVÉS (l'état persiste)

---

## 🔍 VÉRIFICATION DU STATUT EN BASE

```sql
-- Vérifier les statuts créés
SELECT id_statut, libelle FROM statut ORDER BY id_statut;

-- Vérifier les fichiers uploadés
SELECT id_demande, file_name, file_type, est_fourni 
FROM check_piece 
WHERE file_name IS NOT NULL;

-- Vérifier les demandes verrouillées
SELECT sd.id_demande, sd.id_statut, sd.date_, d.updated_at
FROM statut_demande sd
JOIN demande d ON sd.id_demande = d.id_demande
WHERE sd.id_statut = 'SCAN_TERMINE'
ORDER BY sd.date_ DESC;
```

---

## ⚠️ TROUBLESHOOTING

### Erreur: 404 Not Found
**Cause**: Les URLs ne correspondent pas  
**Solution**: 
- Frontend appelle: `/visa-backoffice/api/demandes/{id}/...` ✅
- Backend écoute sur: `@RequestMapping("/api/demandes")` ✅
- Les URLs doivent correspondre

### Erreur: "Statut 'SCAN_TERMINE' non trouvé"
**Cause**: Le statut n'a pas été inséré en base  
**Solution**: Exécuter le script SQL:
```bash
psql -U postgres -d visa_db -f base/insert_statuts.sql
```

### Erreur: "Aucun fichier n'a été uploadé"
**Cause**: La demande n'a pas de check_piece avec file_name  
**Solution**: 
1. Upload d'abord un fichier
2. Puis verrouiller

### L'état de verrouillage ne persiste pas après rechargement
**Cause**: La détection du verrouillage peut ne pas fonctionner correctement  
**Solution**: 
- Vérifier que `findByDemandeId()` retourne les bons résultats
- Vérifier en base que statut_demande existe

---

## 📊 FICHIERS MODIFIÉS

| Fichier | Statut | Changes |
|---------|--------|---------|
| DemandeSpring3Controller.java | ✅ CRÉÉ | 3 endpoints + 2 méthodes utilitaires |
| demandes.html | ✅ MODIFIÉ | URLs corrigées + gestion erreurs améliorée |
| StatutDemandeRepository.java | ✅ MODIFIÉ | Ajout findByDemandeId() |
| insert_statuts.sql | ✅ CRÉÉ | Script pour insérer statuts |

---

## 🎯 POINTS CLÉS À RETENIR

1. **Upload**: Filtre par type MIME + taille max 10MB
2. **Verrouillage**: Demande un fichier + valide le statut 'SCAN_TERMINE'
3. **Verrouillage**: Empêche la modification (frontend et backend si possible)
4. **Base de données**: Utilise check_piece et statut_demande
5. **URLs**: Doivent inclure `/visa-backoffice/api/demandes/`

---

## ✅ STATUS: PRÊT POUR TEST

- Backend: ✅ Compile
- Frontend: ✅ URLs corrigées
- Base: ⏳ Attente insertion statuts
- Test: ⏳ À faire par l'utilisateur

**Prochaine étape**: Exécuter la procédure de déploiement ci-dessus
