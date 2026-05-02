<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis solicitudes — PoliServis</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&family=Montserrat:wght@700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<%@ include file="../navbar.jsp" %>

<div class="container">
    <div style="display:flex; justify-content:space-between; align-items:center;">
        <h2>Mis Servicios Solicitados</h2>
        <a href="${pageContext.request.contextPath}/servicio/solicitudes-recibidas" class="btn btn-outline btn-sm">Ver solicitudes recibidas</a>
    </div>
    <p>Servicios que has contratado — desde aquí puedes calificarlos y comunicarte</p>

    <%-- Mensajes de sesión --%>
    <c:if test="${not empty sessionScope.mensajeExito}">
        <div class="alert alert-success mt-2">${sessionScope.mensajeExito}</div>
        <c:remove var="mensajeExito" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.mensajeError}">
        <div class="alert alert-danger mt-2">${sessionScope.mensajeError}</div>
        <c:remove var="mensajeError" scope="session"/>
    </c:if>

    <c:choose>
        <c:when test="${empty solicitudes}">
            <div class="empty-state">
                <div class="icon">📋</div>
                <h3>Aún no tienes servicios solicitados</h3>
                <p class="mt-1">Explora el catálogo y solicita el servicio que necesites</p>
                <a href="${pageContext.request.contextPath}/home" class="btn btn-primary mt-2">
                    Ver servicios disponibles
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="grid-servicios">
                <c:forEach var="sol" items="${solicitudes}">
                    <div class="card servicio-card">
                        <div class="card-body">
                            <span class="badge badge-primary mb-1">Solicitado</span>
                            <h3 class="card-title">${sol.servicio.tituloServicio}</h3>
                            <p class="card-text">${sol.servicio.descripcionServicio}</p>
                            <p class="text-muted" style="font-size:.82rem">
                                Proveedor: <strong>${sol.servicio.usuario.nombre}</strong>
                            </p>
                            <p class="text-muted" style="font-size:.8rem">
                                Solicitado el:
                                <fmt:formatDate value="${sol.fechaSolicitud}" pattern="dd/MM/yyyy"/>
                            </p>
                        </div>
                        <div class="card-footer">
                            <span class="precio">
                                $<fmt:formatNumber value="${sol.servicio.precioServicio}" pattern="0.00"/>
                            </span>
                            <div>
                                <span class="badge ${sol.estado == 'SOLICITADO' ? 'badge-primary' : (sol.estado == 'EN_PROGRESO' ? 'badge-accent' : 'badge-secondary')} mb-1" style="display:inline-block;">
                                    ${sol.estado}
                                </span><br>
                                <a href="${pageContext.request.contextPath}/servicio/detalle?id=${sol.servicio.idServicio}" class="btn btn-primary btn-sm mt-1">Ver servicio</a>
                                <a href="${pageContext.request.contextPath}/chat?userId=${sol.servicio.usuario.idUsuario}&servicioId=${sol.servicio.idServicio}" class="btn btn-outline btn-sm mt-1">Chat proveedor</a>
                                <a href="${pageContext.request.contextPath}/servicio/calificar?id=${sol.servicio.idServicio}"
                                   class="btn btn-accent btn-sm mt-1">⭐ Calificar</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

    <div style="height:3rem"></div>
</div>

</body>
</html>
