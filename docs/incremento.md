# Incremento Ágil

## Descripción del Incremento
Este incremento abarca la funcionalidad esencial para que la plataforma opere como un marketplace de servicios completo. Se ha añadido la capacidad de gestionar (crear, editar, eliminar) los servicios ofrecidos, gestionar el estado de las solicitudes (Solicitado, En Progreso, Finalizado), e interactuar mediante un sistema de Chat sin límites para la comunicación directa entre proveedor y cliente. Además, se permite a usuarios no registrados navegar por el catálogo libremente.

## Historias de Usuario Implementadas

1. **HU-01: Navegación Pública:** Como invitado, quiero navegar por el catálogo de servicios sin necesidad de registrarme, para conocer qué ofrece la plataforma antes de crear una cuenta.
2. **HU-02: Gestión de Servicios:** Como proveedor, quiero poder publicar, editar y eliminar mis servicios, además de cambiar su estado (Activo/Inactivo), para mantener mi oferta actualizada.
3. **HU-03: Gestión de Solicitudes:** Como proveedor, quiero poder cambiar el estado de las solicitudes que me han hecho (En progreso, Finalizado) para llevar un control del trabajo.
4. **HU-04: Chat Integrado:** Como usuario, quiero poder chatear con el proveedor/cliente sobre un servicio específico, para acordar detalles sin salir de la plataforma.

## Planificación (Scrum/XP)
- **Fase de Diseño:** Se diseñaron las nuevas entidades `EstadoSolicitud`, `Conversacion` y `Mensaje`.
- **TDD:** Se escribieron pruebas unitarias para `ChatService` y `SolicitudService` antes de conectarlos a los servlets.
- **Implementación y Refactorización:** Se separó la lógica en capas (Servicios vs Controladores). Se mejoraron las validaciones.
- **CI/CD:** Se integró Jenkins y Docker para asegurar que los incrementos futuros mantengan la calidad.
