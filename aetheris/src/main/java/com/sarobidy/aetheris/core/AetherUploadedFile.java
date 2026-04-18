package com.sarobidy.aetheris.core;

import java.nio.file.Path;

/**
 * Représente un fichier uploadé via multipart/form-data.
 * Conserve les métadonnées utiles (nom, type, taille) + le chemin du fichier sauvegardé.
 */
public class AetherUploadedFile {

    private final String fieldName;
    private final String originalFilename;
    private final String contentType;
    private final long size;
    private final Path savedPath;

    public AetherUploadedFile(
            String fieldName,
            String originalFilename,
            String contentType,
            long size,
            Path savedPath
    ) {
        this.fieldName = fieldName;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.size = size;
        this.savedPath = savedPath;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }

    public Path getSavedPath() {
        return savedPath;
    }
}
