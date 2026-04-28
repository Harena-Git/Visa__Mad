package com.visa.backoffice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "statut")
public class Statut {
    @Id
    @Column(name = "id_statut")
    private String id;

    @Column(name = "libelle", nullable = false, length = 150)
    private String libelle;

    @JsonIgnore
    @OneToMany(mappedBy = "statut")
    private List<StatutDemande> statutDemandes;

    public Statut() {}

    public Statut(String id, String libelle) {
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

    public List<StatutDemande> getStatutDemandes() {
        return statutDemandes;
    }

    public void setStatutDemandes(List<StatutDemande> statutDemandes) {
        this.statutDemandes = statutDemandes;
    }
}
