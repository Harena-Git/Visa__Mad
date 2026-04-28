package com.visa.backoffice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "type_visa")
public class TypeVisa {
    @Id
    @Column(name = "id_type_visa")
    private String id;

    @Column(name = "libelle", nullable = false, length = 150)
    private String libelle;

    @JsonIgnore
    @OneToMany(mappedBy = "typeVisa")
    private List<Piece> pieces;

    @JsonIgnore
    @OneToMany(mappedBy = "typeVisa")
    private List<Demande> demandes;

    public TypeVisa() {}

    public TypeVisa(String id, String libelle) {
        this.id = id;
        this.libelle = libelle;
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

    public List<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(List<Piece> pieces) {
        this.pieces = pieces;
    }

    public List<Demande> getDemandes() {
        return demandes;
    }

    public void setDemandes(List<Demande> demandes) {
        this.demandes = demandes;
    }
}
