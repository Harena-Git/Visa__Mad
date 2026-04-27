package com.visa.backoffice.repository;

import com.visa.backoffice.entity.Demandeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DemandeurRepository extends JpaRepository<Demandeur, String> {
    List<Demandeur> findByNomContainingIgnoreCase(String nom);
    List<Demandeur> findByPrenomContainingIgnoreCase(String prenom);
    @Query("SELECT d FROM Demandeur d WHERE d.nationalite.id = :idNationalite")
    List<Demandeur> findByIdNationalite(@Param("idNationalite") String idNationalite);
    
    @Query("SELECT d FROM Demandeur d WHERE d.situationFamille.id = :idSituationFamille")
    List<Demandeur> findByIdSituationFamille(@Param("idSituationFamille") String idSituationFamille);
    List<Demandeur> findByEmail(String email);
    List<Demandeur> findByTelephoneContainingIgnoreCase(String telephone);
    List<Demandeur> findByAdresseMadaContainingIgnoreCase(String adresse);
    List<Demandeur> findByDtnBetween(LocalDate startDate, LocalDate endDate);
    List<Demandeur> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate);
}
