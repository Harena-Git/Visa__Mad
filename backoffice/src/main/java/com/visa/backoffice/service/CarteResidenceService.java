package com.visa.backoffice.service;

import com.visa.backoffice.entity.CarteResidence;
import com.visa.backoffice.repository.CarteResidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CarteResidenceService {

    @Autowired
    private CarteResidenceRepository carteResidenceRepository;

    public List<CarteResidence> findAll() { return carteResidenceRepository.findAll(); }
    public Optional<CarteResidence> findById(String id) { return carteResidenceRepository.findById(id); }
    public CarteResidence save(CarteResidence carteResidence) {
        if (carteResidence.getId() == null || carteResidence.getId().isEmpty()) {
            carteResidence.setId("CR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        return carteResidenceRepository.save(carteResidence);
    }
    public void deleteById(String id) { carteResidenceRepository.deleteById(id); }
    public boolean existsById(String id) { return carteResidenceRepository.existsById(id); }

    public List<CarteResidence> findByIdPassport(String idPassport) {
        return carteResidenceRepository.findByIdPassport(idPassport);
    }
}
