package com.visa.backoffice.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "carte_residence")
public class CarteResidence {
    @Id
    @Column(name = "id_carte_residence")
    private String id;

    @Column(name = "ref_carte_residence", unique = true, length = 50)
    private String refCarteResidence;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @ManyToOne
    @JoinColumn(name = "id_passport", referencedColumnName = "id_passport", nullable = false)
    private Passport passport;

    @ManyToOne
    @JoinColumn(name = "id_demande", referencedColumnName = "id_demande", nullable = false)
    private Demande demande;

    public CarteResidence() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefCarteResidence() {
        return refCarteResidence;
    }

    public void setRefCarteResidence(String refCarteResidence) {
        this.refCarteResidence = refCarteResidence;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
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
