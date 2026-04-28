package com.visa.backoffice.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.visa.backoffice.entity.StatutDemande;

@Repository
public interface StatutDemandeRepository extends JpaRepository<StatutDemande, String> {
    @Query("SELECT sd FROM StatutDemande sd WHERE sd.statut.id = :idStatut")
    List<StatutDemande> findByIdStatut(@Param("idStatut") String idStatut);
    
    @Query("SELECT sd FROM StatutDemande sd WHERE sd.demande.demandeur.id = :idDemandeur")
    List<StatutDemande> findByDemandeurId(@Param("idDemandeur") String idDemandeur);
    
    @Query("SELECT sd FROM StatutDemande sd WHERE sd.demande.id = :idDemande")
    List<StatutDemande> findByDemandeId(@Param("idDemande") String idDemande);
    
    List<StatutDemande> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
