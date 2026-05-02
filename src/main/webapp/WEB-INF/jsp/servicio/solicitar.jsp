<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Solicitar servicio — PoliServis</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&family=Montserrat:wght@700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<%@ include file="../navbar.jsp" %>

<div class="container">
    <div class="page-header">
        <a href="${pageContext.request.contextPath}/servicio/detalle?id=${servicio.idServicio}"
           class="text-muted">← Volver al detalle</a>
        <h1>Solicitar servicio</h1>
        <p>Confirma tu solicitud para el siguiente servicio</p>
    </div>

    <div class="card" style="max-width:600px">
        <div class="card-body">

            <%-- Mensaje de error si viene de validación --%>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <%-- Resumen del servicio seleccionado --%>
            <div style="background:var(--bg-secondary);border-radius:8px;padding:1rem;margin-bottom:1.5rem">
                <p class="text-muted" style="font-size:.8rem;margin-bottom:.25rem">SERVICIO SELECCIONADO</p>
                <h3 style="margin:0 0 .25rem">${servicio.tituloServicio}</h3>
                <p class="text-muted" style="font-size:.9rem">${servicio.descripcionServicio}</p>
                <p class="precio" style="margin-top:.5rem">
                    $<fmt:formatNumber value="${servicio.precioServicio}" pattern="0.00"/>
                </p>
            </div>

            <%-- Formulario de confirmación de solicitud --%>
            <form action="${pageContext.request.contextPath}/servicio/solicitar" method="post">
                <input type="hidden" name="idServicio" value="${servicio.idServicio}">

                <p style="margin-bottom:1.5rem;line-height:1.6">
                    Al confirmar, el proveedor
                    <strong>${servicio.usuario.nombre}</strong>
                    recibirá una notificación de tu solicitud.
                </p>

                <div class="d-flex gap-1">
                    <button type="submit" class="btn btn-primary">
                        ✅ Confirmar solicitud
                    </button>
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
