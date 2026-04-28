package com.visa.backoffice.service;

import com.visa.backoffice.entity.Demandeur;
import com.visa.backoffice.repository.DemandeurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class DemandeurService {

    @Autowired
    private DemandeurRepository demandeurRepository;

    public List<Demandeur> findAll() {
        return demandeurRepository.findAll();
    }

    public Optional<Demandeur> findById(String id) {
        return demandeurRepository.findById(id);
    }

    public Demandeur save(Demandeur demandeur) {
        if (demandeur.getCreatedAt() == null) {
            demandeur.setCreatedAt(LocalDate.now());
        }
        demandeur.setUpdatedAt(LocalDate.now());
        return demandeurRepository.save(demandeur);
    }

    public Demandeur createDemandeur(Demandeur demandeur) {
        demandeur.setId("DEM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        demandeur.setCreatedAt(LocalDate.now());
        demandeur.setUpdatedAt(LocalDate.now());
        return demandeurRepository.save(demandeur);
    }

    public Optional<Demandeur> updateDemandeur(String id, Demandeur demandeurDetails) {
        return demandeurRepository.findById(id).map(existingDemandeur -> {
            existingDemandeur.setNom(demandeurDetails.getNom());
            existingDemandeur.setPrenom(demandeurDetails.getPrenom());
            existingDemandeur.setNomJeuneFille(demandeurDetails.getNomJeuneFille());
            existingDemandeur.setDtn(demandeurDetails.getDtn());
            existingDemandeur.setAdresseMada(demandeurDetails.getAdresseMada());
            existingDemandeur.setTelephone(demandeurDetails.getTelephone());
            existingDemandeur.setEmail(demandeurDetails.getEmail());
            existingDemandeur.setNationalite(demandeurDetails.getNationalite());
            existingDemandeur.setSituationFamille(demandeurDetails.getSituationFamille());
            existingDemandeur.setUpdatedAt(LocalDate.now());
            return demandeurRepository.save(existingDemandeur);
        });
    }

    public boolean deleteById(String id) {
        if (demandeurRepository.existsById(id)) {
            demandeurRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Demandeur> findByNomContaining(String nom) {
        return demandeurRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Demandeur> findByPrenomContaining(String prenom) {
        return demandeurRepository.findByPrenomContainingIgnoreCase(prenom);
    }

    public List<Demandeur> findByIdNationalite(String idNationalite) {
        return demandeurRepository.findByIdNationalite(idNationalite);
    }

    public List<Demandeur> findByIdSituationFamille(String idSituationFamille) {
        return demandeurRepository.findByIdSituationFamille(idSituationFamille);
    }

    public Optional<Demandeur> findByEmail(String email) {
        return demandeurRepository.findByEmail(email).stream().findFirst();
    }

    public List<Demandeur> findByTelephone(String telephone) {
        return demandeurRepository.findByTelephoneContainingIgnoreCase(telephone);
    }

    public List<Demandeur> findByAdresseContaining(String adresse) {
        return demandeurRepository.findByAdresseMadaContainingIgnoreCase(adresse);
    }

    public List<Demandeur> findByDateNaissanceBetween(LocalDate startDate, LocalDate endDate) {
        return demandeurRepository.findByDtnBetween(startDate, endDate);
    }

    public List<Demandeur> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate) {
        return demandeurRepository.findByCreatedAtBetween(startDate, endDate);
    }

    public boolean existsById(String id) {
        return demandeurRepository.existsById(id);
    }

    public boolean existsByEmail(String email) {
        return demandeurRepository.findByEmail(email).stream().findFirst().isPresent();
    }

    public long count() {
        return demandeurRepository.count();
    }

    public List<Demandeur> findRecentDemandeurs(int days) {
        LocalDate startDate = LocalDate.now().minusDays(days);
        return demandeurRepository.findByCreatedAtBetween(startDate, LocalDate.now());
    }

    public List<Demandeur> searchDemandeurs(String searchTerm) {
        List<Demandeur> byNom = demandeurRepository.findByNomContainingIgnoreCase(searchTerm);
        List<Demandeur> byPrenom = demandeurRepository.findByPrenomContainingIgnoreCase(searchTerm);
        List<Demandeur> byEmail = demandeurRepository.findByEmail(searchTerm);
        
        return byNom.stream()
                .filter(d -> !byPrenom.contains(d))
                .collect(java.util.stream.Collectors.toList());
    }
}
