package servlet.Servicio;

import dao.CalificacionDAO;
import dao.ServicioDAO;
import dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Servicio;
import modelo.Usuario;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/vendedor/perfil")
public class PerfilVendedorServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ServicioDAO servicioDAO = new ServicioDAO();
    private final CalificacionDAO calificacionDAO = new CalificacionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Optional<Usuario> vendedorOpt = usuarioDAO.buscarPorId(id);
        if (vendedorOpt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Usuario vendedor = vendedorOpt.get();
        List<Servicio> serviciosActivos = servicioDAO.listarActivosPorUsuario(id);
        double promedio = calificacionDAO.calcularPromedioPorUsuario(id);

        req.setAttribute("vendedor", vendedor);
        req.setAttribute("serviciosActivos", serviciosActivos);
        req.setAttribute("promedio", promedio);

        req.getRequestDispatcher("/WEB-INF/jsp/vendedor/perfil.jsp").forward(req, resp);
    }
}
