package servlet;

import dao.NotificacionDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import modelo.Notificacion;
import modelo.Usuario;
import util.GestorSesion;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Pantalla de notificaciones del proveedor.
 * GET  /notificaciones        → lista todas las notificaciones del usuario actual
 * POST /notificaciones?accion=leer&id=X → marca una notificación como leída
 */
@WebServlet("/notificaciones")
public class NotificacionServlet extends HttpServlet {

    private final NotificacionDAO notificacionDAO = new NotificacionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuarioActual = GestorSesion.getUsuarioActual(req);

        List<Notificacion> notificaciones = notificacionDAO.listarPorUsuario(usuarioActual);

        // Contar no leídas para el badge de la navbar
        long noLeidas = notificaciones.stream().filter(n -> !n.isLeida()).count();

        // Actualizar el badge en sesión
        req.getSession().setAttribute("notifNoLeidas", noLeidas);

        req.setAttribute("notificaciones", notificaciones);
        req.setAttribute("noLeidas", noLeidas);
        req.getRequestDispatcher("/WEB-INF/jsp/notificaciones.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("accion");
        String idStr  = req.getParameter("id");

        if ("leer".equals(accion) && idStr != null) {
            try {
                int idNotificacion = Integer.parseInt(idStr);
                Optional<Notificacion> opt = notificacionDAO.buscarPorId(idNotificacion);
                opt.ifPresent(notificacionDAO::marcarLeida);
            } catch (NumberFormatException ignored) {}
        } else if ("leerTodas".equals(accion)) {
            Usuario usuarioActual = GestorSesion.getUsuarioActual(req);
            List<Notificacion> notificaciones = notificacionDAO.listarPorUsuario(usuarioActual);
            notificaciones.stream()
                    .filter(n -> !n.isLeida())
                    .forEach(notificacionDAO::marcarLeida);
        }

        resp.sendRedirect(req.getContextPath() + "/notificaciones");
    }
}
