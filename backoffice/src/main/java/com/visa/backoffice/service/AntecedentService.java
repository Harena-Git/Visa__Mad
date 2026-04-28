package com.visa.backoffice.service;

import com.visa.backoffice.dto.AntecedentDTO;
import com.visa.backoffice.dto.CasProblematiqueRequest;
import com.visa.backoffice.entity.*;
import com.visa.backoffice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AntecedentService {

    @Autowired private PassportRepository passportRepository;
    @Autowired private DemandeurRepository demandeurRepository;
    @Autowired private DemandeRepository demandeRepository;
    @Autowired private VisaRepository visaRepository;
    @Autowired private CarteResidenceRepository carteResidenceRepository;
    @Autowired private VisaTransformableRepository visaTransformableRepository;
    @Autowired private CategorieDemandeRepository categorieDemandeRepository;
    @Autowired private StatutRepository statutRepository;
    @Autowired private StatutDemandeRepository statutDemandeRepository;
    @Autowired private NationaliteRepository nationaliteRepository;
    @Autowired private SituationFamilleRepository situationFamilleRepository;

    /**
     * Recherche un usager par numéro de passeport et retourne toutes ses données.
     */
    public AntecedentDTO rechercherParPassport(String numeroPassport) {
        AntecedentDTO dto = new AntecedentDTO();

        Optional<Passport> passportOpt = passportRepository.findByNumero(numeroPassport);

        if (passportOpt.isEmpty()) {
            dto.setUsagerTrouve(false);
            return dto;
        }

        Passport passport = passportOpt.get();
        Demandeur demandeur = passport.getDemandeur();

        dto.setUsagerTrouve(true);
        dto.setDemandeur(demandeur);
        dto.setPassport(passport);

        // Récupérer les demandes
        List<Demande> demandes = demandeRepository.findByDemandeurId(demandeur.getId());
        dto.setDemandes(demandes);

        // Récupérer les visas via le passeport
        List<Visa> visas = visaRepository.findByIdPassport(passport.getId());
        dto.setVisas(visas);

        // Récupérer les cartes de résidence via le passeport
        List<CarteResidence> cartes = carteResidenceRepository.findByIdPassport(passport.getId());
        dto.setCartesResidence(cartes);

        // Récupérer les visas transformables
        List<VisaTransformable> vts = visaTransformableRepository.findByIdDemandeur(demandeur.getId());
        dto.setVisasTransformables(vts);

        return dto;
    }

    /**
     * Cas Normal : l'usager existe en base, on crée juste la demande Duplicata/Transfert.
     */
    public AntecedentDTO traiterCasNormal(String numeroPassport, String typeDemande, String idTypeVisa) {
        // D'abord rechercher l'usager
        AntecedentDTO dto = rechercherParPassport(numeroPassport);

        if (!dto.isUsagerTrouve()) {
            throw new IllegalArgumentException("Usager non trouvé avec le passeport: " + numeroPassport);
        }

        // Déterminer la catégorie
        String idCategorie = "Duplicata".equalsIgnoreCase(typeDemande) ? "CAT004" : "CAT005";

        // Trouver la catégorie et le type visa
        CategorieDemande categorie = categorieDemandeRepository.findById(idCategorie)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée: " + idCategorie));
        TypeVisa typeVisa = findOrDefaultTypeVisa(idTypeVisa);

        // Trouver la dernière demande existante pour la lier
        String idDemandeLiee = null;
        if (dto.getDemandes() != null && !dto.getDemandes().isEmpty()) {
            idDemandeLiee = dto.getDemandes().get(dto.getDemandes().size() - 1).getId();
        }

        // Créer la demande
        Demande nouvelleDemande = new Demande();
        nouvelleDemande.setId("REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        nouvelleDemande.setCreatedAt(LocalDate.now());
        nouvelleDemande.setUpdatedAt(LocalDate.now());
        nouvelleDemande.setCategorie(categorie);
        nouvelleDemande.setTypeVisa(typeVisa);
        nouvelleDemande.setDemandeur(dto.getDemandeur());
        nouvelleDemande.setIdDemande1(idDemandeLiee);

        nouvelleDemande = demandeRepository.save(nouvelleDemande);

        // Créer le statut initial "En attente"
        creerStatutDemande(nouvelleDemande, "ST001");

        dto.setDemandeCreee(nouvelleDemande);
        return dto;
    }

    /**
     * Cas Problématique : l'usager n'existe pas en base.
     * 1) Créer le Demandeur + Passport
     * 2) Créer une demande "Nouveau Titre (Injection)" avec statut "Visa Approuvé"
     * 3) Créer un VisaTransformable pour l'historique papier
     * 4) Créer la demande Duplicata/Transfert liée
     */
    public AntecedentDTO traiterCasProblematique(CasProblematiqueRequest request) {
        AntecedentDTO dto = new AntecedentDTO();
        dto.setUsagerTrouve(false);

        // 1. Créer le Demandeur
        Demandeur demandeur = new Demandeur();
        demandeur.setId("DEM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        demandeur.setNom(request.getNom());
        demandeur.setPrenom(request.getPrenom());
        demandeur.setNomJeuneFille(request.getNomJeuneFille());
        demandeur.setDtn(request.getDtn());
        demandeur.setAdresseMada(request.getAdresseMada());
        demandeur.setTelephone(request.getTelephone());
        demandeur.setEmail(request.getEmail());
        demandeur.setCreatedAt(LocalDate.now());
        demandeur.setUpdatedAt(LocalDate.now());

        Nationalite nationalite = nationaliteRepository.findById(request.getIdNationalite())
                .orElseThrow(() -> new RuntimeException("Nationalité non trouvée"));
        SituationFamille situation = situationFamilleRepository.findById(request.getIdSituationFamille())
                .orElseThrow(() -> new RuntimeException("Situation famille non trouvée"));
        demandeur.setNationalite(nationalite);
        demandeur.setSituationFamille(situation);

        demandeur = demandeurRepository.save(demandeur);
        dto.setDemandeur(demandeur);

        // 2. Créer le Passport
        Passport passport = new Passport();
        passport.setId("PASS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        passport.setNumero(request.getNumeroPassport());
        passport.setDelivreLe(request.getPassportDelivreLe());
        passport.setExpireLe(request.getPassportExpireLe());
        passport.setDemandeur(demandeur);

        passport = passportRepository.save(passport);
        dto.setPassport(passport);

        // 3. Créer la demande "Nouveau Titre (Injection)" avec statut "Visa Approuvé"
        CategorieDemande catInjection = categorieDemandeRepository.findById("CAT006")
                .orElseThrow(() -> new RuntimeException("Catégorie 'Nouveau Titre (Injection)' non trouvée"));
        TypeVisa typeVisa = findOrDefaultTypeVisa(request.getIdTypeVisa());

        Demande demandeInjection = new Demande();
        demandeInjection.setId("REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        demandeInjection.setCreatedAt(LocalDate.now());
        demandeInjection.setUpdatedAt(LocalDate.now());
        demandeInjection.setCategorie(catInjection);
        demandeInjection.setTypeVisa(typeVisa);
        demandeInjection.setDemandeur(demandeur);

        demandeInjection = demandeRepository.save(demandeInjection);

        // Statut immédiat: Visa Approuvé
        creerStatutDemande(demandeInjection, "ST011");
        dto.setDemandeInjection(demandeInjection);

        // 4. Créer le VisaTransformable pour l'historique papier
        if (request.getRefVisaExistant() != null && !request.getRefVisaExistant().isEmpty()) {
            VisaTransformable vt = new VisaTransformable();
            vt.setId("VT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            vt.setRefVisa(request.getRefVisaExistant());
            vt.setDateDebut(request.getVisaDateDebut());
            vt.setDateFin(request.getVisaDateFin());
            vt.setPassport(passport);
            vt.setDemandeur(demandeur);
            vt.setTypeTransformable("HISTORIQUE");

            vt = visaTransformableRepository.save(vt);
            List<VisaTransformable> vts = new ArrayList<>();
            vts.add(vt);
            dto.setVisasTransformables(vts);
        }

        // 5. Créer la demande Duplicata/Transfert liée à la demande d'injection
        String idCatDemande = "Duplicata".equalsIgnoreCase(request.getTypeDemande()) ? "CAT004" : "CAT005";
        CategorieDemande catDemande = categorieDemandeRepository.findById(idCatDemande)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée: " + idCatDemande));

        Demande demandeDupTransfert = new Demande();
        demandeDupTransfert.setId("REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        demandeDupTransfert.setCreatedAt(LocalDate.now());
        demandeDupTransfert.setUpdatedAt(LocalDate.now());
        demandeDupTransfert.setCategorie(catDemande);
        demandeDupTransfert.setTypeVisa(typeVisa);
        demandeDupTransfert.setDemandeur(demandeur);
        demandeDupTransfert.setIdDemande1(demandeInjection.getId());

        demandeDupTransfert = demandeRepository.save(demandeDupTransfert);

        // Statut initial: En attente
        creerStatutDemande(demandeDupTransfert, "ST001");
        dto.setDemandeCreee(demandeDupTransfert);

        List<Demande> demandes = new ArrayList<>();
        demandes.add(demandeInjection);
        demandes.add(demandeDupTransfert);
        dto.setDemandes(demandes);

        return dto;
    }

    private void creerStatutDemande(Demande demande, String idStatut) {
        Statut statut = statutRepository.findById(idStatut)
                .orElseThrow(() -> new RuntimeException("Statut non trouvé: " + idStatut));

        StatutDemande sd = new StatutDemande();
        sd.setId("SD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        sd.setDate(LocalDate.now());
        sd.setStatut(statut);
        sd.setDemande(demande);

        statutDemandeRepository.save(sd);
    }

    private TypeVisa findOrDefaultTypeVisa(String idTypeVisa) {
        if (idTypeVisa != null && !idTypeVisa.isEmpty()) {
            return new TypeVisa() {{ setId(idTypeVisa); }};
        }
        // Default to first type visa
        return new TypeVisa() {{ setId("TV001"); }};
    }
}
