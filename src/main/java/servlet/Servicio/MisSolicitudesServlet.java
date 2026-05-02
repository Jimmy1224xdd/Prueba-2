package servlet.Servicio;

import dao.SolicitudDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import modelo.Solicitud;
import modelo.Usuario;

import java.io.IOException;
import java.util.List;

/**
 * InterfazServiciosContratados — muestra los servicios que el usuario ha solicitado.
 * Trazabilidad: Diagrama de secuencia "Calificar Servicio" paso 1
 *   GET /servicio/mis-solicitudes
 */
@WebServlet("/servicio/mis-solicitudes")
public class MisSolicitudesServlet extends HttpServlet {

    private final SolicitudDAO solicitudDAO = new SolicitudDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuarioActual = (Usuario) req.getSession(false).getAttribute("usuarioActual");

        // Paso 1 del diagrama: mostrar servicios contratados del usuario
        List<Solicitud> misSolicitudes = solicitudDAO.listarPorUsuario(usuarioActual);

        req.setAttribute("solicitudes", misSolicitudes);
        req.getRequestDispatcher("/WEB-INF/jsp/servicio/mis-solicitudes.jsp").forward(req, resp);
    }
}
