package com.visa.backoffice.controller;

import com.visa.backoffice.entity.Demande;
import com.visa.backoffice.service.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "*")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

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
}
