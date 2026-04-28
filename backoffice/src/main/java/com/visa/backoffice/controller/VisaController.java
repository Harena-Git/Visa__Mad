package com.visa.backoffice.controller;

import com.visa.backoffice.entity.Visa;
import com.visa.backoffice.service.VisaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/visas")
@CrossOrigin(origins = "*")
public class VisaController {

    @Autowired
    private VisaService visaService;

    @GetMapping
    public ResponseEntity<List<Visa>> getAllVisas() {
        return ResponseEntity.ok(visaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visa> getVisaById(@PathVariable String id) {
        return visaService.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Visa> createVisa(@RequestBody Visa visa) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(visaService.save(visa));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Visa> updateVisa(@PathVariable String id, @RequestBody Visa visa) {
        if (!visaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        visa.setId(id);
        return ResponseEntity.ok(visaService.save(visa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteVisa(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();
        if (visaService.existsById(id)) {
            visaService.deleteById(id);
            response.put("message", "Visa supprimé avec succès");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Visa non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/passport/{idPassport}")
    public ResponseEntity<List<Visa>> getByIdPassport(@PathVariable String idPassport) {
        return ResponseEntity.ok(visaService.findByIdPassport(idPassport));
    }

    @GetMapping("/demandeur/{idDemandeur}")
    public ResponseEntity<List<Visa>> getByIdDemandeur(@PathVariable String idDemandeur) {
        return ResponseEntity.ok(visaService.findByIdDemandeur(idDemandeur));
    }
}
