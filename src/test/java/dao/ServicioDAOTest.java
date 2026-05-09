package dao;

import modelo.Servicio;
import modelo.Usuario;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ServicioDAOTest {

    private final ServicioDAO servicioDAO = new ServicioDAO();

    @Test
    public void testListarActivosPorUsuario_Empty() {

        Usuario user = new Usuario();
        user.setIdUsuario(999);

        List<Servicio> result = servicioDAO.listarActivosPorUsuario(user.getIdUsuario());
        assertNotNull(result, "Should return a list (even if empty)");
    }
}
