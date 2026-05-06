<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Chat con ${otroUsuario.nombre} — PoliServis</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&family=Montserrat:wght@700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .chat-box { height: 400px; overflow-y: auto; background: #ffffff; padding: 1.5rem; border-radius: 12px; border: 1px solid var(--border); display: flex; flex-direction: column; gap: 1rem; box-shadow: inset 0 2px 4px rgba(0,0,0,0.02); }
        .msg { padding: 0.8rem 1.2rem; border-radius: 18px; max-width: 75%; word-wrap: break-word; position: relative; font-size: 0.95rem; line-height: 1.4; box-shadow: 0 2px 5px rgba(0,0,0,0.05); }
        .msg-mine { background: #1a4a7a; color: #ffffff !important; align-self: flex-end; border-bottom-right-radius: 4px; }
        .msg-other { background: #f0f2f5; color: #1c1e21 !important; align-self: flex-start; border-bottom-left-radius: 4px; }
        .msg-time { font-size: 0.7rem; opacity: 0.7; margin-top: 5px; display: block; text-align: right; }
    </style>
</head>
<body>
<%@ include file="../navbar.jsp" %>
<div class="container" style="max-width: 600px; margin: 2rem auto;">
    <div class="card">
        <div class="card-body">
            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1rem; padding-bottom:1rem; border-bottom:1px solid var(--border);">
                <div>
                    <h3 style="margin:0;">${otroUsuario.nombre}</h3>
                    <span class="text-muted" style="font-size:0.85rem;">${conversacion.servicio.tituloServicio}</span>
                    <c:if test="${not empty estadoSolicitud}">
                        <span class="badge ${estadoSolicitud == 'SOLICITADO' ? 'badge-primary' : (estadoSolicitud == 'EN_PROGRESO' ? 'badge-accent' : 'badge-secondary')}" style="font-size:0.7rem; margin-left:0.5rem; vertical-align:middle;">
                            ${estadoSolicitud}
                        </span>
                    </c:if>
                </div>
                <a href="${pageContext.request.contextPath}/chat" class="text-muted" style="font-size:0.9rem;">← Volver</a>
            </div>
            
            <div class="chat-box" id="chatBox">
                <c:if test="${empty mensajes}">
                    <p class="text-center text-muted" style="margin:auto;">Envía un mensaje para iniciar la conversación.</p>
                </c:if>
                <c:forEach var="msg" items="${mensajes}">
                    <div class="msg ${msg.remitente.idUsuario == sessionScope.usuarioActual.idUsuario ? 'msg-mine' : 'msg-other'}">
                        ${msg.contenido}
                        <span class="msg-time"><fmt:formatDate value="${msg.fechaEnvio}" pattern="HH:mm"/></span>
                    </div>
                </c:forEach>
            </div>
            
            <form id="chatForm" action="${pageContext.request.contextPath}/chat" method="post" style="display:flex; gap:0.5rem; margin-top:1rem;">
                <input type="hidden" name="idConversacion" value="${conversacion.idConversacion}">
                <input type="text" name="mensaje" class="form-control" placeholder="Escribe un mensaje..." required autocomplete="off" style="flex:1;">
                <button type="submit" class="btn btn-primary">Enviar</button>
            </form>
        </div>
    </div>
</div>
<script>
    const chatBox = document.getElementById("chatBox");
    let messageCount = chatBox.querySelectorAll('.msg').length;

    function scrollAlFinal() {
        chatBox.scrollTop = chatBox.scrollHeight;
    }
    
    // Scroll inicial
    scrollAlFinal();

    // Polling cada 2 segundos
    setInterval(() => {
        fetch(window.location.href)
            .then(res => res.text())
            .then(html => {
                const parser = new DOMParser();
                const doc = parser.parseFromString(html, 'text/html');
                const newChatBox = doc.getElementById('chatBox');
                if (newChatBox) {
                    const newMessages = newChatBox.querySelectorAll('.msg');
                    if (newMessages.length > messageCount) {
                        chatBox.innerHTML = newChatBox.innerHTML;
                        messageCount = newMessages.length;
                        scrollAlFinal();
                    }
                }
            })
            .catch(err => console.error("Error actualizando el chat:", err));
    }, 2000);

    // Envío del formulario por AJAX
    const form = document.getElementById('chatForm');
    if (form) {
        form.addEventListener('submit', (e) => {
        e.preventDefault();
        const inputMensaje = form.querySelector('input[name="mensaje"]');
        const mensajeText = inputMensaje.value.trim();
        if (!mensajeText) return;

        const formData = new URLSearchParams(new FormData(form));
        
        // Limpiamos el input inmediatamente para mejor UX
        inputMensaje.value = '';

        fetch(form.action, {
            method: 'POST',
            body: formData,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).then(res => {
            if (!res.ok) throw new Error("Error en el servidor");
            // Actualizamos inmediatamente el chat tras enviar
            fetch(window.location.href)
                .then(res => res.text())
                .then(html => {
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(html, 'text/html');
                    const newChatBox = doc.getElementById('chatBox');
                    if (newChatBox) {
                        chatBox.innerHTML = newChatBox.innerHTML;
                        messageCount = newChatBox.querySelectorAll('.msg').length;
                        scrollAlFinal();
                    }
                });
        }).catch(err => {
            console.error("Error enviando mensaje:", err);
            alert("No se pudo enviar el mensaje. Revisa tu conexión.");
        });
        });
    }
</script>
</body>
</html>
