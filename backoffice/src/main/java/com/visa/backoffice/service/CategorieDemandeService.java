package com.visa.backoffice.service;

import com.visa.backoffice.entity.CategorieDemande;
import com.visa.backoffice.repository.CategorieDemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategorieDemandeService {

    @Autowired
    private CategorieDemandeRepository categorieDemandeRepository;

    public List<CategorieDemande> findAll() {
        return categorieDemandeRepository.findAll();
    }

    public Optional<CategorieDemande> findById(String id) {
        return categorieDemandeRepository.findById(id);
    }

    public CategorieDemande save(CategorieDemande categorieDemande) {
        return categorieDemandeRepository.save(categorieDemande);
    }

    public void deleteById(String id) {
        categorieDemandeRepository.deleteById(id);
    }

    public List<CategorieDemande> findByLibelleContaining(String libelle) {
        return categorieDemandeRepository.findByLibelleContainingIgnoreCase(libelle);
    }

    public boolean existsById(String id) {
        return categorieDemandeRepository.existsById(id);
    }
}
