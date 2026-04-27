package com.visa.backoffice.service;

import com.visa.backoffice.entity.Nationalite;
import com.visa.backoffice.repository.NationaliteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NationaliteService {

    @Autowired
    private NationaliteRepository nationaliteRepository;

    public List<Nationalite> findAll() {
        return nationaliteRepository.findAll();
    }

    public Optional<Nationalite> findById(String id) {
        return nationaliteRepository.findById(id);
    }

    public Nationalite save(Nationalite nationalite) {
        return nationaliteRepository.save(nationalite);
    }

    public void deleteById(String id) {
        nationaliteRepository.deleteById(id);
    }

    public List<Nationalite> findByLibelleContaining(String libelle) {
        return nationaliteRepository.findByLibelleContainingIgnoreCase(libelle);
    }

    public boolean existsById(String id) {
        return nationaliteRepository.existsById(id);
    }
}
