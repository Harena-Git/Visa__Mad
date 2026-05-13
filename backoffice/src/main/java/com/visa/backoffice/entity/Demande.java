package com.visa.backoffice.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "demande")
public class Demande {
    @Id
    @Column(name = "id_demande")
    private String id;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "id_demande_1")
    private String idDemande1;

    @ManyToOne
    @JoinColumn(name = "id_categorie", referencedColumnName = "id_categorie", nullable = false)
    private CategorieDemande categorie;

    @ManyToOne
    @JoinColumn(name = "id_type_visa", referencedColumnName = "id_type_visa", nullable = false)
    private TypeVisa typeVisa;

    @ManyToOne
    @JoinColumn(name = "id_demandeur", referencedColumnName = "id_demandeur", nullable = false)
    private Demandeur demandeur;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_demande_1", referencedColumnName = "id_demande", insertable = false, updatable = false)
    private Demande demandeLiee;

    @JsonIgnore
    @OneToMany(mappedBy = "demande")
    private List<CarteResidence> carteResidences;

    @JsonIgnore
    @OneToMany(mappedBy = "demande")
    private List<Visa> visas;

    @OneToMany(mappedBy = "demande", fetch = FetchType.EAGER)
    private List<StatutDemande> statutDemandes;

    @OneToMany(mappedBy = "demande", fetch = FetchType.EAGER)
    private List<CheckPiece> checkPieces;

    @Column(name = "tracking_token", unique = true, nullable = true, length = 255)
    private String trackingToken;

    @Column(name = "photo_path")
    private String photoPath;

    @Column(name = "signature_path")
    private String signaturePath;

    public Demande() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIdDemande1() {
        return idDemande1;
    }

    public void setIdDemande1(String idDemande1) {
        this.idDemande1 = idDemande1;
    }

    public CategorieDemande getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieDemande categorie) {
        this.categorie = categorie;
    }

    public TypeVisa getTypeVisa() {
        return typeVisa;
    }

    public void setTypeVisa(TypeVisa typeVisa) {
        this.typeVisa = typeVisa;
    }

    public Demandeur getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(Demandeur demandeur) {
        this.demandeur = demandeur;
    }

    public Demande getDemandeLiee() {
        return demandeLiee;
    }

    public void setDemandeLiee(Demande demandeLiee) {
        this.demandeLiee = demandeLiee;
    }

    public List<CarteResidence> getCarteResidences() {
        return carteResidences;
    }

    public void setCarteResidences(List<CarteResidence> carteResidences) {
        this.carteResidences = carteResidences;
    }


    public List<Visa> getVisas() {
        return visas;
    }

    public void setVisas(List<Visa> visas) {
        this.visas = visas;
    }

    public List<StatutDemande> getStatutDemandes() {
        return statutDemandes;
    }

    public void setStatutDemandes(List<StatutDemande> statutDemandes) {
        this.statutDemandes = statutDemandes;
    }

    public List<CheckPiece> getCheckPieces() {
        return checkPieces;
    }

    public void setCheckPieces(List<CheckPiece> checkPieces) {
        this.checkPieces = checkPieces;
    }

    public String getTrackingToken() {
        return trackingToken;
    }

    public void setTrackingToken(String trackingToken) {
        this.trackingToken = trackingToken;
    }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
    public String getSignaturePath() { return signaturePath; }
    public void setSignaturePath(String signaturePath) { this.signaturePath = signaturePath; }
}
