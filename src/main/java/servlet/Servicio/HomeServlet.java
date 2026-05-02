package servlet.Servicio;

import dao.NotificacionDAO;
import dao.ServicioDAO;
import dao.CategoriaDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import modelo.Notificacion;
import modelo.Usuario;
import util.GestorSesion;

import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private final ServicioDAO servicioDAO = new ServicioDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final NotificacionDAO notificacionDAO = new NotificacionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("servicios", servicioDAO.listarActivos());
        req.setAttribute("categorias", categoriaDAO.listarTodas());

        // Refrescar badge de notificaciones no leídas en sesión
        Usuario usuarioActual = GestorSesion.getUsuarioActual(req);
        if (usuarioActual != null) {
            List<Notificacion> notifs = notificacionDAO.listarPorUsuario(usuarioActual);
            long noLeidas = notifs.stream().filter(n -> !n.isLeida()).count();
            req.getSession().setAttribute("notifNoLeidas", noLeidas);
        }

        req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
    }
}
