package servlet.auth;

import dao.UsuarioDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import modelo.Usuario;
import modelo.types.Rol;
import util.GestorSesion;

import java.io.IOException;

@WebServlet("/register")
public class RegistroServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (GestorSesion.estaAutenticado(request)) { // EXTRACT CLASS
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nombre     = request.getParameter("nombre")     != null ? request.getParameter("nombre").trim()     : "";
        String correo     = request.getParameter("correo")     != null ? request.getParameter("correo").trim()     : "";
        String contrasena = request.getParameter("contrasena") != null ? request.getParameter("contrasena").trim() : "";
        String confirmar  = request.getParameter("confirmar")  != null ? request.getParameter("confirmar").trim()  : "";

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            reenviarConError(request, response, "Todos los campos son obligatorios.", null, null);
            return;
        }

        if (!contrasena.equals(confirmar)) {
            reenviarConError(request, response, "Las contraseñas no coinciden.", nombre, correo);
            return;
        }

        if (contrasena.length() < 6) {
            reenviarConError(request, response, "La contraseña debe tener al menos 6 caracteres.", nombre, correo);
            return;
        }

        if (usuarioDAO.existeCorreo(correo)) {
            reenviarConError(request, response, "El correo ya está registrado.", nombre, null);
            return;
        }

        Usuario usuario = new Usuario(nombre, correo, contrasena, Rol.ESTUDIANTE);
        usuarioDAO.guardar(usuario);

        GestorSesion.iniciarSesion(request, usuario); // EXTRACT CLASS
        response.sendRedirect(request.getContextPath() + "/home");
    }

    // ── EXTRACT METHOD ────────────────────────────────────────────────────────
    // Centraliza el patrón repetido: setear error + (opcionalmente nombre/correo) + forward al registro
    private void reenviarConError(HttpServletRequest request, HttpServletResponse response,
                                  String error, String nombre, String correo)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        if (nombre != null) request.setAttribute("nombre", nombre);
        if (correo  != null) request.setAttribute("correo",  correo);
        request.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(request, response);
    }
}
