package servicio;

// ============================================================
//  INCREMENTO TDD — Módulo: Gestión de estado del Servicio
//  Funcionalidad nueva: validación de precio, estado activo,
//  marcarDisponibilidad y esPropietario en el modelo Servicio.
// ============================================================

import modelo.Servicio;
import modelo.Usuario;
import modelo.types.EstadoServicio;
import modelo.types.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServicioTest {

    // ── Fixtures compartidos ────────────────────────────────
    private Usuario proveedor;
    private Servicio servicio;

    @BeforeEach
    void setUp() {
        proveedor = new Usuario("Ana Torres", "ana@test.com", "pass", Rol.ESTUDIANTE);
        proveedor.setIdUsuario(1);
        servicio  = new Servicio("Clases de Matemáticas", "Refuerzo escolar", 15.0, proveedor, null);
    }


    // ════════════════════════════════════════════════════════
    //  TEST 1 — Estado inicial ACTIVO al crear un servicio
    // ════════════════════════════════════════════════════════
    @Test
    @DisplayName("T1 – Un servicio nuevo debe tener estado ACTIVO")
    void servicioNuevo_estadoDebeSerActivo() {
        // ARRANGE — servicio creado en setUp()

        // ACT
        EstadoServicio estadoActual = servicio.getEstado();

        // ASSERT
        assertEquals(
            EstadoServicio.ACTIVO,
            estadoActual,
            "Un servicio nuevo debe estar ACTIVO al ser creado"
        );
    }


    // ════════════════════════════════════════════════════════
    //  TEST 2 — marcarDisponibilidad(INACTIVO) desactiva el servicio
    // ════════════════════════════════════════════════════════

    @Test
    @DisplayName("T2 – marcarDisponibilidad(INACTIVO) debe desactivar el servicio")
    void marcarDisponibilidad_conInactivo_debeDesactivarServicio() {
        // ARRANGE
        // servicio comienza como ACTIVO (verificado en T1)

        // ACT
        servicio.marcarDisponibilidad(EstadoServicio.INACTIVO);

        // ASSERT — verificamos AMBAS propiedades derivadas
        assertAll(
            "Al marcar INACTIVO, estado y disponibilidad deben cambiar",
            () -> assertEquals(EstadoServicio.INACTIVO, servicio.getEstado(),
                    "El estado debe ser INACTIVO"),
            () -> assertFalse(servicio.isDisponibilidad(),
                    "La disponibilidad debe ser false cuando el estado es INACTIVO")
        );
    }


    // ════════════════════════════════════════════════════════
    //  TEST 3 — marcarDisponibilidad(ACTIVO) reactiva el servicio
    // ════════════════════════════════════════════════════════

    @Test
    @DisplayName("T3 – marcarDisponibilidad(ACTIVO) debe reactivar el servicio")
    void marcarDisponibilidad_conActivo_debeReactivarServicio() {
        // ARRANGE — primero desactivamos
        servicio.marcarDisponibilidad(EstadoServicio.INACTIVO);
        assertFalse(servicio.isDisponibilidad(), "Precondición: debe estar inactivo");

        // ACT
        servicio.marcarDisponibilidad(EstadoServicio.ACTIVO);

        // ASSERT
        assertAll(
            "Al marcar ACTIVO, estado y disponibilidad deben volver a activo",
            () -> assertEquals(EstadoServicio.ACTIVO, servicio.getEstado(),
                    "El estado debe ser ACTIVO"),
            () -> assertTrue(servicio.isDisponibilidad(),
                    "La disponibilidad debe ser true cuando el estado es ACTIVO")
        );
    }


    // ════════════════════════════════════════════════════════
    //  TEST 4 — esPropietario devuelve false para otro usuario
    // ════════════════════════════════════════════════════════

    @Test
    @DisplayName("T4 – esPropietario devuelve false para un usuario distinto")
    void esPropietario_usuarioDistinto_debeRetornarFalse() {
        // ARRANGE
        Usuario otroUsuario = new Usuario("Luis Mendez", "luis@test.com", "pass", Rol.ESTUDIANTE);
        otroUsuario.setIdUsuario(2); // ID diferente al proveedor (id=1)

        // ACT
        boolean resultado = servicio.esPropietario(otroUsuario);

        // ASSERT
        assertFalse(
            resultado,
            "esPropietario debe devolver false cuando el usuario NO es el dueño del servicio"
        );
    }
}
