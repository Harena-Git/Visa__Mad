package com.visa.backoffice.repository;

import com.visa.backoffice.entity.SituationFamille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SituationFamilleRepository extends JpaRepository<SituationFamille, String> {
    List<SituationFamille> findByLibelleContainingIgnoreCase(String libelle);
}
