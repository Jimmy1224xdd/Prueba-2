package servlet.Servicio;

import dao.NotificacionDAO;
import dao.ServicioDAO;
import dao.SolicitudDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import modelo.Servicio;
import modelo.Solicitud;
import modelo.Usuario;
import servlet.Servicio.NotificacionService;
import util.GestorSesion;

import java.io.IOException;
import java.util.Optional;

/**
 * ControlSolicitud — gestiona la lógica de solicitar un servicio.
 * Trazabilidad: Diagrama de secuencia "Solicitar Servicio"
 *   POST /servicio/solicitar
 *   Flujo: Cliente → InterfazDeSolicitud → ControlValidacion → ControlSolicitud → Servicios
 */
@WebServlet("/servicio/solicitar")
public class SolicitarServicioServlet extends HttpServlet {

    private final ServicioDAO        servicioDAO        = new ServicioDAO();
    private final SolicitudDAO       solicitudDAO       = new SolicitudDAO();
    private final NotificacionService notificacionService = new NotificacionService(new NotificacionDAO());

    /**
     * GET: muestra el formulario de confirmación de solicitud
     * (InterfazDeSolicitud del diagrama de robustez)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Optional<Servicio> opt = servicioDAO.buscarPorId(id);

            if (opt.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }

            Servicio servicio = opt.get();
            Usuario usuarioActual = GestorSesion.getUsuarioActual(req); // EXTRACT CLASS

            // No puede solicitar su propio servicio
            if (servicio.esPropietario(usuarioActual)) { // MOVE METHOD
                req.setAttribute("error", "No puedes solicitar tu propio servicio.");
                req.setAttribute("servicio", servicio);
                req.getRequestDispatcher("/WEB-INF/jsp/servicio/detalle.jsp").forward(req, resp);
                return;
            }

            req.setAttribute("servicio", servicio);
            req.getRequestDispatcher("/WEB-INF/jsp/servicio/solicitar.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }

    /**
     * POST: procesa la solicitud
     * ControlValidacion + ControlSolicitud del diagrama de secuencia
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String idStr = req.getParameter("idServicio");
        Usuario usuarioActual = GestorSesion.getUsuarioActual(req); // EXTRACT CLASS

        // ── ControlValidacion: validar datos ─────────────────────────────────
        if (idStr == null || idStr.isEmpty()) {
            req.setAttribute("error", "Datos erróneos. Intenta de nuevo.");
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        int idServicio;
        try {
            idServicio = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Datos erróneos.");
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Optional<Servicio> opt = servicioDAO.buscarPorId(idServicio);
        if (opt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Servicio servicio = opt.get();

        // Validar que no sea su propio servicio
        if (servicio.esPropietario(usuarioActual)) { // MOVE METHOD
            req.getSession().setAttribute("mensajeError", "No puedes solicitar tu propio servicio.");
            resp.sendRedirect(req.getContextPath() + "/servicio/detalle?id=" + idServicio);
            return;
        }

        // Validar que no haya solicitado ya este servicio
        if (solicitudDAO.existeSolicitud(usuarioActual, servicio)) {
            req.getSession().setAttribute("mensajeError", "Ya enviaste una solicitud para este servicio.");
            resp.sendRedirect(req.getContextPath() + "/servicio/detalle?id=" + idServicio);
            return;
        }

        // ── ControlSolicitud: registrarSolicitud() ───────────────────────────
        Solicitud solicitud = new Solicitud(usuarioActual, servicio);
        solicitudDAO.guardar(solicitud);

        // REFACTOR (FASE GREEN→REFACTOR): llamada al NotificacionService
        // para notificar al proveedor que alguien solicitó su servicio.
        notificacionService.notificarSolicitudRecibida(solicitud);

        // Mensaje: "Solicitud enviada" — del diagrama de secuencia paso 11
        req.getSession().setAttribute("mensajeExito", "¡Solicitud enviada correctamente!");
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
