-- ============================================
-- UniServicios / PoliServis - Init Script
-- Datos mínimos de demo
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS=0;

-- --------------------------------------------
-- 1. CATEGORIAS
-- --------------------------------------------
DROP TABLE IF EXISTS `categorias`;
CREATE TABLE `categorias` (
                              `id_categoria` int NOT NULL AUTO_INCREMENT,
                              `nombre` varchar(100) NOT NULL,
                              `descripcion` text,
                              PRIMARY KEY (`id_categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `categorias` VALUES
                             (1,'Tecnología','Servicios de software, hardware y soporte técnico'),
                             (2,'Diseño','Diseño gráfico, UI/UX, ilustración'),
                             (3,'Tutorías','Clases particulares y asesorías académicas'),
                             (4,'Idiomas','Traducción, clases de idiomas'),
                             (5,'Fotografía','Fotografía y edición de imagen'),
                             (6,'Música','Clases de instrumentos, producción musical'),
                             (7,'Otros','Servicios varios');

-- --------------------------------------------
-- 2. USUARIOS
-- --------------------------------------------
DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
                            `id_usuario` int NOT NULL AUTO_INCREMENT,
                            `nombre` varchar(150) NOT NULL,
                            `correo` varchar(150) NOT NULL,
                            `contrasena` varchar(255) NOT NULL,
                            `descripcion_perfil` text,
                            `rol` enum('ESTUDIANTE','ADMIN') NOT NULL DEFAULT 'ESTUDIANTE',
                            `fecha_registro` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `activo` tinyint(1) NOT NULL DEFAULT '1',
                            PRIMARY KEY (`id_usuario`),
                            UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `usuarios` VALUES
    (1,'GenericUser','GenericUser@epn.edu.ec','123456',NULL,'ESTUDIANTE',NOW(),1);

-- --------------------------------------------
-- 3. SERVICIOS
-- --------------------------------------------
DROP TABLE IF EXISTS `servicios`;
CREATE TABLE `servicios` (
                             `id_servicio` int NOT NULL AUTO_INCREMENT,
                             `titulo` varchar(200) NOT NULL,
                             `descripcion` text NOT NULL,
                             `precio` double DEFAULT NULL,
                             `disponibilidad` tinyint(1) NOT NULL DEFAULT '1',
                             `fecha_publicacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `estado` enum('ACTIVO','INACTIVO','PENDIENTE') NOT NULL DEFAULT 'ACTIVO',
                             `id_usuario` int NOT NULL,
                             `id_categoria` int DEFAULT NULL,
                             PRIMARY KEY (`id_servicio`),
                             KEY `id_usuario` (`id_usuario`),
                             KEY `id_categoria` (`id_categoria`),
                             CONSTRAINT `servicios_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`),
                             CONSTRAINT `servicios_ibfk_2` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `servicios` VALUES
    (1,'Desarrollo de páginas web','Creo páginas web responsivas con HTML, CSS y JavaScript.',25,1,'2026-04-10 02:47:34','ACTIVO',1,1);

-- --------------------------------------------
-- 4. SOLICITUDES
-- --------------------------------------------
DROP TABLE IF EXISTS `solicitudes`;
CREATE TABLE `solicitudes` (
                               `id_solicitud` int NOT NULL AUTO_INCREMENT,
                               `fecha_solicitud` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               `id_usuario` int NOT NULL,
                               `id_servicio` int NOT NULL,
                               `estado` enum('SOLICITADO','EN_PROGRESO','FINALIZADO') DEFAULT NULL,
                               PRIMARY KEY (`id_solicitud`),
                               KEY `id_usuario` (`id_usuario`),
                               KEY `id_servicio` (`id_servicio`),
                               CONSTRAINT `solicitudes_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`),
                               CONSTRAINT `solicitudes_ibfk_2` FOREIGN KEY (`id_servicio`) REFERENCES `servicios` (`id_servicio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------
-- 5. NOTIFICACIONES
-- --------------------------------------------
DROP TABLE IF EXISTS `notificaciones`;
CREATE TABLE `notificaciones` (
                                  `id_notificacion` int NOT NULL AUTO_INCREMENT,
                                  `contenido` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                  `fecha_notificacion` datetime(6) DEFAULT NULL,
                                  `leida` bit(1) DEFAULT NULL,
                                  `tipo_notificacion` enum('SOLICITUD_RECIBIDA','SOLICITUD_ACEPTADA','SOLICITUD_RECHAZADA','CALIFICACION_RECIBIDA') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                  `id_usuario` int DEFAULT NULL,
                                  PRIMARY KEY (`id_notificacion`),
                                  KEY `FKrr0ikjdv4qycj44q3lohskm4k` (`id_usuario`),
                                  CONSTRAINT `FKrr0ikjdv4qycj44q3lohskm4k` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------
-- 6. CALIFICACIONES
-- --------------------------------------------
DROP TABLE IF EXISTS `calificaciones`;
CREATE TABLE `calificaciones` (
                                  `id_calificacion` int NOT NULL AUTO_INCREMENT,
                                  `fecha_calificacion` datetime(6) DEFAULT NULL,
                                  `puntuacion` int DEFAULT NULL,
                                  `resena` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                  `id_servicio` int DEFAULT NULL,
                                  `id_usuario` int DEFAULT NULL,
                                  PRIMARY KEY (`id_calificacion`),
                                  KEY `FKqpli5ngxipsm7gq1tt00ijql5` (`id_servicio`),
                                  KEY `FKhlrkf2nd88h7woy6a8xaiqhl6` (`id_usuario`),
                                  CONSTRAINT `FKhlrkf2nd88h7woy6a8xaiqhl6` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`),
                                  CONSTRAINT `FKqpli5ngxipsm7gq1tt00ijql5` FOREIGN KEY (`id_servicio`) REFERENCES `servicios` (`id_servicio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------
-- 7. CONVERSACIONES
-- --------------------------------------------
DROP TABLE IF EXISTS `conversaciones`;
CREATE TABLE `conversaciones` (
                                  `id_conversacion` int NOT NULL AUTO_INCREMENT,
                                  `fecha_creacion` datetime(6) DEFAULT NULL,
                                  `id_cliente` int DEFAULT NULL,
                                  `id_proveedor` int DEFAULT NULL,
                                  `id_servicio` int DEFAULT NULL,
                                  PRIMARY KEY (`id_conversacion`),
                                  KEY `FKait80b9r3ge6bdsponrjnbtg9` (`id_cliente`),
                                  KEY `FKfn2pcwvrlu8fo59hhn8925x0a` (`id_proveedor`),
                                  KEY `FKol62yjo27kdte9u41p5ab6a6f` (`id_servicio`),
                                  CONSTRAINT `FKait80b9r3ge6bdsponrjnbtg9` FOREIGN KEY (`id_cliente`) REFERENCES `usuarios` (`id_usuario`),
                                  CONSTRAINT `FKfn2pcwvrlu8fo59hhn8925x0a` FOREIGN KEY (`id_proveedor`) REFERENCES `usuarios` (`id_usuario`),
                                  CONSTRAINT `FKol62yjo27kdte9u41p5ab6a6f` FOREIGN KEY (`id_servicio`) REFERENCES `servicios` (`id_servicio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------
-- 8. MENSAJES
-- --------------------------------------------
DROP TABLE IF EXISTS `mensajes`;
CREATE TABLE `mensajes` (
                            `id_mensaje` int NOT NULL AUTO_INCREMENT,
                            `contenido` text COLLATE utf8mb4_unicode_ci,
                            `fecha_envio` datetime(6) DEFAULT NULL,
                            `id_conversacion` int DEFAULT NULL,
                            `id_remitente` int DEFAULT NULL,
                            PRIMARY KEY (`id_mensaje`),
                            KEY `FK70ylh4dtvnhiprrcpev790ome` (`id_conversacion`),
                            KEY `FK91f20kcib8cge6x0exr74jkph` (`id_remitente`),
                            CONSTRAINT `FK70ylh4dtvnhiprrcpev790ome` FOREIGN KEY (`id_conversacion`) REFERENCES `conversaciones` (`id_conversacion`),
                            CONSTRAINT `FK91f20kcib8cge6x0exr74jkph` FOREIGN KEY (`id_remitente`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS=1;