<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
        Poli<span class="brand-servis">Servis</span>
        <span class="epn-pill">EPN</span>
    </a>
    <div class="navbar-nav">
        <a href="${pageContext.request.contextPath}/home">Inicio</a>
        <a href="${pageContext.request.contextPath}/servicio/buscar">Buscar</a>
        <c:if test="${not empty sessionScope.usuarioActual}">
            <a href="${pageContext.request.contextPath}/servicio/publicar">Publicar servicio</a>
            <a href="${pageContext.request.contextPath}/servicio/mis-servicios">Mis servicios</a>
            <a href="${pageContext.request.contextPath}/servicio/mis-solicitudes">Mis solicitudes</a>
            <a href="${pageContext.request.contextPath}/chat">Chat</a>
            <%-- Enlace a notificaciones con badge de no leídas --%>
            <a href="${pageContext.request.contextPath}/notificaciones"
               style="position:relative;display:inline-flex;align-items:center;gap:.3rem">
                🔔 Notificaciones
                <c:if test="${not empty sessionScope.notifNoLeidas and sessionScope.notifNoLeidas > 0}">
                    <span style="
                        background:var(--accent);
                        color:#fff;
                        font-size:.65rem;
                        font-weight:700;
                        padding:.1rem .4rem;
                        border-radius:999px;
                        line-height:1.4;
                        min-width:1.2rem;
                        text-align:center;
                    ">${sessionScope.notifNoLeidas}</span>
                </c:if>
            </a>
            <span class="text-muted" style="font-size:.85rem">
                Hola, <strong>${sessionScope.usuarioActual.nombre}</strong>
            </span>
            <form action="${pageContext.request.contextPath}/logout" method="post" style="margin:0">
                <button type="submit" class="btn btn-outline btn-sm">Cerrar sesión</button>
            </form>
        </c:if>
        <c:if test="${empty sessionScope.usuarioActual}">
            <a href="${pageContext.request.contextPath}/login">Iniciar sesión</a>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-accent btn-sm">Registrarse</a>
        </c:if>
    </div>
</nav>

<!-- Modal de Autenticación para Invitados -->
<c:if test="${empty sessionScope.usuarioActual}">
    <div id="authModal" class="modal" style="display:none; position:fixed; z-index:1000; left:0; top:0; width:100%; height:100%; background-color:rgba(0,0,0,0.5); align-items:center; justify-content:center;">
        <div style="background:#fff; padding:2rem; border-radius:8px; max-width:400px; width:90%; box-shadow:0 10px 25px rgba(0,0,0,0.2);">
            <h2 style="margin-top:0; color:var(--text-main);">Inicia Sesión</h2>
            <p style="color:var(--text-secondary); margin-bottom:1.5rem;">Debes iniciar sesión para realizar esta acción.</p>
            <form action="${pageContext.request.contextPath}/login" method="post">
                <input type="email" name="correo" placeholder="Correo institucional" required class="form-control mb-2" style="width:100%;">
                <input type="password" name="contrasena" placeholder="Contraseña" required class="form-control mb-2" style="width:100%;">
                <button type="submit" class="btn btn-primary btn-full mb-1">Entrar</button>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-outline btn-full text-center" style="display:block; text-align:center;">Registrarse</a>
                <button type="button" id="closeModalBtn" class="btn btn-sm mt-2" style="width:100%; background:transparent; border:none; color:var(--text-secondary);">Cancelar</button>
            </form>
        </div>
    </div>

    <script>
        document.addEventListener("DOMContentLoaded", function() {
            var modal = document.getElementById("authModal");
            var closeBtn = document.getElementById("closeModalBtn");
            
            // Cerrar modal
            closeBtn.onclick = function() {
                modal.style.display = "none";
            }
            
            // Interceptar clicks en elementos que requieren autenticación
            var reqAuthElements = document.querySelectorAll(".req-auth");
            reqAuthElements.forEach(function(el) {
                el.addEventListener("click", function(e) {
                    e.preventDefault();
                    modal.style.display = "flex";
                });
            });
            
            window.onclick = function(event) {
                if (event.target == modal) {
                    modal.style.display = "none";
                }
            }
        });
    </script>
</c:if>
