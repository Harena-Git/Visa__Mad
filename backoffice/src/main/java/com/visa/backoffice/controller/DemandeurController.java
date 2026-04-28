package com.visa.backoffice.controller;

import com.visa.backoffice.entity.Demandeur;
import com.visa.backoffice.service.DemandeurService;
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
@RequestMapping("/api/demandeurs")
@CrossOrigin(origins = "*")
public class DemandeurController {

    @Autowired
    private DemandeurService demandeurService;

    @GetMapping
    public ResponseEntity<List<Demandeur>> getAllDemandeurs() {
        List<Demandeur> demandeurs = demandeurService.findAll();
        return ResponseEntity.ok(demandeurs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Demandeur> getDemandeurById(@PathVariable String id) {
        Optional<Demandeur> demandeur = demandeurService.findById(id);
        return demandeur.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Demandeur> createDemandeur(@RequestBody Demandeur demandeur) {
        try {
            Demandeur nouveauDemandeur = demandeurService.createDemandeur(demandeur);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouveauDemandeur);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Demandeur> updateDemandeur(@PathVariable String id, @RequestBody Demandeur demandeur) {
        Optional<Demandeur> updatedDemandeur = demandeurService.updateDemandeur(id, demandeur);
        return updatedDemandeur.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDemandeur(@PathVariable String id) {
        boolean deleted = demandeurService.deleteById(id);
        Map<String, String> response = new HashMap<>();
        if (deleted) {
            response.put("message", "Demandeur supprimé avec succès");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Demandeur non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/search/nom")
    public ResponseEntity<List<Demandeur>> searchByNom(@RequestParam String nom) {
        List<Demandeur> demandeurs = demandeurService.findByNomContaining(nom);
        return ResponseEntity.ok(demandeurs);
    }

    @GetMapping("/search/prenom")
    public ResponseEntity<List<Demandeur>> searchByPrenom(@RequestParam String prenom) {
        List<Demandeur> demandeurs = demandeurService.findByPrenomContaining(prenom);
        return ResponseEntity.ok(demandeurs);
    }

    @GetMapping("/search/nationalite/{idNationalite}")
    public ResponseEntity<List<Demandeur>> searchByNationalite(@PathVariable String idNationalite) {
        List<Demandeur> demandeurs = demandeurService.findByIdNationalite(idNationalite);
        return ResponseEntity.ok(demandeurs);
    }

    @GetMapping("/search/situation-famille/{idSituationFamille}")
    public ResponseEntity<List<Demandeur>> searchBySituationFamille(@PathVariable String idSituationFamille) {
        List<Demandeur> demandeurs = demandeurService.findByIdSituationFamille(idSituationFamille);
        return ResponseEntity.ok(demandeurs);
    }

    @GetMapping("/search/email")
    public ResponseEntity<Demandeur> searchByEmail(@RequestParam String email) {
        Optional<Demandeur> demandeur = demandeurService.findByEmail(email);
        return demandeur.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/telephone")
    public ResponseEntity<List<Demandeur>> searchByTelephone(@RequestParam String telephone) {
        List<Demandeur> demandeurs = demandeurService.findByTelephone(telephone);
        return ResponseEntity.ok(demandeurs);
    }

    @GetMapping("/search/adresse")
    public ResponseEntity<List<Demandeur>> searchByAdresse(@RequestParam String adresse) {
        List<Demandeur> demandeurs = demandeurService.findByAdresseContaining(adresse);
        return ResponseEntity.ok(demandeurs);
    }

    @GetMapping("/search/naissance-range")
    public ResponseEntity<List<Demandeur>> searchByDateNaissance(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Demandeur> demandeurs = demandeurService.findByDateNaissanceBetween(startDate, endDate);
        return ResponseEntity.ok(demandeurs);
    }

    @GetMapping("/search/creation-range")
    public ResponseEntity<List<Demandeur>> searchByDateCreation(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Demandeur> demandeurs = demandeurService.findByCreatedAtBetween(startDate, endDate);
        return ResponseEntity.ok(demandeurs);
    }

    @GetMapping("/search/global")
    public ResponseEntity<List<Demandeur>> searchGlobal(@RequestParam String term) {
        List<Demandeur> demandeurs = demandeurService.searchDemandeurs(term);
        return ResponseEntity.ok(demandeurs);
    }

    @GetMapping("/recent/{days}")
    public ResponseEntity<List<Demandeur>> getRecentDemandeurs(@PathVariable int days) {
        List<Demandeur> demandeurs = demandeurService.findRecentDemandeurs(days);
        return ResponseEntity.ok(demandeurs);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getDemandeursCount() {
        Map<String, Long> response = new HashMap<>();
        response.put("count", demandeurService.count());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Map<String, Boolean>> checkDemandeurExists(@PathVariable String id) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", demandeurService.existsById(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/email")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(@RequestParam String email) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", demandeurService.existsByEmail(email));
        return ResponseEntity.ok(response);
    }
}
