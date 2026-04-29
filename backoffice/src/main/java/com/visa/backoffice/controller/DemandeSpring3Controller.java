package com.visa.backoffice.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.visa.backoffice.entity.CheckPiece;
import com.visa.backoffice.entity.Demande;
import com.visa.backoffice.entity.Statut;
import com.visa.backoffice.entity.StatutDemande;
import com.visa.backoffice.repository.CheckPieceRepository;
import com.visa.backoffice.repository.DemandeRepository;
import com.visa.backoffice.repository.StatutDemandeRepository;
import com.visa.backoffice.repository.StatutRepository;

/**
 * Contrôleur REST pour les fonctionnalités Sprint 3
 * - Upload de fichiers pour les demandes
 * - Verrouillage des demandes (TERMINER SCANN)
 */
@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "*")
public class DemandeSpring3Controller {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private CheckPieceRepository checkPieceRepository;

    @Autowired
    private StatutDemandeRepository statutDemandeRepository;

    @Autowired
    private StatutRepository statutRepository;

    // Répertoire de stockage des fichiers
    private static final String UPLOAD_DIR = "uploads/demandes/";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

    /**
     * Vérifier si une demande est verrouillée
     * GET /demandes/{demandeId}/is-verrouille
     * 
     * @param demandeId ID de la demande
     * @return { "verrouille": true/false }
     */
    @GetMapping("/{demandeId}/is-verrouille")
    public ResponseEntity<?> isVerrouille(@PathVariable String demandeId) {
        try {
            Optional<Demande> demandeOpt = demandeRepository.findById(demandeId);
            if (!demandeOpt.isPresent()) {
                return ResponseEntity.badRequest().body(
                    Map.of("verrouille", false));
            }

            boolean verrouille = isDemandeVerrouille(demandeOpt.get());
            return ResponseEntity.ok(Map.of(
                "verrouille", verrouille,
                "demandeId", demandeId
            ));

        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("verrouille", false));
        }
    }

    /**
     * Endpoint 1: Upload d'un fichier pour une demande
     * POST /demandes/{demandeId}/upload-fichier
     * 
     * @param demandeId ID de la demande
     * @param file Fichier à uploader
     * @return Message de succès ou d'erreur
     */
    @PostMapping("/{demandeId}/upload-fichier")
    public ResponseEntity<?> uploadFichier(
            @PathVariable String demandeId,
            @RequestParam("file") MultipartFile file) {

        try {
            // 1. Vérifier que la demande existe
            Optional<Demande> demandeOpt = demandeRepository.findById(demandeId);
            if (!demandeOpt.isPresent()) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Demande non trouvée: " + demandeId));
            }

            // 2. Vérifier que la demande n'est pas verrouillée
            Demande demande = demandeOpt.get();
            if (isDemandeVerrouille(demande)) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Cette demande est verrouillée et ne peut pas être modifiée"));
            }

            // 3. Valider le fichier
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Le fichier est vide"));
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Le fichier dépasse la taille maximale (10 MB)"));
            }

            // 4. Vérifier le type MIME
            String contentType = file.getContentType();
            if (!isValidFileType(contentType)) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Type de fichier non autorisé. Utilisez PDF ou images"));
            }

            // 5. Créer le répertoire de stockage s'il n'existe pas
            String uploadDirPath = UPLOAD_DIR + demandeId;
            Files.createDirectories(Paths.get(uploadDirPath));

            // 6. Sauvegarder le fichier
            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
            Path filePath = Paths.get(uploadDirPath, fileName);
            Files.write(filePath, file.getBytes());

            // 7. Mettre à jour la première CheckPiece avec les infos du fichier
            List<CheckPiece> checkPieces = checkPieceRepository.findByDemandeId(demandeId);
            if (!checkPieces.isEmpty()) {
                CheckPiece firstPiece = checkPieces.get(0);
                firstPiece.setFileName(originalFileName);
                firstPiece.setFileType(contentType);
                firstPiece.setEstFourni(true);
                firstPiece.setUpdatedAt(LocalDate.now());
                checkPieceRepository.save(firstPiece);
            }

            // 8. Retourner succès
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Fichier uploadé avec succès",
                "fileName", originalFileName,
                "fileType", contentType
            ));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("success", false, "error", "Erreur lors de la sauvegarde du fichier: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("success", false, "error", "Erreur serveur: " + e.getMessage()));
        }
    }

    /**
     * Endpoint 2: Terminer le scan et verrouiller la demande
     * POST /demandes/{demandeId}/terminer-scan
     * 
     * @param demandeId ID de la demande
     * @param payload Map contenant idStatut et date
     * @return Message de succès ou d'erreur
     */
    @PostMapping("/{demandeId}/terminer-scan")
    public ResponseEntity<?> terminerScan(
            @PathVariable String demandeId,
            @RequestBody Map<String, String> payload) {

        try {
            // 1. Vérifier que la demande existe
            Optional<Demande> demandeOpt = demandeRepository.findById(demandeId);
            if (!demandeOpt.isPresent()) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Demande non trouvée: " + demandeId));
            }

            Demande demande = demandeOpt.get();

            // 2. Vérifier que la demande n'est pas déjà verrouillée
            if (isDemandeVerrouille(demande)) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Cette demande est déjà verrouillée"));
            }

            // 3. ⭐ VALIDATION BACKEND: Vérifier qu'il y a au moins un fichier uploadé
            List<CheckPiece> checkPieces = checkPieceRepository.findByDemandeId(demandeId);
            boolean hasFile = checkPieces.stream()
                .anyMatch(piece -> piece.getFileName() != null && !piece.getFileName().trim().isEmpty());
            
            if (!hasFile) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", 
                        "Impossible de verrouiller: aucun fichier n'a été uploadé pour cette demande"));
            }

            // 4. Récupérer l'ID du statut (devrait être 'SCAN_TERMINE')
            String idStatut = payload.get("idStatut");
            String dateStr = payload.get("date");

            if (idStatut == null || dateStr == null) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Paramètres manquants: idStatut et date requis"));
            }

            // 5. Vérifier que le statut existe (sinon, créer un enregistrement avec le statut par défaut)
            Statut statut = null;
            Optional<Statut> statutOpt = statutRepository.findById(idStatut);
            if (statutOpt.isPresent()) {
                statut = statutOpt.get();
            } else {
                // Le statut n'existe pas - le créer ou retourner erreur
                System.out.println("⚠️ Statut '" + idStatut + "' non trouvé en base. Vérifiez que les statuts sont créés.");
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", 
                        "Statut '" + idStatut + "' non trouvé. Assurez-vous qu'il existe en base de données"));
            }

            // 6. Créer un nouvel enregistrement StatutDemande
            StatutDemande statutDemande = new StatutDemande();
            statutDemande.setId(UUID.randomUUID().toString());
            statutDemande.setDate(LocalDate.parse(dateStr));
            statutDemande.setDemande(demande);
            statutDemande.setStatut(statut);

            statutDemandeRepository.save(statutDemande);

            // 7. Mettre à jour la demande (updated_at)
            demande.setUpdatedAt(LocalDate.now());
            demandeRepository.save(demande);

            System.out.println("✅ Demande " + demandeId + " verrouillée avec le statut " + idStatut);

            // 8. Retourner succès
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Demande verrouillée avec succès (SCAN TERMINÉ)"
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("success", false, "error", "Erreur serveur: " + e.getMessage()));
        }
    }

    /**
     * Vérifie si une demande est verrouillée (statut SCAN_TERMINE)
     * 
     * @param demande La demande à vérifier
     * @return true si la demande est verrouillée, false sinon
     */
    private boolean isDemandeVerrouille(Demande demande) {
        // Chercher si elle a un statut SCAN_TERMINE
        List<StatutDemande> statuts = statutDemandeRepository.findByDemandeId(demande.getId());
        return statuts.stream()
            .anyMatch(s -> s.getStatut() != null && "SCAN_TERMINE".equals(s.getStatut().getId()));
    }

    /**
     * Vérifie si le type MIME du fichier est autorisé
     * 
     * @param contentType Type MIME du fichier
     * @return true si le type est autorisé, false sinon
     */
    private boolean isValidFileType(String contentType) {
        if (contentType == null) {
            return false;
        }
        
        // Fichiers autorisés: PDF et images courantes
        return contentType.equalsIgnoreCase("application/pdf") ||
               contentType.equalsIgnoreCase("image/jpeg") ||
               contentType.equalsIgnoreCase("image/jpg") ||
               contentType.equalsIgnoreCase("image/png") ||
               contentType.equalsIgnoreCase("image/gif") ||
               contentType.equalsIgnoreCase("image/webp");
    }
}
