package visa.project.s6.service;

import java.util.List;

import visa.project.s6.model.Demandeur;

public interface DemandeurService {
    Demandeur save(Demandeur d);
    List<Demandeur> findAll();
    Demandeur findById(String id);
    void delete(String id);
}