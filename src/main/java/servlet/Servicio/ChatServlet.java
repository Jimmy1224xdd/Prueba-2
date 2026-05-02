package servlet.Servicio;

import dao.ConversacionDAO;
import dao.MensajeDAO;
import dao.ServicioDAO;
import dao.SolicitudDAO;
import dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Conversacion;
import modelo.Mensaje;
import modelo.Servicio;
import modelo.Solicitud;
import modelo.Usuario;
import util.GestorSesion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {

    private final ConversacionDAO conversacionDAO = new ConversacionDAO();
    private final MensajeDAO mensajeDAO = new MensajeDAO();
    private final ServicioDAO servicioDAO = new ServicioDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final SolicitudDAO solicitudDAO = new SolicitudDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuarioActual = GestorSesion.getUsuarioActual(req);

        // Si no está logueado, redirigir al login
        if (usuarioActual == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String userIdStr = req.getParameter("userId");
        String servicioIdStr = req.getParameter("servicioId");

        // Sin parámetros → listar todas las conversaciones del usuario
        if (userIdStr == null || servicioIdStr == null) {
            try {
                List<Conversacion> misConversaciones = conversacionDAO.listarPorUsuario(usuarioActual);
                req.setAttribute("conversaciones", misConversaciones);
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("conversaciones", new ArrayList<>());
                req.setAttribute("errorDetalle", "No se pudieron cargar las conversaciones: " + e.getMessage());
            }
            req.getRequestDispatcher("/WEB-INF/jsp/chat/lista.jsp").forward(req, resp);
            return;
        }

        // Con parámetros → abrir/crear conversación específica
        try {
            int otherUserId = Integer.parseInt(userIdStr);
            int servicioId = Integer.parseInt(servicioIdStr);

            Optional<Servicio> optS = servicioDAO.buscarPorId(servicioId);
            Optional<Usuario> optU = usuarioDAO.buscarPorId(otherUserId);

            if (optS.isEmpty() || optU.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/chat");
                return;
            }

            Servicio servicio = optS.get();
            Usuario otroUsuario = optU.get();

            // Determinar quién es cliente y quién es proveedor
            Usuario proveedor = servicio.getUsuario();
            Usuario cliente;

            if (usuarioActual.getIdUsuario() == proveedor.getIdUsuario()) {
                // Yo soy el proveedor, el otro es el cliente
                cliente = otroUsuario;
            } else {
                // Yo soy el cliente
                cliente = usuarioActual;
            }

            // Buscar conversación existente o crear una nueva
            Optional<Conversacion> optC = conversacionDAO.buscarPorUsuariosYServicio(cliente, proveedor, servicioId);
            Conversacion conversacion;

            if (optC.isPresent()) {
                conversacion = optC.get();
            } else {
                conversacion = new Conversacion(cliente, proveedor, servicio);
                conversacionDAO.guardar(conversacion);
            }

            List<Mensaje> mensajes = mensajeDAO.listarPorConversacion(conversacion);

            Optional<Solicitud> optSolicitud = solicitudDAO.buscarPorUsuarioYServicio(cliente, servicio);
            if (optSolicitud.isPresent()) {
                req.setAttribute("estadoSolicitud", optSolicitud.get().getEstado().toString());
            }

            req.setAttribute("conversacion", conversacion);
            req.setAttribute("mensajes", mensajes);
            req.setAttribute("otroUsuario", otroUsuario);
            req.getRequestDispatcher("/WEB-INF/jsp/chat/detalle.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/chat");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("conversaciones", new ArrayList<>());
            req.setAttribute("errorDetalle", "Error al cargar el chat: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/chat/lista.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuarioActual = GestorSesion.getUsuarioActual(req);

        if (usuarioActual == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setCharacterEncoding("UTF-8");
        String convIdStr = req.getParameter("idConversacion");
        String texto = req.getParameter("mensaje");

        if (convIdStr == null || texto == null || texto.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/chat");
            return;
        }

        try {
            int convId = Integer.parseInt(convIdStr);
            Optional<Conversacion> optC = conversacionDAO.buscarPorId(convId);

            if (optC.isPresent()) {
                Conversacion conversacion = optC.get();

                boolean esParticipante =
                        conversacion.getCliente().getIdUsuario() == usuarioActual.getIdUsuario() ||
                        conversacion.getProveedor().getIdUsuario() == usuarioActual.getIdUsuario();

                if (esParticipante) {
                    Mensaje m = new Mensaje(conversacion, usuarioActual, texto.trim());
                    mensajeDAO.guardar(m);

                    int otherUserId = (conversacion.getCliente().getIdUsuario() == usuarioActual.getIdUsuario())
                            ? conversacion.getProveedor().getIdUsuario()
                            : conversacion.getCliente().getIdUsuario();

                    resp.sendRedirect(req.getContextPath() + "/chat?userId=" + otherUserId
                            + "&servicioId=" + conversacion.getServicio().getIdServicio());
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect(req.getContextPath() + "/chat");
    }
}
