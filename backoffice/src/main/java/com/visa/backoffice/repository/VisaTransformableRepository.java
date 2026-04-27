package com.visa.backoffice.repository;

import com.visa.backoffice.entity.VisaTransformable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisaTransformableRepository extends JpaRepository<VisaTransformable, String> {
    Optional<VisaTransformable> findByRefVisa(String refVisa);
    
    @Query("SELECT vt FROM VisaTransformable vt WHERE vt.passport.id = :idPassport")
    List<VisaTransformable> findByIdPassport(@Param("idPassport") String idPassport);
    
    @Query("SELECT vt FROM VisaTransformable vt WHERE vt.demandeur.id = :idDemandeur")
    List<VisaTransformable> findByIdDemandeur(@Param("idDemandeur") String idDemandeur);
    
    List<VisaTransformable> findByDateDebutBetween(LocalDate startDate, LocalDate endDate);
    List<VisaTransformable> findByDateFinBetween(LocalDate startDate, LocalDate endDate);
    List<VisaTransformable> findByTypeTransformable(String typeTransformable);
}
