-- ============================================================================
-- SQL: Insérer les statuts manquants pour Sprint 3
-- ============================================================================

-- Vérifier et créer les statuts s'ils n'existent pas

INSERT INTO statut (id_statut, libelle) VALUES 
('SCAN_TERMINE', 'Scan Terminé - Demande Verrouillée'),
('CREATED', 'Créé'),
('SUBMITTED', 'Soumis'),
('APPROVED', 'Approuvé'),
('REJECTED', 'Rejeté')
ON CONFLICT (id_statut) DO NOTHING;

-- Vérifier l'insertion
SELECT id_statut, libelle FROM statut ORDER BY id_statut;

COMMIT;
