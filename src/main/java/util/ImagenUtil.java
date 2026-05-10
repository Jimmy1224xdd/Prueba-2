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
     * Convierte la imagen a una cadena Base64 (Data URI).
     * @param filePart El Part del archivo.
     * @return La cadena Base64 lista para src de img.
     * @throws IOException Si hay error al leer.
     */
    public static String convertirABase64(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) return null;
        
        byte[] bytes;
        try (InputStream input = filePart.getInputStream()) {
            bytes = input.readAllBytes();
        }
        
        String base64 = java.util.Base64.getEncoder().encodeToString(bytes);
        return "data:" + filePart.getContentType() + ";base64," + base64;
    }
}
