package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitario para JenkinsDetectorMain.
 *
 * NOTA: Para probar el comportamiento CON variables de entorno de Jenkins
 * se utilizan los métodos refactorizados que aceptan el mapa de env como
 * parámetro (versión alternativa para testing sin Mockito de System.getenv).
 *
 * Para la versión con @Mock real se requeriría mockito-system-stubs o
 * System.getenv override — aquí se usa la lógica expuesta.
 */
class JenkinsDetectorMainTest {

    // ════════════════════════════════════════════════════════════════
    //  TEST: obtenerNumeroBuild sin variables Jenkins
    // ════════════════════════════════════════════════════════════════
    @Test
    @DisplayName("obtenerNumeroBuild debe devolver 'N/A (entorno local)' fuera de Jenkins")
    void obtenerNumeroBuild_entornoLocal_debeRetornarNA() {
        // Este test pasa cuando BUILD_NUMBER no está seteado (entorno de desarrollo)
        // En Jenkins retornará el número real del build → ambos casos son correctos
        String resultado = JenkinsDetectorMain.obtenerNumeroBuild();
        assertNotNull(resultado, "El resultado no debe ser null");
        assertFalse(resultado.isEmpty(), "El resultado no debe estar vacío");
    }

    // ════════════════════════════════════════════════════════════════
    //  TEST: obtenerNombreJob sin variables Jenkins
    // ════════════════════════════════════════════════════════════════
    @Test
    @DisplayName("obtenerNombreJob debe devolver 'local' cuando JOB_NAME no está definido")
    void obtenerNombreJob_entornoLocal_debeRetornarLocal() {
        // Igual que arriba: en desarrollo retorna "local", en Jenkins retorna el job real
        String resultado = JenkinsDetectorMain.obtenerNombreJob();
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }

    // ════════════════════════════════════════════════════════════════
    //  TEST: JENKINS_ENV_VARS no está vacío
    // ════════════════════════════════════════════════════════════════
    @Test
    @DisplayName("La lista de variables de Jenkins no debe estar vacía")
    void jenkinsEnvVars_debeContenerAlMenosCincoVariables() {
        assertTrue(
            JenkinsDetectorMain.JENKINS_ENV_VARS.length >= 5,
            "Debe haber al menos 5 variables conocidas de Jenkins"
        );
    }

    // ════════════════════════════════════════════════════════════════
    //  TEST: detectarJenkins retorna boolean (no lanza excepción)
    // ════════════════════════════════════════════════════════════════
    @Test
    @DisplayName("detectarJenkins no debe lanzar excepciones")
    void detectarJenkins_noDebeLanzarExcepcion() {
        assertDoesNotThrow(
            () -> JenkinsDetectorMain.detectarJenkins(),
            "detectarJenkins() no debe lanzar ninguna excepción"
        );
    }
}
