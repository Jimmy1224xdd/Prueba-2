package util;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║           JenkinsDetectorMain.java — UniServicios               ║
 * ║                                                                  ║
 * ║  Propósito: Detectar si la aplicación está siendo ejecutada     ║
 * ║  dentro de un pipeline de Jenkins, e imprimir información de    ║
 * ║  diagnóstico del entorno de CI/CD.                              ║
 * ║                                                                  ║
 * ║  Comparación con calculator-unit-test-example-java:             ║
 * ║   ► El laboratorio de calculadora demostró el patrón            ║
 * ║     "clase pura + tests JUnit".  Aquí aplicamos el mismo        ║
 * ║     principio: lógica de detección aislada en métodos           ║
 * ║     estáticos, fácilmente probables con @Test / @Mock.          ║
 * ║   ► El lab usaba Calculator.java + CalculatorTest.java          ║
 * ║     Aquí usamos JenkinsDetector (lógica) + JenkinsDetectorTest  ║
 * ║     para mantener la misma separación concerns → testability.  ║
 * ╚══════════════════════════════════════════════════════════════════╝
 *
 * Variables de entorno que Jenkins inyecta automáticamente:
 *   JENKINS_URL     → URL del servidor Jenkins
 *   BUILD_NUMBER    → Número incremental del build (ej. "42")
 *   BUILD_ID        → Identificador del build (ej. "2024-05-01_10-30-00")
 *   JOB_NAME        → Nombre del pipeline (ej. "UniServicios-pipeline")
 *   BUILD_URL       → URL directa al build actual
 *   GIT_COMMIT      → SHA del commit que disparó el build
 *   GIT_BRANCH      → Rama del repositorio
 *   WORKSPACE       → Directorio de trabajo en el agente Jenkins
 *
 * Si NINGUNA de estas variables existe → la app NO corre en Jenkins.
 */
public class JenkinsDetectorMain {

    // ─── Variables de entorno estándar de Jenkins ─────────────────
    static final String[] JENKINS_ENV_VARS = {
        "JENKINS_URL",
        "BUILD_NUMBER",
        "BUILD_ID",
        "JOB_NAME",
        "BUILD_URL"
    };

    /**
     * Punto de entrada principal.
     * Se llama desde el Jenkinsfile en el stage('Verify CI Environment').
     */
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║   UniServicios — CI Environment Detector   ║");
        System.out.println("╚═══════════════════════════════════════════╝");

        boolean enJenkins = detectarJenkins();

        if (enJenkins) {
            System.out.println("\n✅  ENTORNO DETECTADO: Jenkins CI/CD");
            imprimirInfoJenkins();
        } else {
            System.out.println("\n💻  ENTORNO DETECTADO: Desarrollo local");
            System.out.println("    (No se encontraron variables de Jenkins)");
        }

        System.out.println("\n═══════════════════════════════════════════");
        imprimirInfoJava();
    }

    // ═══════════════════════════════════════════════════════════════
    //  MÉTODO PRINCIPAL DE DETECCIÓN — extraíble para tests unitarios
    // ═══════════════════════════════════════════════════════════════
    /**
     * Retorna true si al menos una variable de entorno de Jenkins
     * está presente con valor no vacío.
     *
     * TRAZABILIDAD con el lab de calculator:
     *   Así como Calculator.sum() encapsula la lógica de suma para
     *   que CalculatorTest.testSum() la verifique sin instanciar nada
     *   extra, este método estático es testeable con:
     *
     *   @Test void testDetectarJenkins_conBuildNumber() {
     *     // (con Mockito o System.setenv) → assertFalse / assertTrue
     *   }
     */
    public static boolean detectarJenkins() {
        for (String var : JENKINS_ENV_VARS) {
            String valor = System.getenv(var);
            if (valor != null && !valor.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna el número de build de Jenkins, o "N/A" si no está en CI.
     * Útil para logs de la aplicación: saber qué build produjo el WAR.
     */
    public static String obtenerNumeroBuild() {
        String build = System.getenv("BUILD_NUMBER");
        return (build != null && !build.isEmpty()) ? build : "N/A (entorno local)";
    }

    /**
     * Retorna el nombre del job de Jenkins, o "local" si no está en CI.
     */
    public static String obtenerNombreJob() {
        String job = System.getenv("JOB_NAME");
        return (job != null && !job.isEmpty()) ? job : "local";
    }

    // ─── Helpers de impresión ──────────────────────────────────────

    private static void imprimirInfoJenkins() {
        String[] campos = {
            "JOB_NAME    → " + System.getenv("JOB_NAME"),
            "BUILD_NUMBER→ " + System.getenv("BUILD_NUMBER"),
            "BUILD_ID    → " + System.getenv("BUILD_ID"),
            "BUILD_URL   → " + System.getenv("BUILD_URL"),
            "GIT_BRANCH  → " + System.getenv("GIT_BRANCH"),
            "GIT_COMMIT  → " + abreviarCommit(System.getenv("GIT_COMMIT")),
            "WORKSPACE   → " + System.getenv("WORKSPACE")
        };

        System.out.println("\n  Información del Build:");
        for (String campo : campos) {
            System.out.println("    " + campo);
        }
    }

    private static void imprimirInfoJava() {
        System.out.println("  Entorno Java:");
        System.out.println("    Java Version → " + System.getProperty("java.version"));
        System.out.println("    OS           → " + System.getProperty("os.name"));
        System.out.println("    Arch         → " + System.getProperty("os.arch"));
    }

    private static String abreviarCommit(String sha) {
        if (sha == null || sha.isEmpty()) return "N/A";
        return sha.length() > 8 ? sha.substring(0, 8) + "..." : sha;
    }
}
