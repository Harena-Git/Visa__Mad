package com.visa.backoffice.dto;

import java.time.LocalDate;

/**
 * DTO for the "Cas Problématique" — when the user has no prior data in the system.
 * Contains all the information needed to inject a new demandeur + passport + visa_transformable.
 */
public class CasProblematiqueRequest {
    // Demandeur info
    private String nom;
    private String prenom;
    private String nomJeuneFille;
    private LocalDate dtn;
    private String adresseMada;
    private String telephone;
    private String email;
    private String idNationalite;
    private String idSituationFamille;

    // Passport info
    private String numeroPassport;
    private LocalDate passportDelivreLe;
    private LocalDate passportExpireLe;

    // Visa transformable (historique papier)
    private String refVisaExistant;
    private LocalDate visaDateDebut;
    private LocalDate visaDateFin;

    // Type of demand to create
    private String typeDemande; // "Duplicata" or "Transfert"
    private String idTypeVisa;

    public CasProblematiqueRequest() {}

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNomJeuneFille() { return nomJeuneFille; }
    public void setNomJeuneFille(String nomJeuneFille) { this.nomJeuneFille = nomJeuneFille; }

    public LocalDate getDtn() { return dtn; }
    public void setDtn(LocalDate dtn) { this.dtn = dtn; }

    public String getAdresseMada() { return adresseMada; }
    public void setAdresseMada(String adresseMada) { this.adresseMada = adresseMada; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getIdNationalite() { return idNationalite; }
    public void setIdNationalite(String idNationalite) { this.idNationalite = idNationalite; }

    public String getIdSituationFamille() { return idSituationFamille; }
    public void setIdSituationFamille(String idSituationFamille) { this.idSituationFamille = idSituationFamille; }

    public String getNumeroPassport() { return numeroPassport; }
    public void setNumeroPassport(String numeroPassport) { this.numeroPassport = numeroPassport; }

    public LocalDate getPassportDelivreLe() { return passportDelivreLe; }
    public void setPassportDelivreLe(LocalDate passportDelivreLe) { this.passportDelivreLe = passportDelivreLe; }

    public LocalDate getPassportExpireLe() { return passportExpireLe; }
    public void setPassportExpireLe(LocalDate passportExpireLe) { this.passportExpireLe = passportExpireLe; }

    public String getRefVisaExistant() { return refVisaExistant; }
    public void setRefVisaExistant(String refVisaExistant) { this.refVisaExistant = refVisaExistant; }

    public LocalDate getVisaDateDebut() { return visaDateDebut; }
    public void setVisaDateDebut(LocalDate visaDateDebut) { this.visaDateDebut = visaDateDebut; }

    public LocalDate getVisaDateFin() { return visaDateFin; }
    public void setVisaDateFin(LocalDate visaDateFin) { this.visaDateFin = visaDateFin; }

    public String getTypeDemande() { return typeDemande; }
    public void setTypeDemande(String typeDemande) { this.typeDemande = typeDemande; }

    public String getIdTypeVisa() { return idTypeVisa; }
    public void setIdTypeVisa(String idTypeVisa) { this.idTypeVisa = idTypeVisa; }
}
