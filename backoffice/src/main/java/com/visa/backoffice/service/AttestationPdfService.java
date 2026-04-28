package com.visa.backoffice.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.visa.backoffice.entity.*;
import com.visa.backoffice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AttestationPdfService {

    @Autowired private DemandeRepository demandeRepository;
    @Autowired private VisaRepository visaRepository;
    @Autowired private CarteResidenceRepository carteResidenceRepository;
    @Autowired private VisaTransformableRepository visaTransformableRepository;
    @Autowired private CheckPieceRepository checkPieceRepository;
    @Autowired private StatutDemandeRepository statutDemandeRepository;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] genererAttestation(String idDemande) throws Exception {
        // Récupérer la demande
        Demande demande = demandeRepository.findById(idDemande)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée: " + idDemande));

        Demandeur demandeur = demande.getDemandeur();
        Passport passport = null;

        // Trouver le passeport du demandeur
        List<Visa> visas = visaRepository.findByIdPassport("");
        // Chercher par demandeur
        List<CarteResidence> cartes = carteResidenceRepository.findByDemandeur(demandeur);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, baos);

        document.open();

        // ========== EN-TÊTE ==========
        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD, new Color(0, 51, 102));
        Font subtitleFont = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(0, 51, 102));
        Font headerFont = new Font(Font.HELVETICA, 11, Font.BOLD, new Color(51, 51, 51));
        Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL, new Color(51, 51, 51));
        Font smallFont = new Font(Font.HELVETICA, 8, Font.ITALIC, new Color(128, 128, 128));

        // Titre de la république
        Paragraph repTitre = new Paragraph("RÉPUBLIQUE DE MADAGASCAR", titleFont);
        repTitre.setAlignment(Element.ALIGN_CENTER);
        document.add(repTitre);

        Paragraph minTitre = new Paragraph("Ministère de l'Intérieur et de la Décentralisation", subtitleFont);
        minTitre.setAlignment(Element.ALIGN_CENTER);
        document.add(minTitre);

        document.add(new Paragraph("\n"));

        // Ligne de séparation
        PdfPTable separateur = new PdfPTable(1);
        separateur.setWidthPercentage(100);
        PdfPCell cellSep = new PdfPCell();
        cellSep.setBorderWidthBottom(2f);
        cellSep.setBorderColorBottom(new Color(0, 51, 102));
        cellSep.setBorderWidthTop(0);
        cellSep.setBorderWidthLeft(0);
        cellSep.setBorderWidthRight(0);
        cellSep.setFixedHeight(5);
        separateur.addCell(cellSep);
        document.add(separateur);

        document.add(new Paragraph("\n"));

        // Titre du document
        Paragraph docTitre = new Paragraph("ATTESTATION RÉCÉPISSÉ DE DOSSIER", new Font(Font.HELVETICA, 14, Font.BOLD, new Color(0, 102, 51)));
        docTitre.setAlignment(Element.ALIGN_CENTER);
        document.add(docTitre);

        Paragraph refParagraph = new Paragraph("Réf: " + demande.getId() + " — Date: " + demande.getCreatedAt().format(DATE_FORMAT), normalFont);
        refParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(refParagraph);

        document.add(new Paragraph("\n"));

        // ========== SECTION ÉTAT CIVIL ==========
        document.add(createSectionTitle("1. ÉTAT CIVIL", headerFont));
        PdfPTable etatCivil = createInfoTable();
        addInfoRow(etatCivil, "Nom", demandeur.getNom() != null ? demandeur.getNom() : "", normalFont);
        addInfoRow(etatCivil, "Prénom", demandeur.getPrenom() != null ? demandeur.getPrenom() : "", normalFont);
        addInfoRow(etatCivil, "Nom de jeune fille", demandeur.getNomJeuneFille() != null ? demandeur.getNomJeuneFille() : "", normalFont);
        addInfoRow(etatCivil, "Date de naissance", demandeur.getDtn() != null ? demandeur.getDtn().format(DATE_FORMAT) : "", normalFont);
        addInfoRow(etatCivil, "Nationalité", demandeur.getNationalite() != null ? demandeur.getNationalite().getLibelle() : "", normalFont);
        addInfoRow(etatCivil, "Situation familiale", demandeur.getSituationFamille() != null ? demandeur.getSituationFamille().getLibelle() : "", normalFont);
        addInfoRow(etatCivil, "Adresse", demandeur.getAdresseMada() != null ? demandeur.getAdresseMada() : "", normalFont);
        addInfoRow(etatCivil, "Téléphone", demandeur.getTelephone() != null ? demandeur.getTelephone() : "", normalFont);
        addInfoRow(etatCivil, "Email", demandeur.getEmail() != null ? demandeur.getEmail() : "", normalFont);
        document.add(etatCivil);
        document.add(new Paragraph("\n"));

        // ========== SECTION PASSEPORT ==========
        // Rechercher le passeport via les visas ou cartes résidence
        document.add(createSectionTitle("2. INFORMATIONS PASSEPORT", headerFont));
        PdfPTable passeportTable = createInfoTable();
        // On cherche les passeports du demandeur
        // Pour l'instant, on affiche via la demande liée
        if (demande.getIdDemande1() != null) {
            addInfoRow(passeportTable, "Demande liée", demande.getIdDemande1(), normalFont);
        }
        addInfoRow(passeportTable, "Type de visa", demande.getTypeVisa() != null ? demande.getTypeVisa().getLibelle() : "", normalFont);
        addInfoRow(passeportTable, "Catégorie", demande.getCategorie() != null ? demande.getCategorie().getLibelle() : "", normalFont);
        document.add(passeportTable);
        document.add(new Paragraph("\n"));

        // ========== SECTION DEMANDE ==========
        document.add(createSectionTitle("3. INFORMATIONS DE LA DEMANDE", headerFont));
        PdfPTable demandeTable = createInfoTable();
        addInfoRow(demandeTable, "ID Demande", demande.getId(), normalFont);
        addInfoRow(demandeTable, "Catégorie", demande.getCategorie() != null ? demande.getCategorie().getLibelle() : "", normalFont);
        addInfoRow(demandeTable, "Type de visa", demande.getTypeVisa() != null ? demande.getTypeVisa().getLibelle() : "", normalFont);
        addInfoRow(demandeTable, "Date de création", demande.getCreatedAt() != null ? demande.getCreatedAt().format(DATE_FORMAT) : "", normalFont);
        addInfoRow(demandeTable, "Date de MAJ", demande.getUpdatedAt() != null ? demande.getUpdatedAt().format(DATE_FORMAT) : "", normalFont);
        document.add(demandeTable);
        document.add(new Paragraph("\n"));

        // ========== SECTION PIÈCES FOURNIES ==========
        document.add(createSectionTitle("4. PIÈCES JUSTIFICATIVES REÇUES", headerFont));
        List<CheckPiece> pieces = checkPieceRepository.findByDemandeId(idDemande);
        if (pieces != null && !pieces.isEmpty()) {
            PdfPTable piecesTable = new PdfPTable(3);
            piecesTable.setWidthPercentage(100);
            piecesTable.setWidths(new float[]{3, 4, 2});

            // En-tête du tableau
            addTableHeader(piecesTable, "ID Pièce", headerFont);
            addTableHeader(piecesTable, "Libellé", headerFont);
            addTableHeader(piecesTable, "Fourni", headerFont);

            for (CheckPiece cp : pieces) {
                addTableCell(piecesTable, cp.getPiece() != null ? cp.getPiece().getId() : "", normalFont);
                addTableCell(piecesTable, cp.getPiece() != null ? cp.getPiece().getLibelle() : "", normalFont);
                addTableCell(piecesTable, cp.getEstFourni() != null && cp.getEstFourni() ? "✓ Oui" : "✗ Non", normalFont);
            }
            document.add(piecesTable);
        } else {
            document.add(new Paragraph("Aucune pièce justificative enregistrée.", normalFont));
        }

        document.add(new Paragraph("\n\n"));

        // ========== SIGNATURE ==========
        Paragraph signature = new Paragraph("Fait à Antananarivo, le " +
                java.time.LocalDate.now().format(DATE_FORMAT), normalFont);
        signature.setAlignment(Element.ALIGN_RIGHT);
        document.add(signature);

        document.add(new Paragraph("\n\n"));

        Paragraph signatureLabel = new Paragraph("L'Agent de Saisie", headerFont);
        signatureLabel.setAlignment(Element.ALIGN_RIGHT);
        document.add(signatureLabel);

        // Pied de page
        document.add(new Paragraph("\n\n"));
        Paragraph footer = new Paragraph(
                "Document généré automatiquement — Système de Gestion des Visas — Ministère de l'Intérieur",
                smallFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();

        return baos.toByteArray();
    }

    private Paragraph createSectionTitle(String title, Font font) {
        Paragraph p = new Paragraph(title, font);
        p.setSpacingBefore(5);
        p.setSpacingAfter(8);
        return p;
    }

    private PdfPTable createInfoTable() {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        try {
            table.setWidths(new float[]{3, 7});
        } catch (DocumentException e) {
            // ignore
        }
        return table;
    }

    private void addInfoRow(PdfPTable table, String label, String value, Font font) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, new Font(Font.HELVETICA, 10, Font.BOLD, new Color(80, 80, 80))));
        labelCell.setBorderWidth(0.5f);
        labelCell.setBorderColor(new Color(200, 200, 200));
        labelCell.setPadding(5);
        labelCell.setBackgroundColor(new Color(245, 245, 250));
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        valueCell.setBorderWidth(0.5f);
        valueCell.setBorderColor(new Color(200, 200, 200));
        valueCell.setPadding(5);
        table.addCell(valueCell);
    }

    private void addTableHeader(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new Color(0, 51, 102));
        cell.setPadding(6);
        cell.setBorderWidth(0);
        Font whiteFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        cell.setPhrase(new Phrase(text, whiteFont));
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setBorderWidth(0.5f);
        cell.setBorderColor(new Color(200, 200, 200));
        table.addCell(cell);
    }
}
