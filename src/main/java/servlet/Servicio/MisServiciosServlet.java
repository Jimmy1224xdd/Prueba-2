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
import java.util.List;

@WebServlet("/servicio/mis-servicios")
public class MisServiciosServlet extends HttpServlet {

    private final ServicioDAO servicioDAO = new ServicioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuarioActual = GestorSesion.getUsuarioActual(req);
        List<Servicio> servicios = servicioDAO.listarPorUsuario(usuarioActual);
        req.setAttribute("misServicios", servicios);
        req.getRequestDispatcher("/WEB-INF/jsp/servicio/mis-servicios.jsp").forward(req, resp);
    }
}
