# Sprint 4 - Consultation des Demandes et Suivi en Temps Réel

## ✅ Implémentation Complétée

Toutes les fonctionnalités du Sprint 4 ont été implémentées avec succès.

---

## 📋 Récapitulatif des Changements

### 1. **Dépendances Maven Ajoutées**
```xml
<!-- Génération de QR Codes -->
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>core</artifactId>
    <version>3.5.2</version>
</dependency>
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>javase</artifactId>
    <version>3.5.2</version>
</dependency>
```

### 2. **Services Créés**

#### **QRCodeService.java**
- Service pour générer les codes QR
- Génère des codes QR à partir d'une URL de suivi
- Supporte la sortie en Base64 pour affichage HTML direct

**Méthodes:**
- `byte[] generateQRCode(String url, int size)` - Génère un QR code en PNG
- `String generateQRCodeBase64(String url, int size)` - Génère un QR code en Base64

### 3. **Modifications d'Entité**

#### **Demande.java**
- Ajout du champ `trackingToken` (VARCHAR 255, UNIQUE)
- Getters et setters correspondants

### 4. **Modifications de Repository**

#### **DemandeRepository.java**
- Ajout de `Optional<Demande> findByTrackingToken(String trackingToken)`

### 5. **Modifications de Service**

#### **DemandeService.java**
Nouvelles méthodes:
```java
// Génère un token unique et l'assigne à une demande
String generateTrackingToken(String demandeId)

// Récupère une demande par son token de suivi
Optional<Demande> findByTrackingToken(String trackingToken)

// Construit l'URL de suivi complète
String buildTrackingUrl(String trackingToken, String baseUrl)
```

### 6. **Nouveaux Contrôleurs**

#### **DemandeController.java**
Nouveaux endpoints:
- **POST** `/api/demandes/{demandeId}/generate-qr?baseUrl=...`
  - Génère un QR code et un token de suivi pour une demande
  - Retourne le token, l'URL de suivi et le QR code en Base64

- **GET** `/api/demandes/tracking/{trackingToken}`
  - Récupère les informations de suivi d'une demande via son token

#### **TrackingController.java** (NOUVEAU)
Contrôleur public accessible sans authentification:

- **GET** `/api/public/tracking/passport/{numero}`
  - Récupère toutes les demandes associées à un numéro de passeport
  - Retourne la liste enrichie avec statuts et dates

- **GET** `/api/public/tracking/token/{trackingToken}`
  - Récupère les détails complets d'une demande via ses token de suivi
  - Affiche l'historique des statuts

- **GET** `/api/public/tracking/search?passport=...&statut=...`
  - Recherche avancée par passeport et optionnellement par statut

- **GET** `/api/public/tracking/health`
  - Health check du service de suivi

### 7. **Interface Utilisateur**

#### **tracking.html** (NOUVEAU)
Page publique à deux onglets:

**Onglet 1: Recherche par Numéro de Passeport**
- L'utilisateur entre son numéro de passeport
- Affiche toutes ses demandes avec statuts
- Timeline complète de l'évolution de chaque demande

**Onglet 2: Suivi par Lien/Token**
- L'utilisateur peut scanner un QR code ou entrer un code de suivi
- Affichage détaillé de la demande correspondante
- Timeline historique des statuts

**Fonctionnalités:**
- Interface responsive (mobile-friendly)
- Timeline visuelle des statuts
- Codes couleur pour les différents états
- Messages d'erreur et de succès clairs
- Chargement asynchrone avec spinner

### 8. **Migration Base de Données**

Fichier: `migration_sprint4_tracking.sql`
```sql
ALTER TABLE demande ADD COLUMN tracking_token VARCHAR(255) UNIQUE;
CREATE INDEX idx_tracking_token ON demande(tracking_token);
```

---

## 🚀 Guide d'Utilisation

### À titre d'administrateur (Backoffice)

#### 1. **Générer un QR Code pour une Demande**

```bash
POST /api/demandes/REQ-XXXXX/generate-qr?baseUrl=https://example.com

Response:
{
  "success": true,
  "trackingToken": "uuid-string-here",
  "trackingUrl": "https://example.com/tracking/uuid-string-here",
  "qrCode": "data:image/png;base64,iVBORw0KGgoAAAANS...",
  "demandeId": "REQ-XXXXX"
}
```

#### 2. **Utilisée le QR Code dans le Backoffice**
- L'endpoint retourne directement le QR code en Base64
- Peut être affiché dans un attribut `src` d'une image HTML
- Peut être imprimé avec la lettre de demande

### À titre d'utilisateur final (Public)

#### 1. **Suivi par Numéro de Passeport**

```bash
GET /api/public/tracking/passport/PAD123456789

Response:
{
  "success": true,
  "demandes": [
    {
      "demandeId": "REQ-XXXXX",
      "demandeur": {
        "nom": "Dupont",
        "prenom": "Jean"
      },
      "typeVisa": "Visa d'Affaires",
      "categorie": "Court Terme",
      "dateCreation": "2026-05-01",
      "dateMiseAJour": "2026-05-03",
      "statutActuel": "SCAN TERMINÉ",
      "historique": [
        {
          "statut": "Créée",
          "date": "2026-05-01"
        },
        {
          "statut": "SCAN TERMINÉ",
          "date": "2026-05-03"
        }
      ]
    }
  ],
  "count": 1
}
```

#### 2. **Suivi par Token**

```bash
GET /api/public/tracking/token/uuid-string-here

Response: (même structure que ci-dessus)
```

---

## 🔄 Flux Métier Complet

### Création et Suivi d'une Demande

``` mermaid
graph TD
    A["Demande Créée<br/>REQ-XXXXX"] 
    --> B["Admin génère QR Code<br/>POST /api/demandes/.../generate-qr"]
    --> C["QR Code généré<br/>+ tracking_token assigné"]
    --> D["QR Code fourni au demandeur<br/>Via mail ou impression"]
    --> E["Utilisateur scanne QR<br/>ou accède au lien"]
    --> F["Redirection tracking.html"]
    --> G["Affichage statut demande<br/>+ historique"]
    --> H["Timeline mise à jour<br/>en temps réel"]
```

### Statuts Possibles et Flow

```
CRÉÉE 
  ↓
[SCAN TERMINÉ] (verrouille la demande)
  ↓
[APPROUVÉE / REJETÉE / EN COURS]
```

---

## 💾 Migration Base de Données

Avant de déployer, exécuter la migration:

```sql
\c visa_db;
\i migration_sprint4_tracking.sql;

-- Vérifier l'ajout
\d demande;
```

---

## 🧪 Exemples de Test

### Créer une demande et générer son QR Code

```bash
# 1. Créer une demande
curl -X POST http://localhost:8080/visa-backoffice/api/demandes \
  -H "Content-Type: application/json" \
  -d '{
    "categorie": {"id": "CAT1"},
    "typeVisa": {"id": "AFFAIRES"},
    "demandeur": {"id": "DEM001"}
  }'

# Résultat: demande créée avec ID "REQ-XXXXX"

# 2. Générer QR Code
curl -X POST http://localhost:8080/visa-backoffice/api/demandes/REQ-XXXXX/generate-qr

# Résultat: trackingToken et QR code en Base64
```

### Consulter le suivi en tant qu'utilisateur

```bash
# Option 1: Par numéro de passeport
curl http://localhost:8080/visa-backoffice/api/public/tracking/passport/PAD123456789

# Option 2: Par token
curl http://localhost:8080/visa-backoffice/api/public/tracking/token/uuid-here
```

---

## 📱 Interface Utilisateur

La page [tracking.html](tracking.html) offre une expérience utilisateur:
- **Responsive** - Fonctionne sur mobile, tablette, desktop
- **Deux modes de recherche**:
  1. Par numéro de passeport
  2. Par lien de suivi/token
- **Timeline visuelle** de l'évolution du dossier
- **Codes couleur** pour rapidement identifier le statut
- **Recherche avancée** avec filtres optionnels

---

## 🔐 Sécurité

### Access Control
- `/api/demandes/*/generate-qr` - Authentification requise (backoffice)
- `/api/demandes/tracking/*` - Authentification requise (backoffice)
- `/api/public/tracking/*` - **AUCUNE authentification** (public)

### Token de Suivi
- Chaque token est un UUID unique
- Stocké dans la base de données
- Créé à la demande (pas de génération batch)

---

## 📊 Implémentation Complète

| Fonctionnalité | Statut | Détails |
|---|---|---|
| ✅ Interface de consultation | Complétée | tracking.html |
| ✅ Recherche par passeport | Complétée | /api/public/tracking/passport/* |
| ✅ API de consultation | Complétée | TrackingController |
| ✅ Génération QR Code | Complétée | QRCodeService + endpoint |
| ✅ Suivi via QR | Complétée | Token + tracking.html |
| ✅ Statuts en temps réel | Complétée | Timeline avec historique |
| ✅ Sécurisation des liens | Complétée | UUID unique par demande |
| ✅ Migration DB | Complétée | migration_sprint4_tracking.sql |

---

## 📝 Informations Complémentaires

### Champs de Réponse Complète

```json
{
  "demandeId": "REQ-ABC123DE",
  "trackingToken": "550e8400-e29b-41d4-a716-446655440000",
  "demandeur": {
    "nom": "Dupont",
    "prenom": "Jean"
  },
  "typeVisa": "Visa d'Affaires",
  "categorie": "Court Terme",
  "dateCreation": "2026-05-01",
  "dateMiseAJour": "2026-05-05",
  "statutActuel": "SCAN TERMINÉ",
  "historique": [
    {
      "statut": "Créée",
      "date": "2026-05-01"
    },
    {
      "statut": "SCAN TERMINÉ",
      "date": "2026-05-03"
    }
  ]
}
```

---

**Sprint 4 - Validé et Déployable ✅**
