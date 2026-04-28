package com.visa.backoffice.service;

import com.visa.backoffice.entity.Demande;
import com.visa.backoffice.entity.Demandeur;
import com.visa.backoffice.repository.DemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class DemandeService {

    @Autowired
    private DemandeRepository demandeRepository;

    public List<Demande> findAll() {
        return demandeRepository.findAll();
    }

    public Optional<Demande> findById(String id) {
        return demandeRepository.findById(id);
    }

    public Demande save(Demande demande) {
        if (demande.getCreatedAt() == null) {
            demande.setCreatedAt(LocalDate.now());
        }
        demande.setUpdatedAt(LocalDate.now());
        return demandeRepository.save(demande);
    }

    public Demande createDemande(Demande demande) {
        demande.setId("REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        demande.setCreatedAt(LocalDate.now());
        demande.setUpdatedAt(LocalDate.now());
        return demandeRepository.save(demande);
    }

    public Optional<Demande> updateDemande(String id, Demande demandeDetails) {
        return demandeRepository.findById(id).map(existingDemande -> {
            existingDemande.setCategorie(demandeDetails.getCategorie());
            existingDemande.setTypeVisa(demandeDetails.getTypeVisa());
            existingDemande.setDemandeur(demandeDetails.getDemandeur());
            existingDemande.setIdDemande1(demandeDetails.getIdDemande1());
            existingDemande.setUpdatedAt(LocalDate.now());
            return demandeRepository.save(existingDemande);
        });
    }

    public boolean deleteById(String id) {
        if (demandeRepository.existsById(id)) {
            demandeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Demande> findByIdCategorie(String idCategorie) {
        return demandeRepository.findByCategorieId(idCategorie);
    }

    public List<Demande> findByIdTypeVisa(String idTypeVisa) {
        return demandeRepository.findByTypeVisaId(idTypeVisa);
    }

    public List<Demande> findByIdDemandeur(String idDemandeur) {
        return demandeRepository.findByDemandeurId(idDemandeur);
    }

    public List<Demande> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate) {
        return demandeRepository.findByCreatedAtBetween(startDate, endDate);
    }

    public List<Demande> findByIdDemande1(String idDemande1) {
        return demandeRepository.findCustomByIdDemande1(idDemande1);
    }

    public List<Demande> findByUpdatedAtBetween(LocalDate startDate, LocalDate endDate) {
        return demandeRepository.findByUpdatedAtBetween(startDate, endDate);
    }

    public boolean existsById(String id) {
        return demandeRepository.existsById(id);
    }

    public long count() {
        return demandeRepository.count();
    }

    public List<Demande> findRecentDemandes(int days) {
        LocalDate startDate = LocalDate.now().minusDays(days);
        return demandeRepository.findByCreatedAtBetween(startDate, LocalDate.now());
    }

    public List<Demande> findByPassportNumero(String numero) {
        return demandeRepository.findByPassportNumero(numero);
    }
}
