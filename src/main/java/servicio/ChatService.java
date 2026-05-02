package servicio;

import dao.ConversacionDAO;
import dao.MensajeDAO;
import modelo.Conversacion;
import modelo.Mensaje;
import modelo.Servicio;
import modelo.Usuario;

import java.util.List;
import java.util.Optional;

public class ChatService {

    private final ConversacionDAO conversacionDAO;
    private final MensajeDAO mensajeDAO;

    public ChatService(ConversacionDAO conversacionDAO, MensajeDAO mensajeDAO) {
        this.conversacionDAO = conversacionDAO;
        this.mensajeDAO = mensajeDAO;
    }

    public Conversacion obtenerOCrearConversacion(Usuario cliente, Usuario proveedor, Servicio servicio) {
        if (cliente == null || proveedor == null || servicio == null) {
            throw new IllegalArgumentException("Parámetros inválidos para crear conversación");
        }

        // REFACTOR 3: Eliminar Magic Numbers (no hay límite, así que se quitó la validación estática de límite)
        // Antes íbamos a tener un maxConversaciones = 5, pero el usuario pidió sin límite.

        Optional<Conversacion> opt = conversacionDAO.buscarPorUsuariosYServicio(cliente, proveedor, servicio.getIdServicio());
        if (opt.isPresent()) {
            return opt.get();
        }

        Conversacion nueva = new Conversacion(cliente, proveedor, servicio);
        conversacionDAO.guardar(nueva);
        return nueva;
    }

    public Mensaje enviarMensaje(int idConversacion, Usuario remitente, String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }

        Optional<Conversacion> opt = conversacionDAO.buscarPorId(idConversacion);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Conversación no existe");
        }

        Conversacion conv = opt.get();
        if (conv.getCliente().getIdUsuario() != remitente.getIdUsuario() && 
            conv.getProveedor().getIdUsuario() != remitente.getIdUsuario()) {
            throw new IllegalArgumentException("El remitente no pertenece a la conversación");
        }

        Mensaje mensaje = new Mensaje(conv, remitente, texto.trim());
        mensajeDAO.guardar(mensaje);
        return mensaje;
    }
}
