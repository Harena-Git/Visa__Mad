package com.visa.backoffice.controller;

import com.visa.backoffice.entity.Nationalite;
import com.visa.backoffice.service.NationaliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/nationalites")
@CrossOrigin(origins = "*")
public class NationaliteController {

    @Autowired
    private NationaliteService nationaliteService;

    @GetMapping
    public ResponseEntity<List<Nationalite>> getAllNationalites() {
        List<Nationalite> nationalites = nationaliteService.findAll();
        return ResponseEntity.ok(nationalites);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nationalite> getNationaliteById(@PathVariable String id) {
        Optional<Nationalite> nationalite = nationaliteService.findById(id);
        return nationalite.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Nationalite> createNationalite(@RequestBody Nationalite nationalite) {
        try {
            Nationalite nouvelleNationalite = nationaliteService.save(nationalite);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleNationalite);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nationalite> updateNationalite(@PathVariable String id, @RequestBody Nationalite nationalite) {
        if (!nationaliteService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        nationalite.setId(id);
        return ResponseEntity.ok(nationaliteService.save(nationalite));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteNationalite(@PathVariable String id) {
        if (!nationaliteService.findById(id).isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Nationalité non trouvée");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        nationaliteService.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Nationalité supprimée avec succès");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Nationalite>> searchNationalites(@RequestParam String libelle) {
        List<Nationalite> nationalites = nationaliteService.findByLibelleContaining(libelle);
        return ResponseEntity.ok(nationalites);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getNationalitesCount() {
        Map<String, Long> response = new HashMap<>();
        response.put("count", nationaliteService.findAll().stream().count());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Map<String, Boolean>> checkNationaliteExists(@PathVariable String id) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", nationaliteService.existsById(id));
        return ResponseEntity.ok(response);
    }
}
