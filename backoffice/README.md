# Visa Management Backoffice System

Projet Spring Boot pour la gestion des visas avec base de données PostgreSQL.

## Prérequis

- Java 17 ou supérieur
- Maven 3.6 ou supérieur
- PostgreSQL 12 ou supérieur

## Configuration de la base de données

1. Créer la base de données `visa_db` dans PostgreSQL
2. Exécuter le script SQL `base/create.sql` pour créer les tables
3. Mettre à jour les informations de connexion dans `src/main/resources/application.properties`

## Lancer l'application

### Via Maven
```bash
mvn spring-boot:run
```

### Via Java
```bash
mvn clean package
java -jar target/backoffice-0.0.1-SNAPSHOT.jar
```

## CRUD Complet - Endpoints API

### Types de Visa (`/api/types-visa`)
- `GET /api/types-visa` - Lister tous les types de visa
- `GET /api/types-visa/{id}` - Obtenir un type de visa par ID
- `POST /api/types-visa` - Créer un type de visa
- `PUT /api/types-visa/{id}` - Mettre à jour un type de visa
- `DELETE /api/types-visa/{id}` - Supprimer un type de visa
- `GET /api/types-visa/search?libelle={libelle}` - Rechercher par libellé
- `GET /api/types-visa/count` - Obtenir le nombre total
- `GET /api/types-visa/exists/{id}` - Vérifier l'existence

### Demandeurs (`/api/demandeurs`) - CRUD Complet
- `GET /api/demandeurs` - Lister tous les demandeurs
- `GET /api/demandeurs/{id}` - Obtenir un demandeur par ID
- `POST /api/demandeurs` - Créer un demandeur
- `PUT /api/demandeurs/{id}` - Mettre à jour un demandeur
- `DELETE /api/demandeurs/{id}` - Supprimer un demandeur
- `GET /api/demandeurs/search/nom?nom={nom}` - Rechercher par nom
- `GET /api/demandeurs/search/prenom?prenom={prenom}` - Rechercher par prénom
- `GET /api/demandeurs/search/nationalite/{idNationalite}` - Rechercher par nationalité
- `GET /api/demandeurs/search/situation-famille/{idSituationFamille}` - Rechercher par situation familiale
- `GET /api/demandeurs/search/email?email={email}` - Rechercher par email
- `GET /api/demandeurs/search/telephone?telephone={telephone}` - Rechercher par téléphone
- `GET /api/demandeurs/search/adresse?adresse={adresse}` - Rechercher par adresse
- `GET /api/demandeurs/search/naissance-range?startDate={startDate}&endDate={endDate}` - Rechercher par plage de dates de naissance
- `GET /api/demandeurs/search/creation-range?startDate={startDate}&endDate={endDate}` - Rechercher par plage de dates de création
- `GET /api/demandeurs/search/global?term={term}` - Recherche globale
- `GET /api/demandeurs/recent/{days}` - Obtenir les demandeurs récents
- `GET /api/demandeurs/count` - Obtenir le nombre total
- `GET /api/demandeurs/exists/{id}` - Vérifier l'existence
- `GET /api/demandeurs/exists/email?email={email}` - Vérifier l'existence par email

### Demandes (`/api/demandes`) - CRUD Complet
- `GET /api/demandes` - Lister toutes les demandes
- `GET /api/demandes/{id}` - Obtenir une demande par ID
- `POST /api/demandes` - Créer une demande
- `PUT /api/demandes/{id}` - Mettre à jour une demande
- `DELETE /api/demandes/{id}` - Supprimer une demande
- `GET /api/demandes/categorie/{idCategorie}` - Rechercher par catégorie
- `GET /api/demandes/type-visa/{idTypeVisa}` - Rechercher par type de visa
- `GET /api/demandes/demandeur/{idDemandeur}` - Rechercher par demandeur
- `GET /api/demandes/date-range?startDate={startDate}&endDate={endDate}` - Rechercher par plage de dates de création
- `GET /api/demandes/updated-range?startDate={startDate}&endDate={endDate}` - Rechercher par plage de dates de mise à jour
- `GET /api/demandes/linked/{idDemande1}` - Rechercher les demandes liées
- `GET /api/demandes/recent/{days}` - Obtenir les demandes récentes
- `GET /api/demandes/count` - Obtenir le nombre total
- `GET /api/demandes/exists/{id}` - Vérifier l'existence

### Nationalités (`/api/nationalites`) - CRUD Complet
- `GET /api/nationalites` - Lister toutes les nationalités
- `GET /api/nationalites/{id}` - Obtenir une nationalité par ID
- `POST /api/nationalites` - Créer une nationalité
- `PUT /api/nationalites/{id}` - Mettre à jour une nationalité
- `DELETE /api/nationalites/{id}` - Supprimer une nationalité
- `GET /api/nationalites/search?libelle={libelle}` - Rechercher par libellé
- `GET /api/nationalites/count` - Obtenir le nombre total
- `GET /api/nationalites/exists/{id}` - Vérifier l'existence

### Situations Familiales (`/api/situations-famille`) - CRUD Complet
- `GET /api/situations-famille` - Lister toutes les situations familiales
- `GET /api/situations-famille/{id}` - Obtenir une situation familiale par ID
- `POST /api/situations-famille` - Créer une situation familiale
- `PUT /api/situations-famille/{id}` - Mettre à jour une situation familiale
- `DELETE /api/situations-famille/{id}` - Supprimer une situation familiale
- `GET /api/situations-famille/search?libelle={libelle}` - Rechercher par libellé
- `GET /api/situations-famille/count` - Obtenir le nombre total
- `GET /api/situations-famille/exists/{id}` - Vérifier l'existence

### Catégories de Demande (`/api/categories-demande`) - CRUD Complet
- `GET /api/categories-demande` - Lister toutes les catégories de demande
- `GET /api/categories-demande/{id}` - Obtenir une catégorie par ID
- `POST /api/categories-demande` - Créer une catégorie de demande
- `PUT /api/categories-demande/{id}` - Mettre à jour une catégorie de demande
- `DELETE /api/categories-demande/{id}` - Supprimer une catégorie de demande
- `GET /api/categories-demande/search?libelle={libelle}` - Rechercher par libellé
- `GET /api/categories-demande/count` - Obtenir le nombre total
- `GET /api/categories-demande/exists/{id}` - Vérifier l'existence

### Statuts (`/api/statuts`) - CRUD Complet
- `GET /api/statuts` - Lister tous les statuts
- `GET /api/statuts/{id}` - Obtenir un statut par ID
- `POST /api/statuts` - Créer un statut
- `PUT /api/statuts/{id}` - Mettre à jour un statut
- `DELETE /api/statuts/{id}` - Supprimer un statut
- `GET /api/statuts/search?libelle={libelle}` - Rechercher par libellé
- `GET /api/statuts/count` - Obtenir le nombre total
- `GET /api/statuts/exists/{id}` - Vérifier l'existence

### Passports (`/api/passports`) - CRUD Complet
- `GET /api/passports` - Lister tous les passports
- `GET /api/passports/{id}` - Obtenir un passport par ID
- `POST /api/passports` - Créer un passport (vérifie l'unicité du numéro)
- `PUT /api/passports/{id}` - Mettre à jour un passport (vérifie l'unicité du numéro)
- `DELETE /api/passports/{id}` - Supprimer un passport
- `GET /api/passports/numero/{numero}` - Obtenir un passport par numéro
- `GET /api/passports/demandeur/{idDemandeur}` - Rechercher par demandeur
- `GET /api/passports/search?numero={numero}` - Rechercher par numéro
- `GET /api/passports/count` - Obtenir le nombre total
- `GET /api/passports/exists/{id}` - Vérifier l'existence
- `GET /api/passports/exists/numero?numero={numero}` - Vérifier l'existence par numéro

## Fonctionnalités Avancées

### Gestion des Erreurs
- Gestion globale des exceptions avec `GlobalExceptionHandler`
- Messages d'erreur structurés avec timestamp et détails
- Validation des contraintes (numéro de passport unique, etc.)

### Recherche et Filtrage
- Recherche par texte partiel (insensible à la casse)
- Recherche par plages de dates
- Recherche globale combinée
- Filtres par relations (nationalité, catégorie, etc.)

### Transactions
- Gestion automatique des dates de création/mise à jour
- Support des transactions avec `@Transactional`

## Structure du projet

```
src/main/java/com/visa/backoffice/
  entity/          # 13 Entités JPA (TypeVisa, Demandeur, Demande, etc.)
  repository/      # 13 Repositories Spring Data JPA
  service/         # Services métier avec logique CRUD avancée
  controller/      # 8 Contrôleurs REST avec endpoints complets
  exception/       # Gestion globale des exceptions
  VisaBackofficeApplication.java  # Classe principale Spring Boot
```

## Configuration

L'application écoute sur le port 8080 par défaut. Configuration CORS activée pour tous les origines.

## Exemples d'utilisation

### Créer un demandeur
```bash
curl -X POST http://localhost:8080/api/demandeurs \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean.dupont@email.com",
    "nationalite": {"id": "FR"},
    "situationFamille": {"id": "CELIBATAIRE"}
  }'
```

### Rechercher des demandeurs par nom
```bash
curl "http://localhost:8080/api/demandeurs/search/nom?nom=dupont"
```

### Créer une demande
```bash
curl -X POST http://localhost:8080/api/demandes \
  -H "Content-Type: application/json" \
  -d '{
    "categorie": {"id": "TOURISTE"},
    "typeVisa": {"id": "SHORT_STAY"},
    "demandeur": {"id": "DEMANDEUR_001"}
  }'
```

## Tests

L'application inclut des endpoints de vérification d'existence et de comptage pour faciliter les tests et la validation.
