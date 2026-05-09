package util;

import jakarta.servlet.http.Part;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ImagenUtilTest {

    @Test
    public void testValidarImagen_SizeExceeded() {
        Part mockPart = Mockito.mock(Part.class);
        // 3 MB
        when(mockPart.getSize()).thenReturn(3L * 1024 * 1024);
        when(mockPart.getContentType()).thenReturn("image/jpeg");

        String result = ImagenUtil.validarImagen(mockPart);

        assertEquals("El archivo supera el tamaño máximo de 2 MB.", result, "Should return error for large file");
    }

    @Test
    public void testValidarImagen_InvalidMime() {
        Part mockPart = Mockito.mock(Part.class);
        when(mockPart.getSize()).thenReturn(1L * 1024 * 1024);
        when(mockPart.getContentType()).thenReturn("application/pdf");

        String result = ImagenUtil.validarImagen(mockPart);

        assertEquals("Solo se permiten imágenes JPG o PNG.", result, "Should return error for invalid MIME type");
    }

    @ParameterizedTest
    @ValueSource(strings = { "image/jpeg", "image/png" })
    public void testValidarImagen_ValidMimes(String mimeType) {
        Part mockPart = Mockito.mock(Part.class);
        when(mockPart.getSize()).thenReturn(1L * 1024 * 1024);
        when(mockPart.getContentType()).thenReturn(mimeType);

        String result = ImagenUtil.validarImagen(mockPart);

        assertNull(result, "Should return null (no error) for valid MIME: " + mimeType);
    }
}
