<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Servicios — PoliServis</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&family=Montserrat:wght@700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script>
        function confirmarEliminacion(nombreServicio) {
            return confirm("¿Seguro que quieres eliminar el servicio '" + nombreServicio + "'?");
        }
    </script>
</head>
<body>
<%@ include file="../navbar.jsp" %>
<div class="container">
    <h2>Mis Servicios</h2>

    <c:if test="${not empty sessionScope.mensajeExito}">
        <div class="alert alert-success">${sessionScope.mensajeExito}</div>
        <c:remove var="mensajeExito" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.mensajeError}">
        <div class="alert alert-danger">${sessionScope.mensajeError}</div>
        <c:remove var="mensajeError" scope="session"/>
    </c:if>

    <a href="${pageContext.request.contextPath}/servicio/publicar" class="btn btn-primary mb-3">Publicar nuevo servicio</a>

    <c:choose>
        <c:when test="${empty misServicios}">
            <p>Aún no has publicado ningún servicio.</p>
        </c:when>
        <c:otherwise>
            <table class="table" style="width:100%; border-collapse:collapse; margin-top:1rem;">
                <thead>
                    <tr style="border-bottom:2px solid var(--border); text-align:left;">
                        <th style="padding:1rem;">Título</th>
                        <th style="padding:1rem;">Precio</th>
                        <th style="padding:1rem;">Estado</th>
                        <th style="padding:1rem;">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="srv" items="${misServicios}">
                        <tr style="border-bottom:1px solid var(--border);">
                            <td style="padding:1rem;">${srv.tituloServicio}</td>
                            <td style="padding:1rem;">$<fmt:formatNumber value="${srv.precioServicio}" pattern="0.00"/></td>
                            <td style="padding:1rem;">
                                <span class="badge ${srv.estado == 'ACTIVO' ? 'badge-primary' : 'badge-secondary'}">
                                    ${srv.estado}
                                </span>
                            </td>
                            <td style="padding:1rem;">
                                <a href="${pageContext.request.contextPath}/servicio/editar?id=${srv.idServicio}" class="btn btn-sm btn-outline">Editar</a>
                                <form action="${pageContext.request.contextPath}/servicio/eliminar" method="post" style="display:inline;" onsubmit="return confirmarEliminacion('${srv.tituloServicio}');">
                                    <input type="hidden" name="idServicio" value="${srv.idServicio}">
                                    <button type="submit" class="btn btn-sm" style="background:#e74c3c; color:white; border:none;">Eliminar</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
