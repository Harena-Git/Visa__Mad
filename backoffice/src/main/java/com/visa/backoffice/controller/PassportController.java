package com.visa.backoffice.controller;

import com.visa.backoffice.entity.Passport;
import com.visa.backoffice.service.PassportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/passports")
@CrossOrigin(origins = "*")
public class PassportController {

    @Autowired
    private PassportService passportService;

    @GetMapping
    public ResponseEntity<List<Passport>> getAllPassports() {
        List<Passport> passports = passportService.findAll();
        return ResponseEntity.ok(passports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passport> getPassportById(@PathVariable String id) {
        Optional<Passport> passport = passportService.findById(id);
        return passport.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Passport> createPassport(@RequestBody Passport passport) {
        try {
            if (passportService.existsByNumero(passport.getNumero())) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Un passport avec ce numéro existe déjà");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }
            Passport nouveauPassport = passportService.save(passport);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouveauPassport);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Passport> updatePassport(@PathVariable String id, @RequestBody Passport passport) {
        Optional<Passport> existingPassport = passportService.findById(id);
        if (!existingPassport.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        // Vérifier si le nouveau numéro est déjà utilisé par un autre passport
        Optional<Passport> passportWithSameNumero = passportService.findByNumero(passport.getNumero());
        if (passportWithSameNumero.isPresent() && !passportWithSameNumero.get().getId().equals(id)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Un passport avec ce numéro existe déjà");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        
        passport.setId(id);
        return ResponseEntity.ok(passportService.save(passport));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePassport(@PathVariable String id) {
        if (!passportService.findById(id).isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Passport non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        passportService.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Passport supprimé avec succès");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<Passport> getPassportByNumero(@PathVariable String numero) {
        Optional<Passport> passport = passportService.findByNumero(numero);
        return passport.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/demandeur/{idDemandeur}")
    public ResponseEntity<List<Passport>> getPassportsByDemandeur(@PathVariable String idDemandeur) {
        List<Passport> passports = passportService.findByIdDemandeur(idDemandeur);
        return ResponseEntity.ok(passports);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Passport>> searchPassports(@RequestParam String numero) {
        List<Passport> passports = passportService.findByNumeroContaining(numero);
        return ResponseEntity.ok(passports);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getPassportsCount() {
        Map<String, Long> response = new HashMap<>();
        response.put("count", passportService.findAll().stream().count());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Map<String, Boolean>> checkPassportExists(@PathVariable String id) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", passportService.existsById(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/numero")
    public ResponseEntity<Map<String, Boolean>> checkNumeroExists(@RequestParam String numero) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", passportService.existsByNumero(numero));
        return ResponseEntity.ok(response);
    }
}
