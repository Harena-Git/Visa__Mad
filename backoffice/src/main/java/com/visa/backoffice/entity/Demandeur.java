package com.visa.backoffice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "demandeur")
public class Demandeur {
    @Id
    @Column(name = "id_demandeur")
    private String id;

    @Column(name = "nom", length = 250)
    private String nom;

    @Column(name = "prenom", length = 250)
    private String prenom;

    @Column(name = "nom_jeune_fille", length = 50)
    private String nomJeuneFille;

    @Column(name = "dtn")
    private LocalDate dtn;

    @Column(name = "adresse_mada", length = 250)
    private String adresseMada;

    @Column(name = "telephone", length = 50)
    private String telephone;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "id_nationalite", referencedColumnName = "id_nationalite", nullable = false)
    private Nationalite nationalite;

    @ManyToOne
    @JoinColumn(name = "id_situation_famille", referencedColumnName = "id_situation_famille", nullable = false)
    private SituationFamille situationFamille;

    @JsonIgnore
    @OneToMany(mappedBy = "demandeur")
    private List<Passport> passports;

    @JsonIgnore
    @OneToMany(mappedBy = "demandeur")
    private List<Demande> demandes;

    @JsonIgnore
    @OneToMany(mappedBy = "demandeur")
    private List<VisaTransformable> visaTransformables;

    public Demandeur() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNomJeuneFille() {
        return nomJeuneFille;
    }

    public void setNomJeuneFille(String nomJeuneFille) {
        this.nomJeuneFille = nomJeuneFille;
    }

    public LocalDate getDtn() {
        return dtn;
    }

    public void setDtn(LocalDate dtn) {
        this.dtn = dtn;
    }

    public String getAdresseMada() {
        return adresseMada;
    }

    public void setAdresseMada(String adresseMada) {
        this.adresseMada = adresseMada;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Nationalite getNationalite() {
        return nationalite;
    }

    public void setNationalite(Nationalite nationalite) {
        this.nationalite = nationalite;
    }

    public SituationFamille getSituationFamille() {
        return situationFamille;
    }

    public void setSituationFamille(SituationFamille situationFamille) {
        this.situationFamille = situationFamille;
    }

    public List<Passport> getPassports() {
        return passports;
    }

    public void setPassports(List<Passport> passports) {
        this.passports = passports;
    }

    public List<Demande> getDemandes() {
        return demandes;
    }

    public void setDemandes(List<Demande> demandes) {
        this.demandes = demandes;
    }

    public List<VisaTransformable> getVisaTransformables() {
        return visaTransformables;
    }

    public void setVisaTransformables(List<VisaTransformable> visaTransformables) {
        this.visaTransformables = visaTransformables;
    }
}
