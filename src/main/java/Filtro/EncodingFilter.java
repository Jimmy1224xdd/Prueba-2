package Filtro;

import jakarta.servlet.*;

import java.io.IOException;

// Registro via web.xml — NO usar @WebFilter para evitar registro duplicado
public class EncodingFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        chain.doFilter(request, response);
    }
}