package com.visa.backoffice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "champs")
public class Champs {
    @Id
    @Column(name = "id_champs")
    private String id;

    @Column(name = "libelle", nullable = false, length = 150)
    private String libelle;

    @Column(name = "est_obligatoire", nullable = false)
    private Integer estObligatoire;

    public Champs() {}

    public Champs(String id, String libelle, Integer estObligatoire) {
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
}
