package com.visa.backoffice.controller;

import com.visa.backoffice.entity.CarteResidence;
import com.visa.backoffice.service.CarteResidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cartes-residence")
@CrossOrigin(origins = "*")
public class CarteResidenceController {

    @Autowired
    private CarteResidenceService carteResidenceService;

    @GetMapping
    public ResponseEntity<List<CarteResidence>> getAllCartes() {
        return ResponseEntity.ok(carteResidenceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarteResidence> getCarteById(@PathVariable String id) {
        return carteResidenceService.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CarteResidence> createCarte(@RequestBody CarteResidence carte) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carteResidenceService.save(carte));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarteResidence> updateCarte(@PathVariable String id, @RequestBody CarteResidence carte) {
        if (!carteResidenceService.existsById(id)) return ResponseEntity.notFound().build();
        carte.setId(id);
        return ResponseEntity.ok(carteResidenceService.save(carte));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCarte(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();
        if (carteResidenceService.existsById(id)) {
            carteResidenceService.deleteById(id);
            response.put("message", "Carte de résidence supprimée");
            return ResponseEntity.ok(response);
        }
        response.put("message", "Non trouvé");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @GetMapping("/passport/{idPassport}")
    public ResponseEntity<List<CarteResidence>> getCartesByPassport(@PathVariable String idPassport) {
        return ResponseEntity.ok(carteResidenceService.findByIdPassport(idPassport));
    }
}
