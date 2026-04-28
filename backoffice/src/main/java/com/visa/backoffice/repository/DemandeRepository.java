package com.visa.backoffice.repository;

import com.visa.backoffice.entity.Demande;
import com.visa.backoffice.entity.Demandeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, String> {
    @org.springframework.data.jpa.repository.Query("SELECT d FROM Demande d WHERE d.categorie.id = :idCategorie")
    List<Demande> findByCategorieId(@org.springframework.data.repository.query.Param("idCategorie") String idCategorie);

    @org.springframework.data.jpa.repository.Query("SELECT d FROM Demande d WHERE d.typeVisa.id = :idTypeVisa")
    List<Demande> findByTypeVisaId(@org.springframework.data.repository.query.Param("idTypeVisa") String idTypeVisa);

    @org.springframework.data.jpa.repository.Query("SELECT d FROM Demande d WHERE d.demandeur.id = :idDemandeur")
    List<Demande> findByDemandeurId(@org.springframework.data.repository.query.Param("idDemandeur") String idDemandeur);

    List<Demande> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate);
    @org.springframework.data.jpa.repository.Query("SELECT d FROM Demande d WHERE d.idDemande1 = :idDemande1")
    List<Demande> findCustomByIdDemande1(@org.springframework.data.repository.query.Param("idDemande1") String idDemande1);
    List<Demande> findByUpdatedAtBetween(LocalDate startDate, LocalDate endDate);

    @org.springframework.data.jpa.repository.Query("SELECT d FROM Demande d WHERE d.demandeur.id IN " +
            "(SELECT p.demandeur.id FROM Passport p WHERE p.numero = :numero)")
    List<Demande> findByPassportNumero(@org.springframework.data.repository.query.Param("numero") String numero);
}
