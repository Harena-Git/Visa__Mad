package com.visa.backoffice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "passport")
public class Passport {
    @Id
    @Column(name = "id_passport")
    private String id;

    @Column(name = "numero", nullable = false, unique = true, length = 150)
    private String numero;

    @Column(name = "delivre_le", nullable = false)
    private LocalDate delivreLe;

    @Column(name = "expire_le", nullable = false)
    private LocalDate expireLe;

    @ManyToOne
    @JoinColumn(name = "id_demandeur", referencedColumnName = "id_demandeur", nullable = false)
    private Demandeur demandeur;

    @JsonIgnore
    @OneToMany(mappedBy = "passport")
    private List<CarteResidence> carteResidences;

    @JsonIgnore
    @OneToMany(mappedBy = "passport")
    private List<VisaTransformable> visaTransformables;

    @JsonIgnore
    @OneToMany(mappedBy = "passport")
    private List<Visa> visas;

    public Passport() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDelivreLe() {
        return delivreLe;
    }

    public void setDelivreLe(LocalDate delivreLe) {
        this.delivreLe = delivreLe;
    }

    public LocalDate getExpireLe() {
        return expireLe;
    }

    public void setExpireLe(LocalDate expireLe) {
        this.expireLe = expireLe;
    }

    public Demandeur getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(Demandeur demandeur) {
        this.demandeur = demandeur;
    }

    public List<CarteResidence> getCarteResidences() {
        return carteResidences;
    }

    public void setCarteResidences(List<CarteResidence> carteResidences) {
        this.carteResidences = carteResidences;
    }

    public List<VisaTransformable> getVisaTransformables() {
        return visaTransformables;
    }

    public void setVisaTransformables(List<VisaTransformable> visaTransformables) {
        this.visaTransformables = visaTransformables;
    }

    public List<Visa> getVisas() {
        return visas;
    }

    public void setVisas(List<Visa> visas) {
        this.visas = visas;
    }
}
