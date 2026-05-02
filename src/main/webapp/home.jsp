<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inicio — PoliServis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<%@ include file="navbar.jsp" %>

<div class="container">

    <%-- Mensaje de éxito tras publicar --%>
    <c:if test="${not empty sessionScope.mensajeExito}">
        <div class="alert alert-success mt-3">${sessionScope.mensajeExito}</div>
        <c:remove var="mensajeExito" scope="session"/>
    </c:if>

    <%-- Hero banner EPN --%>
    <div class="hero-banner">
        <div class="epn-tag">Escuela Politécnica Nacional</div>
        <h1>Hola, <c:choose>
            <c:when test="${not empty sessionScope.usuarioActual}">${sessionScope.usuarioActual.nombre}</c:when>
            <c:otherwise>estudiante</c:otherwise>
        </c:choose> 👋</h1>
        <p>Explora los servicios que tus compañeros de la EPN tienen para ofrecerte, o publica los tuyos.</p>
        <a href="${pageContext.request.contextPath}/servicio/publicar" class="btn btn-accent">
            + Publicar mi servicio
        </a>
    </div>

    <%-- Barra de búsqueda --%>
    <form action="${pageContext.request.contextPath}/servicio/buscar" method="get" class="search-bar mb-3">
        <input type="text" name="q" class="form-control" placeholder="Buscar servicios...">
        <button type="submit" class="btn btn-primary">Buscar</button>
    </form>

    <%-- Categorías --%>
    <div class="d-flex gap-1 mb-3" style="flex-wrap:wrap">
        <c:forEach var="cat" items="${categorias}">
            <a href="${pageContext.request.contextPath}/servicio/buscar?q=${cat.nombre}"
               class="badge badge-primary" style="padding:.35rem .9rem;font-size:.8rem">
               ${cat.nombre}
            </a>
        </c:forEach>
    </div>

    <%-- Grid de servicios --%>
    <c:choose>
        <c:when test="${empty servicios}">
            <div class="empty-state">
                <div class="icon">📭</div>
                <h3>Aún no hay servicios publicados</h3>
                <p class="mt-1">¡Sé el primero en ofrecer un servicio a tus compañeros!</p>
                <a href="${pageContext.request.contextPath}/servicio/publicar" class="btn btn-primary mt-2">
                    Publicar servicio
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="grid-servicios">
                <c:forEach var="srv" items="${servicios}">
                    <div class="card servicio-card">
                        <div class="card-body">
                            <span class="badge badge-primary mb-1">Activo</span>
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
