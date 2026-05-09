<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil de ${vendedor.nombre} — PoliServis</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&family=Montserrat:wght@700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<%@ include file="../navbar.jsp" %>

<div class="container">
    <div class="hero-banner" style="min-height: auto; padding: 3rem 2rem; text-align: center;">
        <div class="hero-stripe"></div>
        <div class="profile-avatar mb-2" style="width: 100px; height: 100px; background: white; border-radius: 50%; margin: 0 auto; display: flex; align-items: center; justify-content: center; font-size: 3rem; color: var(--primary); box-shadow: var(--shadow);">
            ${vendedor.nombre.substring(0,1)}
        </div>
        <h1 style="font-size: 2.5rem; margin-bottom: 0.5rem;">${vendedor.nombre}</h1>
        <div class="rating-badge" style="background: rgba(255,255,255,0.2); display: inline-block; padding: 0.5rem 1rem; border-radius: 20px; font-weight: 700;">
            <c:choose>
                <c:when test="${promedio > 0}">
                    <fmt:formatNumber value="${promedio}" pattern="0.0"/> ⭐ Promedio
                </c:when>
                <c:otherwise>
                    Sin calificaciones aún
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="grid" style="display: grid; grid-template-columns: 1fr 2fr; gap: 2rem; margin-top: 2rem;">
        <!-- Información del Vendedor -->
        <div class="sidebar">
            <div class="card">
                <div class="card-body">
                    <h3 class="mb-2">Sobre el vendedor</h3>
                    <p class="text-muted" style="line-height: 1.6;">
                        ${not empty vendedor.descripcionPerfil ? vendedor.descripcionPerfil : 'Este estudiante aún no ha agregado una descripción a su perfil.'}
                    </p>
                    <hr class="my-3">
                    <p class="small text-muted">Miembro desde: <fmt:formatDate value="${vendedor.fechaRegistro}" pattern="MMMM yyyy"/></p>
                    
                    <c:if test="${sessionScope.usuarioActual.idUsuario == vendedor.idUsuario}">
                        <a href="${pageContext.request.contextPath}/usuario/editar-perfil" class="btn btn-outline btn-sm w-100 mt-2">Editar mi perfil</a>
                    </c:if>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-link w-100 mt-2">← Volver al inicio</a>
        </div>

        <!-- Servicios del Vendedor -->
        <div class="main-content">
            <h2 class="mb-3">Servicios activos</h2>
            <c:choose>
                <c:when test="${empty serviciosActivos}">
                    <div class="empty-state" style="padding: 3rem;">
                        <p>Este vendedor no tiene servicios activos en este momento.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="grid-servicios" style="grid-template-columns: 1fr 1fr;">
                        <c:forEach var="srv" items="${serviciosActivos}">
                            <div class="card servicio-card">
                                <div class="card-img-container" style="height: 140px; background: #eee; border-radius: 12px 12px 0 0;">
                                    <c:choose>
                                        <c:when test="${not empty srv.fotoUrl}">
                                            <img src="${pageContext.request.contextPath}/uploads/servicios/${srv.fotoUrl}" 
                                                 alt="${srv.tituloServicio}" style="width: 100%; height: 100%; object-fit: cover;">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/img/no-image.png" 
                                                 alt="Sin imagen" style="width: 100%; height: 100%; object-fit: cover; opacity: 0.5;">
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="card-body" style="padding: 1rem;">
                                    <h4 class="card-title" style="font-size: 1.1rem;">${srv.tituloServicio}</h4>
                                    <div class="d-flex justify-content-between align-items-center mt-2">
                                        <span class="precio" style="font-size: 1rem;">$<fmt:formatNumber value="${srv.precioServicio}" pattern="0.00"/></span>
                                        <a href="${pageContext.request.contextPath}/servicio/detalle?id=${srv.idServicio}" class="btn btn-primary btn-sm">Ver</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

</body>
</html>
