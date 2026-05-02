<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Notificaciones — PoliServis</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&family=Montserrat:wght@700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <style>
    .notif-list { display: flex; flex-direction: column; gap: .75rem; max-width: 720px; }

    .notif-item {
      background: var(--card);
      border: 1px solid var(--border);
      border-radius: var(--radius);
      padding: 1rem 1.2rem;
      display: flex;
      gap: 1rem;
      align-items: flex-start;
      box-shadow: var(--shadow);
      transition: background .15s;
    }
    .notif-item.no-leida {
      border-left: 4px solid var(--primary);
      background: #f0f5ff;
    }
    .notif-icon {
      font-size: 1.6rem;
      flex-shrink: 0;
      margin-top: .1rem;
    }
    .notif-body { flex: 1; }
    .notif-contenido {
      font-size: .93rem;
      color: var(--text);
      line-height: 1.5;
      margin-bottom: .3rem;
    }
    .notif-meta {
      font-size: .78rem;
      color: var(--muted);
      display: flex;
      gap: .75rem;
      align-items: center;
      flex-wrap: wrap;
    }
    .notif-tipo {
      display: inline-block;
      background: #dce8f8;
      color: var(--primary);
      font-size: .7rem;
      font-weight: 600;
      padding: .15rem .5rem;
      border-radius: 999px;
      text-transform: uppercase;
      letter-spacing: .4px;
    }
    .btn-leer {
      background: transparent;
      border: 1.5px solid var(--primary);
      color: var(--primary);
      font-size: .75rem;
      padding: .2rem .6rem;
      border-radius: 6px;
      cursor: pointer;
      font-weight: 600;
      transition: background .15s, color .15s;
      white-space: nowrap;
    }
    .btn-leer:hover { background: var(--primary); color: #fff; }

    .header-actions {
      display: flex;
      justify-content: space-between;
      align-items: center;
      flex-wrap: wrap;
      gap: .75rem;
      margin-bottom: 1.25rem;
    }
  </style>
</head>
<body>

<%@ include file="navbar.jsp" %>

<div class="container">
  <div class="page-header">
    <h1>🔔 Mis notificaciones
      <c:if test="${noLeidas > 0}">
                <span class="badge badge-accent" style="font-size:.7rem;vertical-align:middle;margin-left:.4rem">
                    ${noLeidas} nueva<c:if test="${noLeidas > 1}">s</c:if>
                </span>
      </c:if>
    </h1>
    <p>Avisos que has recibido cuando alguien solicita tus servicios</p>
  </div>

  <%-- Mensajes flash --%>
  <c:if test="${not empty sessionScope.mensajeExito}">
    <div class="alert alert-success">${sessionScope.mensajeExito}</div>
    <c:remove var="mensajeExito" scope="session"/>
  </c:if>

  <c:choose>
    <c:when test="${empty notificaciones}">
      <div class="empty-state">
        <div class="icon">🔕</div>
        <h3>Sin notificaciones</h3>
        <p class="mt-1">Cuando alguien solicite uno de tus servicios, aparecerá aquí.</p>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary mt-2">Ir al inicio</a>
      </div>
    </c:when>
    <c:otherwise>

      <div class="header-actions">
        <p class="text-muted">${notificaciones.size()} notificación<c:if test="${notificaciones.size() != 1}">es</c:if> en total</p>
        <c:if test="${noLeidas > 0}">
          <form action="${pageContext.request.contextPath}/notificaciones" method="post" style="margin:0">
            <input type="hidden" name="accion" value="leerTodas">
            <button type="submit" class="btn btn-primary btn-sm">✓ Marcar todas como leídas</button>
          </form>
        </c:if>
      </div>

      <div class="notif-list">
        <c:forEach var="n" items="${notificaciones}">
          <div class="notif-item ${n.leida ? '' : 'no-leida'}">
            <div class="notif-icon">
              <c:choose>
                <c:when test="${n.tipoNotificacion == 'SOLICITUD_RECIBIDA'}">📩</c:when>
                <c:when test="${n.tipoNotificacion == 'CALIFICACION_RECIBIDA'}">⭐</c:when>
                <c:when test="${n.tipoNotificacion == 'SOLICITUD_ACEPTADA'}">✅</c:when>
                <c:when test="${n.tipoNotificacion == 'SOLICITUD_RECHAZADA'}">❌</c:when>
                <c:otherwise>🔔</c:otherwise>
              </c:choose>
            </div>

            <div class="notif-body">
              <p class="notif-contenido">${n.contenido}</p>
              <div class="notif-meta">
                <span class="notif-tipo">${n.tipoNotificacion}</span>
                <span>
                                    <fmt:formatDate value="${n.fechaNotificacion}" pattern="dd/MM/yyyy HH:mm"/>
                                </span>
                <c:if test="${n.leida}">
                  <span style="color:var(--success)">✔ Leída</span>
                </c:if>
              </div>
            </div>

            <c:if test="${!n.leida}">
              <form action="${pageContext.request.contextPath}/notificaciones" method="post" style="margin:0;flex-shrink:0">
                <input type="hidden" name="accion" value="leer">
                <input type="hidden" name="id" value="${n.idNotificacion}">
                <button type="submit" class="btn-leer">Marcar leída</button>
              </form>
            </c:if>
          </div>
        </c:forEach>
      </div>

    </c:otherwise>
  </c:choose>

  <div style="height:3rem"></div>
</div>

</body>
</html>
