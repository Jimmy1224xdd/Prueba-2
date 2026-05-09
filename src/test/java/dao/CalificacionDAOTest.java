package dao;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalificacionDAOTest {

    private final CalificacionDAO calificacionDAO = new CalificacionDAO();

    @Test
    public void testCalcularPromedioPorUsuario_NoReviews() {
        // TDD: Method doesn't exist or returns 0.0
        double result = calificacionDAO.calcularPromedioPorUsuario(999);
        assertEquals(0.0, result, "Should return 0.0 if user has no reviews");
    }
}
