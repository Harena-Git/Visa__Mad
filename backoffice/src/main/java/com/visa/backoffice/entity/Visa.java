package com.visa.backoffice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visa")
public class Visa {
    @Id
    @Column(name = "id_visa")
    private String id;

    @Column(name = "ref_visa", nullable = false, unique = true, length = 50)
    private String refVisa;

    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDateTime dateFin;

    @ManyToOne
    @JoinColumn(name = "id_passport", referencedColumnName = "id_passport", nullable = false)
    private Passport passport;

    @ManyToOne
    @JoinColumn(name = "id_demande", referencedColumnName = "id_demande", nullable = false)
    private Demande demande;

    public Visa() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefVisa() {
        return refVisa;
    }

    public void setRefVisa(String refVisa) {
        this.refVisa = refVisa;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }
}
