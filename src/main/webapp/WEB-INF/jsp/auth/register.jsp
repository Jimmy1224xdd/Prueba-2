<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrarse — PoliServis</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&family=Montserrat:wght@700;800;900&display=swap" rel="stylesheet">
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

        <h2 class="auth-title">Crear cuenta</h2>
        <p class="auth-subtitle">Regístrate como estudiante EPN</p>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post" novalidate>
            <div class="form-group">
                <label for="nombre">Nombre completo</label>
                <input type="text" id="nombre" name="nombre" class="form-control"
                       placeholder="Juan Pérez"
                       value="${not empty nombre ? nombre : ''}" required>
            </div>
            <div class="form-group">
                <label for="correo">Correo institucional</label>
                <input type="email" id="correo" name="correo" class="form-control"
                       placeholder="usuario@epn.edu.ec"
                       value="${not empty correo ? correo : ''}" required>
            </div>
            <div class="form-group">
                <label for="contrasena">Contraseña</label>
                <input type="password" id="contrasena" name="contrasena" class="form-control"
                       placeholder="Mínimo 6 caracteres" required minlength="6">
            </div>
            <div class="form-group">
                <label for="confirmar">Confirmar contraseña</label>
                <input type="password" id="confirmar" name="confirmar" class="form-control"
                       placeholder="Repite tu contraseña" required>
            </div>
            <button type="submit" class="btn btn-primary btn-full mt-2">Crear cuenta</button>
        </form>

        <p class="auth-footer">
            ¿Ya tienes cuenta?
            <a href="${pageContext.request.contextPath}/login">Inicia sesión</a>
        </p>
    </div>
</div>
</body>
</html>
