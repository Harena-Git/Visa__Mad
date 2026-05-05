package com.visa.backoffice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visa.backoffice.entity.Demande;
import com.visa.backoffice.entity.StatutDemande;
import com.visa.backoffice.repository.StatutDemandeRepository;
import com.visa.backoffice.service.DemandeService;
import com.visa.backoffice.service.QRCodeService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Contrôleur public pour le suivi des demandes
 * Accessible sans authentification
 */
@RestController
@RequestMapping("/api/public/tracking")
@CrossOrigin(origins = "*")
public class TrackingController {

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private StatutDemandeRepository statutDemandeRepository;

    @Autowired
    private QRCodeService qrCodeService;

    /**
     * Récupère les demandes associées à un numéro de passeport
     * GET /api/public/tracking/passport/{numero}
     * 
     * @param numero Le numéro de passeport
     * @return Liste des demandes associées au passeport
     */
    @GetMapping("/passport/{numero}")
    public ResponseEntity<?> getDemandesByPassport(@PathVariable String numero) {
        try {
            if (numero == null || numero.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Numéro de passeport requis"));
            }

            List<Demande> demandes = demandeService.findByPassportNumero(numero);

            if (demandes.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Aucune demande trouvée pour ce numéro de passeport",
                    "demandes", demandes
                ));
            }

            // Enrichir les demandes avec leurs statuts
            List<Map<String, Object>> demandesEnrichies = demandes.stream()
                .map(this::enrichirDemande)
                .toList();

            return ResponseEntity.ok(Map.of(
                "success", true,
                "demandes", demandesEnrichies,
                "count", demandesEnrichies.size()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("success", false, "error", "Erreur serveur: " + e.getMessage()));
        }
    }

    /**
     * Récupère les informations détaillées de suivi via le token de suivi
     * GET /api/public/tracking/token/{trackingToken}
     * 
     * @param trackingToken Le token unique de suivi
     * @return Informations détaillées de la demande et son statut
     */
    @GetMapping("/token/{trackingToken}")
    public ResponseEntity<?> getDemandeByTrackingToken(@PathVariable String trackingToken) {
        try {
            if (trackingToken == null || trackingToken.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Token de suivi requis"));
            }

            Optional<Demande> demandeOpt = demandeService.findByTrackingToken(trackingToken);

            if (!demandeOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("success", false, "error", "Demande non trouvée"));
            }

            Map<String, Object> trackingInfo = enrichirDemande(demandeOpt.get());
            return ResponseEntity.ok(trackingInfo);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("success", false, "error", "Erreur serveur: " + e.getMessage()));
        }
    }

    /**
     * Recherche les demandes par numéro de passeport ET optionnellement filtre par statut
     * GET /api/public/tracking/search?passport=...&statut=...
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchDemandes(
            @RequestParam String passport,
            @RequestParam(required = false) String statut) {
        try {
            if (passport == null || passport.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "error", "Numéro de passeport requis"));
            }

            List<Demande> demandes = demandeService.findByPassportNumero(passport);

            // Filtrer par statut si fourni
            if (statut != null && !statut.isEmpty()) {
                demandes = demandes.stream()
                    .filter(d -> {
                        List<StatutDemande> statuts = statutDemandeRepository.findByDemandeId(d.getId());
                        return !statuts.isEmpty() && 
                               statuts.stream().anyMatch(s -> s.getStatut() != null && 
                                   s.getStatut().getId().equalsIgnoreCase(statut));
                    })
                    .toList();
            }

            List<Map<String, Object>> demandesEnrichies = demandes.stream()
                .map(this::enrichirDemande)
                .toList();

            return ResponseEntity.ok(Map.of(
                "success", true,
                "demandes", demandesEnrichies,
                "count", demandesEnrichies.size()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("success", false, "error", "Erreur serveur: " + e.getMessage()));
        }
    }

    /**
     * Enrichit une demande avec ses statuts et autres informations
     */
    private Map<String, Object> enrichirDemande(Demande demande) {
        Map<String, Object> demandInfo = new HashMap<>();
        demandInfo.put("demandeId", demande.getId());
        demandInfo.put("trackingToken", demande.getTrackingToken());
        
        // Informations du demandeur
        if (demande.getDemandeur() != null) {
            demandInfo.put("demandeur", Map.of(
                "nom", demande.getDemandeur().getNom() != null ? 
                    demande.getDemandeur().getNom() : "",
                "prenom", demande.getDemandeur().getPrenom() != null ? 
                    demande.getDemandeur().getPrenom() : ""
            ));
        }
        
        // Informations du type de visa
        if (demande.getTypeVisa() != null) {
            demandInfo.put("typeVisa", demande.getTypeVisa().getLibelle() != null ? 
                demande.getTypeVisa().getLibelle() : "");
        }
        
        // Informations de catégorie
        if (demande.getCategorie() != null) {
            demandInfo.put("categorie", demande.getCategorie().getLibelle() != null ? 
                demande.getCategorie().getLibelle() : "");
        }
        
        // Dates
        demandInfo.put("dateCreation", demande.getCreatedAt());
        demandInfo.put("dateMiseAJour", demande.getUpdatedAt());
        
        // Statuts
        List<StatutDemande> statuts = statutDemandeRepository.findByDemandeId(demande.getId());
        List<Map<String, Object>> statutsList = statuts.stream()
            .map(s -> {
                Map<String, Object> map = new HashMap<>();
                map.put("statut", s.getStatut() != null ? s.getStatut().getLibelle() : "N/A");
                map.put("date", s.getDate());
                return map;
            })
            .toList();
        
        demandInfo.put("historique", statutsList);
        
        // Statut actuel
        if (!statuts.isEmpty()) {
            StatutDemande lastStatut = statuts.get(statuts.size() - 1);
            demandInfo.put("statutActuel", lastStatut.getStatut() != null ? 
                lastStatut.getStatut().getLibelle() : "N/A");
        } else {
            demandInfo.put("statutActuel", "Créée");
        }
        
        // Ajouter le QR code si le tracking token existe
        if (demande.getTrackingToken() != null && !demande.getTrackingToken().isEmpty()) {
            try {
                String trackingUrl = "http://localhost:8080/visa-backoffice/tracking.html?token=" + demande.getTrackingToken();
                String qrCodeBase64 = qrCodeService.generateQRCodeBase64(trackingUrl, 400);
                demandInfo.put("qrCodeBase64", qrCodeBase64);
            } catch (Exception e) {
                // Si la génération du QR code échoue, continuer sans
                System.err.println("Erreur lors de la génération du QR code: " + e.getMessage());
            }
        }
        
        return demandInfo;
    }

    /**
     * Endpoint de santé pour vérifier que le service de suivi est disponible
     * GET /api/public/tracking/health
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Tracking API",
            "version", "1.0"
        ));
    }
}
