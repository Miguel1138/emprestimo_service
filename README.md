# (Projeto) emprestimo-service

Este projeto é o microsserviço de gerenciamento de empréstimos para a API "Scripta" de biblioteca universitária. Ele é responsável por todo o ciclo de vida da locação de livros, incluindo solicitação, renovação, devolução, controle de prazos e aplicação automática de penalidades.

A aplicação é construída em **Java 21** com **Spring Boot 3.5.7** e atua como um **Resource Server**, validando tokens via **Spring Security** e comunicando-se com outros serviços via **WebClient**.

## Stack de Tecnologias

| Categoria | Tecnologia | Justificativa / Uso |
| :--- | :--- | :--- |
| **Core** | Java 21 | Linguagem principal da aplicação. |
| **Framework** | Spring Boot 3.5.7 | Framework base (Web, Data, Security, Validation). |
| **Segurança** | Spring Security | Configurado como Resource Server (OAuth2). |
| | Nimbus JOSE + JWT | Decodificação e validação de tokens JWT. |
| **Persistência** | Spring Data JPA | Camada de acesso a dados e abstração de repositórios. |
| **Banco de Dados** | PostgreSQL | Banco de dados relacional para produção. |
| | H2 Database | Banco de dados em memória para testes. |
| **Comunicação** | Spring WebFlux | Cliente HTTP reativo (`WebClient`) para chamadas externas. |
| **Resiliência** | Spring Retry | Retentativas automáticas (`@Retryable`) em integrações instáveis. |
| **Agendamento** | Spring Scheduling | Jobs agendados (`@Scheduled`) para verificação de atrasos. |
| **Utilitários** | Lombok | Redução de código boilerplate (ex: @Data, @Builder). |

## Arquitetura (Design)

A arquitetura do projeto segue o padrão **Arquitetura Hexagonal** (Ports and Adapters). As regras de negócio e as entidades de domínio (`Emprestimo`, `Penalidade`) residem no pacote `application.domain`, totalmente isoladas de frameworks. As interfaces de uso (`EmprestimoService`) definem as "Portas".

A camada de `Infrastructure` (`infra`) e `Repository` (`repository`) atuam como **Adapters**. O `EmprestimoRepository` implementa a porta de serviço, orquestrando a persistência via `EmprestimoEntityRepository` (JPA) e a comunicação externa via `CatalogoServiceOrquestrador`. A integração com o catálogo utiliza **Consistência Eventual** com mecanismos de retry para garantir a baixa no estoque. A segurança é configurada para validar tokens assinados simetricamente (HMAC), extraindo as *roles* para autorização nos endpoints.

## Configuração e Execução (IntelliJ IDEA)

### 1. Pré-requisitos

* Java 21 (JDK)
* Uma instância do PostgreSQL rodando.
* O serviço de Catálogo (`catalogo-service`) rodando (ou URL configurada).

### 2. Configurando o IntelliJ IDEA

1.  **Abra o projeto:**
    * Abra o projeto (a pasta `emprestimo_service`) no IntelliJ IDEA.
    * A IDE detectará o arquivo `pom.xml` e deve baixar automaticamente todas as dependências do Maven.

2.  **Configure as Variáveis de Ambiente:**
    * A aplicação precisa de variáveis de ambiente para banco de dados, segurança e integração.
    * No canto superior direito do IntelliJ, clique em `Edit Configurations...`.
    * Na janela que abrir, localize a aplicação `EmprestimoServiceApplication`.
    * No campo **"Environment variables"**, adicione o seguinte, substituindo pelos seus valores:

    ```bash
    DB_LOAN=db_loan_scripta;
    POSTGRE_USERNAME=seu_usuario_postgre;
    POSTGRE_PASSWORD=sua_senha_postgre;
    JWT_SECRECT_KEY=sua_chave_secreta_aqui;
    client.catalogo.url=http://localhost:8082/livros
    ```

    * Clique em "OK" para salvar.

3.  **Execute a Aplicação:**
    * Navegue até `src/main/java/br/com/scripta_api/emprestimo_service/EmprestimoServiceApplication.java`.
    * Clique no ícone verde "Play" ao lado da classe.
    * A aplicação iniciará no console, rodando na porta `8083`.

## API Endpoints (Contrato da API)

### Módulo: Empréstimos (`/emprestimos`)

#### `POST /emprestimos`

Solicita o empréstimo de um livro. Verifica limites e status do aluno.

* **Acesso**: Autenticado. Requer Role: `ALUNO`.
* **Request Body** (`SolicitacaoEmprestimoRequest`):

    ```json
    {
      "livroId": 10
    }
    ```

* **Response 202 Accepted** (`EmprestimoResponse`):
  *(Retorna 202 pois o processo de baixa de estoque pode ser assíncrono/eventual)*

    ```json
    {
      "id": 1,
      "livroId": 10,
      "status": "PENDENTE",
      "dataEmprestimo": "2025-10-20",
      "dataPrevistaDevolucao": null,
      "renovado": false
    }
    ```

#### `POST /emprestimos/{id}/renovar`

Renova um empréstimo por mais 30 dias, caso elegível.

* **Acesso**: Autenticado. Requer Role: `ALUNO`.
* **Response 200 OK** (`EmprestimoResponse`):

    ```json
    {
      "id": 1,
      "status": "CONFIRMADO",
      "renovado": true,
      "dataPrevistaDevolucao": "2025-11-20"
      ...
    }
    ```

#### `POST /emprestimos/devolucao`

Registra a devolução de um livro e calcula penalidades se houver atraso.

* **Acesso**: Autenticado. Requer Role: `BIBLIOTECARIO`.
* **Request Body** (`DevolucaoRequest`):

    ```json
    {
      "emprestimoId": 1
    }
    ```

* **Response 200 OK** (`EmprestimoResponse`): O status muda para `DEVOLVIDO`.

#### `GET /emprestimos/meus`

Lista os empréstimos ativos (Pendentes, Confirmados ou Atrasados) do aluno logado.

* **Acesso**: Autenticado. Requer Role: `ALUNO`.

#### `GET /emprestimos/historico/{alunoId}`

Consulta o histórico completo de empréstimos de um aluno específico.

* **Acesso**: Autenticado. Requer Role: `BIBLIOTECARIO`.

## Detalhes de Segurança

* **Resource Server**: Este serviço não gera tokens. Ele atua como um *Resource Server*, recebendo tokens gerados pelo `usuario-service`.
* **Validação JWT**: Utiliza uma chave secreta (`JWT_SECRECT_KEY`) compartilhada para validar a assinatura HMAC do token.
* **Extração de Roles**: O `JwtAuthenticationConverter` está configurado para ler a claim `roles` do token. O sistema espera que o token contenha as roles (ex: `ROLE_ALUNO`) para autorizar as rotas via anotações como `.hasRole("ALUNO")`.
* **Comunicação Segura**: Ao se comunicar com o `catalogo-service`, o token JWT recebido na requisição original é repassado no cabeçalho `Authorization` para manter a rastreabilidade e autorização.

---

### Links Úteis para Aprofundamento

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
* [Spring Retry](https://github.com/spring-projects/spring-retry)
* [Spring WebFlux (WebClient)](https://docs.spring.io/spring-framework/reference/web/webflux-client.html)
* [Arquitetura Hexagonal](https://engsoftmoderna.info/artigos/arquitetura-hexagonal.html)
