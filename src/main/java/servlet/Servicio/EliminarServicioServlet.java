package servlet.Servicio;

import dao.ServicioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Servicio;
import modelo.Usuario;
import util.GestorSesion;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/servicio/eliminar")
public class EliminarServicioServlet extends HttpServlet {

    private final ServicioDAO servicioDAO = new ServicioDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("idServicio");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Optional<Servicio> opt = servicioDAO.buscarPorId(id);
                if (opt.isPresent()) {
                    Servicio s = opt.get();
                    Usuario usuarioActual = GestorSesion.getUsuarioActual(req);
                    if (s.esPropietario(usuarioActual)) {
                        // Eliminar registros dependientes primero, luego el servicio
                        servicioDAO.eliminarConDependencias(s);
                        req.getSession().setAttribute("mensajeExito", "Servicio eliminado correctamente.");
                    } else {
                        req.getSession().setAttribute("mensajeError", "No tienes permiso para eliminar este servicio.");
                    }
                } else {
                    req.getSession().setAttribute("mensajeError", "Servicio no encontrado.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                req.getSession().setAttribute("mensajeError", "Error al eliminar: " + e.getMessage());
            }
        }
        resp.sendRedirect(req.getContextPath() + "/servicio/mis-servicios");
    }
}
