package com.visa.backoffice.service;

import com.visa.backoffice.entity.Statut;
import com.visa.backoffice.repository.StatutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatutService {

    @Autowired
    private StatutRepository statutRepository;

    public List<Statut> findAll() {
        return statutRepository.findAll();
    }

    public Optional<Statut> findById(String id) {
        return statutRepository.findById(id);
    }

    public Statut save(Statut statut) {
        return statutRepository.save(statut);
    }

    public void deleteById(String id) {
        statutRepository.deleteById(id);
    }

    public List<Statut> findByLibelleContaining(String libelle) {
        return statutRepository.findByLibelleContainingIgnoreCase(libelle);
    }

    public boolean existsById(String id) {
        return statutRepository.existsById(id);
    }
}
