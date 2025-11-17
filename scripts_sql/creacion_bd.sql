-- Creacion del esquema
CREATE DATABASE IF NOT EXISTS tpi_usuarios;
USE tpi_usuarios;

-- Creacion de la tabla CredencialAcceso (B)
CREATE TABLE CredencialAcceso (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado TINYINT DEFAULT 0,
    hashPassword VARCHAR(255) NOT NULL,
    salt VARCHAR(64),
    ultimoCambio DATETIME,
    requiereReset TINYINT DEFAULT 0
);

-- Creacion de la tabla Usuario (A)
-- Usuario → CredencialAcceso (1 : 1)
CREATE TABLE Usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(30) NOT NULL,
    email VARCHAR(120) NOT NULL,
    activo TINYINT NOT NULL,
    fechaRegistro DATETIME,
    CredencialAcceso_id BIGINT UNIQUE,
    eliminado TINYINT DEFAULT 0
);

-- Creacion de la relación 1→1 (FK única)
-- Usuario.CredencialAcceso_id → CredencialAcceso.id
ALTER TABLE Usuario
ADD CONSTRAINT fk_usuario_credencial
FOREIGN KEY (CredencialAcceso_id)
REFERENCES CredencialAcceso(id)
ON DELETE CASCADE;