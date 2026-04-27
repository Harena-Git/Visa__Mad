package com.visa.backoffice.repository;

import com.visa.backoffice.entity.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatutRepository extends JpaRepository<Statut, String> {
    List<Statut> findByLibelleContainingIgnoreCase(String libelle);
}
