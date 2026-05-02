<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
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
    <div class="page-header">
        <a href="${pageContext.request.contextPath}/home" class="text-muted">← Volver al inicio</a>
    </div>

    <%-- Mensajes de sesión --%>
    <c:if test="${not empty sessionScope.mensajeExito}">
        <div class="alert alert-success mt-2">${sessionScope.mensajeExito}</div>
        <c:remove var="mensajeExito" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.mensajeError}">
        <div class="alert alert-danger mt-2">${sessionScope.mensajeError}</div>
        <c:remove var="mensajeError" scope="session"/>
    </c:if>

    <div style="display:grid;grid-template-columns:2fr 1fr;gap:1.5rem;align-items:start">

        <%-- Detalle principal --%>
        <div>
            <div class="card">
                <div class="card-body">
                    <c:if test="${not empty categoria}">
                        <span class="badge badge-primary mb-2">${categoria.nombre}</span>
                    </c:if>
                    <h1 class="card-title" style="font-size:1.5rem">${servicio.tituloServicio}</h1>
                    <p class="text-muted mt-1">
                        Publicado el:
                        <fmt:formatDate value="${servicio.fechaPublicacionServicio}" pattern="dd/MM/yyyy"/>
                    </p>
                    <hr style="border:none;border-top:1px solid var(--border);margin:1rem 0">
                    <h3 style="font-size:1rem;margin-bottom:.5rem">Descripción</h3>
                    <p style="line-height:1.7">${servicio.descripcionServicio}</p>
                </div>
            </div>

            <%-- Sección de calificaciones --%>
            <div class="card mt-3">
                <div class="card-body">
                    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:1rem">
                        <h3 style="font-size:1rem;margin:0">Calificaciones</h3>
                        <c:if test="${not empty promedio}">
                            <span class="badge badge-primary">
                                ⭐ <fmt:formatNumber value="${promedio}" pattern="0.0"/> / 5
                            </span>
                        </c:if>
                    </div>

                    <c:choose>
                        <c:when test="${empty calificaciones}">
                            <p class="text-muted" style="font-size:.9rem">
                                Aún no hay calificaciones para este servicio.
                            </p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="cal" items="${calificaciones}">
                                <div style="border-bottom:1px solid var(--border);padding:.75rem 0">
                                    <div style="display:flex;justify-content:space-between">
                                        <strong style="font-size:.9rem">${cal.usuario.nombre}</strong>
                                        <span style="color:#f5a623">
                                            <c:forEach begin="1" end="${cal.puntuacion}">★</c:forEach>
                                            <c:forEach begin="${cal.puntuacion + 1}" end="5">☆</c:forEach>
                                        </span>
                                    </div>
                                    <c:if test="${not empty cal.resena}">
                                        <p style="font-size:.87rem;margin:.25rem 0 0;color:var(--text-secondary)">
                                            ${cal.resena}
                                        </p>
                                    </c:if>
                                    <p class="text-muted" style="font-size:.78rem;margin:.2rem 0 0">
                                        <fmt:formatDate value="${cal.fechaCalificacion}" pattern="dd/MM/yyyy"/>
                                    </p>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <%-- Panel lateral --%>
        <div>
            <div class="card mb-2">
                <div class="card-body" style="text-align:center">
                    <p class="text-muted" style="font-size:.8rem;margin-bottom:.25rem">PRECIO</p>
                    <p class="precio" style="font-size:2rem">
                        $<fmt:formatNumber value="${servicio.precioServicio}" pattern="0.00"/>
                    </p>

                    <c:choose>
                        <%-- Es el propio servicio del usuario --%>
                        <c:when test="${esMiServicio}">
                            <p class="text-muted mt-2" style="font-size:.85rem">Este es tu servicio</p>
                        </c:when>

                        <%-- Ya lo solicitó: mostrar opción de calificar --%>
                        <c:when test="${yaSolicite}">
                            <p class="text-muted mt-2" style="font-size:.85rem;color:green">
                                ✅ Ya solicitaste este servicio
                            </p>
                            <c:choose>
                                <c:when test="${yaCalifico}">
                                    <p class="text-muted mt-1" style="font-size:.82rem">
                                        ⭐ Ya calificaste este servicio
                                    </p>
                                </c:when>
                                <c:otherwise>
                                    <%-- Botón calificar — lleva al flujo Calificar Servicio --%>
                                    <a href="${pageContext.request.contextPath}/servicio/calificar?id=${servicio.idServicio}"
                                       class="btn btn-accent btn-full mt-2">
                                        ⭐ Calificar servicio
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>

                        <%-- Puede solicitar --%>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${not empty sessionScope.usuarioActual}">
                                    <a href="${pageContext.request.contextPath}/servicio/solicitar?id=${servicio.idServicio}"
                                       class="btn btn-primary btn-full mt-2">
                                        Solicitar servicio
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="#" class="btn btn-primary btn-full mt-2 req-auth">
                                        Solicitar servicio
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <%-- Info del proveedor --%>
            <c:if test="${not empty proveedor}">
                <div class="card">
                    <div class="card-body">
                        <h3 style="font-size:.95rem;margin-bottom:.5rem">Proveedor</h3>
                        <p class="fw-bold">${proveedor.nombre}</p>
                        <p class="text-muted">${proveedor.correo}</p>
                        <c:if test="${not empty proveedor.descripcionPerfil}">
                            <p class="mt-1" style="font-size:.87rem">${proveedor.descripcionPerfil}</p>
                        </c:if>
                        
                        <%-- Botón contactar proveedor por chat --%>
                        <c:if test="${not esMiServicio}">
                            <c:choose>
                                <c:when test="${not empty sessionScope.usuarioActual}">
                                    <a href="${pageContext.request.contextPath}/chat?userId=${proveedor.idUsuario}&servicioId=${servicio.idServicio}"
                                       class="btn btn-outline btn-full mt-2" style="display:flex; align-items:center; justify-content:center; gap:.4rem;">
                                        💬 Contactar proveedor
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="#" class="btn btn-outline btn-full mt-2 req-auth" style="display:flex; align-items:center; justify-content:center; gap:.4rem;">
                                        💬 Contactar proveedor
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </div>

    </div>

    <div style="height:3rem"></div>
</div>

</body>
</html>
