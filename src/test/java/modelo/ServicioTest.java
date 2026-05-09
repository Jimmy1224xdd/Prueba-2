package modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class ServicioTest {

    @Test
    public void testEsPropietario_True() {
        Usuario owner = new Usuario();
        owner.setIdUsuario(1);
        
        Servicio srv = new Servicio();
        srv.setUsuario(owner);
        
        assertTrue(srv.esPropietario(owner));
    }

    @Test
    public void testEsPropietario_False() {
        Usuario owner = new Usuario();
        owner.setIdUsuario(1);
        
        Usuario other = new Usuario();
        other.setIdUsuario(2);
        
        Servicio srv = new Servicio();
        srv.setUsuario(owner);
        
        assertFalse(srv.esPropietario(other));
    }

    @ParameterizedTest
    @ValueSource(doubles = {10.0, 50.5, 100.99})
    public void testSetPrecioServicio(double precio) {
        Servicio srv = new Servicio();
        srv.setPrecioServicio(precio);
        assertEquals(precio, srv.getPrecioServicio());
    }
}
