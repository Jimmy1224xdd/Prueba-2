package Filtro;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter(urlPatterns = {"/home", "/servicio/*", "/chat"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        boolean autenticado = session != null && session.getAttribute("usuarioActual") != null;
        
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // URLs públicas
        if (path.equals("/home") || path.equals("/servicio/detalle") || path.equals("/servicio/buscar") || path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) {
            chain.doFilter(request, response);
            return;
        }

        if (autenticado) {
            chain.doFilter(request, response);
        } else {
            // Si intenta ir a publicar/solicitar sin login
            if (path.startsWith("/servicio/publicar") || path.startsWith("/servicio/solicitar")) {
                // Se envía redirect para que el UI muestre error o modal, pero si no se quiere redirigir 
                // podemos setear una variable para disparar el modal. En este caso redirigimos al login con mensaje.
                req.getSession().setAttribute("mensajeError", "Debes iniciar sesión para realizar esta acción.");
                resp.sendRedirect(req.getContextPath() + "/login");
            } else {
                resp.sendRedirect(req.getContextPath() + "/login");
            }
        }
    }
}
