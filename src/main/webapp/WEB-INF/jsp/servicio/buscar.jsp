<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Buscar servicios — PoliServis</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&family=Montserrat:wght@700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<%@ include file="../navbar.jsp" %>

<div class="container">
    <div class="page-header">
        <h1>Buscar servicios</h1>
        <c:choose>
            <c:when test="${not empty keyword}">
                <p>Resultados para <strong>"${keyword}"</strong> — ${totalResultados} encontrado(s)</p>
            </c:when>
            <c:otherwise>
                <p>Mostrando todos los servicios disponibles (${totalResultados})</p>
            </c:otherwise>
        </c:choose>
    </div>

    <%-- Buscador --%>
    <form action="${pageContext.request.contextPath}/servicio/buscar" method="get" class="search-bar mb-3">
        <input type="text" name="q" class="form-control"
               placeholder="Buscar servicios..."
               value="${not empty keyword ? keyword : ''}">
        <button type="submit" class="btn btn-primary">Buscar</button>
    </form>

    <%-- Filtro por categoría --%>
    <div class="d-flex gap-1 mb-3" style="flex-wrap:wrap">
        <a href="${pageContext.request.contextPath}/servicio/buscar"
           class="badge badge-primary" style="padding:.35rem .9rem;font-size:.82rem">
           Todas
        </a>
        <c:forEach var="cat" items="${categorias}">
            <a href="${pageContext.request.contextPath}/servicio/buscar?q=${cat.nombre}"
               class="badge badge-primary" style="padding:.35rem .9rem;font-size:.82rem">
               ${cat.nombre}
            </a>
        </c:forEach>
    </div>

    <%-- Resultados --%>
    <c:choose>
        <c:when test="${empty resultados}">
            <div class="empty-state">
                <div class="icon">🔍</div>
                <h3>No se encontraron servicios</h3>
                <p class="mt-1">Intenta con otras palabras clave</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="grid-servicios">
                <c:forEach var="srv" items="${resultados}">
                    <div class="card servicio-card">
                        <div class="card-body">
                            <span class="badge badge-success mb-1">Disponible</span>
                            <h3 class="card-title">${srv.tituloServicio}</h3>
                            <p class="card-text">${srv.descripcionServicio}</p>
                        </div>
                        <div class="card-footer">
                            <span class="precio">$<fmt:formatNumber value="${srv.precioServicio}" pattern="0.00"/></span>
                            <a href="${pageContext.request.contextPath}/servicio/detalle?id=${srv.idServicio}"
                               class="btn btn-outline btn-sm">Ver detalle</a>
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
