-- =====================================================
-- Migration Sprint 4: Ajout du suivi et QR Code
-- =====================================================

-- Ajouter la colonne tracking_token à la table demande
ALTER TABLE demande ADD COLUMN tracking_token VARCHAR(255) UNIQUE;

-- Créer un index sur tracking_token pour les recherches rapides
CREATE INDEX idx_tracking_token ON demande(tracking_token);
