# 📚 Sistema de Gerenciamento de Bibliotecas

Este é um projeto de um sistema web completo para gerenciamento de bibliotecas, construído com **Java 21** e **Spring Boot 3.2**. A aplicação implementa funcionalidades de CRUD, controle de estoque, gerenciamento de empréstimos e um sistema de autenticação e autorização baseado em perfis (RBAC).

## ✨ Funcionalidades

### Técnicas
* **Arquitetura MVC (Model-View-Controller):** Separação clara de responsabilidades entre lógica de negócio, dados e apresentação.
* **Persistência com Spring Data JPA:** Abstração de acesso ao banco de dados com repositórios, facilitando operações CRUD e consultas complexas.
* **Segurança com Spring Security 6:** Controle robusto de autenticação (login) e autorização (permissões por perfil).
* **Renderização Server-Side com Thymeleaf:** Geração de HTML dinâmico no servidor, permitindo que as permissões de segurança sejam aplicadas diretamente na renderização dos componentes da tela.
* **Banco de Dados Relacional:** Utilização do MySQL para garantir integridade referencial entre livros, usuários e empréstimos.
* **Controle Transacional:** O registro de empréstimos utiliza transações (`@Transactional`) para garantir que o empréstimo só seja salvo se o estoque do livro for atualizado com sucesso (e vice-versa).
* **Inicialização de Dados:** O arquivo `data.sql` é executado na inicialização (`spring.jpa.defer-datasource-initialization=true`) para popular o banco com dados de exemplo.

### Funcionais
* **Autenticação:** Tela de login segura.
* **Controle de Acesso por Perfil:**
    * `ADMIN`: Acesso total, incluindo gerenciamento de usuários e relatórios.
    * `BIBLIOTECARIO`: Gerenciamento de livros e empréstimos.
    * `USUARIO`: Apenas visualização de livros e seus próprios empréstimos.
* **Dashboard Dinâmico:** O menu principal se adapta e exibe as funcionalidades permitidas para o usuário logado.
* **Gerenciamento de Livros (CRUD):** Cadastro, listagem, busca, edição e exclusão de livros.
* **Gerenciamento de Usuários (CRUD):** (Apenas ADMIN) Gerenciamento completo de usuários do sistema.
* **Gerenciamento de Empréstimos:**
    * Registro de novos empréstimos (associando um livro a um usuário).
    * Listagem de empréstimos ativos, atrasados e devolvidos.
    * Registro de devolução (edição de empréstimo).
* **Controle de Estoque:** Ao realizar um empréstimo, a quantidade do livro é abatida do estoque. Ao devolver, a quantidade é restaurada.
* **Relatórios e Estatísticas:**
    * KPIs com total de livros, usuários e empréstimos.
    * Ranking de livros mais emprestados e usuários mais ativos (usando consultas de agregação `COUNT` e `GROUP BY`).

---

## ⚙️ Stack Tecnológica

| Categoria | Tecnologia | Versão/Descrição |
| :--- | :--- | :--- |
| **Linguagem** | Java | 21 |
| **Framework Principal** | Spring Boot | 3.2.0 |
| **Banco de Dados** | MySQL | 8.0 |
| **Persistência** | Spring Data JPA (Hibernate) | Para mapeamento objeto-relacional (ORM) e repositórios. |
| **Segurança** | Spring Security | 6 (Para autenticação e autorização). |
| **Servidor Web** | Spring Web (MVC) | Para construção dos *Controllers* e da aplicação. |
| **Frontend (View)** | Thymeleaf | Motor de templates (Server-Side Rendering). |
| **Build / Dependências** | Apache Maven | Gerenciamento do projeto e suas bibliotecas. |
| **Conector DB** | `mysql-connector-j` | Driver JDBC para conexão com o MySQL. |

---

## 🏛️ Arquitetura e Banco de Dados

### Padrão MVC
* **Model:** As classes de entidade (`@Entity`) que mapeiam o banco de dados: `Usuario`, `Livro`, `Emprestimo`.
* **View:** Os arquivos `.html` em `src/main/resources/templates` que usam Thymeleaf para exibir os dados.
* **Controller:** As classes Java (`@Controller`) que recebem as requisições web, processam a lógica e retornam a *View* correta.

### Esquema do Banco de Dados

O `data.sql` define a estrutura principal:

1.  **`usuario`**: Armazena os dados de login, informações pessoais e o `tipo_usuario` (ADMIN, BIBLIOTECARIO, USUARIO).
    * `id (PK)`
    * `email (UNIQUE)`
    * `senha`
    * `nome`
    * `tipo_usuario`
    * `ativo`
2.  **`livro`**: Armazena o acervo, incluindo a `quantidade` para controle de estoque.
    * `id (PK)`
    * `titulo`
    * `autor`
    * `isbn (UNIQUE)`
    * `quantidade`
    * `... (outros campos)`
3.  **`emprestimo`**: Tabela associativa que conecta `livro` e `usuario`.
    * `id (PK)`
    * `livro_id (FK -> livro.id)`
    * `usuario_id (FK -> usuario.id)`
    * `data_emprestimo`
    * `data_devolucao_prevista`
    * `data_devolucao_real`
    * `status` (ex: 'EMPRESTADO', 'DEVOLVIDO')

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* **Java JDK 21**
* **Apache Maven**
* **MySQL Server** (rodando localmente ou em um container)
* **Git**

### 1. Clonar o Repositório
```bash
git clone [https://github.com/rubemmoreira/SistemaGerenciadorDeBibliotecas.git](https://github.com/rubemmoreira/SistemaGerenciadorDeBibliotecas.git)
cd SistemaGerenciadorDeBibliotecas
```

# 2. Configurar o Banco de Dados

Acesse seu servidor **MySQL**.

Crie o banco de dados. O nome padrão esperado é **bibliotecadb**.

```sql
CREATE DATABASE bibliotecadb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

# 3. Configurar a Aplicação

Abra o arquivo `src/main/resources/application.properties`.

**IMPORTANTE:** Altere as linhas `spring.datasource.username` e `spring.datasource.password` com as suas credenciais de acesso ao MySQL.

```properties
# ================= CONFIGURAÇÃO MYSQL =================
spring.datasource.url=jdbc:mysql://localhost:3306/bibliotecadb?useUnicode=true&characterEncoding=utf8
spring.datasource.username=root
spring.datasource.password=sua_senha_aqui

# ... (demais configurações)

# Executar data.sql após criação das tabelas
spring.jpa.defer-datasource-initialization=true
```

---

# 4. Executar a Aplicação

Use o **Maven Wrapper** para iniciar o servidor **Spring Boot**.

```bash
# Em Linux/macOS
./mvnw spring-boot:run

# Em Windows
./mvnw.cmd spring-boot:run
```

O servidor será iniciado na porta **8080**.

---

# 5. Acessar o Sistema

Abra seu navegador e acesse:

```text
http://localhost:8080
```

Você será redirecionado para a tela de login.

---

## 🔑 Credenciais de Acesso (Padrão)

Use os dados de administrador padrão (do `data.sql`) para acessar todas as funcionalidades:

```text
Email: admin@biblioteca.com
Senha: 123456
```

> **Observação:** Em um projeto de produção real, as senhas no `data.sql` deveriam ser armazenadas em formato criptografado com **BCrypt**.




