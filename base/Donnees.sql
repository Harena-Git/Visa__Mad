-- =========================================
-- DONNÉES CONSOLIDÉES POUR TOUTES LES TABLES
-- =========================================
-- Ce fichier contient tous les données de test et de configuration
-- =========================================

\c visa_db;

-- =====================================================
-- 1. INSERTION DES TYPES DE VISA
-- =====================================================
INSERT INTO type_visa (id_type_visa, libelle) VALUES
('TV1', 'ETUDIANT'),
('TV2', 'TRAVAILLEUR'),
('TV001', 'Visa Touristique'),
('TV002', 'Visa Affaires'),
('TV003', 'Visa Étudiant'),
('TV004', 'Visa Travail'),
('TV005', 'Visa Familial')
ON CONFLICT (id_type_visa) DO NOTHING;

-- =====================================================
-- 2. INSERTION DES SITUATIONS FAMILIALES
-- =====================================================
INSERT INTO situation_famille (id_situation_famille, libelle) VALUES
('SF1', 'CELIBATAIRE'),
('SF2', 'MARIE'),
('SF001', 'Célibataire'),
('SF002', 'Marié'),
('SF003', 'Divorcé'),
('SF004', 'Veuf(ve)'),
('SF005', 'Concubinage')
ON CONFLICT (id_situation_famille) DO NOTHING;

-- =====================================================
-- 3. INSERTION DES CATÉGORIES DE DEMANDE
-- =====================================================
INSERT INTO categorie_demande (id_categorie, libelle) VALUES
('CD1', 'NOUVEAU'),
('CD2', 'DUPLICATA'),
('CD3', 'TRANSFERT'),
('CAT001', 'Première demande'),
('CAT002', 'Renouvellement'),
('CAT003', 'Transformation'),
('CAT004', 'Duplicata'),
('CAT005', 'Transfert'),
('CAT006', 'Nouveau Titre (Injection)')
ON CONFLICT (id_categorie) DO NOTHING;

-- =====================================================
-- 4. INSERTION DES NATIONALITÉS
-- =====================================================
INSERT INTO nationalite (id_nationalite, libelle) VALUES
('NAT1', 'MALGACHE'),
('NAT2', 'FRANCAISE'),
('NAT001', 'Malgache'),
('NAT002', 'Français'),
('NAT003', 'Américain'),
('NAT_TEST', 'Test'),
('NAT004', 'Australien'),
('NAT005', 'Allemand')
ON CONFLICT (id_nationalite) DO NOTHING;

-- =====================================================
-- 5. INSERTION DES STATUTS
-- =====================================================
INSERT INTO statut (id_statut, libelle) VALUES
('ST1', 'CREE'),
('ST2', 'SCAN_TERMINE'),
('ST3', 'VISA_APPROUVE'),
('SCAN_TERMINE', 'SCAN TERMINE'),
('STAT_TEST', 'Test'),
('CREATED', 'Créé'),
('SUBMITTED', 'Soumis'),
('APPROVED', 'Approuvé'),
('REJECTED', 'Rejeté'),
('ST001', 'En attente'),
('ST002', 'En cours de traitement'),
('ST003', 'Approuvé'),
('ST004', 'Rejeté'),
('ST010', 'Complété'),
('ST011', 'Visa Approuvé'),
('STAT001', 'CREATED'),
('STAT002', 'SCAN_TERMINE'),
('STAT003', 'VISA_APPROVED')
ON CONFLICT (id_statut) DO NOTHING;

-- =====================================================
-- 6. INSERTION DES CHAMPS REQUIS
-- =====================================================
INSERT INTO champs (id_champs, libelle, est_obligatoire) VALUES
('CH001', 'Nom complet', 1),
('CH002', 'Date de naissance', 1),
('CH003', 'Adresse', 1),
('CH004', 'Téléphone', 1),
('CH005', 'Email', 0)
ON CONFLICT (id_champs) DO NOTHING;

-- =====================================================
-- 7. INSERTION DES PIÈCES REQUISES
-- =====================================================
INSERT INTO piece (id_piece, libelle, est_obligatoire, id_type_visa) VALUES
('P1', 'Passeport', 1, 'TV1'),
('P2', 'Photo', 1, 'TV1'),
('P3', 'Certificat de scolarite', 1, 'TV1'),
('P4', 'Contrat de travail', 1, 'TV2'),
('P5', 'Lettre employeur', 0, 'TV2'),
('PIECE001', 'Passeport', 1, 'TV001'),
('PIECE002', 'Photo d''identité', 1, 'TV001'),
('P001', 'Passeport valide', 1, 'TV001'),
('P002', 'Photo d''identité', 1, 'TV001'),
('P003', 'Justificatif de domicile', 1, 'TV001'),
('P004', 'Lettre d''invitation', 0, 'TV001'),
('P005', 'Contrat de travail', 1, 'TV004'),
('P006', 'Attestation d''inscription', 1, 'TV003'),
('P007', 'Relevés bancaires', 1, 'TV002'),
('P008', 'Certificat de mariage', 1, 'TV005')
ON CONFLICT (id_piece) DO NOTHING;

-- =====================================================
-- 8. INSERTION DES DEMANDEURS
-- =====================================================
INSERT INTO demandeur (id_demandeur, nom, prenom, nom_jeune_fille, dtn, adresse_mada, telephone, email, created_at, updated_at, id_nationalite, id_situation_famille) VALUES
('D1', 'RAKOTO', 'Jean', NULL, '1995-05-10', 'Antananarivo', '0340000000', 'jean@mail.com', CURRENT_DATE, CURRENT_DATE, 'NAT1', 'SF1'),
('D2', 'DUPONT', 'Marie', NULL, '1990-03-15', 'Paris', '0330000000', 'marie@mail.com', CURRENT_DATE, CURRENT_DATE, 'NAT2', 'SF2'),
('DEM001', 'RAKOTO', 'Jean', NULL, '1995-05-15', '123 Rue de l''Ecole, Antananarivo', '261340012345', 'jean.rakoto@email.com', '2026-04-20', '2026-04-20', 'NAT001', 'SF001'),
('DEM002', 'DUPONT', 'Marie', 'MARTIN', '1992-08-22', '456 Rue de la Paix, Antananarivo', '261340054321', 'marie.dupont@email.com', '2026-04-21', '2026-04-21', 'NAT002', 'SF002'),
('DEM003', 'SMITH', 'David', NULL, '1998-03-30', '789 Rue de la Liberté, Antananarivo', '261340098765', 'david.smith@email.com', '2026-04-19', '2026-04-19', 'NAT003', 'SF001'),
('DEM004', 'BERNARD', 'Sophie', 'LEONARD', '1994-12-08', '321 Rue des Fleurs, Antananarivo', '261340055555', 'sophie.bernard@email.com', '2026-04-22', '2026-04-22', 'NAT002', 'SF003'),
('DEM_TEST_001', 'TEST', 'User', NULL, '1990-01-01', '123 Rue Test', '261340000000', 'test@test.com', NOW(), NOW(), 'NAT_TEST', 'SF_TEST'),
('DEM_TEST_002', 'Harena', 'Harena', NULL, '2003-04-13', '123 Rue Andoram', '2034213', 'harena@test.com', NOW(), NOW(), 'NAT_TEST', 'SF_TEST'),
('DEM001_FULL', 'RAKOTO', 'Jean', 'RABE', '1990-05-15', 'Antananarivo, Madagascar', '+261341234567', 'jean.rakoto@email.com', CURRENT_DATE, CURRENT_DATE, 'NAT001', 'SF002'),
('DEM002_FULL', 'DUPONT', 'Marie', NULL, '1985-08-22', 'Paris, France', '+33612345678', 'marie.dupont@email.com', CURRENT_DATE, CURRENT_DATE, 'NAT002', 'SF001'),
('DEM003_FULL', 'SMITH', 'John', NULL, '1992-03-10', 'New York, USA', '+12125551234', 'john.smith@email.com', CURRENT_DATE, CURRENT_DATE, 'NAT004', 'SF002'),
('DEM004_FULL', 'MARTIN', 'Sophie', 'LEBLANC', '1988-12-05', 'Lyon, France', '+33698765432', 'sophie.martin@email.com', CURRENT_DATE, CURRENT_DATE, 'NAT002', 'SF002'),
('DEM005_FULL', 'TAN', 'Wei', NULL, '1995-07-18', 'Shanghai, Chine', '+8613812345678', 'wei.tan@email.com', CURRENT_DATE, CURRENT_DATE, 'NAT005', 'SF001')
ON CONFLICT (id_demandeur) DO NOTHING;

-- =====================================================
-- 9. INSERTION DES PASSEPORTS
-- =====================================================
INSERT INTO passport (id_passport, numero, delivre_le, expire_le, id_demandeur) VALUES
('PASS1', 'P123456', '2020-01-01', '2030-01-01', 'D1'),
('PASS2', 'P654321', '2019-06-01', '2029-06-01', 'D2'),
('PASS001', 'A123456789', '2020-01-15', '2030-01-15', 'DEM001'),
('PASS002', 'B987654321', '2019-06-10', '2029-06-10', 'DEM002'),
('PASS003', 'C555666777', '2021-11-05', '2031-11-05', 'DEM003'),
('PASS004', 'D444888999', '2022-02-20', '2032-02-20', 'DEM004'),
('PASS_TEST_001', 'T123456789', '2020-01-01', '2030-01-01', 'DEM_TEST_001'),
('PASS001_FULL', 'PA123456789', '2020-01-15', '2030-01-15', 'DEM001_FULL'),
('PASS002_FULL', 'FR987654321', '2019-06-20', '2029-06-20', 'DEM002_FULL'),
('PASS003_FULL', 'US456789123', '2021-03-10', '2031-03-10', 'DEM003_FULL'),
('PASS004_FULL', 'FR789123456', '2018-11-30', '2028-11-30', 'DEM004_FULL'),
('PASS005_FULL', 'DE321654987', '2022-09-25', '2032-09-25', 'DEM005_FULL')
ON CONFLICT (id_passport) DO NOTHING;

-- =====================================================
-- 10. INSERTION DES DEMANDES
-- =====================================================
INSERT INTO demande (id_demande, created_at, updated_at, id_demande_1, id_categorie, id_type_visa, id_demandeur) VALUES
('DEM1', CURRENT_DATE, CURRENT_DATE, NULL, 'CD1', 'TV1', 'D1'),
('DEM2', CURRENT_DATE, CURRENT_DATE, NULL, 'CD2', 'TV2', 'D2'),
('DEM_REQ001', '2026-04-20', '2026-04-22', NULL, 'CAT001', 'TV001', 'DEM001'),
('DEM_REQ002', '2026-04-21', '2026-04-21', NULL, 'CAT001', 'TV002', 'DEM002'),
('DEM_REQ003', '2026-04-19', '2026-04-22', NULL, 'CAT001', 'TV001', 'DEM003'),
('DEM_REQ004', '2026-04-22', '2026-04-22', NULL, 'CAT001', 'TV002', 'DEM004'),
('DEM_REQ_TEST_001', NOW(), NOW(), NULL, 'CAT_TEST', 'TV_TEST', 'DEM_TEST_001'),
('DEM001_FULL', CURRENT_DATE, CURRENT_DATE, NULL, 'CAT001', 'TV001', 'DEM001_FULL'),
('DEM002_FULL', CURRENT_DATE, CURRENT_DATE, NULL, 'CAT002', 'TV002', 'DEM002_FULL'),
('DEM003_FULL', CURRENT_DATE, CURRENT_DATE, NULL, 'CAT001', 'TV004', 'DEM003_FULL'),
('DEM004_FULL', CURRENT_DATE, CURRENT_DATE, 'DEM001_FULL', 'CAT003', 'TV003', 'DEM004_FULL'),
('DEM005_FULL', CURRENT_DATE, CURRENT_DATE, NULL, 'CAT001', 'TV002', 'DEM005_FULL')
ON CONFLICT (id_demande) DO NOTHING;

-- =====================================================
-- 11. INSERTION DES VISAS
-- =====================================================
INSERT INTO visa (id_visa, ref_visa, date_debut, date_fin, id_passport, id_demande) VALUES
('V1', 'VISA001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 year', 'PASS1', 'DEM1')
ON CONFLICT (id_visa) DO NOTHING;

-- =====================================================
-- 12. INSERTION DES VISAS TRANSFORMABLES
-- =====================================================
INSERT INTO visa_transformable (id_visa_transformable, ref_visa, date_debut, date_fin, id_passport, id_demandeur) VALUES
('VT1', 'VT001', '2025-01-01', '2026-01-01', 'PASS1', 'D1'),
('VT2', 'VT002', '2025-02-01', '2026-02-01', 'PASS2', 'D2'),
('VT001_FULL', 'VT-MAD-001', '2023-02-01', '2025-02-01', 'PASS001_FULL', 'DEM001_FULL'),
('VT002_FULL', 'VT-FRA-001', '2022-07-01', '2024-07-01', 'PASS002_FULL', 'DEM002_FULL')
ON CONFLICT (id_visa_transformable) DO NOTHING;

-- =====================================================
-- 13. INSERTION DES CARTES DE RÉSIDENCE
-- =====================================================
INSERT INTO carte_residence (id_carte_residence, ref_carte_residence, date_debut, date_fin, id_passport, id_demande) VALUES
('CR1', 'CR001', '2025-01-01', '2026-01-01', 'PASS1', 'DEM1'),
('CR001_FULL', 'CR-MAD-001', '2023-01-01', '2028-01-01', 'PASS001_FULL', 'DEM001_FULL'),
('CR002_FULL', 'CR-FRA-001', '2022-06-01', '2027-06-01', 'PASS002_FULL', 'DEM002_FULL'),
('CR003_FULL', 'CR-USA-001', '2023-03-01', '2028-03-01', 'PASS003_FULL', 'DEM003_FULL')
ON CONFLICT (id_carte_residence) DO NOTHING;

-- =====================================================
-- 14. INSERTION DES STATUTS DE DEMANDE
-- =====================================================
INSERT INTO statut_demande (id_statut_demande, date_, id_statut, id_demande) VALUES
('SD1', CURRENT_DATE, 'ST1', 'DEM1'),
('SD2', CURRENT_DATE, 'ST2', 'DEM1'),
('SD3', CURRENT_DATE, 'ST3', 'DEM2'),
('STAT_DEM001', '2026-04-22', 'STAT002', 'DEM_REQ003')
ON CONFLICT (id_statut_demande) DO NOTHING;

-- =====================================================
-- 15. INSERTION DES VÉRIFICATIONS DE PIÈCES
-- =====================================================
INSERT INTO check_piece (id_demande, id_piece, est_fourni, updated_at, file_name, file_type) VALUES
('DEM1', 'P1', TRUE, CURRENT_DATE, 'passport_jean.pdf', 'application/pdf'),
('DEM1', 'P2', TRUE, CURRENT_DATE, 'photo_jean.jpg', 'image/jpeg'),
('DEM1', 'P3', TRUE, CURRENT_DATE, 'certificat_jean.pdf', 'application/pdf'),
('DEM2', 'P4', TRUE, CURRENT_DATE, 'contrat_marie.pdf', 'application/pdf'),
('DEM2', 'P5', FALSE, CURRENT_DATE, NULL, NULL)
ON CONFLICT (id_demande, id_piece) DO NOTHING;

COMMIT;
