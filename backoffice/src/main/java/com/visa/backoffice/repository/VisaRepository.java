package com.visa.backoffice.repository;

import com.visa.backoffice.entity.Visa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisaRepository extends JpaRepository<Visa, String> {
    Optional<Visa> findByRefVisa(String refVisa);
    
    @Query("SELECT v FROM Visa v WHERE v.passport.id = :idPassport")
    List<Visa> findByIdPassport(@Param("idPassport") String idPassport);
    
    @Query("SELECT v FROM Visa v WHERE v.demande.id = :idDemandeur")
    List<Visa> findByIdDemandeur(@Param("idDemandeur") String idDemandeur);
    
    List<Visa> findByDateDebutBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Visa> findByDateFinBetween(LocalDateTime startDate, LocalDateTime endDate);
}
