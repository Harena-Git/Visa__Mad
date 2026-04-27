package com.visa.backoffice.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "visa_transformable")
public class VisaTransformable {
    @Id
    @Column(name = "id_visa_transformable")
    private String id;

    @Column(name = "ref_visa", nullable = false, unique = true, length = 50)
    private String refVisa;

    /** ETUDIANT, TRAVAILLEUR, MISSIONNAIRE */
    @Column(name = "type_transformable", nullable = false, length = 30)
    private String typeTransformable;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @ManyToOne
    @JoinColumn(name = "id_passport", referencedColumnName = "id_passport", nullable = false)
    private Passport passport;

    @ManyToOne
    @JoinColumn(name = "id_demandeur", referencedColumnName = "id_demandeur", nullable = false)
    private Demandeur demandeur;

    /** Visa normal d'origine dont la durée n'est pas encore expirée */
    @ManyToOne
    @JoinColumn(name = "id_visa_original", referencedColumnName = "id_visa")
    private Visa visaOriginal;

    public VisaTransformable() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRefVisa() { return refVisa; }
    public void setRefVisa(String refVisa) { this.refVisa = refVisa; }

    public String getTypeTransformable() { return typeTransformable; }
    public void setTypeTransformable(String typeTransformable) { this.typeTransformable = typeTransformable; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public Passport getPassport() { return passport; }
    public void setPassport(Passport passport) { this.passport = passport; }

    public Demandeur getDemandeur() { return demandeur; }
    public void setDemandeur(Demandeur demandeur) { this.demandeur = demandeur; }

    public Visa getVisaOriginal() { return visaOriginal; }
    public void setVisaOriginal(Visa visaOriginal) { this.visaOriginal = visaOriginal; }
}
