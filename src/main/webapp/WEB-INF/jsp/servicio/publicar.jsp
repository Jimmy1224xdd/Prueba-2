<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Publicar servicio — PoliServis</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&family=Montserrat:wght@700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<%@ include file="../navbar.jsp" %>

<div class="container">
    <div class="page-header">
        <h1>Publicar un servicio</h1>
        <p>Ofrece tus habilidades a otros estudiantes de la EPN</p>
    </div>

    <div class="card" style="max-width:640px">
        <div class="card-body">

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/servicio/publicar" method="post" enctype="multipart/form-data" novalidate>

                <div class="form-group">
                    <label for="titulo">Título del servicio *</label>
                    <input type="text" id="titulo" name="titulo" class="form-control"
                           placeholder="Ej: Clases de cálculo diferencial" required maxlength="100">
                </div>

                <div class="form-group">
                    <label for="descripcion">Descripción *</label>
                    <textarea id="descripcion" name="descripcion" class="form-control"
                              placeholder="Describe detalladamente lo que ofreces..." required></textarea>
                </div>

                <div class="form-group">
                    <label for="precio">Precio (USD) *</label>
                    <input type="number" id="precio" name="precio" class="form-control"
                           placeholder="0.00" min="0" step="0.50" required>
                </div>

                <div class="form-group">
                    <label for="idCategoria">Categoría *</label>
                    <select id="idCategoria" name="idCategoria" class="form-control" required>
                        <option value="">— Selecciona una categoría —</option>
                        <c:forEach var="cat" items="${categorias}">
                            <option value="${cat.idCategoria}">${cat.nombre}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="foto">Foto del servicio (JPG o PNG, máx 2MB)</label>
                    <input type="file" id="foto" name="foto" class="form-control" accept="image/jpeg,image/png">
                </div>

                <div class="d-flex gap-1 mt-3">
                    <button type="submit" class="btn btn-primary">Publicar servicio</button>
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-outline">Cancelar</a>
                </div>

            </form>
        </div>
    </div>

    <div style="height:3rem"></div>
</div>

</body>
</html>