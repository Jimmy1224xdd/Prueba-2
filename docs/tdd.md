# Prácticas de TDD (Test-Driven Development)

## Enfoque
Para este incremento, se utilizó un enfoque TDD para las nuevas funcionalidades críticas: el sistema de Chat y la actualización de estados de Solicitudes. Se crearon Clases de Servicio (`ChatService`, `SolicitudService`) para abstraer la lógica de negocio y facilitar las pruebas sin depender del contenedor de Servlets.

## Casos Implementados

### 1. `ChatServiceTest.java`
- **Mocks Utilizados:** Se utilizó Mockito para simular el comportamiento de `ConversacionDAO` y `MensajeDAO`.
- **Casos:**
  - `testObtenerOCrearConversacion_Existente`: Prueba que si la conversación ya existe, no se intenta guardar una nueva en la BD.
  - `testEnviarMensaje_Exito`: Prueba que se captura correctamente el mensaje y se invoca el guardado en BD.
  - `testEnviarMensaje_RemitenteNoPertenece`: Verifica que se lanza una excepción si un usuario que no es ni proveedor ni cliente intenta enviar un mensaje a la conversación.

### 2. `SolicitudServiceTest.java`
- **Fake Objects:** Se usó una implementación "Fake" o Dummy de `SolicitudDAO` para las pruebas unitarias.
- **Casos:**
  - `testActualizarEstado_NoExiste`: Prueba que devuelve `false` si el ID de la solicitud no es válido.
  - `testActualizarEstado_UsuarioNoAutorizado`: Prueba que un usuario distinto al proveedor no puede cambiar el estado de la solicitud.
  - `testActualizarEstado_ExitoParametrizado` (**Prueba Parametrizada**): Utiliza `@EnumSource` para probar exitosamente la actualización de la solicitud a los distintos estados posibles (`SOLICITADO`, `EN_PROGRESO`, `FINALIZADO`).
