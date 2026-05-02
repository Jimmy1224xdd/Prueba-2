package util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import modelo.Usuario;

/**
 * EXTRACT CLASS — Gestión centralizada de la sesión HTTP.
 *
 * Antes: cada servlet manejaba la sesión directamente con getSession(false),
 * getAttribute("usuarioActual"), setAttribute, setMaxInactiveInterval, etc.
 * Esa responsabilidad estaba dispersa en 6+ clases distintas.
 *
 * Después: toda la lógica de sesión vive aquí. Si cambia el nombre del atributo
 * o la política de timeout, solo se modifica este archivo.
 */
public class GestorSesion {

    private static final String ATTR_USUARIO = "usuarioActual";
    private static final String ATTR_ROL     = "rolActual";
    private static final int    TIMEOUT_SEG  = 30 * 60; // 30 minutos

    /** Devuelve el usuario autenticado de la sesión, o null si no hay sesión activa. */
    public static Usuario getUsuarioActual(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (Usuario) session.getAttribute(ATTR_USUARIO);
    }

    /** Indica si hay una sesión activa con usuario autenticado. */
    public static boolean estaAutenticado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(ATTR_USUARIO) != null;
    }

    /** Crea la sesión y registra los atributos del usuario autenticado. */
    public static void iniciarSesion(HttpServletRequest request, Usuario usuario) {
        HttpSession session = request.getSession();
        session.setAttribute(ATTR_USUARIO, usuario);
        session.setAttribute(ATTR_ROL,     usuario.getRol().name());
        session.setMaxInactiveInterval(TIMEOUT_SEG);
    }

    /** Invalida la sesión actual si existe. */
    public static void cerrarSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
    }
}
