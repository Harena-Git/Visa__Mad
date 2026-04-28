-- Données de test pour la base de données visa_db
-- Utiliser après avoir exécuté create.sql

\c visa_db;

-- Insertion des types de visa
INSERT INTO type_visa (id_type_visa, libelle) VALUES
('TV001', 'Visa Touristique'),
('TV002', 'Visa Affaires'),
('TV003', 'Visa Étudiant'),
('TV004', 'Visa Travail'),
('TV005', 'Visa Familial');

-- Insertion des situations familiales
INSERT INTO situation_famille (id_situation_famille, libelle) VALUES
('SF001', 'Célibataire'),
('SF002', 'Marié(e)'),
('SF003', 'Divorcé(e)'),
('SF004', 'Veuf(ve)'),
('SF005', 'Concubinage');

-- Insertion des catégories de demande
INSERT INTO categorie_demande (id_categorie, libelle) VALUES
('CAT001', 'Première demande'),
('CAT002', 'Renouvellement'),
('CAT003', 'Transformation'),
('CAT004', 'Duplicata'),
('CAT005', 'Transfert'),
('CAT006', 'Nouveau Titre (Injection)');

-- Insertion des nationalités
INSERT INTO nationalite (id_nationalite, libelle) VALUES
('NAT001', 'Française'),
('NAT002', 'Malagasy'),
('NAT003', 'Canadienne'),
('NAT004', 'Américaine'),
('NAT005', 'Allemande');

-- Insertion des statuts
INSERT INTO statut (id_statut, libelle) VALUES
('ST001', 'En attente'),
('ST002', 'En cours de traitement'),
('ST003', 'Approuvé'),
('ST004', 'Rejeté'),
('ST010', 'Complété'),
('ST011', 'Visa Approuvé');

-- Insertion des champs requis
INSERT INTO champs (id_champs, libelle, est_obligatoire) VALUES
('CH001', 'Nom complet', 1),
('CH002', 'Date de naissance', 1),
('CH003', 'Adresse', 1),
('CH004', 'Téléphone', 1),
('CH005', 'Email', 0);

-- Insertion des pièces requises par type de visa
INSERT INTO piece (id_piece, libelle, est_obligatoire, id_type_visa) VALUES
('P001', 'Passeport valide', 1, 'TV001'),
('P002', 'Photo d''identité', 1, 'TV001'),
('P003', 'Justificatif de domicile', 1, 'TV001'),
('P004', 'Lettre d''invitation', 0, 'TV001'),
('P005', 'Contrat de travail', 1, 'TV004'),
('P006', 'Attestation d''inscription', 1, 'TV003'),
('P007', 'Relevés bancaires', 1, 'TV002'),
('P008', 'Certificat de mariage', 1, 'TV005');

-- Insertion des demandeurs
INSERT INTO demandeur (id_demandeur, nom, prenom, nom_jeune_fille, dtn, adresse_mada, telephone, email, created_at, updated_at, id_nationalite, id_situation_famille) VALUES
('DEM001', 'RAKOTO', 'Jean', 'RABE', '1990-05-15', 'Antananarivo, Madagascar', '+261341234567', 'jean.rakoto@email.com', CURRENT_DATE, CURRENT_DATE, 'NAT002', 'SF002'),
('DEM002', 'DUPONT', 'Marie', NULL, '1985-08-22', 'Paris, France', '+33612345678', 'marie.dupont@email.com', CURRENT_DATE, CURRENT_DATE, 'NAT001', 'SF001'),
('DEM003', 'SMITH', 'John', NULL, '1992-03-10', 'New York, USA', '+12125551234', 'john.smith@email.com', CURRENT_DATE, CURRENT_DATE, 'NAT004', 'SF002'),
('DEM004', 'MARTIN', 'Sophie', 'LEBLANC', '1988-12-05', 'Lyon, France', '+33698765432', 'sophie.martin@email.com', CURRENT_DATE, CURRENT_DATE, 'NAT001', 'SF002'),
('DEM005', 'TAN', 'Wei', NULL, '1995-07-18', 'Shanghai, Chine', '+8613812345678', 'wei.tan@email.com', CURRENT_DATE, CURRENT_DATE, 'NAT005', 'SF001');

-- Insertion des passeports
INSERT INTO passport (id_passport, numero, delivre_le, expire_le, id_demandeur) VALUES
('PASS001', 'PA123456789', '2020-01-15', '2030-01-15', 'DEM001'),
('PASS002', 'FR987654321', '2019-06-20', '2029-06-20', 'DEM002'),
('PASS003', 'US456789123', '2021-03-10', '2031-03-10', 'DEM003'),
('PASS004', 'FR789123456', '2018-11-30', '2028-11-30', 'DEM004'),
('PASS005', 'DE321654987', '2022-09-25', '2032-09-25', 'DEM005');

-- Insertion des demandes
INSERT INTO demande (id_demande, created_at, updated_at, id_demande_1, id_categorie, id_type_visa, id_demandeur) VALUES
('DEM001', CURRENT_DATE, CURRENT_DATE, NULL, 'CAT001', 'TV001', 'DEM001'),
('DEM002', CURRENT_DATE, CURRENT_DATE, NULL, 'CAT002', 'TV002', 'DEM002'),
('DEM003', CURRENT_DATE, CURRENT_DATE, NULL, 'CAT001', 'TV004', 'DEM003'),
('DEM004', CURRENT_DATE, CURRENT_DATE, 'DEM001', 'CAT003', 'TV003', 'DEM004'),
('DEM005', CURRENT_DATE, CURRENT_DATE, NULL, 'CAT001', 'TV002', 'DEM005');

-- Insertion des cartes de résidence
INSERT INTO carte_residence (id_carte_residence, ref_carte_residence, date_debut, date_fin, id_passport, id_demande) VALUES
('CR001', 'CR-MAD-001', '2023-01-01', '2028-01-01', 'PASS001', 'DEM001'),
('CR002', 'CR-FRA-001', '2022-06-01', '2027-06-01', 'PASS002', 'DEM002'),
('CR003', 'CR-USA-001', '2023-03-01', '2028-03-01', 'PASS003', 'DEM003');

-- Insertion des visas transformables
INSERT INTO visa_transformable (id_visa_transformable, ref_visa, date_debut, date_fin, id_passport, id_demandeur) VALUES
('VT001', 'VT-MAD-001', '2023-02-01', '2025-02-01', 'PASS001', 'DEM001'),
('VT002', 'VT-FRA-001', '2022-07-01', '2024-07-01', 'PASS002', 'DEM002');

-- Insertion des statuts de demande
INSERT INTO statut_demande (id_statut_demande, date_, id_statut, id_demande) VALUES
('SD001', CURRENT_DATE, 'ST002', 'DEM001'),
('SD002', CURRENT_DATE, 'ST003', 'DEM002'),
('SD003', CURRENT_DATE, 'ST002', 'DEM003'),
('SD004', CURRENT_DATE, 'ST001', 'DEM004'),
('SD005', CURRENT_DATE, 'ST002', 'DEM005');

-- Insertion des visas
INSERT INTO visa (id_visa, ref_visa, date_debut, date_fin, id_passport, id_demande) VALUES
('V001', 'VISA-TOUR-001', '2023-06-01 10:00:00', '2023-12-01 23:59:59', 'PASS001', 'DEM001'),
('V002', 'VISA-AFF-001', '2022-08-01 09:00:00', '2023-08-01 23:59:59', 'PASS002', 'DEM002'),
('V003', 'VISA-TRAV-001', '2023-04-01 11:00:00', '2024-04-01 23:59:59', 'PASS003', 'DEM003');

-- Insertion des vérifications de pièces
INSERT INTO check_piece (id_demande, id_piece, est_fourni, updated_at) VALUES
('DEM001', 'P001', true, CURRENT_DATE),
('DEM001', 'P002', true, CURRENT_DATE),
('DEM001', 'P003', true, CURRENT_DATE),
('DEM001', 'P004', false, CURRENT_DATE),
('DEM002', 'P001', true, CURRENT_DATE),
('DEM002', 'P002', true, CURRENT_DATE),
('DEM002', 'P007', true, CURRENT_DATE),
('DEM003', 'P001', true, CURRENT_DATE),
('DEM003', 'P002', true, CURRENT_DATE),
('DEM003', 'P005', true, CURRENT_DATE),
('DEM004', 'P001', true, CURRENT_DATE),
('DEM004', 'P002', true, CURRENT_DATE),
('DEM004', 'P006', false, CURRENT_DATE),
('DEM005', 'P001', true, CURRENT_DATE),
('DEM005', 'P002', false, CURRENT_DATE),
('DEM005', 'P007', true, CURRENT_DATE);

-- Affichage des données insérées pour vérification
SELECT 'Types de visa:' as table_name;
SELECT * FROM type_visa;

SELECT 'Situations familiales:' as table_name;
SELECT * FROM situation_famille;

SELECT 'Catégories de demande:' as table_name;
SELECT * FROM categorie_demande;

SELECT 'Nationalités:' as table_name;
SELECT * FROM nationalite;

SELECT 'Statuts:' as table_name;
SELECT * FROM statut;

SELECT 'Demandeurs:' as table_name;
SELECT * FROM demandeur;

SELECT 'Passeports:' as table_name;
SELECT * FROM passport;

SELECT 'Demandes:' as table_name;
SELECT * FROM demande;

SELECT 'Cartes de résidence:' as table_name;
SELECT * FROM carte_residence;

SELECT 'Visas transformables:' as table_name;
SELECT * FROM visa_transformable;

SELECT 'Statuts de demande:' as table_name;
SELECT * FROM statut_demande;

SELECT 'Visas:' as table_name;
SELECT * FROM visa;

SELECT 'Vérifications de pièces:' as table_name;
SELECT * FROM check_piece;
