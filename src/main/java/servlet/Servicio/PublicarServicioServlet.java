package servlet.Servicio;

import dao.ServicioDAO;
import dao.CategoriaDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import modelo.Servicio;
import modelo.Usuario;
import modelo.Categoria;
import modelo.types.EstadoServicio;

import java.io.IOException;
import java.util.Date;

@WebServlet("/servicio/publicar")
public class PublicarServicioServlet extends HttpServlet {

    private final ServicioDAO servicioDAO = new ServicioDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("categorias", categoriaDAO.listarTodas());
        request.getRequestDispatcher("/WEB-INF/jsp/servicio/publicar.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String titulo      = request.getParameter("titulo")      != null ? request.getParameter("titulo").trim()      : "";
        String descripcion = request.getParameter("descripcion") != null ? request.getParameter("descripcion").trim() : "";
        String precioStr   = request.getParameter("precio")      != null ? request.getParameter("precio").trim()      : "0";
        String catStr      = request.getParameter("idCategoria") != null ? request.getParameter("idCategoria").trim()  : "0";

        if (titulo.isEmpty() || descripcion.isEmpty()) {
            request.setAttribute("error", "Título y descripción son obligatorios.");
            request.setAttribute("categorias", categoriaDAO.listarTodas());
            request.getRequestDispatcher("/WEB-INF/jsp/servicio/publicar.jsp").forward(request, response);
            return;
        }

        double precio;
        int idCategoria;
        try {
            precio      = Double.parseDouble(precioStr);
            idCategoria = Integer.parseInt(catStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Precio o categoría inválidos.");
            request.setAttribute("categorias", categoriaDAO.listarTodas());
            request.getRequestDispatcher("/WEB-INF/jsp/servicio/publicar.jsp").forward(request, response);
            return;
        }

        HttpSession session  = request.getSession(false);
        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");

        Categoria categoria = categoriaDAO.buscarPorId(idCategoria).orElse(null);

        Servicio servicio = new Servicio();
        servicio.setTituloServicio(titulo);
        servicio.setDescripcionServicio(descripcion);
        servicio.setPrecioServicio(precio);
        servicio.setUsuario(usuarioActual);
        servicio.setCategoria(categoria);
        servicio.setEstado(EstadoServicio.ACTIVO);
        servicio.setDisponibilidad(true);
        servicio.setFechaPublicacionServicio(new Date());

        servicioDAO.guardar(servicio);

        request.getSession().setAttribute("mensajeExito", "¡Servicio publicado correctamente!");
        response.sendRedirect(request.getContextPath() + "/home");
    }
}
