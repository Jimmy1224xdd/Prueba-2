package servicio;

// ============================================================
//  INCREMENTO TDD — Tests avanzados: Mock + Parametrizado
//  Módulo: NotificacionService
//
//  TEST 5: Mock — verifica que el DAO se invoca exactamente
//          una vez al notificar una solicitud.
//  TEST 6: Parametrizado — verifica que el contenido de la
//          notificación es correcto para diferentes solicitantes.
//
// ============================================================

import dao.NotificacionDAO;
import modelo.Notificacion;
import modelo.Servicio;
import modelo.Solicitud;
import modelo.Usuario;
import modelo.types.Rol;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import servlet.Servicio.NotificacionService;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceAvanzadoTest {

    private Usuario  proveedor;
    private Servicio servicio;

    // Mockito crea un doble de prueba del DAO — sin BD real
    @Mock
    private NotificacionDAO notificacionDAOMock;

    @BeforeEach
    void setUp() {
        proveedor = new Usuario("Ana Torres", "ana@test.com", "pass", Rol.ESTUDIANTE);
        servicio  = new Servicio("Clases de Matemáticas", "Refuerzo escolar", 10.0, proveedor, null);
    }


    // ════════════════════════════════════════════════════════
    //  TEST 5 — Mock: el DAO.guardar() se llama exactamente 1 vez
    // ════════════════════════════════════════════════════════
    //
    //  🔴 RED (antes de implementar notificacionDAO.guardar()):
    //     El método notificarSolicitudRecibida() existía pero NO
    //     llamaba al DAO.  Al ejecutar el test, Mockito verificaba
    //     la interacción y FALLABA con:
    //
    //       org.mockito.exceptions.verification.WantedButNotInvoked:
    //         Wanted but not invoked:
    //           notificacionDAO.guardar(<any modelo.Notificacion>);
    //         However, there were zero interactions with this mock.
    //
    //     Esto evidencia que SIN el test, el bug pasaría desapercibido.
    //
    //  🟢 GREEN (implementación mínima en NotificacionService.java):
    //     Se añadió la llamada:
    //       notificacionDAO.guardar(notificacion);
    //     Ahora Mockito verifica 1 invocación → test pasa.
    //
    //  🔵 REFACTOR:
    //     Se extrajo la construcción del mensaje a un método privado
    //     construirContenido(), separando la lógica de formato de
    //     la lógica de persistencia.  El Mock sigue verificando
    //     que guardar() se invoque exactamente una vez.
    // ════════════════════════════════════════════════════════
    @Test
    @DisplayName("T5 [Mock] – notificacionDAO.guardar() debe invocarse exactamente 1 vez")
    void notificacionDAO_guardarDebeInvocarseUnaVez() {
        // ARRANGE
        Usuario solicitante = new Usuario("Luis Mendez", "luis@test.com", "pass", Rol.ESTUDIANTE);
        Solicitud solicitud = new Solicitud(solicitante, servicio);
        NotificacionService service = new NotificacionService(notificacionDAOMock);

        // ACT
        service.notificarSolicitudRecibida(solicitud);

        // ASSERT — Mockito verifica la interacción con el doble de prueba
        // Si guardar() no fue invocado, o fue invocado más de una vez, el test FALLA
        verify(notificacionDAOMock, times(1))
                .guardar(any(Notificacion.class));
    }


    // ════════════════════════════════════════════════════════
    //  TEST 6 — Parametrizado: contenido correcto por solicitante
    // ════════════════════════════════════════════════════════
    //
    //  🔴 RED (antes del método construirContenido):
    //     construirContenido() no existía como método separado.
    //     El formato del mensaje era hardcodeado e incorrecto.
    //     Al ejecutar con "María García" el test FALLABA con:
    //
    //       org.opentest4j.AssertionFailedError:
    //         Debe incluir el nombre: María García ==>
    //         expected: <true>
    //          but was: <false>
    //         (porque el mensaje usaba variables incorrectas)
    //
    //  🟢 GREEN (implementación en NotificacionService.java):
    //     El método construirContenido() produce:
    //       "El usuario %s (%s) ha solicitado tu servicio '%s'."
    //     Que incluye nombre, correo y título → test pasa.
    //
    //  🔵 REFACTOR:
    //     Se parametrizó el test con @MethodSource para cubrir
    //     3 solicitantes distintos en un solo test, evitando
    //     repetición de código de prueba.  El mismo ciclo RED-GREEN
    //     se aplica para CADA combinación de parámetros.
    // ════════════════════════════════════════════════════════

    // Fuente de datos: 3 solicitantes distintos
    static Stream<Usuario> listaDeSolicitantes() {
        return Stream.of(
                new Usuario("María García",  "maria@test.com",  "pass", Rol.ESTUDIANTE),
                new Usuario("Carlos Ruiz",   "carlos@test.com", "pass", Rol.ESTUDIANTE),
                new Usuario("Sofía Herrera", "sofia@test.com",  "pass", Rol.ESTUDIANTE)
        );
    }

    @ParameterizedTest(name = "T6 [{index}] – Solicitante: {0}")
    @MethodSource("listaDeSolicitantes")
    @DisplayName("T6 [Parametrizado] – El contenido incluye nombre y correo de cada solicitante")
    void notificacion_contenidoCorrectoPorSolicitante(Usuario solicitanteParam) {
        // ARRANGE — se usa el solicitante inyectado por @MethodSource
        Solicitud solicitudParam = new Solicitud(solicitanteParam, servicio);
        NotificacionService service = new NotificacionService(notificacionDAOMock);

        // ACT
        Notificacion resultado = service.notificarSolicitudRecibida(solicitudParam);
        String contenido = resultado.getContenido();

        // ASSERT — se comprueba para CADA solicitante del Stream
        assertAll(
                "Contenido debe identificar correctamente al solicitante",
                () -> assertTrue(contenido.contains(solicitanteParam.getNombre()),
                        "Debe incluir el nombre: " + solicitanteParam.getNombre()),
                () -> assertTrue(contenido.contains(solicitanteParam.getCorreo()),
                        "Debe incluir el correo: " + solicitanteParam.getCorreo()),
                () -> assertTrue(contenido.contains(servicio.getTituloServicio()),
                        "Debe incluir el título del servicio")
        );
    }
}
