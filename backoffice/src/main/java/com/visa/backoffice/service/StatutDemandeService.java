package com.visa.backoffice.service;

import com.visa.backoffice.entity.StatutDemande;

import com.visa.backoffice.repository.StatutDemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StatutDemandeService {

    @Autowired
    private StatutDemandeRepository statutDemandeRepository;

    public List<StatutDemande> findAll() {
        return statutDemandeRepository.findAll();
    }

    public Optional<StatutDemande> findById(String id) {
        return statutDemandeRepository.findById(id);
    }

    public StatutDemande save(StatutDemande statutDemande) {
        return statutDemandeRepository.save(statutDemande);
    }

    public void deleteById(String id) {
        statutDemandeRepository.deleteById(id);
    }

    public List<StatutDemande> findByIdStatut(String idStatut) {
        return statutDemandeRepository.findByIdStatut(idStatut);
    }

    public List<StatutDemande> findByIdDemandeur(String idDemandeur) {
        return statutDemandeRepository.findByDemandeurId(idDemandeur);
    }

    public List<StatutDemande> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return statutDemandeRepository.findByDateBetween(startDate, endDate);
    }

    public boolean existsById(String id) {
        return statutDemandeRepository.existsById(id);
    }

    public long count() {
        return statutDemandeRepository.count();
    }
}
