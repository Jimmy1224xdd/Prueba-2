package servicio;

import dao.SolicitudDAO;
import modelo.Solicitud;
import modelo.Usuario;
import modelo.types.EstadoSolicitud;

import java.util.Optional;

public class SolicitudService {

    private final SolicitudDAO solicitudDAO;

    public SolicitudService(SolicitudDAO solicitudDAO) {
        this.solicitudDAO = solicitudDAO;
    }

    public boolean actualizarEstadoSolicitud(int idSolicitud, Usuario actualizador, EstadoSolicitud nuevoEstado) {
        Optional<Solicitud> opt = solicitudDAO.buscarPorId(idSolicitud);
        if (opt.isEmpty()) {
            return false;
        }

        Solicitud solicitud = opt.get();
        
        // REFACTOR 2: Extract Method para validación
        if (!puedeActualizar(solicitud, actualizador)) {
            return false;
        }

        solicitud.setEstado(nuevoEstado);
        solicitudDAO.actualizar(solicitud);
        return true;
    }

    private boolean puedeActualizar(Solicitud solicitud, Usuario usuario) {
        return solicitud.getServicio().getUsuario().getIdUsuario() == usuario.getIdUsuario();
    }
}
