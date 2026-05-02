package servlet.Servicio;

import dao.CategoriaDAO;
import dao.ServicioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Categoria;
import modelo.Servicio;
import modelo.Usuario;
import modelo.types.EstadoServicio;
import util.GestorSesion;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/servicio/editar")
public class EditarServicioServlet extends HttpServlet {

    private final ServicioDAO servicioDAO = new ServicioDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/servicio/mis-servicios");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/servicio/mis-servicios");
            return;
        }

        Optional<Servicio> opt = servicioDAO.buscarPorId(id);
        if (opt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/servicio/mis-servicios");
            return;
        }

        Servicio servicio = opt.get();
        Usuario usuarioActual = GestorSesion.getUsuarioActual(req);

        if (!servicio.esPropietario(usuarioActual)) {
            req.getSession().setAttribute("mensajeError", "No puedes editar un servicio que no es tuyo.");
            resp.sendRedirect(req.getContextPath() + "/servicio/mis-servicios");
            return;
        }

        req.setAttribute("servicio", servicio);
        req.setAttribute("categorias", categoriaDAO.listarTodas());
        req.getRequestDispatcher("/WEB-INF/jsp/servicio/editar.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String idStr = req.getParameter("idServicio");
        String titulo = req.getParameter("titulo");
        String descripcion = req.getParameter("descripcion");
        String precioStr = req.getParameter("precio");
        String catStr = req.getParameter("idCategoria");
        String estadoStr = req.getParameter("estado");

        if (idStr == null || titulo == null || descripcion == null || precioStr == null || catStr == null || estadoStr == null) {
            req.getSession().setAttribute("mensajeError", "Todos los campos son obligatorios.");
            resp.sendRedirect(req.getContextPath() + "/servicio/mis-servicios");
            return;
        }

        int id = Integer.parseInt(idStr);
        double precio = Double.parseDouble(precioStr);
        int idCategoria = Integer.parseInt(catStr);
        EstadoServicio estado = EstadoServicio.valueOf(estadoStr);

        Optional<Servicio> opt = servicioDAO.buscarPorId(id);
        if (opt.isPresent()) {
            Servicio s = opt.get();
            Usuario usuarioActual = GestorSesion.getUsuarioActual(req);
            
            if (s.esPropietario(usuarioActual)) {
                s.setTituloServicio(titulo);
                s.setDescripcionServicio(descripcion);
                s.setPrecioServicio(precio);
                s.setCategoria(categoriaDAO.buscarPorId(idCategoria).orElse(s.getCategoria()));
                s.marcarDisponibilidad(estado);

                servicioDAO.actualizar(s);
                req.getSession().setAttribute("mensajeExito", "Servicio actualizado correctamente.");
            }
        }
        resp.sendRedirect(req.getContextPath() + "/servicio/mis-servicios");
    }
}
