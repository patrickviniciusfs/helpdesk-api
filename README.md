# HelpDesk API

API REST de um sistema de abertura e acompanhamento de chamados técnicos, com autenticação JWT e dois perfis de acesso (usuário comum e técnico).

Este é o backend do projeto **HelpDesk - Sistema de Chamados**. O frontend (React) está no repositório [helpdesk-web](https://github.com/SEU-USUARIO/helpdesk-web).

## Sobre o projeto

Um usuário comum abre chamados técnicos e acompanha apenas os próprios. Um técnico visualiza todos os chamados do sistema, pode assumi-los para si e atualizar status/prioridade. A API foi construída em camadas (Controller → Service → Repository), com validação de entrada, tratamento global de erros e documentação interativa via Swagger.

## Tecnologias

- Java 17
- Spring Boot 3 (Web, Data JPA, Security, Validation)
- PostgreSQL
- JWT ([jjwt](https://github.com/jwtk/jjwt)) via cookie `HttpOnly`
- Springdoc OpenAPI (Swagger)
- Maven
- Lombok

## Arquitetura

```
controller/   → recebe requisições HTTP e retorna respostas
service/      → regras de negócio e autorização por dado
repository/   → acesso ao banco via Spring Data JPA
entity/       → mapeamento das tabelas
dto/          → contratos de entrada/saída da API (nunca expõe a Entity direto)
security/     → JWT, filtro de autenticação e configuração do Spring Security
exception/    → exceções customizadas e tratamento global de erros
enums/        → Prioridade, Status, Role
```

## Autenticação e autorização

- Login via `POST /auth/login` retorna um JWT dentro de um cookie `HttpOnly` (não acessível via JavaScript no navegador, mitigando roubo de token por XSS).
- Dois perfis (`Role`): `USUARIO` e `TECNICO`.
- `USUARIO` só visualiza e acessa os chamados que ele mesmo abriu.
- `TECNICO` visualiza todos os chamados, pode assumi-los (`POST /chamados/{id}/assumir`) e atualizar status/prioridade.
- Rotas protegidas usam `@PreAuthorize` por perfil; acesso a um chamado específico é validado por dono no Service.

## Modelo de dados

**Chamado**: id, título, descrição, solicitante (relacionamento com Usuario), técnico responsável (opcional), prioridade (`BAIXA`/`MEDIA`/`ALTA`), status (`ABERTO`/`EM_ANDAMENTO`/`RESOLVIDO`), data de criação.

**Usuario**: id, nome, email (único), senha (hash BCrypt), role (`USUARIO`/`TECNICO`).

## Endpoints principais

| Método | Endpoint | Acesso |
|---|---|---|
| POST | `/auth/registrar` | Público |
| POST | `/auth/login` | Público |
| POST | `/auth/logout` | Autenticado |
| GET | `/auth/me` | Autenticado |
| POST | `/chamados` | Autenticado |
| GET | `/chamados` | Autenticado (filtrado por perfil) |
| GET | `/chamados?prioridade=ALTA` | Autenticado |
| GET | `/chamados/{id}` | Autenticado (dono ou técnico) |
| PUT | `/chamados/{id}` | Somente técnico |
| POST | `/chamados/{id}/assumir` | Somente técnico |
| DELETE | `/chamados/{id}` | Somente técnico |

Documentação completa e interativa disponível via Swagger em `/swagger-ui.html` com a aplicação rodando.

## Rodando localmente

### Pré-requisitos

- Java 17+
- Maven
- PostgreSQL rodando localmente

### Passos

```bash
# 1. Clone o repositório
git clone https://github.com/patrickviniciusfs/helpdesk-api.git
cd helpdesk-api

# 2. Crie o banco de dados
psql -U postgres -c "CREATE DATABASE helpdesk_db;"

# 3. Ajuste as credenciais do banco em
# src/main/resources/application.properties, se necessário

# 4. Rode a aplicação
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`.

### Variáveis sensíveis

O `application.properties` traz uma chave JWT (`jwt.secret`) e configuração de cookie (`app.cookie.secure=false`) preparadas para desenvolvimento local. **Em produção, a chave JWT deve vir de variável de ambiente** — isso é tratado no deploy (ver seção abaixo).

## Deploy

Pensado para deploy no [Render](https://render.com) - veja as instruções específicas na seção de deploy do repositório principal / issue de deploy.

## Roadmap (possíveis evoluções)

- Migrations com Flyway em vez de `ddl-auto=update`
- Testes automatizados (unitários e de integração)
- Paginação na listagem de chamados
- Notificação por e-mail ao mudar status

## Autor

Desenvolvido como projeto de portfólio para vaga de Desenvolvedor Full Stack Júnior.

