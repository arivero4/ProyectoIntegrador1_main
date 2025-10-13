-- Script SQL para crear la tabla de propietarios
-- Base de datos: MySQL
-- Proyecto: Sistema de Inspecciones Fitosanitarias

-- Crear la tabla propietarios si no existe
CREATE TABLE IF NOT EXISTS propietarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario VARCHAR(50) NOT NULL UNIQUE,
    rol VARCHAR(50) NOT NULL DEFAULT 'Propietario',
    tipo_identificacion VARCHAR(20) NOT NULL,
    numero_identificacion VARCHAR(50) NOT NULL UNIQUE,
    nombres_completos VARCHAR(200) NOT NULL,
    telefono_contacto VARCHAR(20),
    correo_electronico VARCHAR(100),
    codigo_ica_predio VARCHAR(50),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_numero_identificacion (numero_identificacion),
    INDEX idx_nombres (nombres_completos),
    INDEX idx_codigo_ica (codigo_ica_predio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar datos de prueba (opcional)
INSERT INTO propietarios (
    id_usuario, 
    rol, 
    tipo_identificacion, 
    numero_identificacion, 
    nombres_completos, 
    telefono_contacto, 
    correo_electronico, 
    codigo_ica_predio
) VALUES 
(
    'PROP-123456789',
    'Propietario',
    'CC',
    '123456789',
    'Juan Pérez García',
    '3001234567',
    'juan.perez@email.com',
    'ICA-001'
),
(
    'PROP-987654321',
    'Propietario',
    'NIT',
    '987654321',
    'María López Rodríguez',
    '3109876543',
    'maria.lopez@email.com',
    'ICA-002'
),
(
    'PROP-555555555',
    'Propietario',
    'CE',
    '555555555',
    'Carlos Ramírez Sánchez',
    '3155555555',
    'carlos.ramirez@email.com',
    'ICA-003'
);

-- Verificar los datos insertados
SELECT * FROM propietarios;

-- Consultas útiles para pruebas

-- Consultar un propietario por número de identificación
-- SELECT * FROM propietarios WHERE numero_identificacion = '123456789';

-- Actualizar un propietario
-- UPDATE propietarios 
-- SET nombres_completos = 'Nuevo Nombre', 
--     telefono_contacto = '3009999999'
-- WHERE numero_identificacion = '123456789';

-- Eliminar un propietario (usar con precaución)
-- DELETE FROM propietarios WHERE numero_identificacion = '123456789';

-- Contar total de propietarios
-- SELECT COUNT(*) as total_propietarios FROM propietarios;

-- Listar propietarios por tipo de identificación
-- SELECT tipo_identificacion, COUNT(*) as cantidad 
-- FROM propietarios 
-- GROUP BY tipo_identificacion;
