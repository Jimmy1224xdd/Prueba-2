package servlet.auth;

import dao.UsuarioDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import modelo.Usuario;
import util.GestorSesion;

import java.io.IOException;
import java.util.Optional;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (GestorSesion.estaAutenticado(request)) { // EXTRACT CLASS
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String correo     = request.getParameter("correo")     != null ? request.getParameter("correo").trim()     : "";
        String contrasena = request.getParameter("contrasena") != null ? request.getParameter("contrasena").trim() : "";

        if (correo.isEmpty() || contrasena.isEmpty()) {
            request.setAttribute("error", "Correo y contraseña son obligatorios.");
            request.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(request, response);
            return;
        }

        Optional<Usuario> resultado = usuarioDAO.login(correo, contrasena);

        if (resultado.isPresent()) {
            GestorSesion.iniciarSesion(request, resultado.get()); // EXTRACT CLASS
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            request.setAttribute("error", "Correo o contraseña incorrectos.");
            request.setAttribute("correo", correo);
            request.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(request, response);
        }
    }
}
