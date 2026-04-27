package com.visa.backoffice.service;

import com.visa.backoffice.entity.Visa;
import com.visa.backoffice.entity.Demande;
import com.visa.backoffice.entity.Passport;
import com.visa.backoffice.repository.VisaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class VisaService {

    @Autowired
    private VisaRepository visaRepository;

    public List<Visa> findAll() {
        return visaRepository.findAll();
    }

    public Optional<Visa> findById(String id) {
        return visaRepository.findById(id);
    }

    public Visa save(Visa visa) {
        if (visa.getId() == null || visa.getId().isEmpty()) {
            visa.setId("VN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        return visaRepository.save(visa);
    }

    public void deleteById(String id) {
        visaRepository.deleteById(id);
    }

    public Optional<Visa> findByRefVisa(String refVisa) {
        return visaRepository.findByRefVisa(refVisa);
    }

    public List<Visa> findByIdPassport(String idPassport) {
        return visaRepository.findByIdPassport(idPassport);
    }

    public List<Visa> findByIdDemandeur(String idDemandeur) {
        return visaRepository.findByIdDemandeur(idDemandeur);
    }

    public List<Visa> findByDateDebutBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return visaRepository.findByDateDebutBetween(startDate, endDate);
    }

    public List<Visa> findByDateFinBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return visaRepository.findByDateFinBetween(startDate, endDate);
    }

    public boolean existsById(String id) {
        return visaRepository.existsById(id);
    }

    public boolean existsByRefVisa(String refVisa) {
        return visaRepository.findByRefVisa(refVisa).isPresent();
    }

    public long count() {
        return visaRepository.count();
    }
}
