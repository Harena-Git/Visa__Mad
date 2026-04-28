package com.visa.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/demandeurs")
    public String demandeursPage() {
        return "forward:/demandeurs.html";
    }

    @GetMapping("/demandes")
    public String demandesPage() {
        return "forward:/demandes.html";
    }

    @GetMapping("/visas")
    public String visasPage() {
        return "forward:/visas.html";
    }

    @GetMapping("/visas-transformables")
    public String visasTransformablesPage() {
        return "forward:/visas-transformables.html";
    }

    @GetMapping("/cartes-residence")
    public String cartesResidencePage() {
        return "forward:/cartes-residence.html";
    }

    @GetMapping("/antecedents")
    public String antecedentsPage() {
        return "forward:/antecedents.html";
    }
}
