package servicio;

import dao.ConversacionDAO;
import dao.MensajeDAO;
import modelo.Conversacion;
import modelo.Mensaje;
import modelo.Servicio;
import modelo.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ConversacionDAO conversacionDAO;
    
    @Mock
    private MensajeDAO mensajeDAO;

    private ChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new ChatService(conversacionDAO, mensajeDAO);
    }

    @Test
    void testObtenerOCrearConversacion_Existente() {
        Usuario cliente = new Usuario(); cliente.setIdUsuario(1);
        Usuario prov = new Usuario(); prov.setIdUsuario(2);
        Servicio serv = new Servicio(); serv.setIdServicio(10);
        
        Conversacion esperada = new Conversacion(cliente, prov, serv);
        when(conversacionDAO.buscarPorUsuariosYServicio(cliente, prov, 10))
                .thenReturn(Optional.of(esperada));

        Conversacion obtenida = chatService.obtenerOCrearConversacion(cliente, prov, serv);

        assertEquals(esperada, obtenida);
        verify(conversacionDAO, never()).guardar(any(Conversacion.class));
    }

    @Test
    void testEnviarMensaje_Exito() {
        Usuario remitente = new Usuario(); remitente.setIdUsuario(1);
        Usuario prov = new Usuario(); prov.setIdUsuario(2);
        Conversacion conv = new Conversacion(remitente, prov, new Servicio());
        
        when(conversacionDAO.buscarPorId(100)).thenReturn(Optional.of(conv));

        chatService.enviarMensaje(100, remitente, "Hola");

        ArgumentCaptor<Mensaje> captor = ArgumentCaptor.forClass(Mensaje.class);
        verify(mensajeDAO).guardar(captor.capture());
        assertEquals("Hola", captor.getValue().getContenido());
    }

    @Test
    void testEnviarMensaje_RemitenteNoPertenece() {
        Usuario c1 = new Usuario(); c1.setIdUsuario(1);
        Usuario p1 = new Usuario(); p1.setIdUsuario(2);
        Usuario intruso = new Usuario(); intruso.setIdUsuario(3);
        
        Conversacion conv = new Conversacion(c1, p1, new Servicio());
        when(conversacionDAO.buscarPorId(100)).thenReturn(Optional.of(conv));

        assertThrows(IllegalArgumentException.class, () -> {
            chatService.enviarMensaje(100, intruso, "Spying");
        });
    }
}
