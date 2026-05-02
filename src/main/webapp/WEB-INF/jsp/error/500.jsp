<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Error del servidor — PoliServis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="empty-state" style="padding-top:6rem">
    <div class="icon">⚠️</div>
    <h3 style="font-size:2rem">500</h3>
    <p>Ocurrió un error interno. Por favor intenta más tarde.</p>
    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary mt-2">Volver al inicio</a>
</div>
</body>
</html>
