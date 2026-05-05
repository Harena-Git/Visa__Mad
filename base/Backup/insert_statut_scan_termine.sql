-- =========================================
-- Insertion du statut SCAN TERMINE
-- =========================================
-- Ce statut est utilisé pour verrouiller une demande
-- après que tous les fichiers aient été importés et scannés.
-- Une demande avec ce statut ne peut plus être modifiée.
-- =========================================

INSERT INTO statut (id_statut, libelle) VALUES ('SCAN_TERMINE', 'SCAN TERMINE')
ON CONFLICT (id_statut) DO NOTHING;
