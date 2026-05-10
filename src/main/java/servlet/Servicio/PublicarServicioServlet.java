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

import util.ImagenUtil;
import java.io.IOException;
import java.util.Date;

@WebServlet("/servicio/publicar")
@jakarta.servlet.annotation.MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 2,      // 2 MB
        maxRequestSize = 1024 * 1024 * 10   // 10 MB
)
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

        // Refactor: Extract parameter parsing to a method
        ServicioParams params = parseRequestParams(request);

        if (params.titulo.isEmpty() || params.descripcion.isEmpty()) {
            request.setAttribute("error", "Título y descripción son obligatorios.");
            request.setAttribute("categorias", categoriaDAO.listarTodas());
            request.getRequestDispatcher("/WEB-INF/jsp/servicio/publicar.jsp").forward(request, response);
            return;
        }

        if (params.error != null) {
            request.setAttribute("error", params.error);
            request.setAttribute("categorias", categoriaDAO.listarTodas());
            request.getRequestDispatcher("/WEB-INF/jsp/servicio/publicar.jsp").forward(request, response);
            return;
        }

        double precio = params.precio;
        int idCategoria = params.idCategoria;

        // Manejo de la foto
        Part filePart = request.getPart("foto");
        String fotoUrl = null;
        if (filePart != null && filePart.getSize() > 0) {
            String errorImagen = ImagenUtil.validarImagen(filePart);
            if (errorImagen != null) {
                request.setAttribute("error", errorImagen);
                request.setAttribute("categorias", categoriaDAO.listarTodas());
                request.getRequestDispatcher("/WEB-INF/jsp/servicio/publicar.jsp").forward(request, response);
                return;
            }

            try {
                fotoUrl = ImagenUtil.convertirABase64(filePart);
            } catch (IOException e) {
                request.setAttribute("error", "Error al procesar la imagen: " + e.getMessage());
                request.setAttribute("categorias", categoriaDAO.listarTodas());
                request.getRequestDispatcher("/WEB-INF/jsp/servicio/publicar.jsp").forward(request, response);
                return;
            }
        }

        HttpSession session  = request.getSession(false);
        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");

        Categoria categoria = categoriaDAO.buscarPorId(idCategoria).orElse(null);

        Servicio servicio = new Servicio();
        servicio.setTituloServicio(params.titulo);
        servicio.setDescripcionServicio(params.descripcion);
        servicio.setPrecioServicio(precio);
        servicio.setUsuario(usuarioActual);
        servicio.setCategoria(categoria);
        servicio.setEstado(EstadoServicio.ACTIVO);
        servicio.setDisponibilidad(true);
        servicio.setFechaPublicacionServicio(new Date());
        servicio.setFotoUrl(fotoUrl);

        servicioDAO.guardar(servicio);

        request.getSession().setAttribute("mensajeExito", "¡Servicio publicado correctamente!");
        response.sendRedirect(request.getContextPath() + "/home");
    }

    private ServicioParams parseRequestParams(HttpServletRequest request) {
        String titulo = request.getParameter("titulo") != null ? request.getParameter("titulo").trim() : "";
        String descripcion = request.getParameter("descripcion") != null ? request.getParameter("descripcion").trim() : "";
        String precioStr = request.getParameter("precio") != null ? request.getParameter("precio").trim() : "0";
        String catStr = request.getParameter("idCategoria") != null ? request.getParameter("idCategoria").trim() : "0";

        double precio = 0;
        int idCategoria = 0;
        String error = null;

        try {
            precio = Double.parseDouble(precioStr);
            idCategoria = Integer.parseInt(catStr);
        } catch (NumberFormatException e) {
            error = "Precio o categoría inválidos.";
        }

        return new ServicioParams(titulo, descripcion, precio, idCategoria, error);
    }

    private static class ServicioParams {
        String titulo, descripcion, error;
        double precio;
        int idCategoria;

        ServicioParams(String titulo, String descripcion, double precio, int idCategoria, String error) {
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.precio = precio;
            this.idCategoria = idCategoria;
            this.error = error;
        }
    }
}
