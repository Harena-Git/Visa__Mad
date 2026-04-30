-- ============================================================================
-- SPRINT 3: DONNÉES DE TEST MINIMALES - POUR DÉMARRAGE RAPIDE
-- ============================================================================
-- Ce fichier crée une seule demande pour tester rapidement le Sprint 3
-- Utiliser celui-ci si vous voulez un test simple et rapide
-- ============================================================================

-- 1. Vérifier que les références existent (sinon les créer)
INSERT INTO nationalite (id_nationalite, libelle) VALUES ('NAT_TEST', 'Test') ON CONFLICT DO NOTHING;
INSERT INTO situation_famille (id_situation_famille, libelle) VALUES ('SF_TEST', 'Test') ON CONFLICT DO NOTHING;
INSERT INTO type_visa (id_type_visa, libelle) VALUES ('TV_TEST', 'Test') ON CONFLICT DO NOTHING;
INSERT INTO categorie_demande (id_categorie, libelle) VALUES ('CAT_TEST', 'Test') ON CONFLICT DO NOTHING;
INSERT INTO statut (id_statut, libelle) VALUES ('STAT_TEST', 'Test') ON CONFLICT DO NOTHING;

-- 2. Créer un demandeur simple
INSERT INTO demandeur (
    id_demandeur, nom, prenom, nom_jeune_fille, dtn, 
    adresse_mada, telephone, email, 
    created_at, updated_at, 
    id_nationalite, id_situation_famille
) VALUES (
    'DEM_TEST_001', 'TEST', 'User', NULL, '1990-01-01',
    '123 Rue Test', '261340000000', 'test@test.com',
    NOW(), NOW(),
    'NAT_TEST', 'SF_TEST'
) ON CONFLICT DO NOTHING;

-- 3. Créer un passeport pour le demandeur
INSERT INTO passport (id_passport, numero, delivre_le, expire_le, id_demandeur) VALUES 
    ('PASS_TEST_001', 'T123456789', '2020-01-01', '2030-01-01', 'DEM_TEST_001')
ON CONFLICT DO NOTHING;

-- 4. Créer une demande
INSERT INTO demande (
    id_demande, created_at, updated_at, 
    id_demande_1, id_categorie, id_type_visa, id_demandeur
) VALUES (
    'DEM_REQ_TEST_001', NOW(), NOW(),
    NULL, 'CAT_TEST', 'TV_TEST', 'DEM_TEST_001'
) ON CONFLICT DO NOTHING;

-- 5. Vérification
SELECT 
    d.id_demande as "ID Demande",
    dem.nom || ' ' || dem.prenom as "Demandeur",
    d.created_at as "Date Création",
    d.updated_at as "Date MAJ"
FROM demande d
JOIN demandeur dem ON d.id_demandeur = dem.id_demandeur
WHERE d.id_demande = 'DEM_REQ_TEST_001';

-- Résultat attendu: 1 ligne avec DEM_REQ_TEST_001

INSERT INTO demandeur (
    id_demandeur, nom, prenom, nom_jeune_fille, dtn, 
    adresse_mada, telephone, email, 
    created_at, updated_at, 
    id_nationalite, id_situation_famille
) VALUES (
    'DEM_TEST_002', 'Harena', 'Harena', NULL, '2003-04-13',
    '123 Rue Andoram', '2034213', 'harena@test.com',
    NOW(), NOW(),
    'NAT_TEST', 'SF_TEST'
) ON CONFLICT DO NOTHING;

COMMIT;
