package com.visa.backoffice.repository;

import com.visa.backoffice.entity.Piece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PieceRepository extends JpaRepository<Piece, String> {
    List<Piece> findByLibelleContainingIgnoreCase(String libelle);
    List<Piece> findByEstObligatoire(Integer estObligatoire);
    @Query("SELECT p FROM Piece p WHERE p.typeVisa.id = :idTypeVisa")
    List<Piece> findByIdTypeVisa(@Param("idTypeVisa") String idTypeVisa);
}
