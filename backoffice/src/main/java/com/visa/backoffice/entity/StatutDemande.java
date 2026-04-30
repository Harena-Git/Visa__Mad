package com.visa.backoffice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "statut_demande")
public class StatutDemande {
    @Id
    @Column(name = "id_statut_demande")
    private String id;

    @Column(name = "date_", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "id_statut", referencedColumnName = "id_statut")
    private Statut statut;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_demande", referencedColumnName = "id_demande")
    private Demande demande;

    public StatutDemande() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }
}
