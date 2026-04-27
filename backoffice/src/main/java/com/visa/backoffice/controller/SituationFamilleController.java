package com.visa.backoffice.controller;

import com.visa.backoffice.entity.SituationFamille;
import com.visa.backoffice.service.SituationFamilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/situations-famille")
@CrossOrigin(origins = "*")
public class SituationFamilleController {

    @Autowired
    private SituationFamilleService situationFamilleService;

    @GetMapping
    public ResponseEntity<List<SituationFamille>> getAllSituationsFamille() {
        List<SituationFamille> situations = situationFamilleService.findAll();
        return ResponseEntity.ok(situations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SituationFamille> getSituationFamilleById(@PathVariable String id) {
        Optional<SituationFamille> situation = situationFamilleService.findById(id);
        return situation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SituationFamille> createSituationFamille(@RequestBody SituationFamille situationFamille) {
        try {
            SituationFamille nouvelleSituation = situationFamilleService.save(situationFamille);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleSituation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SituationFamille> updateSituationFamille(@PathVariable String id, @RequestBody SituationFamille situationFamille) {
        if (!situationFamilleService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        situationFamille.setId(id);
        return ResponseEntity.ok(situationFamilleService.save(situationFamille));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSituationFamille(@PathVariable String id) {
        if (!situationFamilleService.findById(id).isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Situation familiale non trouvée");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        situationFamilleService.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Situation familiale supprimée avec succès");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SituationFamille>> searchSituationsFamille(@RequestParam String libelle) {
        List<SituationFamille> situations = situationFamilleService.findByLibelleContaining(libelle);
        return ResponseEntity.ok(situations);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getSituationsFamilleCount() {
        Map<String, Long> response = new HashMap<>();
        response.put("count", situationFamilleService.findAll().stream().count());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Map<String, Boolean>> checkSituationFamilleExists(@PathVariable String id) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", situationFamilleService.existsById(id));
        return ResponseEntity.ok(response);
    }
}
