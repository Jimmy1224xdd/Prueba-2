package servlet.Servicio;

import dao.CalificacionDAO;
import dao.ServicioDAO;
import dao.SolicitudDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import modelo.Calificacion;
import modelo.Servicio;
import modelo.Usuario;
import util.GestorSesion;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Muestra el detalle de un servicio.
 * Trazabilidad: punto de entrada para Solicitar y Calificar servicio.
 * GET /servicio/detalle?id=X
 */
@WebServlet("/servicio/detalle")
public class DetalleServicioServlet extends HttpServlet {

    private final ServicioDAO     servicioDAO     = new ServicioDAO();
    private final CalificacionDAO calificacionDAO = new CalificacionDAO();
    private final SolicitudDAO    solicitudDAO    = new SolicitudDAO();

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
            Optional<Servicio> optServicio = servicioDAO.buscarPorId(id);

            if (optServicio.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }

            Servicio servicio = optServicio.get();
            Usuario usuarioActual = GestorSesion.getUsuarioActual(req); // EXTRACT CLASS

            // Calificaciones del servicio para mostrar en detalle
            List<Calificacion> calificaciones = calificacionDAO.listarPorServicio(servicio);
            Double promedio = calificacionDAO.obtenerPromedio(servicio);

            // Flags útiles para la vista (controlar qué botones mostrar)
            boolean esMiServicio    = servicio.esPropietario(usuarioActual); // MOVE METHOD
            boolean yaSolicite      = solicitudDAO.existeSolicitud(usuarioActual, servicio);
            boolean yaCalifico      = calificacionDAO.yaCalifico(usuarioActual, servicio);

            req.setAttribute("servicio",      servicio);
            req.setAttribute("proveedor",     servicio.getUsuario());
            req.setAttribute("categoria",     servicio.getCategoria());
            req.setAttribute("calificaciones", calificaciones);
            req.setAttribute("promedio",      promedio);
            req.setAttribute("esMiServicio",  esMiServicio);
            req.setAttribute("yaSolicite",    yaSolicite);
            req.setAttribute("yaCalifico",    yaCalifico);

            req.getRequestDispatcher("/WEB-INF/jsp/servicio/detalle.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }
}
