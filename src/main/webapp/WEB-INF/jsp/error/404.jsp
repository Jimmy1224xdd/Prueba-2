<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Página no encontrada — PoliServis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="empty-state" style="padding-top:6rem">
    <div class="icon">🔍</div>
    <h3 style="font-size:2rem">404</h3>
    <p>La página que buscas no existe.</p>
    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary mt-2">Volver al inicio</a>
</div>
</body>
</html>
