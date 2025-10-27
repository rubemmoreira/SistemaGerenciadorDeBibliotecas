SET NAMES 'utf8mb4';
SET CHARACTER SET utf8mb4;
SET collation_connection = 'utf8mb4_unicode_ci';
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(50) NOT NULL DEFAULT 'USUARIO',
    ativo BOOLEAN NOT NULL DEFAULT TRUE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE TABLE IF NOT EXISTS livro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    editora VARCHAR(255),
    ano_publicacao INT,
    categoria VARCHAR(100),
    quantidade INT NOT NULL DEFAULT 0,
    descricao TEXT
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE TABLE IF NOT EXISTS emprestimo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    livro_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    data_emprestimo DATETIME NOT NULL,
    data_devolucao_prevista DATETIME NOT NULL,
    data_devolucao_real DATETIME,
    status VARCHAR(50) NOT NULL,
    observacoes VARCHAR(500),
    FOREIGN KEY (livro_id) REFERENCES livro(id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
INSERT IGNORE INTO usuario (id, email, senha, nome, tipo_usuario, ativo) 
VALUES (1, 'admin@biblioteca.com', '123456', 'Administrador Padrão', 'ADMIN', TRUE);
INSERT IGNORE INTO usuario (id, email, senha, nome, tipo_usuario, ativo) 
VALUES 
(2, 'ana.silva@email.com', '123456', 'Ana Silva', 'USUARIO', TRUE),
(3, 'carlos.santos@email.com', '123456', 'Carlos Santos', 'USUARIO', TRUE),
(4, 'maria.oliveira@email.com', '123456', 'Maria Oliveira', 'USUARIO', TRUE),
(5, 'joao.pereira@email.com', '123456', 'João Pereira', 'USUARIO', TRUE),
(6, 'lucia.ferreira@email.com', '123456', 'Lúcia Ferreira', 'USUARIO', TRUE),
(7, 'pedro.costa@email.com', '123456', 'Pedro Costa', 'USUARIO', TRUE),
(8, 'fernanda.lima@email.com', '123456', 'Fernanda Lima', 'USUARIO', TRUE),
(9, 'rafael.mendes@email.com', '123456', 'Rafael Mendes', 'USUARIO', TRUE),
(10, 'julia.rodrigues@email.com', '123456', 'Júlia Rodrigues', 'USUARIO', TRUE),
(11, 'bruno.almeida@email.com', '123456', 'Bruno Almeida', 'USUARIO', TRUE);
INSERT IGNORE INTO usuario (id, email, senha, nome, tipo_usuario, ativo) 
VALUES 
(12, 'prof.martins@email.com', '123456', 'Prof. Dr. Martins', 'BIBLIOTECARIO', TRUE),
(13, 'prof.silva@email.com', '123456', 'Prof. Dra. Silva', 'BIBLIOTECARIO', TRUE),
(14, 'prof.oliveira@email.com', '123456', 'Prof. Dr. Oliveira', 'BIBLIOTECARIO', TRUE);
INSERT IGNORE INTO livro (id, titulo, autor, isbn, editora, ano_publicacao, categoria, quantidade, descricao) 
VALUES 
(1, 'Java: Como Programar', 'Deitel & Deitel', '978-0134685991', 'Pearson', 2017, 'Programação', 5, 'Livro completo sobre programação Java'),
(2, 'Spring Boot in Action', 'Craig Walls', '978-1617292545', 'Manning', 2019, 'Programação', 3, 'Guia prático para desenvolvimento com Spring Boot'),
(3, 'Clean Code', 'Robert Martin', '978-0132350884', 'Prentice Hall', 2008, 'Programação', 4, 'Princípios para escrever código limpo'),
(4, 'Effective Java', 'Joshua Bloch', '978-0134685992', 'Addison-Wesley', 2018, 'Programação', 2, 'Melhores práticas para programadores Java'),
(5, 'Design Patterns', 'Erich Gamma et al.', '978-0201633610', 'Addison-Wesley', 1994, 'Programação', 1, 'Padrões de projeto de software'),
(6, 'Python Crash Course', 'Eric Matthes', '978-1593279288', 'No Starch Press', 2019, 'Programação', 6, 'Introdução rápida à programação com Python'),
(7, 'Eloquent JavaScript', 'Marijn Haverbeke', '978-1593279509', 'No Starch Press', 2018, 'Programação', 3, 'Guia moderno para programação JavaScript'),
(8, 'React in Action', 'Mark Tielens Thomas', '978-1617296109', 'Manning', 2020, 'Programação', 2, 'Desenvolvimento de aplicações web com React');
INSERT IGNORE INTO livro (id, titulo, autor, isbn, editora, ano_publicacao, categoria, quantidade, descricao) 
VALUES 
(9, 'O Senhor dos Anéis', 'J.R.R. Tolkien', '978-0544003415', 'HarperCollins', 1954, 'Fantasia', 8, 'Trilogia épica de fantasia'),
(10, '1984', 'George Orwell', '978-0451524935', 'Signet', 1949, 'Ficção', 6, 'Romance distópico clássico'),
(11, 'Duna', 'Frank Herbert', '978-0441172719', 'Ace Books', 1965, 'Ficção Científica', 5, 'Clássico da ficção científica'),
(12, 'Fundação', 'Isaac Asimov', '978-0553803717', 'Bantam Spectra', 1951, 'Ficção Científica', 4, 'Primeiro livro da série Fundação'),
(13, 'O Hobbit', 'J.R.R. Tolkien', '978-0345339683', 'Del Rey', 1937, 'Fantasia', 7, 'A jornada de Bilbo Bolseiro'),
(14, 'Neuromancer', 'William Gibson', '978-0441569595', 'Ace Books', 1984, 'Cyberpunk', 3, 'Marco do gênero cyberpunk'),
(15, 'O Guia do Mochileiro das Galáxias', 'Douglas Adams', '978-0345391805', 'Del Rey', 1979, 'Ficção Científica', 6, 'Comédia de ficção científica'),
(16, 'Cem Anos de Solidão', 'Gabriel García Márquez', '978-0060883287', 'Harper Perennial', 1967, 'Realismo Mágico', 4, 'Obra-prima da literatura latino-americana');
INSERT IGNORE INTO livro (id, titulo, autor, isbn, editora, ano_publicacao, categoria, quantidade, descricao) 
VALUES 
(17, 'Princípios de Economia', 'N. Gregory Mankiw', '978-8522128021', 'Cengage Learning', 2015, 'Economia', 3, 'Introdução aos princípios da economia'),
(18, 'História do Brasil', 'Boris Fausto', '978-8531405721', 'Edusp', 2006, 'História', 4, 'Visão abrangente da história brasileira'),
(19, 'Fundamentos de Física', 'Halliday, Resnick, Walker', '978-8521620000', 'LTC', 2014, 'Física', 2, 'Livro texto padrão de física'),
(20, 'Química Orgânica', 'Paula Yurkanis Bruice', '978-8582604570', 'Pearson', 2017, 'Química', 3, 'Texto completo de química orgânica'),
(21, 'Cálculo Vol. 1', 'James Stewart', '978-8522112587', 'Cengage Learning', 2013, 'Matemática', 5, 'Cálculo diferencial e integral'),
(22, 'Introdução à Psicologia', 'Charles G. Morris, Albert A. Maisto', '978-8582602002', 'Pearson', 2015, 'Psicologia', 3, 'Fundamentos da psicologia'),
(23, 'Sociologia', 'Anthony Giddens', '978-8582602003', 'Penso', 2012, 'Sociologia', 2, 'Conceitos e teorias sociológicas'),
(24, 'O Mundo de Sofia', 'Jostein Gaarder', '978-8535902740', 'Companhia das Letras', 1995, 'Filosofia', 4, 'Romance sobre a história da filosofia');
INSERT IGNORE INTO livro (id, titulo, autor, isbn, editora, ano_publicacao, categoria, quantidade, descricao) 
VALUES 
(25, 'A Arte da Guerra', 'Sun Tzu', '978-8573263334', 'Martin Claret', 2005, 'Negócios', 5, 'Estratégias militares aplicadas a negócios'),
(26, 'O Poder do Hábito', 'Charles Duhigg', '978-8539502726', 'Objetiva', 2012, 'Negócios', 4, 'Como os hábitos funcionam e como mudá-los'),
(27, 'The Lean Startup', 'Eric Ries', '978-0307887894', 'Crown Business', 2011, 'Negócios', 3, 'Abordagem para criar empresas inovadoras'),
(28, 'Pai Rico, Pai Pobre', 'Robert Kiyosaki', '978-8575421175', 'Elsevier', 2000, 'Finanças', 6, 'Lições sobre finanças pessoais'),
(29, 'Os 7 Hábitos das Pessoas Altamente Eficazes', 'Stephen Covey', '978-8576840612', 'FranklinCovey', 1989, 'Autoajuda', 5, 'Guia para eficácia pessoal e profissional'),
(30, 'Think and Grow Rich', 'Napoleon Hill', '978-1585424337', 'TarcherPerigee', 1937, 'Autoajuda', 4, 'Filosofia para o sucesso pessoal');
INSERT IGNORE INTO livro (id, titulo, autor, isbn, editora, ano_publicacao, categoria, quantidade, descricao) 
VALUES 
(31, 'Dom Casmurro', 'Machado de Assis', '978-8580860613', 'Penguin Companhia', 1899, 'Literatura Brasileira', 7, 'Clássico da literatura brasileira'),
(32, 'O Cortiço', 'Aluísio Azevedo', '978-8572328000', 'Martin Claret', 1890, 'Literatura Brasileira', 6, 'Romance naturalista brasileiro'),
(33, 'Capitães da Areia', 'Jorge Amado', '978-8535914099', 'Companhia das Letras', 1937, 'Literatura Brasileira', 5, 'A vida de meninos de rua em Salvador'),
(34, 'Grande Sertão: Veredas', 'João Guimarães Rosa', '978-8520931712', 'Nova Fronteira', 1956, 'Literatura Brasileira', 4, 'Obra-prima da literatura modernista'),
(35, 'Macunaíma', 'Mário de Andrade', '978-8503012302', 'Livraria José Olympio', 1928, 'Literatura Brasileira', 3, 'Herói sem nenhum caráter'),
(36, 'A Hora da Estrela', 'Clarice Lispector', '978-8532507622', 'Rocco', 1977, 'Literatura Brasileira', 2, 'A história de Macabéa');
INSERT IGNORE INTO livro (id, titulo, autor, isbn, editora, ano_publicacao, categoria, quantidade, descricao) 
VALUES 
(37, 'Teste Estoque Baixo', 'Autor Teste', '978-1111111111', 'Editora Teste', 2020, 'Não Ficção', 1, 'Livro para teste de estoque baixo'),
(38, 'Teste Estoque Zero', 'Autor Teste', '978-2222222222', 'Editora Teste', 2021, 'Ficção', 0, 'Livro para teste de estoque zero');
INSERT IGNORE INTO emprestimo (id, livro_id, usuario_id, data_emprestimo, data_devolucao_prevista, status, observacoes) 
VALUES 
(1, 1, 2, '2025-10-20 10:00:00', '2025-10-27 10:00:00', 'EMPRESTADO', 'Empréstimo normal para Ana Silva'),
(2, 3, 3, '2025-10-22 11:00:00', '2025-10-29 11:00:00', 'EMPRESTADO', 'Empréstimo normal para Carlos Santos'),
(3, 9, 4, '2025-10-23 14:00:00', '2025-10-30 14:00:00', 'EMPRESTADO', 'Empréstimo normal para Maria Oliveira'),
(4, 10, 5, '2025-10-24 09:00:00', '2025-10-31 09:00:00', 'EMPRESTADO', 'Empréstimo normal para João Pereira'),
(5, 17, 6, '2025-10-25 16:00:00', '2025-11-01 16:00:00', 'EMPRESTADO', 'Empréstimo normal para Lúcia Ferreira'),
(6, 25, 7, '2025-10-26 08:30:00', '2025-11-02 08:30:00', 'EMPRESTADO', 'Empréstimo normal para Pedro Costa'),
(7, 31, 2, '2025-10-21 10:00:00', '2025-10-28 10:00:00', 'EMPRESTADO', 'Segundo livro de Ana Silva'),
(8, 2, 2, '2025-10-22 10:00:00', '2025-10-29 10:00:00', 'EMPRESTADO', 'Terceiro livro de Ana Silva');
INSERT IGNORE INTO emprestimo (id, livro_id, usuario_id, data_emprestimo, data_devolucao_prevista, data_devolucao_real, status, observacoes) 
VALUES 
(9, 4, 3, '2025-10-01 10:00:00', '2025-10-08 10:00:00', '2025-10-07 15:00:00', 'DEVOLVIDO', 'Devolvido antes do prazo'),
(10, 11, 5, '2025-10-05 14:00:00', '2025-10-12 14:00:00', '2025-10-12 10:00:00', 'DEVOLVIDO', 'Devolvido no prazo'),
(11, 18, 6, '2025-10-08 09:00:00', '2025-10-15 09:00:00', '2025-10-14 11:00:00', 'DEVOLVIDO', 'Devolvido antes do prazo'),
(12, 26, 7, '2025-10-10 11:00:00', '2025-10-17 11:00:00', '2025-10-17 16:00:00', 'DEVOLVIDO', 'Devolvido no prazo'),
(13, 32, 8, '2025-10-12 13:00:00', '2025-10-19 13:00:00', '2025-10-18 09:00:00', 'DEVOLVIDO', 'Devolvido antes do prazo');
INSERT IGNORE INTO emprestimo (id, livro_id, usuario_id, data_emprestimo, data_devolucao_prevista, status, observacoes) 
VALUES 
(14, 5, 8, '2025-10-10 10:00:00', '2025-10-17 10:00:00', 'EMPRESTADO', 'Livro atrasado para Fernanda Lima'),
(15, 12, 9, '2025-10-11 11:00:00', '2025-10-18 11:00:00', 'EMPRESTADO', 'Livro atrasado para Rafael Mendes'),
(16, 19, 10, '2025-10-12 14:00:00', '2025-10-19 14:00:00', 'EMPRESTADO', 'Livro atrasado para Júlia Rodrigues'),
(17, 27, 11, '2025-10-13 09:00:00', '2025-10-20 09:00:00', 'EMPRESTADO', 'Livro atrasado para Bruno Almeida'),
(18, 33, 1, '2025-10-14 16:00:00', '2025-10-21 16:00:00', 'EMPRESTADO', 'Livro atrasado para Administrador Padrão');
INSERT IGNORE INTO emprestimo (id, livro_id, usuario_id, data_emprestimo, data_devolucao_prevista, data_devolucao_real, status, observacoes) 
VALUES 
(19, 6, 8, '2025-09-20 10:00:00', '2025-09-27 10:00:00', '2025-09-29 10:00:00', 'DEVOLVIDO', 'Devolvido com 2 dias de atraso (Fernanda Lima)'),
(20, 13, 8, '2025-09-25 11:00:00', '2025-10-02 11:00:00', '2025-10-05 11:00:00', 'DEVOLVIDO', 'Devolvido com 3 dias de atraso (Fernanda Lima)'),
(21, 20, 8, '2025-10-01 14:00:00', '2025-10-08 14:00:00', '2025-10-10 14:00:00', 'DEVOLVIDO', 'Devolvido com 2 dias de atraso (Fernanda Lima)'),
(22, 28, 9, '2025-10-03 09:00:00', '2025-10-10 09:00:00', '2025-10-11 09:00:00', 'DEVOLVIDO', 'Devolvido com 1 dia de atraso'),
(23, 34, 10, '2025-10-05 16:00:00', '2025-10-12 16:00:00', '2025-10-15 16:00:00', 'DEVOLVIDO', 'Devolvido com 3 dias de atraso');
INSERT IGNORE INTO emprestimo (id, livro_id, usuario_id, data_emprestimo, data_devolucao_prevista, status, observacoes) 
VALUES 
(24, 7, 12, '2025-10-01 10:00:00', '2025-10-31 10:00:00', 'EMPRESTADO', 'Empréstimo de professor (30 dias)'),
(25, 14, 13, '2025-10-05 11:00:00', '2025-11-04 11:00:00', 'EMPRESTADO', 'Empréstimo de professor (30 dias)'),
(26, 21, 14, '2025-10-10 14:00:00', '2025-11-09 14:00:00', 'EMPRESTADO', 'Empréstimo de professor (30 dias)');
