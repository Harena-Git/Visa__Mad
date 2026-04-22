package visa.project.s6.controller;

import org.springframework.web.bind.annotation.*;
import visa.project.s6.model.Demandeur;
import visa.project.s6.service.DemandeurService;

import java.util.List;

@RestController
@RequestMapping("/api/demandeurs")
public class DemandeurController {

    private final DemandeurService service;

    public DemandeurController(DemandeurService service) {
        this.service = service;
    }

    @PostMapping
    public Demandeur create(@RequestBody Demandeur d) {
        return service.save(d);
    }

    @GetMapping
    public List<Demandeur> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Demandeur getById(@PathVariable String id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Demandeur update(@PathVariable String id, @RequestBody Demandeur d) {
        d.setIdDemandeur(id);
        return service.save(d);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}