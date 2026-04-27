package com.visa.backoffice.repository;

import com.visa.backoffice.entity.CheckPiece;
import com.visa.backoffice.entity.CheckPieceId;
import com.visa.backoffice.entity.Demandeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CheckPieceRepository extends JpaRepository<CheckPiece, CheckPieceId> {
    @Query("SELECT c FROM CheckPiece c WHERE c.demande.demandeur = :demandeur")
    List<CheckPiece> findByDemandeur(@Param("demandeur") Demandeur demandeur);
    @Query("SELECT c FROM CheckPiece c WHERE c.piece.id = :idPiece")
    List<CheckPiece> findByIdPiece(@Param("idPiece") String idPiece);
    List<CheckPiece> findByEstFourni(Boolean estFourni);
    List<CheckPiece> findByUpdatedAtBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT c FROM CheckPiece c WHERE c.demande.id = :idDemande")
    List<CheckPiece> findByDemandeId(@Param("idDemande") String idDemande);
}
