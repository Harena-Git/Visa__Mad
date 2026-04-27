package visa.project.s6.controller;

import org.springframework.web.bind.annotation.*;
import visa.project.s6.model.Demande;
import visa.project.s6.service.DemandeService;

import java.util.List;

@RestController
@RequestMapping("/api/demande")
public class DemandeController {

    private final DemandeService service;

    public DemandeController(DemandeService service) {
        this.service = service;
    }

    @PostMapping
    public Demande create(@RequestBody Demande d) {
        return service.save(d);
    }

    @GetMapping
    public List<Demande> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Demande getById(@PathVariable String id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Demande update(@PathVariable String id, @RequestBody Demande d) {
        d.setIdDemande(id);
        return service.save(d);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}