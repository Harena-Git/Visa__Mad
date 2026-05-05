-- Vérifier que le fichier a été enregistré en base de données
SELECT 
    cp.id_demande,
    cp.id_piece,
    cp.file_name,
    cp.file_type,
    cp.est_fourni,
    cp.updated_at,
    p.libelle as piece_libelle
FROM check_piece cp
LEFT JOIN piece p ON cp.id_piece = p.id_piece
WHERE cp.id_demande = 'REQ-7C5354EB'
ORDER BY cp.updated_at DESC;

-- Vérifier que la pièce par défaut a été créée
SELECT * FROM piece WHERE id_piece = 'DEFAULT_UPLOAD';

-- Vérifier les fichiers uploadés pour cette demande
SELECT 
    id_demande,
    id_piece,
    file_name,
    file_type,
    est_fourni,
    updated_at
FROM check_piece
WHERE id_demande = 'REQ-7C5354EB';
