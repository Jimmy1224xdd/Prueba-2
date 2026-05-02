# Guía de Uso

## Cómo Ejecutar el Proyecto (Docker)
Para iniciar toda la infraestructura con un solo comando:
```bash
docker-compose up -d --build
```
Esto levantará:
- MySQL en el puerto 3306.
- Tomcat (Aplicación) en el puerto 8080.
- Jenkins en el puerto 8081.

Puedes acceder a la aplicación en `http://localhost:8080/`.

## Cómo Usar las Nuevas Funcionalidades

### Navegación Invitado
Simplemente abre la página principal. Verás el catálogo. Si intentas interactuar (Solicitar un servicio o publicar uno), aparecerá un modal pidiéndote que inicies sesión.

### Publicar y Gestionar Servicios
1. Inicia sesión como Proveedor.
2. Usa el botón "Publicar servicio" para crear uno.
3. Dirígete a la pestaña "Mis servicios" en la barra de navegación para editar su información (Ej. Desactivarlo) o eliminarlo.

### Recibir Solicitudes
1. Cuando un cliente solicite tu servicio, aparecerá en tu panel "Mis solicitudes" > "Ver solicitudes recibidas".
2. Puedes cambiar el estado de la solicitud en un selector (A "En Progreso" o "Finalizado").
3. Puedes hacer clic en "Chat proveedor" para iniciar una conversación con el cliente sobre esa solicitud en particular.

### Sistema de Chat
1. Dirígete a "Chat" en la barra de navegación para ver todas tus conversaciones activas.
2. Haz clic en "Abrir Chat" para entrar a la sala de mensajes en tiempo real.
