package com.visa.backoffice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categorie_demande")
public class CategorieDemande {
    @Id
    @Column(name = "id_categorie")
    private String id;

    @Column(name = "libelle", nullable = false, length = 50)
    private String libelle;

    @JsonIgnore
    @OneToMany(mappedBy = "categorie")
    private List<Demande> demandes;

    public CategorieDemande() {}

    public CategorieDemande(String id, String libelle) {
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

    public List<Demande> getDemandes() {
        return demandes;
    }

    public void setDemandes(List<Demande> demandes) {
        this.demandes = demandes;
    }
}
