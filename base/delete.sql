BEGIN;

-- Tables les plus dépendantes (enfants)
DELETE FROM check_piece;
DELETE FROM statut_demande;
DELETE FROM visa;
DELETE FROM visa_transformable;
DELETE FROM carte_residence;

-- Dépendantes intermédiaires
DELETE FROM demande;
DELETE FROM passport;
DELETE FROM demandeur;
DELETE FROM piece;

-- Tables de référence (parents)
DELETE FROM categorie_demande;
DELETE FROM type_visa;
DELETE FROM nationalite;
DELETE FROM situation_famille;
DELETE FROM statut;
DELETE FROM champs;

COMMIT;