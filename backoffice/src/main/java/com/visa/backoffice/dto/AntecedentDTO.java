package com.visa.backoffice.dto;

import com.visa.backoffice.entity.*;
import java.util.List;

public class AntecedentDTO {
    private Demandeur demandeur;
    private Passport passport;
    private List<Visa> visas;
    private List<CarteResidence> cartesResidence;
    private List<VisaTransformable> visasTransformables;
    private List<Demande> demandes;
    private Demande demandeCreee;
    private Demande demandeInjection;
    private boolean usagerTrouve;

    public AntecedentDTO() {}

    public Demandeur getDemandeur() { return demandeur; }
    public void setDemandeur(Demandeur demandeur) { this.demandeur = demandeur; }

    public Passport getPassport() { return passport; }
    public void setPassport(Passport passport) { this.passport = passport; }

    public List<Visa> getVisas() { return visas; }
    public void setVisas(List<Visa> visas) { this.visas = visas; }

    public List<CarteResidence> getCartesResidence() { return cartesResidence; }
    public void setCartesResidence(List<CarteResidence> cartesResidence) { this.cartesResidence = cartesResidence; }

    public List<VisaTransformable> getVisasTransformables() { return visasTransformables; }
    public void setVisasTransformables(List<VisaTransformable> visasTransformables) { this.visasTransformables = visasTransformables; }

    public List<Demande> getDemandes() { return demandes; }
    public void setDemandes(List<Demande> demandes) { this.demandes = demandes; }

    public Demande getDemandeCreee() { return demandeCreee; }
    public void setDemandeCreee(Demande demandeCreee) { this.demandeCreee = demandeCreee; }

    public Demande getDemandeInjection() { return demandeInjection; }
    public void setDemandeInjection(Demande demandeInjection) { this.demandeInjection = demandeInjection; }

    public boolean isUsagerTrouve() { return usagerTrouve; }
    public void setUsagerTrouve(boolean usagerTrouve) { this.usagerTrouve = usagerTrouve; }
}
