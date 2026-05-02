<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Servicio — PoliServis</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&family=Montserrat:wght@700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="../navbar.jsp" %>
<div class="container" style="max-width: 600px; margin: 2rem auto;">
    <div class="card">
        <div class="card-body">
            <h2 class="card-title">Editar Servicio</h2>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger mb-3">${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/servicio/editar" method="post">
                <input type="hidden" name="idServicio" value="${servicio.idServicio}">
                
                <div class="mb-3">
                    <label class="form-label">Título del servicio</label>
                    <input type="text" name="titulo" class="form-control" value="${servicio.tituloServicio}" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Categoría</label>
                    <select name="idCategoria" class="form-control" required>
                        <c:forEach var="cat" items="${categorias}">
                            <option value="${cat.idCategoria}" ${cat.idCategoria == servicio.categoria.idCategoria ? 'selected' : ''}>
                                ${cat.nombre}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Precio ($)</label>
                    <input type="number" name="precio" step="0.01" class="form-control" value="${servicio.precioServicio}" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Estado</label>
                    <select name="estado" class="form-control" required>
                        <option value="ACTIVO" ${servicio.estado == 'ACTIVO' ? 'selected' : ''}>ACTIVO</option>
                        <option value="INACTIVO" ${servicio.estado == 'INACTIVO' ? 'selected' : ''}>INACTIVO</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Descripción</label>
                    <textarea name="descripcion" class="form-control" rows="5" required>${servicio.descripcionServicio}</textarea>
                </div>

                <button type="submit" class="btn btn-primary btn-full">Guardar cambios</button>
            </form>
            <div class="mt-2 text-center">
                <a href="${pageContext.request.contextPath}/servicio/mis-servicios" class="text-muted">Cancelar</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
