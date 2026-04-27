package com.visa.backoffice.service;

import com.visa.backoffice.entity.Passport;
import com.visa.backoffice.entity.Demandeur;
import com.visa.backoffice.repository.PassportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassportService {

    @Autowired
    private PassportRepository passportRepository;

    public List<Passport> findAll() {
        return passportRepository.findAll();
    }

    public Optional<Passport> findById(String id) {
        return passportRepository.findById(id);
    }

    public Passport save(Passport passport) {
        if (passport.getId() == null || passport.getId().isEmpty()) {
            passport.setId("PASS-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        return passportRepository.save(passport);
    }

    public void deleteById(String id) {
        passportRepository.deleteById(id);
    }

    public Optional<Passport> findByNumero(String numero) {
        return passportRepository.findByNumero(numero);
    }

    public List<Passport> findByIdDemandeur(String idDemandeur) {
        return passportRepository.findByDemandeur(idDemandeur);
    }

    public List<Passport> findByNumeroContaining(String numero) {
        return passportRepository.findByNumeroContainingIgnoreCase(numero);
    }

    public boolean existsById(String id) {
        return passportRepository.existsById(id);
    }

    public boolean existsByNumero(String numero) {
        return passportRepository.findByNumero(numero).isPresent();
    }
}
