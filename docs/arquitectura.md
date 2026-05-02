# Arquitectura del Incremento

## Capas y Separación de Responsabilidades
El proyecto sigue un patrón MVC (Model-View-Controller) modificado, apoyado en Servlets y JPA (Hibernate) para la persistencia. Para este incremento, se incluyó una capa de Servicio (`Service Layer`) para aislar la lógica compleja.

1. **Modelos (Entidades JPA):** `Conversacion`, `Mensaje`, y el enumerador `EstadoSolicitud`. Mapean directamente a las tablas en MySQL.
2. **DAOs (Data Access Objects):** `ConversacionDAO`, `MensajeDAO`. Encapsulan las operaciones CRUD y consultas HQL. Mantienen a los controladores limpios de sentencias SQL.
3. **Servicios (Lógica de Negocio):** `ChatService`, `SolicitudService`. Implementados para centralizar la validación (Ej. ¿A quién le pertenece esta conversación? ¿Puede este usuario cambiar el estado de esta solicitud?). Son el punto central para el testing (TDD).
4. **Controladores (Servlets):** `ChatServlet`, `EditarServicioServlet`, `MisServiciosServlet`, etc. Únicamente se encargan de enrutar las peticiones HTTP, recolectar parámetros, invocar a la capa de Servicios o DAOs, y retornar la vista adecuada.
5. **Vistas (JSP):** Se renderiza la UI. La lógica de presentación es mínima, delegando responsabilidades a JSTL (`<c:forEach>`, `<c:if>`, etc.).

## Principios SOLID Aplicados
- **Single Responsibility Principle (SRP):** Al dividir los Servlets y DAOs, y crear clases como `ChatService`, cada clase tiene una única razón para cambiar.
- **Dependency Inversion Principle (DIP):** En las pruebas TDD, los DAOs se simulan (Mockean) y se inyectan en los Servicios, lo que significa que el Servicio depende de la abstracción y no de la base de datos real.

## Refactorizaciones (Justificación)
- **Extract Service Layer:** Los Servlets previos contenían toda la lógica. Se extrajo a `SolicitudService` y `ChatService` para facilitar el testing.
- **Inline/Extract Method:** La validación de propiedades se delegó al modelo (`servicio.esPropietario()`) en lugar de hacer comprobaciones manuales con `==` en múltiples Servlets.
- **Replace Magic Numbers:** El límite de chats, originalmente un número mágico "5", se removió por completo a petición del usuario. Las constantes de Estado se manejaron con Enums formales.
