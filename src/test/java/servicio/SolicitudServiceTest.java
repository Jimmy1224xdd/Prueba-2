package servicio;

import dao.SolicitudDAO;
import modelo.Servicio;
import modelo.Solicitud;
import modelo.Usuario;
import modelo.types.EstadoSolicitud;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudServiceTest {

    private SolicitudDAO fakeDao;
    private SolicitudService solicitudService;

    @BeforeEach
    void setUp() {
        // Uso de dummy / fake impl para test unitario simple
        fakeDao = new SolicitudDAO() {
            @Override
            public Optional<Solicitud> buscarPorId(int id) {
                if (id == 1) {
                    Usuario prov = new Usuario(); prov.setIdUsuario(10);
                    Servicio serv = new Servicio(); serv.setUsuario(prov);
                    Solicitud sol = new Solicitud();
                    sol.setIdSolicitud(1);
                    sol.setServicio(serv);
                    sol.setEstado(EstadoSolicitud.SOLICITADO);
                    return Optional.of(sol);
                }
                return Optional.empty();
            }

            @Override
            public void actualizar(Solicitud solicitud) {
                // do nothing in test
            }
        };
        solicitudService = new SolicitudService(fakeDao);
    }

    @Test
    void testActualizarEstado_NoExiste() {
        Usuario u = new Usuario(); u.setIdUsuario(10);
        boolean res = solicitudService.actualizarEstadoSolicitud(99, u, EstadoSolicitud.EN_PROGRESO);
        assertFalse(res);
    }

    @Test
    void testActualizarEstado_UsuarioNoAutorizado() {
        Usuario u = new Usuario(); u.setIdUsuario(11); // Proveedor es 10
        boolean res = solicitudService.actualizarEstadoSolicitud(1, u, EstadoSolicitud.EN_PROGRESO);
        assertFalse(res);
    }

    @ParameterizedTest
    @EnumSource(EstadoSolicitud.class)
    void testActualizarEstado_ExitoParametrizado(EstadoSolicitud estado) {
        Usuario prov = new Usuario(); prov.setIdUsuario(10);
        boolean res = solicitudService.actualizarEstadoSolicitud(1, prov, estado);
        assertTrue(res);
    }
}
