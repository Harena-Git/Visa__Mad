package com.visa.backoffice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CheckPieceId implements Serializable {
    
    @Column(name = "id_demande")
    private String idDemande;
    
    @Column(name = "id_piece")
    private String idPiece;

    public CheckPieceId() {}

    public CheckPieceId(String idDemande, String idPiece) {
        this.idDemande = idDemande;
        this.idPiece = idPiece;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public String getIdPiece() {
        return idPiece;
    }

    public void setIdPiece(String idPiece) {
        this.idPiece = idPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckPieceId that = (CheckPieceId) o;
        return Objects.equals(idDemande, that.idDemande) && 
               Objects.equals(idPiece, that.idPiece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDemande, idPiece);
    }
}
