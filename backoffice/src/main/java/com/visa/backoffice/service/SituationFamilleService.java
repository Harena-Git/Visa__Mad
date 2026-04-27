package com.visa.backoffice.service;

import com.visa.backoffice.entity.SituationFamille;
import com.visa.backoffice.repository.SituationFamilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SituationFamilleService {

    @Autowired
    private SituationFamilleRepository situationFamilleRepository;

    public List<SituationFamille> findAll() {
        return situationFamilleRepository.findAll();
    }

    public Optional<SituationFamille> findById(String id) {
        return situationFamilleRepository.findById(id);
    }

    public SituationFamille save(SituationFamille situationFamille) {
        return situationFamilleRepository.save(situationFamille);
    }

    public void deleteById(String id) {
        situationFamilleRepository.deleteById(id);
    }

    public List<SituationFamille> findByLibelleContaining(String libelle) {
        return situationFamilleRepository.findByLibelleContainingIgnoreCase(libelle);
    }

    public boolean existsById(String id) {
        return situationFamilleRepository.existsById(id);
    }
}
