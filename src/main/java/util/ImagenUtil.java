package util;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ImagenUtil {

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2 MB
    private static final String[] ALLOWED_MIME_TYPES = {"image/jpeg", "image/png"};

    /**
     * Valida si el archivo es una imagen válida (JPG o PNG) y no supera los 2 MB.
     * @param filePart El Part del archivo del formulario.
     * @return null si es válida, un mensaje de error si no lo es.
     */
    public static String validarImagen(Part filePart) {
        if (filePart == null || filePart.getSize() == 0) {
            return null; // No se subió archivo, es válido (opcional)
        }

        if (filePart.getSize() > MAX_FILE_SIZE) {
            return "El archivo supera el tamaño máximo de 2 MB.";
        }

        String mimeType = filePart.getContentType();
        boolean validMime = false;
        for (String type : ALLOWED_MIME_TYPES) {
            if (type.equalsIgnoreCase(mimeType)) {
                validMime = true;
                break;
            }
        }

        if (!validMime) {
            return "Solo se permiten imágenes JPG o PNG.";
        }

        return null;
    }

    /**
     * Guarda la imagen en el servidor con un nombre UUID.
     * @param filePart El Part del archivo.
     * @param uploadPath Ruta física donde se guardará.
     * @return El nombre del archivo guardado.
     * @throws IOException Si hay error al guardar.
     */
    public static String guardarImagen(Part filePart, String uploadPath) throws IOException {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileName = filePart.getSubmittedFileName();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + extension;

        File file = new File(uploadDir, newFileName);
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        return newFileName;
    }
}
