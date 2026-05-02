package servlet.Servicio;

import dao.ServicioDAO;
import dao.CategoriaDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import modelo.Servicio;

import java.io.IOException;
import java.util.List;

@WebServlet("/servicio/buscar")
public class BuscarServicioServlet extends HttpServlet {

    private final ServicioDAO servicioDAO = new ServicioDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // REPLACE TEMP WITH QUERY: 'keyword' eliminada; se invoca el método directamente
        List<Servicio> resultados = obtenerParametroTrimmed(req, "q").isEmpty()
                ? servicioDAO.listarActivos()
                : servicioDAO.buscarPorPalabraClave(obtenerParametroTrimmed(req, "q"));

        req.setAttribute("resultados",      resultados);
        req.setAttribute("keyword",         obtenerParametroTrimmed(req, "q"));
        req.setAttribute("categorias",      categoriaDAO.listarTodas());
        req.setAttribute("totalResultados", resultados.size());

        req.getRequestDispatcher("/WEB-INF/jsp/servicio/buscar.jsp").forward(req, resp);
    }

    // ── REPLACE TEMP WITH QUERY ───────────────────────────────────────────────
    // Reemplaza la variable temporal con un método que ejecuta la "consulta" al parámetro HTTP.
    // La expresión que antes vivía en 'keyword' ahora es reutilizable desde cualquier método.
    private String obtenerParametroTrimmed(HttpServletRequest req, String nombre) {
        String valor = req.getParameter(nombre);
        return valor != null ? valor.trim() : "";
    }
}
