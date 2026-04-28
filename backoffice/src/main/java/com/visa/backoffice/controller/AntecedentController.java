package com.visa.backoffice.controller;

import com.visa.backoffice.dto.AntecedentDTO;
import com.visa.backoffice.dto.CasProblematiqueRequest;
import com.visa.backoffice.service.AntecedentService;
import com.visa.backoffice.service.AttestationPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/antecedents")
@CrossOrigin(origins = "*")
public class AntecedentController {

    @Autowired
    private AntecedentService antecedentService;

    @Autowired
    private AttestationPdfService attestationPdfService;

    /**
     * Recherche un usager par numéro de passeport.
     */
    @GetMapping("/recherche")
    public ResponseEntity<AntecedentDTO> rechercherParPassport(@RequestParam String numero) {
        try {
            AntecedentDTO dto = antecedentService.rechercherParPassport(numero);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Cas Normal : l'usager existe, on crée la demande Duplicata/Transfert.
     */
    @PostMapping("/cas-normal")
    public ResponseEntity<AntecedentDTO> traiterCasNormal(@RequestBody Map<String, String> request) {
        try {
            String numeroPassport = request.get("numeroPassport");
            String typeDemande = request.get("typeDemande");
            String idTypeVisa = request.get("idTypeVisa");

            AntecedentDTO dto = antecedentService.traiterCasNormal(numeroPassport, typeDemande, idTypeVisa);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Cas Problématique : l'usager n'existe pas, on injecte puis crée la demande.
     */
    @PostMapping("/cas-problematique")
    public ResponseEntity<AntecedentDTO> traiterCasProblematique(@RequestBody CasProblematiqueRequest request) {
        try {
            AntecedentDTO dto = antecedentService.traiterCasProblematique(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Génère le PDF "Attestation récépissé dossier" pour une demande.
     */
    @GetMapping("/attestation/{idDemande}")
    public ResponseEntity<byte[]> genererAttestation(@PathVariable String idDemande) {
        try {
            byte[] pdfBytes = attestationPdfService.genererAttestation(idDemande);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "attestation_recepisse_" + idDemande + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
