package com.visa.backoffice.repository;

import com.visa.backoffice.entity.CarteResidence;
import com.visa.backoffice.entity.Demandeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarteResidenceRepository extends JpaRepository<CarteResidence, String> {
    Optional<CarteResidence> findByRefCarteResidence(String refCarteResidence);
    @Query("SELECT c FROM CarteResidence c WHERE c.passport.id = :idPassport")
    List<CarteResidence> findByIdPassport(@Param("idPassport") String idPassport);
    @Query("SELECT c FROM CarteResidence c WHERE c.demande.demandeur = :demandeur")
    List<CarteResidence> findByDemandeur(@Param("demandeur") Demandeur demandeur);
    List<CarteResidence> findByDateDebutBetween(LocalDate startDate, LocalDate endDate);
    List<CarteResidence> findByDateFinBetween(LocalDate startDate, LocalDate endDate);
}
