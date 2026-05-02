<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Solicitudes Recibidas — PoliServis</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&family=Montserrat:wght@700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="../navbar.jsp" %>
<div class="container">
    <div style="display:flex; justify-content:space-between; align-items:center;">
        <h2>Solicitudes Recibidas</h2>
        <a href="${pageContext.request.contextPath}/servicio/mis-solicitudes" class="btn btn-outline btn-sm">Ver mis solicitudes enviadas</a>
    </div>
    
    <c:if test="${not empty sessionScope.mensajeExito}">
        <div class="alert alert-success">${sessionScope.mensajeExito}</div>
        <c:remove var="mensajeExito" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.mensajeError}">
        <div class="alert alert-danger">${sessionScope.mensajeError}</div>
        <c:remove var="mensajeError" scope="session"/>
    </c:if>

    <c:choose>
        <c:when test="${empty solicitudesRecibidas}">
            <p class="mt-2 text-muted">No has recibido solicitudes para tus servicios.</p>
        </c:when>
        <c:otherwise>
            <div class="mt-2" style="display:grid; gap:1rem;">
                <c:forEach var="sol" items="${solicitudesRecibidas}">
                    <div class="card p-3" style="display:flex; justify-content:space-between; align-items:center;">
                        <div>
                            <strong>${sol.servicio.tituloServicio}</strong><br>
                            <span class="text-muted" style="font-size:0.85rem">Solicitado por: ${sol.usuario.nombre} | Fecha: <fmt:formatDate value="${sol.fechaSolicitud}" pattern="dd/MM/yyyy"/></span>
                        </div>
                        <div style="display:flex; gap:1rem; align-items:center;">
                            <span class="badge ${sol.estado == 'SOLICITADO' ? 'badge-primary' : (sol.estado == 'EN_PROGRESO' ? 'badge-accent' : 'badge-secondary')}">
                                ${sol.estado}
                            </span>
                            <form action="${pageContext.request.contextPath}/servicio/solicitudes-recibidas" method="post" style="display:flex; gap:0.5rem; align-items:center; margin:0;">
                                <input type="hidden" name="idSolicitud" value="${sol.idSolicitud}">
                                <select name="estado" class="form-control" style="padding:0.25rem; font-size:0.85rem;" onchange="this.form.submit()">
                                    <option value="SOLICITADO" ${sol.estado == 'SOLICITADO' ? 'selected' : ''}>Solicitado</option>
                                    <option value="EN_PROGRESO" ${sol.estado == 'EN_PROGRESO' ? 'selected' : ''}>En Progreso</option>
                                    <option value="FINALIZADO" ${sol.estado == 'FINALIZADO' ? 'selected' : ''}>Finalizado</option>
                                </select>
                            </form>
                            <a href="${pageContext.request.contextPath}/chat?userId=${sol.usuario.idUsuario}&servicioId=${sol.servicio.idServicio}" class="btn btn-outline btn-sm">Chat</a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
