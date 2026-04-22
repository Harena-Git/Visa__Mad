package visa.project.s6.service;

import java.util.List;

import org.springframework.stereotype.Service;
 
import visa.project.s6.model.Demande; 

@Service
public interface DemandeService {
    Demande save(Demande d);
    List<Demande> findAll();
    Demande findById(String id);
    void delete(String id);
}