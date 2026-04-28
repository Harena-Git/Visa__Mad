package com.visa.backoffice.controller;

import com.visa.backoffice.entity.CategorieDemande;
import com.visa.backoffice.service.CategorieDemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories-demande")
@CrossOrigin(origins = "*")
public class CategorieDemandeController {

    @Autowired
    private CategorieDemandeService categorieDemandeService;

    @GetMapping
    public ResponseEntity<List<CategorieDemande>> getAllCategoriesDemande() {
        List<CategorieDemande> categories = categorieDemandeService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategorieDemande> getCategorieDemandeById(@PathVariable String id) {
        Optional<CategorieDemande> categorie = categorieDemandeService.findById(id);
        return categorie.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategorieDemande> createCategorieDemande(@RequestBody CategorieDemande categorieDemande) {
        try {
            CategorieDemande nouvelleCategorie = categorieDemandeService.save(categorieDemande);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleCategorie);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategorieDemande> updateCategorieDemande(@PathVariable String id, @RequestBody CategorieDemande categorieDemande) {
        if (!categorieDemandeService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        categorieDemande.setId(id);
        return ResponseEntity.ok(categorieDemandeService.save(categorieDemande));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCategorieDemande(@PathVariable String id) {
        if (!categorieDemandeService.findById(id).isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Catégorie de demande non trouvée");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        categorieDemandeService.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Catégorie de demande supprimée avec succès");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategorieDemande>> searchCategoriesDemande(@RequestParam String libelle) {
        List<CategorieDemande> categories = categorieDemandeService.findByLibelleContaining(libelle);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getCategoriesDemandeCount() {
        Map<String, Long> response = new HashMap<>();
        response.put("count", categorieDemandeService.findAll().stream().count());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Map<String, Boolean>> checkCategorieDemandeExists(@PathVariable String id) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", categorieDemandeService.existsById(id));
        return ResponseEntity.ok(response);
    }
}
