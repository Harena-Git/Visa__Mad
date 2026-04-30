package com.visa.backoffice.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.visa.backoffice.entity.CheckPiece;
import com.visa.backoffice.entity.CheckPieceId;
import com.visa.backoffice.entity.Demande;
import com.visa.backoffice.entity.Piece;
import com.visa.backoffice.entity.Statut;
import com.visa.backoffice.entity.StatutDemande;
import com.visa.backoffice.repository.CheckPieceRepository;
import com.visa.backoffice.repository.PieceRepository;
import com.visa.backoffice.repository.StatutDemandeRepository;
import com.visa.backoffice.repository.StatutRepository;
import com.visa.backoffice.service.DemandeService;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "*")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;
    
    @Autowired
    private CheckPieceRepository checkPieceRepository;

    @Autowired
    private StatutDemandeRepository statutDemandeRepository;

    @Autowired
    private StatutRepository statutRepository;

    @Autowired
    private PieceRepository pieceRepository;

    // Répertoire de stockage des fichiers
    private static final String UPLOAD_DIR = "uploads/demandes/";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

    @GetMapping
    public ResponseEntity<List<Demande>> getAllDemandes() {
        List<Demande> demandes = demandeService.findAll();
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Demande> getDemandeById(@PathVariable String id) {
        Optional<Demande> demande = demandeService.findById(id);
        return demande.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Demande> createDemande(@RequestBody Demande demande) {
        try {
            Demande nouvelleDemande = demandeService.createDemande(demande);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleDemande);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Demande> updateDemande(@PathVariable String id, @RequestBody Demande demande) {
        Optional<Demande> updatedDemande = demandeService.updateDemande(id, demande);
        return updatedDemande.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDemande(@PathVariable String id) {
        boolean deleted = demandeService.deleteById(id);
        Map<String, String> response = new HashMap<>();
        if (deleted) {
            response.put("message", "Demande supprimée avec succès");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Demande non trouvée");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/categorie/{idCategorie}")
    public ResponseEntity<List<Demande>> getByCategorie(@PathVariable String idCategorie) {
        List<Demande> demandes = demandeService.findByIdCategorie(idCategorie);
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/type-visa/{idTypeVisa}")
    public ResponseEntity<List<Demande>> getByTypeVisa(@PathVariable String idTypeVisa) {
        List<Demande> demandes = demandeService.findByIdTypeVisa(idTypeVisa);
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/demandeur/{idDemandeur}")
    public ResponseEntity<List<Demande>> getByDemandeur(@PathVariable String idDemandeur) {
        List<Demande> demandes = demandeService.findByIdDemandeur(idDemandeur);
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Demande>> getByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Demande> demandes = demandeService.findByCreatedAtBetween(startDate, endDate);
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/updated-range")
    public ResponseEntity<List<Demande>> getByUpdatedRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Demande> demandes = demandeService.findByUpdatedAtBetween(startDate, endDate);
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/linked/{idDemande1}")
    public ResponseEntity<List<Demande>> getLinkedDemandes(@PathVariable String idDemande1) {
        List<Demande> demandes = demandeService.findByIdDemande1(idDemande1);
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/recent/{days}")
    public ResponseEntity<List<Demande>> getRecentDemandes(@PathVariable int days) {
        List<Demande> demandes = demandeService.findRecentDemandes(days);
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getDemandesCount() {
        Map<String, Long> response = new HashMap<>();
        response.put("count", demandeService.count());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Map<String, Boolean>> checkDemandeExists(@PathVariable String id) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", demandeService.existsById(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/passport/{numero}")
    public ResponseEntity<List<Demande>> getByPassportNumero(@PathVariable String numero) {
        List<Demande> demandes = demandeService.findByPassportNumero(numero);
        return ResponseEntity.ok(demandes);
    }

    /**
     * Vérifier si une demande est verrouillée
     * GET /demandes/{demandeId}/is-verrouille
     */
    @GetMapping("/{demandeId}/is-verrouille")
    public ResponseEntity<?> isVerrouille(@PathVariable String demandeId) {
        try {
            Optional<Demande> demandeOpt = demandeService.findById(demandeId);
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
     */
    @PostMapping("/{demandeId}/upload-fichier")
    public ResponseEntity<?> uploadFichier(
            @PathVariable String demandeId,
            @RequestParam("file") MultipartFile file) {

        try {
            System.out.println("🔵 [UPLOAD START] DemandeId: " + demandeId + ", FileName: " + file.getOriginalFilename() + ", Size: " + file.getSize());
            
            // 1. Vérifier que la demande existe
            Optional<Demande> demandeOpt = demandeService.findById(demandeId);
            if (!demandeOpt.isPresent()) {
                System.out.println("❌ [UPLOAD] Demande non trouvée: " + demandeId);
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Demande non trouvée: " + demandeId));
            }

            // 2. Vérifier que la demande n'est pas verrouillée
            Demande demande = demandeOpt.get();
            if (isDemandeVerrouille(demande)) {
                System.out.println("❌ [UPLOAD] Demande verrouillée: " + demandeId);
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Cette demande est verrouillée et ne peut pas être modifiée"));
            }

            // 3. Valider le fichier
            if (file.isEmpty()) {
                System.out.println("❌ [UPLOAD] Fichier vide");
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Le fichier est vide"));
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                System.out.println("❌ [UPLOAD] Fichier trop gros: " + file.getSize() + " > " + MAX_FILE_SIZE);
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Le fichier dépasse la taille maximale (10 MB)"));
            }

            // 4. Vérifier le type MIME et l'extension
            String contentType = file.getContentType();
            String originalFileName = file.getOriginalFilename();
            System.out.println("📋 [UPLOAD] Content-Type: " + contentType + ", FileName: " + originalFileName);
            
            if (!isValidFileType(contentType, originalFileName)) {
                System.out.println("❌ [UPLOAD] Type de fichier non autorisé: " + contentType + " pour " + originalFileName);
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Type de fichier non autorisé. Utilisez PDF ou images"));
            }

            // 5. Créer le répertoire de stockage s'il n'existe pas
            String uploadDirPath = UPLOAD_DIR + demandeId;
            System.out.println("📁 [UPLOAD] Création du répertoire: " + uploadDirPath);
            Path dirPath = Paths.get(uploadDirPath);
            Files.createDirectories(dirPath);
            System.out.println("✅ [UPLOAD] Répertoire créé/existe: " + dirPath.toAbsolutePath());

            // 6. Sauvegarder le fichier
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
            Path filePath = Paths.get(uploadDirPath, fileName);
            System.out.println("💾 [UPLOAD] Sauvegarde du fichier: " + filePath.toAbsolutePath());
            Files.write(filePath, file.getBytes());
            System.out.println("✅ [UPLOAD] Fichier sauvegardé avec succès");

            // 7. Mettre à jour la première CheckPiece avec les infos du fichier
            System.out.println("📝 [UPLOAD] Mise à jour CheckPiece pour demandeId: " + demandeId);
            List<CheckPiece> checkPieces = checkPieceRepository.findByDemandeId(demandeId);
            System.out.println("📝 [UPLOAD] CheckPieces trouvées: " + checkPieces.size());
            
            if (!checkPieces.isEmpty()) {
                CheckPiece firstPiece = checkPieces.get(0);
                firstPiece.setFileName(originalFileName);
                firstPiece.setFileType(contentType);
                firstPiece.setEstFourni(true);
                firstPiece.setUpdatedAt(LocalDate.now());
                checkPieceRepository.save(firstPiece);
                System.out.println("✅ [UPLOAD] CheckPiece mise à jour");
            } else {
                System.out.println("⚠️ [UPLOAD] Aucune CheckPiece trouvée, création d'une nouvelle");
                // Chercher une pièce disponible ou créer une par défaut
                List<Piece> pieces = pieceRepository.findByIdTypeVisa(demande.getTypeVisa().getId());
                String pieceId;
                
                if (!pieces.isEmpty()) {
                    pieceId = pieces.get(0).getId();
                } else {
                    List<Piece> allPieces = pieceRepository.findAll();
                    if (!allPieces.isEmpty()) {
                        pieceId = allPieces.get(0).getId();
                    } else {
                        // Créer une pièce par défaut pour les uploads
                        System.out.println("⚠️ [UPLOAD] Aucune pièce trouvée, création d'une pièce par défaut");
                        Piece defaultPiece = new Piece();
                        defaultPiece.setId("DEFAULT_UPLOAD");
                        defaultPiece.setLibelle("Fichier uploadé");
                        defaultPiece.setEstObligatoire(0);
                        try {
                            defaultPiece = pieceRepository.save(defaultPiece);
                            pieceId = defaultPiece.getId();
                            System.out.println("✅ [UPLOAD] Pièce par défaut créée");
                        } catch (Exception e) {
                            System.out.println("❌ [UPLOAD] Erreur lors de la création de la pièce par défaut: " + e.getMessage());
                            return ResponseEntity.badRequest().body(
                                Map.of("success", false, "error", "Impossible de créer une pièce par défaut"));
                        }
                    }
                }
                
                CheckPiece newCheckPiece = new CheckPiece();
                CheckPieceId cpId = new CheckPieceId(demandeId, pieceId);
                newCheckPiece.setId(cpId);
                newCheckPiece.setFileName(originalFileName);
                newCheckPiece.setFileType(contentType);
                newCheckPiece.setEstFourni(true);
                newCheckPiece.setUpdatedAt(LocalDate.now());
                checkPieceRepository.save(newCheckPiece);
                System.out.println("✅ [UPLOAD] Nouvelle CheckPiece créée");
            }

            // 7b. Mettre à jour updated_at de la demande
            System.out.println("📝 [UPLOAD] Mise à jour updated_at de la demande");
            demande.setUpdatedAt(LocalDate.now());
            demandeService.updateDemande(demandeId, demande);

            // 8. Retourner succès
            System.out.println("✅ [UPLOAD SUCCESS] Fichier uploadé pour demandeId: " + demandeId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Fichier uploadé avec succès",
                "fileName", originalFileName,
                "fileType", contentType
            ));

        } catch (IOException e) {
            System.out.println("❌ [UPLOAD IO ERROR] " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("success", false, "error", "Erreur lors de la sauvegarde du fichier: " + e.getMessage()));
        } catch (Exception e) {
            System.out.println("❌ [UPLOAD ERROR] " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("success", false, "error", "Erreur serveur: " + e.getMessage()));
        }
    }

    /**
     * Endpoint 2: Terminer le scan et verrouiller la demande
     * POST /demandes/{demandeId}/terminer-scan
     */
    @PostMapping("/{demandeId}/terminer-scan")
    public ResponseEntity<?> terminerScan(
            @PathVariable String demandeId,
            @RequestBody Map<String, String> payload) {

        try {
            // 1. Vérifier que la demande existe
            Optional<Demande> demandeOpt = demandeService.findById(demandeId);
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

            // 3. VALIDATION BACKEND: Vérifier qu'il y a au moins un fichier uploadé
            List<CheckPiece> checkPieces = checkPieceRepository.findByDemandeId(demandeId);
            boolean hasFile = checkPieces.stream()
                .anyMatch(piece -> piece.getFileName() != null && !piece.getFileName().trim().isEmpty());
            
            if (!hasFile) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", 
                        "Impossible de verrouiller: aucun fichier n'a été uploadé pour cette demande"));
            }

            // 4. Récupérer l'ID du statut
            String idStatut = payload.get("idStatut");
            String dateStr = payload.get("date");

            if (idStatut == null || dateStr == null) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Paramètres manquants: idStatut et date requis"));
            }

            // 5. Vérifier que le statut existe
            Optional<Statut> statutOpt = statutRepository.findById(idStatut);
            if (!statutOpt.isPresent()) {
                System.out.println("⚠️ Statut '" + idStatut + "' non trouvé en base.");
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", 
                        "Statut '" + idStatut + "' non trouvé. Assurez-vous qu'il existe en base de données"));
            }

            Statut statut = statutOpt.get();

            // 6. Créer un nouvel enregistrement StatutDemande
            StatutDemande statutDemande = new StatutDemande();
            statutDemande.setId(UUID.randomUUID().toString());
            statutDemande.setDate(LocalDate.parse(dateStr));
            statutDemande.setDemande(demande);
            statutDemande.setStatut(statut);

            statutDemandeRepository.save(statutDemande);

            // 7. Mettre à jour la demande (updated_at)
            demande.setUpdatedAt(LocalDate.now());
            demandeService.updateDemande(demandeId, demande);

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
     */
    private boolean isDemandeVerrouille(Demande demande) {
        List<StatutDemande> statuts = statutDemandeRepository.findByDemandeId(demande.getId());
        return statuts.stream()
            .anyMatch(s -> s.getStatut() != null && "SCAN_TERMINE".equals(s.getStatut().getId()));
    }

    /**
     * Vérifie si le type MIME du fichier est autorisé
     */
    private boolean isValidFileType(String contentType, String fileName) {
        // D'abord vérifier par content-type
        if (contentType != null) {
            if (contentType.equalsIgnoreCase("application/pdf") ||
               contentType.equalsIgnoreCase("image/jpeg") ||
               contentType.equalsIgnoreCase("image/jpg") ||
               contentType.equalsIgnoreCase("image/png") ||
               contentType.equalsIgnoreCase("image/gif") ||
               contentType.equalsIgnoreCase("image/webp")) {
                return true;
            }
        }
        
        // Si content-type n'existe pas ou n'est pas reconnu, vérifier l'extension du fichier
        if (fileName != null) {
            String lowerFileName = fileName.toLowerCase();
            return lowerFileName.endsWith(".pdf") ||
                   lowerFileName.endsWith(".jpg") ||
                   lowerFileName.endsWith(".jpeg") ||
                   lowerFileName.endsWith(".png") ||
                   lowerFileName.endsWith(".gif") ||
                   lowerFileName.endsWith(".webp");
        }
        
        return false;
    }
}
