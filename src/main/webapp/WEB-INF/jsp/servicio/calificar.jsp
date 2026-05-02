<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Calificar servicio — PoliServis</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&family=Montserrat:wght@700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .star-group { display:flex; gap:.5rem; flex-direction:row-reverse; justify-content:flex-end; margin:.75rem 0; }
        .star-group input[type=radio] { display:none; }
        .star-group label {
            font-size:2rem; cursor:pointer; color:#ccc; transition:color .2s;
        }
        .star-group input[type=radio]:checked ~ label,
        .star-group label:hover,
        .star-group label:hover ~ label { color:#f5a623; }
    </style>
</head>
<body>

<%@ include file="../navbar.jsp" %>

<div class="container">
    <div class="page-header">
        <a href="${pageContext.request.contextPath}/servicio/detalle?id=${servicio.idServicio}"
           class="text-muted">← Volver al detalle</a>
        <h1>Calificar servicio</h1>
        <p>Asigna una puntuación del 1 al 5 — del diagrama de actividades CU04</p>
    </div>

    <div class="card" style="max-width:560px">
        <div class="card-body">

            <%-- Error de validación --%>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <%-- Resumen del servicio --%>
            <div style="background:var(--bg-secondary);border-radius:8px;padding:1rem;margin-bottom:1.5rem">
                <p class="text-muted" style="font-size:.8rem;margin-bottom:.25rem">SERVICIO A CALIFICAR</p>
                <h3 style="margin:0">${servicio.tituloServicio}</h3>
                <p class="text-muted" style="font-size:.85rem;margin-top:.25rem">
                    Proveedor: ${servicio.usuario.nombre}
                </p>
            </div>

            <%-- Formulario de calificación --%>
            <form action="${pageContext.request.contextPath}/servicio/calificar" method="post">
                <input type="hidden" name="idServicio" value="${servicio.idServicio}">

                <%-- Puntuación del 1 al 5 — "Asignar puntuacion del 1 al 5" del diagrama de actividades --%>
                <div class="form-group">
                    <label>Puntuación *</label>
                    <div class="star-group">
                        <input type="radio" id="s5" name="puntuacion" value="5" required>
                        <label for="s5" title="5 - Excelente">★</label>
                        <input type="radio" id="s4" name="puntuacion" value="4">
                        <label for="s4" title="4 - Muy bueno">★</label>
                        <input type="radio" id="s3" name="puntuacion" value="3">
                        <label for="s3" title="3 - Bueno">★</label>
                        <input type="radio" id="s2" name="puntuacion" value="2">
                        <label for="s2" title="2 - Regular">★</label>
                        <input type="radio" id="s1" name="puntuacion" value="1">
                        <label for="s1" title="1 - Malo">★</label>
                    </div>
                </div>

                <%-- Comentario opcional — "Escribir comentario" del diagrama de actividades --%>
                <div class="form-group">
                    <label for="resena">Comentario (opcional)</label>
                    <textarea id="resena" name="resena" class="form-control" rows="3"
                              placeholder="Describe tu experiencia con este servicio..."></textarea>
                </div>

                <div class="d-flex gap-1 mt-3">
                    <button type="submit" class="btn btn-primary">Enviar calificación</button>
                    <a href="${pageContext.request.contextPath}/servicio/detalle?id=${servicio.idServicio}"
                       class="btn btn-outline">Cancelar</a>
                </div>
            </form>

        </div>
    </div>

    <div style="height:3rem"></div>
</div>

</body>
</html>
