<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Chats — PoliServis</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&family=Montserrat:wght@700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="../navbar.jsp" %>
<div class="container">
    <h2>Mis Conversaciones</h2>
    
    <c:if test="${not empty errorDetalle}">
        <div class="alert alert-danger mt-2" style="background:#fee; border:1px solid #c00; padding:1rem; border-radius:4px;">
            <strong>Error técnico:</strong> ${errorDetalle}
        </div>
    </c:if>
    
    <c:choose>
        <c:when test="${empty conversaciones}">
            <p class="text-muted mt-2">Aún no tienes conversaciones.</p>
        </c:when>
        <c:otherwise>
            <div class="mt-3" style="display:grid; gap:1rem; max-width:600px;">
                <c:forEach var="conv" items="${conversaciones}">
                    <c:set var="otroUsuario" value="${conv.cliente.idUsuario == sessionScope.usuarioActual.idUsuario ? conv.proveedor : conv.cliente}"/>
                    <div class="card p-3">
                        <div style="display:flex; justify-content:space-between; align-items:center;">
                            <div>
                                <strong>${otroUsuario.nombre}</strong>
                                <p class="text-muted m-0" style="font-size:0.85rem">Servicio: ${conv.servicio.tituloServicio}</p>
                            </div>
                            <a href="${pageContext.request.contextPath}/chat?userId=${otroUsuario.idUsuario}&servicioId=${conv.servicio.idServicio}" class="btn btn-primary btn-sm">Abrir Chat</a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
