-- ============================================================================
-- SPRINT 3 SIMPLIFIÉ: UPLOAD FICHIERS + VERROUILLAGE - DONNÉES DE TEST
-- ============================================================================
-- Ce fichier contient des données test pour le Sprint 3
-- Chaque demande peut:
--   1. Uploader UN fichier (PDF ou image) via bouton "📤 Importer"
--   2. Verrouiller la demande via bouton "🔒 Terminer Scann"
-- ============================================================================

-- ============================================================================
-- 1. INSERTION DE DONNÉES DE RÉFÉRENCE (si non présentes)
-- ============================================================================

-- Types de visa
INSERT INTO type_visa (id_type_visa, libelle) VALUES 
('TV001', 'ETUDIANT'),
('TV002', 'TRAVAILLEUR')
ON CONFLICT DO NOTHING;

-- Catégories de demande
INSERT INTO categorie_demande (id_categorie, libelle) VALUES 
('CAT001', 'NOUVEAU'),
('CAT002', 'DUPLICATA'),
('CAT003', 'TRANSFERT')
ON CONFLICT DO NOTHING;

-- Nationalités
INSERT INTO nationalite (id_nationalite, libelle) VALUES 
('NAT001', 'Malgache'),
('NAT002', 'Français'),
('NAT003', 'Américain')
ON CONFLICT DO NOTHING;

-- Situations familiales
INSERT INTO situation_famille (id_situation_famille, libelle) VALUES 
('SF001', 'Célibataire'),
('SF002', 'Marié'),
('SF003', 'Divorcé')
ON CONFLICT DO NOTHING;

-- Statuts
INSERT INTO statut (id_statut, libelle) VALUES 
('STAT001', 'CREATED'),
('STAT002', 'SCAN_TERMINE'),
('STAT003', 'VISA_APPROVED')
ON CONFLICT DO NOTHING;

-- Pièces (optionnel pour ce test simplifié)
INSERT INTO piece (id_piece, libelle, est_obligatoire, id_type_visa) VALUES 
('PIECE001', 'Passeport', 1, 'TV001'),
('PIECE002', 'Photo d''identité', 1, 'TV001')
ON CONFLICT DO NOTHING;

-- ============================================================================
-- 2. DEMANDES DE TEST
-- ============================================================================

-- Demandeur 1
INSERT INTO demandeur (id_demandeur, nom, prenom, nom_jeune_fille, dtn, adresse_mada, telephone, email, created_at, updated_at, id_nationalite, id_situation_famille) VALUES 
('DEM001', 'RAKOTO', 'Jean', NULL, '1995-05-15', '123 Rue de l''Ecole, Antananarivo', '261340012345', 'jean.rakoto@email.com', '2026-04-20', '2026-04-20', 'NAT001', 'SF001')
ON CONFLICT DO NOTHING;

-- Passeport pour demandeur 1
INSERT INTO passport (id_passport, numero, delivre_le, expire_le, id_demandeur) VALUES 
('PASS001', 'A123456789', '2020-01-15', '2030-01-15', 'DEM001')
ON CONFLICT DO NOTHING;

-- Demande 1 (ÉTUDIANT)
INSERT INTO demande (id_demande, created_at, updated_at, id_demande_1, id_categorie, id_type_visa, id_demandeur) VALUES 
('DEM_REQ001', '2026-04-20', '2026-04-22', NULL, 'CAT001', 'TV001', 'DEM001')
ON CONFLICT DO NOTHING;

-- ============================================================================
-- Demandeur 2
INSERT INTO demandeur (id_demandeur, nom, prenom, nom_jeune_fille, dtn, adresse_mada, telephone, email, created_at, updated_at, id_nationalite, id_situation_famille) VALUES 
('DEM002', 'DUPONT', 'Marie', 'MARTIN', '1992-08-22', '456 Rue de la Paix, Antananarivo', '261340054321', 'marie.dupont@email.com', '2026-04-21', '2026-04-21', 'NAT002', 'SF002')
ON CONFLICT DO NOTHING;

-- Passeport pour demandeur 2
INSERT INTO passport (id_passport, numero, delivre_le, expire_le, id_demandeur) VALUES 
('PASS002', 'B987654321', '2019-06-10', '2029-06-10', 'DEM002')
ON CONFLICT DO NOTHING;

-- Demande 2 (TRAVAILLEUR)
INSERT INTO demande (id_demande, created_at, updated_at, id_demande_1, id_categorie, id_type_visa, id_demandeur) VALUES 
('DEM_REQ002', '2026-04-21', '2026-04-21', NULL, 'CAT001', 'TV002', 'DEM002')
ON CONFLICT DO NOTHING;

-- ============================================================================
-- Demandeur 3
INSERT INTO demandeur (id_demandeur, nom, prenom, nom_jeune_fille, dtn, adresse_mada, telephone, email, created_at, updated_at, id_nationalite, id_situation_famille) VALUES 
('DEM003', 'SMITH', 'David', NULL, '1998-03-30', '789 Rue de la Liberté, Antananarivo', '261340098765', 'david.smith@email.com', '2026-04-19', '2026-04-19', 'NAT003', 'SF001')
ON CONFLICT DO NOTHING;

-- Passeport pour demandeur 3
INSERT INTO passport (id_passport, numero, delivre_le, expire_le, id_demandeur) VALUES 
('PASS003', 'C555666777', '2021-11-05', '2031-11-05', 'DEM003')
ON CONFLICT DO NOTHING;

-- Demande 3 (ÉTUDIANT) - DÉJÀ VERROUILLÉE (SCAN TERMINÉ)
INSERT INTO demande (id_demande, created_at, updated_at, id_demande_1, id_categorie, id_type_visa, id_demandeur) VALUES 
('DEM_REQ003', '2026-04-19', '2026-04-22', NULL, 'CAT001', 'TV001', 'DEM003')
ON CONFLICT DO NOTHING;

-- Statut SCAN TERMINÉ pour demande 3
INSERT INTO statut_demande (id_statut_demande, date_, id_statut, id_demande) VALUES 
('STAT_DEM001', '2026-04-22', 'STAT002', 'DEM_REQ003')
ON CONFLICT DO NOTHING;

-- ============================================================================
-- Demandeur 4
INSERT INTO demandeur (id_demandeur, nom, prenom, nom_jeune_fille, dtn, adresse_mada, telephone, email, created_at, updated_at, id_nationalite, id_situation_famille) VALUES 
('DEM004', 'BERNARD', 'Sophie', 'LEONARD', '1994-12-08', '321 Rue des Fleurs, Antananarivo', '261340055555', 'sophie.bernard@email.com', '2026-04-22', '2026-04-22', 'NAT002', 'SF003')
ON CONFLICT DO NOTHING;

-- Passeport pour demandeur 4
INSERT INTO passport (id_passport, numero, delivre_le, expire_le, id_demandeur) VALUES 
('PASS004', 'D444888999', '2022-02-20', '2032-02-20', 'DEM004')
ON CONFLICT DO NOTHING;

-- Demande 4 (TRAVAILLEUR)
INSERT INTO demande (id_demande, created_at, updated_at, id_demande_1, id_categorie, id_type_visa, id_demandeur) VALUES 
('DEM_REQ004', '2026-04-22', '2026-04-22', NULL, 'CAT001', 'TV002', 'DEM004')
ON CONFLICT DO NOTHING;

-- ============================================================================
-- 3. SUMMARY DES CAS DE TEST
-- ============================================================================
-- 
-- DEM_REQ001 (RAKOTO Jean - ÉTUDIANT):
--   ✅ État: Nouveau (non verrouillé)
--   → Testez: Clic "📤 Importer" → Upload fichier → Clic "🔒 Terminer Scann"
--
-- DEM_REQ002 (DUPONT Marie - TRAVAILLEUR):
--   ✅ État: Nouveau (non verrouillé)
--   → Testez: Upload + Verrouillage
--
-- DEM_REQ003 (SMITH David - ÉTUDIANT):
--   🔒 État: VERROUILLÉ (SCAN TERMINÉ)
--   → Testez: Bouton "✏️ Modifier" DÉSACTIVÉ
--            Bouton "🔒 Terminer Scann" DÉSACTIVÉ
--
-- DEM_REQ004 (BERNARD Sophie - TRAVAILLEUR):
--   ✅ État: Nouveau (non verrouillé)
--   → Testez: Upload + Verrouillage
--
-- ============================================================================

COMMIT;

