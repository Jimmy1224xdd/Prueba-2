<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${servicio.tituloServicio} — PoliServis</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&family=Montserrat:wght@700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<%@ include file="../navbar.jsp" %>

<div class="container">
    <div class="hero-banner" style="min-height: auto; padding: 2rem;">
        <div class="hero-stripe"></div>
        <div class="epn-tag">${categoria.nombre}</div>
        <h1 style="font-size: 2.5rem;">${servicio.tituloServicio}</h1>
        <p style="font-size: 1.1rem; opacity: 0.9;">Publicado por 
            <a href="${pageContext.request.contextPath}/vendedor/perfil?id=${proveedor.idUsuario}" 
               style="color: white; text-decoration: underline; font-weight: 700;">
                ${proveedor.nombre}
            </a>
        </p>
    </div>

    <div class="grid" style="display: grid; grid-template-columns: 2fr 1fr; gap: 2rem; margin-top: 2rem;">
        <!-- Detalles del Servicio -->
        <div class="card">
            <div class="card-body">
                <div class="service-image-large mb-3" style="width: 100%; height: 300px; overflow: hidden; border-radius: 12px; background: #eee;">
                    <c:choose>
                        <c:when test="${not empty servicio.fotoUrl}">
                            <img src="${pageContext.request.contextPath}/uploads/servicios/${servicio.fotoUrl}" 
                                 alt="${servicio.tituloServicio}" style="width: 100%; height: 100%; object-fit: cover;">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/img/no-image.png" 
                                 alt="Sin imagen" style="width: 100%; height: 100%; object-fit: cover; opacity: 0.5;">
                        </c:otherwise>
                    </c:choose>
                </div>

                <h3 class="mb-2">Descripción del Servicio</h3>
                <p style="line-height: 1.6; color: var(--text-muted);">${servicio.descripcionServicio}</p>
                
                <hr class="my-3">
                
                <h3 class="mb-2">Calificaciones</h3>
                <c:choose>
                    <c:when test="${empty calificaciones}">
                        <p class="text-muted">Aún no hay calificaciones para este servicio.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="promedio-badge mb-3">
                            <span style="font-size: 1.5rem; font-weight: 800; color: var(--accent);">
                                <fmt:formatNumber value="${promedio}" pattern="0.0"/>
                            </span> / 5.0 ⭐
                        </div>
                        <c:forEach var="cal" items="${calificaciones}">
                            <div class="review-item mb-2" style="padding: 1rem; background: #f8f9fa; border-radius: 8px;">
                                <div class="d-flex justify-content-between">
                                    <strong>${cal.usuario.nombre}</strong>
                                    <span class="text-accent">${cal.estrellas} ⭐</span>
                                </div>
                                <p class="small text-muted mt-1">${cal.comentario}</p>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Barra lateral de acción -->
        <div class="sidebar">
            <div class="card" style="position: sticky; top: 2rem;">
                <div class="card-body text-center">
                    <div class="precio-tag" style="font-size: 2.5rem; font-weight: 800; color: var(--primary); margin-bottom: 1rem;">
                        $<fmt:formatNumber value="${servicio.precioServicio}" pattern="0.00"/>
                    </div>
                    
                    <c:choose>
                        <c:when test="${esMiServicio}">
                            <div class="alert alert-info" style="font-size: 0.9rem;">Este es tu propio servicio.</div>
                            <a href="${pageContext.request.contextPath}/servicio/editar?id=${servicio.idServicio}" class="btn btn-outline w-100 mb-2">Editar Servicio</a>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${yaSolicite}">
                                    <div class="alert alert-success" style="font-size: 0.9rem;">Ya has solicitado este servicio.</div>
                                    <a href="${pageContext.request.contextPath}/chat?userId=${proveedor.idUsuario}&servicioId=${servicio.idServicio}" class="btn btn-primary w-100">Ir al Chat</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/servicio/solicitar?id=${servicio.idServicio}" class="btn btn-accent w-100 py-3" style="font-size: 1.1rem; font-weight: 700;">
                                        ¡Solicitar Ahora!
                                    </a>
                                    <p class="text-muted mt-2" style="font-size: 0.8rem;">Pagarás directamente al compañero al finalizar.</p>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            
            <a href="${pageContext.request.contextPath}/home" class="btn btn-link w-100 mt-2">← Volver al inicio</a>
        </div>
    </div>
</div>

<style>
    .promedio-badge { background: #fff5f5; padding: 1rem; border-radius: 12px; display: inline-block; border: 1px solid #ffe3e3; }
    .text-accent { color: var(--accent); font-weight: 700; }
</style>

</body>
</html>
