-- =========================================
-- AJOUT DES COLONNES (SPRINT 3)
-- =========================================
ALTER TABLE check_piece
ADD COLUMN file_name VARCHAR(255);

ALTER TABLE check_piece
ADD COLUMN file_type VARCHAR(50);

-- =========================================
-- TABLE : type_visa
-- =========================================
INSERT INTO type_visa VALUES
('TV1', 'ETUDIANT'),
('TV2', 'TRAVAILLEUR');

-- =========================================
-- TABLE : situation_famille
-- =========================================
INSERT INTO situation_famille VALUES
('SF1', 'CELIBATAIRE'),
('SF2', 'MARIE');

-- =========================================
-- TABLE : categorie_demande
-- =========================================
INSERT INTO categorie_demande VALUES
('CD1', 'NOUVEAU'),
('CD2', 'DUPLICATA'),
('CD3', 'TRANSFERT');

-- =========================================
-- TABLE : nationalite
-- =========================================
INSERT INTO nationalite VALUES
('NAT1', 'MALGACHE'),
('NAT2', 'FRANCAISE');

-- =========================================
-- TABLE : statut
-- =========================================
INSERT INTO statut VALUES
('ST1', 'CREE'),
('ST2', 'SCAN_TERMINE'),
('ST3', 'VISA_APPROUVE');

-- =========================================
-- TABLE : piece
-- =========================================
INSERT INTO piece VALUES
('P1', 'Passeport', 1, 'TV1'),
('P2', 'Photo', 1, 'TV1'),
('P3', 'Certificat de scolarite', 1, 'TV1'),
('P4', 'Contrat de travail', 1, 'TV2'),
('P5', 'Lettre employeur', 0, 'TV2');

-- =========================================
-- TABLE : demandeur
-- =========================================
INSERT INTO demandeur VALUES
('D1', 'RAKOTO', 'Jean', NULL, '1995-05-10', 'Antananarivo', '0340000000', '[jean@mail.com](mailto:jean@mail.com)', CURRENT_DATE, CURRENT_DATE, 'NAT1', 'SF1'),
('D2', 'DUPONT', 'Marie', NULL, '1990-03-15', 'Paris', '0330000000', '[marie@mail.com](mailto:marie@mail.com)', CURRENT_DATE, CURRENT_DATE, 'NAT2', 'SF2');

-- =========================================
-- TABLE : passport
-- =========================================
INSERT INTO passport VALUES
('PASS1', 'P123456', '2020-01-01', '2030-01-01', 'D1'),
('PASS2', 'P654321', '2019-06-01', '2029-06-01', 'D2');

-- =========================================
-- TABLE : demande
-- =========================================
INSERT INTO demande VALUES
('DEM1', CURRENT_DATE, CURRENT_DATE, NULL, 'CD1', 'TV1', 'D1'),
('DEM2', CURRENT_DATE, CURRENT_DATE, NULL, 'CD2', 'TV2', 'D2');

-- =========================================
-- TABLE : visa
-- =========================================
INSERT INTO visa VALUES
('V1', 'VISA001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 year', 'PASS1', 'DEM1');

-- =========================================
-- TABLE : visa_transformable
-- =========================================
INSERT INTO visa_transformable VALUES
('VT1', 'VT001', '2025-01-01', '2026-01-01', 'PASS1', 'D1'),
('VT2', 'VT002', '2025-02-01', '2026-02-01', 'PASS2', 'D2');

-- =========================================
-- TABLE : carte_residence
-- =========================================
INSERT INTO carte_residence VALUES
('CR1', 'CR001', '2025-01-01', '2026-01-01', 'PASS1', 'DEM1');

-- =========================================
-- TABLE : statut_demande
-- =========================================
INSERT INTO statut_demande VALUES
('SD1', CURRENT_DATE, 'ST1', 'DEM1'),
('SD2', CURRENT_DATE, 'ST2', 'DEM1'),
('SD3', CURRENT_DATE, 'ST3', 'DEM2');

-- =========================================
-- TABLE : check_piece (AVEC FICHIERS SCANNÉS)
-- =========================================
INSERT INTO check_piece VALUES
('DEM1', 'P1', TRUE, CURRENT_DATE, 'passport_jean.pdf', 'application/pdf'),
('DEM1', 'P2', TRUE, CURRENT_DATE, 'photo_jean.jpg', 'image/jpeg'),
('DEM1', 'P3', TRUE, CURRENT_DATE, 'certificat_jean.pdf', 'application/pdf'),

('DEM2', 'P4', TRUE, CURRENT_DATE, 'contrat_marie.pdf', 'application/pdf'),
('DEM2', 'P5', FALSE, CURRENT_DATE, NULL, NULL);
