package com.visa.backoffice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "piece")
public class Piece {
    @Id
    @Column(name = "id_piece")
    private String id;

    @Column(name = "libelle", nullable = false, length = 50)
    private String libelle;

    @Column(name = "est_obligatoire", nullable = false)
    private Integer estObligatoire;

    @ManyToOne
    @JoinColumn(name = "id_type_visa", referencedColumnName = "id_type_visa")
    private TypeVisa typeVisa;

    @JsonIgnore
    @OneToMany(mappedBy = "piece")
    private List<CheckPiece> checkPieces;

    public Piece() {}

    public Piece(String id, String libelle, Integer estObligatoire) {
        this.id = id;
        this.libelle = libelle;
        this.estObligatoire = estObligatoire;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getEstObligatoire() {
        return estObligatoire;
    }

    public void setEstObligatoire(Integer estObligatoire) {
        this.estObligatoire = estObligatoire;
    }

    public TypeVisa getTypeVisa() {
        return typeVisa;
    }

    public void setTypeVisa(TypeVisa typeVisa) {
        this.typeVisa = typeVisa;
    }

    public List<CheckPiece> getCheckPieces() {
        return checkPieces;
    }

    public void setCheckPieces(List<CheckPiece> checkPieces) {
        this.checkPieces = checkPieces;
    }
}
