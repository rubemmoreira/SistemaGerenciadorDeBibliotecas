CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL
);

INSERT INTO usuario (email, senha, nome) 
VALUES ('admin@biblioteca.com', '12345', 'Administrador Padrão');