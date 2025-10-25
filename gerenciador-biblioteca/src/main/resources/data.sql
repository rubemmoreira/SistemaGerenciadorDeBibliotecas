-- Cria a tabela se não existir (para MySQL)
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL
);

-- Insere o usuário admin
INSERT IGNORE INTO usuario (email, senha, nome) 
VALUES ('admin@biblioteca.com', '123456', 'Administrador Padrão');