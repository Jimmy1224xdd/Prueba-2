package servlet.Servicio;

import dao.CategoriaDAO;
import dao.ServicioDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.Usuario;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class PublicarServicioServletTest {

    @Test
    public void testDoPost_MissingParams() throws Exception {
        PublicarServicioServlet servlet = new PublicarServicioServlet();
        
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        
        when(request.getParameter("titulo")).thenReturn("");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        
        servlet.doPost(request, response);
        
        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
    }
}
