CREATE DATABASE IF NOT EXISTS speedfast_db;
USE speedfast_db;

CREATE TABLE IF NOT EXISTS repartidor (
    idRepartidor INT AUTO_INCREMENT PRIMARY KEY,
    nombreRepartidor VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS pedido (
    idPedido INT AUTO_INCREMENT PRIMARY KEY,
    direccionEntrega VARCHAR(150) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    estado VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS entrega (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idPedido INT NOT NULL,
    idRepartidor INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    FOREIGN KEY (idPedido) REFERENCES pedido(idPedido),
    FOREIGN KEY (idRepartidor) REFERENCES repartidor(idRepartidor)
);