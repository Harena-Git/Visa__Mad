package com.visa.backoffice.repository;

import com.visa.backoffice.entity.CategorieDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategorieDemandeRepository extends JpaRepository<CategorieDemande, String> {
    List<CategorieDemande> findByLibelleContainingIgnoreCase(String libelle);
}
