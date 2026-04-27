package com.visa.backoffice.controller;

import com.visa.backoffice.entity.VisaTransformable;
import com.visa.backoffice.service.VisaTransformableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/visas-transformables")
@CrossOrigin(origins = "*")
public class VisaTransformableController {

    @Autowired
    private VisaTransformableService visaTransformableService;

    @GetMapping
    public ResponseEntity<List<VisaTransformable>> getAllVisasTransformables() {
        return ResponseEntity.ok(visaTransformableService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisaTransformable> getVisaTransformableById(@PathVariable String id) {
        return visaTransformableService.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createVisaTransformable(@RequestBody VisaTransformable visa) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(visaTransformableService.save(visa));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage() != null ? e.getMessage() : e.getClass().getName());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisaTransformable> updateVisaTransformable(@PathVariable String id, @RequestBody VisaTransformable visa) {
        if (!visaTransformableService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        visa.setId(id);
        return ResponseEntity.ok(visaTransformableService.save(visa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteVisaTransformable(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();
        if (visaTransformableService.existsById(id)) {
            visaTransformableService.deleteById(id);
            response.put("message", "Visa Transformable supprimé avec succès");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Visa Transformable non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/passport/{idPassport}")
    public ResponseEntity<List<VisaTransformable>> getByIdPassport(@PathVariable String idPassport) {
        return ResponseEntity.ok(visaTransformableService.findByIdPassport(idPassport));
    }

    @GetMapping("/demandeur/{idDemandeur}")
    public ResponseEntity<List<VisaTransformable>> getByIdDemandeur(@PathVariable String idDemandeur) {
        return ResponseEntity.ok(visaTransformableService.findByIdDemandeur(idDemandeur));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<VisaTransformable>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(visaTransformableService.findByTypeTransformable(type.toUpperCase()));
    }
}
