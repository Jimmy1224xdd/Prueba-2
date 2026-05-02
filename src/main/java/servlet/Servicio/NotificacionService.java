package servlet.Servicio;

import dao.NotificacionDAO;
import modelo.Notificacion;
import modelo.Solicitud;
import modelo.Usuario;
import modelo.types.TiposNotificacion;

/**
 * Servicio de notificaciones.
 *
 * Responsabilidad única: construir y persistir notificaciones ante
 * eventos de negocio (solicitud de servicio, calificación, etc.).
 *
 * El NotificacionDAO se inyecta por constructor para facilitar
 * las pruebas unitarias con mocks (Test 5).
 *
 * TRAZABILIDAD TDD:
 *   - Test 1 → getTipoNotificacion() == SOLICITUD_RECIBIDA
 *   - Test 2 → getUsuario()          == proveedor del servicio
 *   - Test 3 → getContenido()         contiene nombre, correo y título
 *   - Test 4 → isLeida()             == false (constructor de Notificacion)
 *   - Test 5 → notificacionDAO.guardar() se invoca una vez (mock verifica)
 *   - Test 6 → contenido correcto para distintos solicitantes (parametrizado)
 */
public class NotificacionService {

    private final NotificacionDAO notificacionDAO;

    public NotificacionService(NotificacionDAO notificacionDAO) {
        this.notificacionDAO = notificacionDAO;
    }

    /**
     * Genera y persiste una notificación de tipo SOLICITUD_RECIBIDA
     * dirigida al proveedor del servicio solicitado.
     *
     * @param solicitud la solicitud recién creada
     * @return la notificación generada (ya guardada)
     */
    public Notificacion notificarSolicitudRecibida(Solicitud solicitud) {
        Usuario solicitante = solicitud.getUsuario();
        Usuario proveedor   = solicitud.getServicio().getUsuario();
        String  tituloServicio = solicitud.getServicio().getTituloServicio();

        String contenido = construirContenido(solicitante, tituloServicio);

        Notificacion notificacion = new Notificacion(contenido, TiposNotificacion.SOLICITUD_RECIBIDA, proveedor);

        notificacionDAO.guardar(notificacion);

        return notificacion;
    }

    // ── EXTRACT METHOD (Refactor) ──────────────────────────────────────────
    // Separar la construcción del mensaje facilita pruebas y futuras
    // modificaciones del formato sin tocar la lógica principal.
    private String construirContenido(Usuario solicitante, String tituloServicio) {
        return String.format(
                "El usuario %s (%s) ha solicitado tu servicio '%s'.",
                solicitante.getNombre(),
                solicitante.getCorreo(),
                tituloServicio
        );
    }
}
