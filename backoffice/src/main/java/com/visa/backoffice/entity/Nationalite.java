package com.visa.backoffice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "nationalite")
public class Nationalite {
    @Id
    @Column(name = "id_nationalite")
    private String id;

    @Column(name = "libelle", nullable = false, length = 150)
    private String libelle;

    @JsonIgnore
    @OneToMany(mappedBy = "nationalite")
    private List<Demandeur> demandeurs;

    public Nationalite() {}

    public Nationalite(String id, String libelle) {
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

    public List<Demandeur> getDemandeurs() {
        return demandeurs;
    }

    public void setDemandeurs(List<Demandeur> demandeurs) {
        this.demandeurs = demandeurs;
    }
}
