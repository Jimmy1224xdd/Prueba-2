<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar sesión — PoliServis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-card">

        <div class="auth-epn-header">
            <div class="auth-epn-badge">Escuela Politécnica Nacional</div>
            <div class="auth-logo">Poli<span class="brand-servis">Servis</span></div>
            <p style="font-size:.82rem;color:var(--muted);margin-top:.3rem">Plataforma de servicios estudiantiles</p>
        </div>

        <h2 class="auth-title">Bienvenido de nuevo</h2>
        <p class="auth-subtitle">Ingresa con tu correo institucional</p>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post" novalidate>
            <div class="form-group">
                <label for="correo">Correo institucional</label>
                <input type="email" id="correo" name="correo" class="form-control"
                       placeholder="usuario@epn.edu.ec"
                       value="${not empty correo ? correo : ''}" required>
            </div>
            <div class="form-group">
                <label for="contrasena">Contraseña</label>
                <input type="password" id="contrasena" name="contrasena" class="form-control"
                       placeholder="••••••••" required>
            </div>
            <button type="submit" class="btn btn-primary btn-full mt-2">Iniciar sesión</button>
        </form>

        <p class="auth-footer">
            ¿No tienes cuenta?
            <a href="${pageContext.request.contextPath}/register">Regístrate aquí</a>
        </p>
    </div>
</div>
</body>
</html>
