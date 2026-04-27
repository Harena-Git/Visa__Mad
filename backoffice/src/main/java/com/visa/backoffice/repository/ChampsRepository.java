package com.visa.backoffice.repository;

import com.visa.backoffice.entity.Champs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChampsRepository extends JpaRepository<Champs, String> {
    List<Champs> findByLibelleContainingIgnoreCase(String libelle);
    List<Champs> findByEstObligatoire(Integer estObligatoire);
}
