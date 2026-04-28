package com.visa.backoffice.service;

import com.visa.backoffice.entity.VisaTransformable;
import com.visa.backoffice.entity.Visa;
import com.visa.backoffice.entity.Demandeur;
import com.visa.backoffice.repository.VisaTransformableRepository;
import com.visa.backoffice.repository.VisaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class VisaTransformableService {

    @Autowired
    private VisaTransformableRepository visaTransformableRepository;

    @Autowired
    private VisaRepository visaRepository;

    public List<VisaTransformable> findAll() {
        return visaTransformableRepository.findAll();
    }

    public Optional<VisaTransformable> findById(String id) {
        return visaTransformableRepository.findById(id);
    }

    public VisaTransformable save(VisaTransformable visaTransformable) {
        // Generate ID if not set (new record)
        if (visaTransformable.getId() == null || visaTransformable.getId().isEmpty()) {
            visaTransformable.setId("VT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        // Validate visa original expiry date if linked
        if (visaTransformable.getVisaOriginal() != null
                && visaTransformable.getVisaOriginal().getId() != null
                && !visaTransformable.getVisaOriginal().getId().isEmpty()
                && visaTransformable.getDateDebut() != null) {
            // Fetch the full visa original from DB to get its dateFin
            Optional<Visa> visaOpt = visaRepository.findById(visaTransformable.getVisaOriginal().getId());
            if (visaOpt.isPresent()) {
                Visa visaOriginal = visaOpt.get();
                LocalDate finVisaNormal = visaOriginal.getDateFin() != null
                        ? visaOriginal.getDateFin().toLocalDate()
                        : null;
                if (finVisaNormal != null && visaTransformable.getDateDebut().isAfter(finVisaNormal)) {
                    throw new IllegalArgumentException(
                            "La demande de visa transformable doit être faite avant la fin du visa normal (" + finVisaNormal + ").");
                }
            }
        }
        return visaTransformableRepository.save(visaTransformable);
    }

    public void deleteById(String id) {
        visaTransformableRepository.deleteById(id);
    }

    public Optional<VisaTransformable> findByRefVisa(String refVisa) {
        return visaTransformableRepository.findByRefVisa(refVisa);
    }

    public List<VisaTransformable> findByIdPassport(String idPassport) {
        return visaTransformableRepository.findByIdPassport(idPassport);
    }

    public List<VisaTransformable> findByIdDemandeur(String idDemandeur) {
        return visaTransformableRepository.findByIdDemandeur(idDemandeur);
    }

    public List<VisaTransformable> findByDateDebutBetween(LocalDate startDate, LocalDate endDate) {
        return visaTransformableRepository.findByDateDebutBetween(startDate, endDate);
    }

    public List<VisaTransformable> findByDateFinBetween(LocalDate startDate, LocalDate endDate) {
        return visaTransformableRepository.findByDateFinBetween(startDate, endDate);
    }

    public List<VisaTransformable> findByTypeTransformable(String type) {
        return visaTransformableRepository.findByTypeTransformable(type);
    }

    public boolean existsById(String id) {
        return visaTransformableRepository.existsById(id);
    }

    public long count() {
        return visaTransformableRepository.count();
    }
}
