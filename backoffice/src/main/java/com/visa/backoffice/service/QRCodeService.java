package com.visa.backoffice.service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * Service pour générer des codes QR
 * Utilisé pour créer des codes QR contenant les liens de suivi des demandes
 */
@Service
public class QRCodeService {

    /**
     * Génère un QR code à partir d'une URL
     * 
     * @param trackingUrl L'URL complète de suivi de la demande
     * @param size       La taille du QR code (largeur et hauteur en pixels)
     * @return Un tableau de bytes représentant l'image PNG du QR code
     * @throws Exception Si la génération du QR code échoue
     */
    public byte[] generateQRCode(String trackingUrl, int size) throws Exception {
        if (trackingUrl == null || trackingUrl.isEmpty()) {
            throw new IllegalArgumentException("L'URL de suivi ne peut pas être vide");
        }

        // Configuration du QR code
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.MARGIN, 2);

        // Création du QR code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(trackingUrl, BarcodeFormat.QR_CODE, size, size, hints);

        // Convertir en image PNG
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        
        return outputStream.toByteArray();
    }

    /**
     * Génère un QR code avec une taille par défaut (400x400 pixels)
     * 
     * @param trackingUrl L'URL complète de suivi de la demande
     * @return Un tableau de bytes représentant l'image PNG du QR code
     * @throws Exception Si la génération du QR code échoue
     */
    public byte[] generateQRCode(String trackingUrl) throws Exception {
        return generateQRCode(trackingUrl, 400);
    }

    /**
     * Génère un QR code en Base64 pour l'affichage direct dans le HTML
     * 
     * @param trackingUrl L'URL complète de suivi de la demande
     * @param size       La taille du QR code
     * @return Une chaîne Base64 de l'image PNG du QR code
     * @throws Exception Si la génération du QR code échoue
     */
    public String generateQRCodeBase64(String trackingUrl, int size) throws Exception {
        byte[] qrCodeImage = generateQRCode(trackingUrl, size);
        return java.util.Base64.getEncoder().encodeToString(qrCodeImage);
    }

    /**
     * Génère un QR code en Base64 avec taille par défaut
     * 
     * @param trackingUrl L'URL complète de suivi de la demande
     * @return Une chaîne Base64 de l'image PNG du QR code
     * @throws Exception Si la génération du QR code échoue
     */
    public String generateQRCodeBase64(String trackingUrl) throws Exception {
        return generateQRCodeBase64(trackingUrl, 400);
    }
}
