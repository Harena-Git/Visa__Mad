package com.visa.backoffice.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "check_piece")
public class CheckPiece {
    @EmbeddedId
    private CheckPieceId id;

    @Column(name = "est_fourni")
    private Boolean estFourni;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "id_demande", referencedColumnName = "id_demande", insertable = false, updatable = false)
    private Demande demande;

    @ManyToOne
    @JoinColumn(name = "id_piece", referencedColumnName = "id_piece", insertable = false, updatable = false)
    private Piece piece;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    public CheckPiece() {}

    public CheckPieceId getId() {
        return id;
    }

    public void setId(CheckPieceId id) {
        this.id = id;
    }

    public Boolean getEstFourni() {
        return estFourni;
    }

    public void setEstFourni(Boolean estFourni) {
        this.estFourni = estFourni;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
