package com.visa.backoffice.controller;

import com.visa.backoffice.entity.TypeVisa;
import com.visa.backoffice.service.TypeVisaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/types-visa")
public class TypeVisaController {

    @Autowired
    private TypeVisaService typeVisaService;

    @GetMapping
    public List<TypeVisa> getAllTypesVisa() {
        return typeVisaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeVisa> getTypeVisaById(@PathVariable String id) {
        Optional<TypeVisa> typeVisa = typeVisaService.findById(id);
        return typeVisa.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TypeVisa createTypeVisa(@RequestBody TypeVisa typeVisa) {
        return typeVisaService.save(typeVisa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeVisa> updateTypeVisa(@PathVariable String id, @RequestBody TypeVisa typeVisa) {
        if (!typeVisaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        typeVisa.setId(id);
        return ResponseEntity.ok(typeVisaService.save(typeVisa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeVisa(@PathVariable String id) {
        if (!typeVisaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        typeVisaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<TypeVisa> searchTypesVisa(@RequestParam String libelle) {
        return typeVisaService.findByLibelleContaining(libelle);
    }
}
