package servlet.Servicio;

import dao.CalificacionDAO;
import dao.ServicioDAO;
import dao.SolicitudDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import modelo.Calificacion;
import modelo.Servicio;
import modelo.Solicitud;
import modelo.Usuario;
import util.GestorSesion;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * ControlCalificaciones — gestiona la calificación de un servicio recibido.
 * Trazabilidad: Diagrama de secuencia "Calificar Servicio"
 *   GET  /servicio/calificar?id=X  → InterfazCalificacion
 *   POST /servicio/calificar       → registrarCalificacion → "Calificación guardada"
 *
 * También implementa:
 *   GET  /servicio/mis-solicitudes → InterfazServiciosContratados
 */
@WebServlet("/servicio/calificar")
public class CalificarServicioServlet extends HttpServlet {

    private final CalificacionDAO calificacionDAO = new CalificacionDAO();
    private final ServicioDAO     servicioDAO     = new ServicioDAO();
    private final SolicitudDAO    solicitudDAO    = new SolicitudDAO();

    /**
     * GET: muestra el formulario de calificación (InterfazCalificacion)
     * Primero pasa por ControlDuplicidad
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
            int idServicio = Integer.parseInt(idStr);
            Optional<Servicio> opt = servicioDAO.buscarPorId(idServicio);

            if (opt.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }

            Servicio servicio = opt.get();
            Usuario usuarioActual = GestorSesion.getUsuarioActual(req); // EXTRACT CLASS

            // ── ControlDuplicidad: verifica si ya calificó ────────────────────
            // (paso 3 del diagrama de secuencia "Calificar Servicio")
            if (calificacionDAO.yaCalifico(usuarioActual, servicio)) {
                // Mensaje: "Servicio ya calificado" — paso 4 del diagrama
                req.getSession().setAttribute("mensajeError", "Ya calificaste este servicio.");
                resp.sendRedirect(req.getContextPath() + "/servicio/detalle?id=" + idServicio);
                return;
            }

            // Verificar que el usuario realmente solicitó este servicio
            if (!solicitudDAO.existeSolicitud(usuarioActual, servicio)) {
                req.getSession().setAttribute("mensajeError", "Solo puedes calificar servicios que hayas solicitado.");
                resp.sendRedirect(req.getContextPath() + "/servicio/detalle?id=" + idServicio);
                return;
            }

            // Paso 5: "No calificado" → mostrar InterfazCalificacion
            req.setAttribute("servicio", servicio);
            req.getRequestDispatcher("/WEB-INF/jsp/servicio/calificar.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }

    /**
     * POST: procesa la calificación
     * ControlCalificaciones → registrarCalificacion() → "Calificación guardada"
     * (pasos 8, 9, 10, 11 del diagrama de secuencia)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String idStr       = req.getParameter("idServicio");
        String puntuacionStr = req.getParameter("puntuacion");
        String resena      = req.getParameter("resena") != null ? req.getParameter("resena").trim() : "";

        Usuario usuarioActual = (Usuario) req.getSession(false).getAttribute("usuarioActual");

        // ── Validar datos (ControlValidacion) ────────────────────────────────
        if (idStr == null || puntuacionStr == null) {
            req.setAttribute("error", "Datos incompletos.");
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        int idServicio;
        int puntuacion;
        try {
            idServicio  = Integer.parseInt(idStr);
            puntuacion  = Integer.parseInt(puntuacionStr);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        if (puntuacion < 1 || puntuacion > 5) {
            Optional<Servicio> opt = servicioDAO.buscarPorId(idServicio);
            req.setAttribute("error", "La puntuación debe ser entre 1 y 5.");
            req.setAttribute("servicio", opt.orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/servicio/calificar.jsp").forward(req, resp);
            return;
        }

        Optional<Servicio> opt = servicioDAO.buscarPorId(idServicio);
        if (opt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Servicio servicio = opt.get();

        // ── ControlDuplicidad nuevamente en POST (seguridad) ──────────────────
        if (calificacionDAO.yaCalifico(usuarioActual, servicio)) {
            req.getSession().setAttribute("mensajeError", "Ya calificaste este servicio.");
            resp.sendRedirect(req.getContextPath() + "/servicio/detalle?id=" + idServicio);
            return;
        }

        // ── ControlCalificaciones: registrarCalificacion() ───────────────────
        // (paso 9 del diagrama de secuencia)
        Calificacion calificacion = new Calificacion(puntuacion, resena, servicio, usuarioActual);
        calificacionDAO.guardar(calificacion);

        // Mensaje: "Calificación guardada" — paso 10 del diagrama
        req.getSession().setAttribute("mensajeExito", "¡Calificación guardada correctamente!");
        resp.sendRedirect(req.getContextPath() + "/servicio/detalle?id=" + idServicio);
    }
}
