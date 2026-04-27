package com.visa.backoffice.controller;

import com.visa.backoffice.entity.Statut;
import com.visa.backoffice.service.StatutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/statuts")
@CrossOrigin(origins = "*")
public class StatutController {

    @Autowired
    private StatutService statutService;

    @GetMapping
    public ResponseEntity<List<Statut>> getAllStatuts() {
        List<Statut> statuts = statutService.findAll();
        return ResponseEntity.ok(statuts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Statut> getStatutById(@PathVariable String id) {
        Optional<Statut> statut = statutService.findById(id);
        return statut.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Statut> createStatut(@RequestBody Statut statut) {
        try {
            Statut nouveauStatut = statutService.save(statut);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouveauStatut);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Statut> updateStatut(@PathVariable String id, @RequestBody Statut statut) {
        if (!statutService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        statut.setId(id);
        return ResponseEntity.ok(statutService.save(statut));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStatut(@PathVariable String id) {
        if (!statutService.findById(id).isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Statut non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        statutService.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Statut supprimé avec succès");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Statut>> searchStatuts(@RequestParam String libelle) {
        List<Statut> statuts = statutService.findByLibelleContaining(libelle);
        return ResponseEntity.ok(statuts);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getStatutsCount() {
        Map<String, Long> response = new HashMap<>();
        response.put("count", statutService.findAll().stream().count());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Map<String, Boolean>> checkStatutExists(@PathVariable String id) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", statutService.existsById(id));
        return ResponseEntity.ok(response);
    }
}
