package com.visa.backoffice.repository;

import com.visa.backoffice.entity.Nationalite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NationaliteRepository extends JpaRepository<Nationalite, String> {
    List<Nationalite> findByLibelleContainingIgnoreCase(String libelle);
}
