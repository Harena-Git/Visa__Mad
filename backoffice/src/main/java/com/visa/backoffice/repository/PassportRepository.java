package com.visa.backoffice.repository;

import com.visa.backoffice.entity.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassportRepository extends JpaRepository<Passport, String> {
    Optional<Passport> findByNumero(String numero);
    
    @Query("SELECT p FROM Passport p WHERE p.demandeur.id = :idDemandeur")
    List<Passport> findByDemandeur(@Param("idDemandeur") String idDemandeur);
    
    List<Passport> findByNumeroContainingIgnoreCase(String numero);
}
