package servlet;

import dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import modelo.Usuario;

import java.io.IOException;

@WebServlet("/guardarUsuario")
public class UsuarioServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");

        Usuario u = new Usuario();
        u.setNombre(nombre);

        UsuarioDAO dao = new UsuarioDAO();
        dao.guardar(u);

        response.sendRedirect("index.jsp");
    }
}