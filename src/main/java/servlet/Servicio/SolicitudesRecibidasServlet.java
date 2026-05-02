package servlet.Servicio;

import dao.SolicitudDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Solicitud;
import modelo.Usuario;
import modelo.types.EstadoSolicitud;
import util.GestorSesion;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/servicio/solicitudes-recibidas")
public class SolicitudesRecibidasServlet extends HttpServlet {

    private final SolicitudDAO solicitudDAO = new SolicitudDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuarioActual = GestorSesion.getUsuarioActual(req);
        List<Solicitud> recibidas = solicitudDAO.listarRecibidasPorProveedor(usuarioActual);
        req.setAttribute("solicitudesRecibidas", recibidas);
        req.getRequestDispatcher("/WEB-INF/jsp/servicio/solicitudes-recibidas.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("idSolicitud");
        String estadoStr = req.getParameter("estado");

        if (idStr != null && estadoStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                Optional<Solicitud> opt = solicitudDAO.buscarPorId(id);
                if (opt.isPresent()) {
                    Solicitud s = opt.get();
                    Usuario usuarioActual = GestorSesion.getUsuarioActual(req);
                    
                    if (s.getServicio().esPropietario(usuarioActual)) {
                        s.setEstado(EstadoSolicitud.valueOf(estadoStr));
                        solicitudDAO.actualizar(s);
                        req.getSession().setAttribute("mensajeExito", "Estado actualizado a " + estadoStr);
                    }
                }
            } catch (Exception e) {
                req.getSession().setAttribute("mensajeError", "Error al actualizar.");
            }
        }
        resp.sendRedirect(req.getContextPath() + "/servicio/solicitudes-recibidas");
    }
}
