package servlet.Servicio;

import dao.CalificacionDAO;
import dao.ServicioDAO;
import dao.UsuarioDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Usuario;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class PerfilVendedorServletTest {

    @Test
    public void testDoGet_ValidId() throws Exception {
        PerfilVendedorServlet servlet = new PerfilVendedorServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        
        when(request.getParameter("id")).thenReturn("1");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        
        servlet.doGet(request, response);
        
        verify(request).setAttribute(eq("vendedor"), any());
        verify(request).setAttribute(eq("serviciosActivos"), any());
        verify(request).setAttribute(eq("promedio"), anyDouble());
        verify(dispatcher).forward(request, response);
    }
}
