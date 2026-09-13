# AlgaFood API

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.16-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-brightgreen)
![Spring Authorization Server](https://img.shields.io/badge/Spring%20Authorization%20Server-1.5.x-green)
![SpringDoc](https://img.shields.io/badge/SpringDoc-2.8.11-green)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue)
![Maven](https://img.shields.io/badge/Maven-3.x-red)

API REST de delivery desenvolvida como projeto de estudo do curso **Especialista Spring REST**, da AlgaWorks, modernizada para **Java 17**, **Spring Boot 3.5.x**, **Spring Security 6**, **Spring Authorization Server** e **SpringDoc/OpenAPI 3**.

O projeto concentra **Resource Server** e **Authorization Server** na mesma aplicação e implementa autenticação e autorização OAuth2/JWT, persistência de clients, autorizações e consentimentos, documentação OpenAPI, versionamento da API, HATEOAS, relatórios PDF, envio de e-mails, upload de imagens, migrações Flyway e testes de integração.

> Estado atual: projeto atualizado e validado funcionalmente até os conteúdos equivalentes aos módulos 26, 27 e 28 do curso, adaptados para a stack moderna do Spring Boot 3.

---

## Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 17 | Linguagem |
| Spring Boot 3.5.16 | Base da aplicação |
| Spring MVC | API REST |
| Spring Data JPA / Hibernate | Persistência |
| MySQL | Banco de dados |
| Flyway | Migrações |
| Spring Security | Segurança |
| Spring Authorization Server | Authorization Server OAuth2 |
| OAuth2 Resource Server | Validação de JWT |
| JWT / JWK | Access Tokens |
| Thymeleaf | Login, consentimento e clients autorizados |
| SpringDoc OpenAPI 2.8.11 | Swagger / OpenAPI |
| Spring HATEOAS | Hypermedia |
| ModelMapper | Conversão DTO/model |
| Bean Validation / Jakarta Validation | Validação |
| JasperReports | Relatórios PDF |
| FreeMarker | Templates de e-mail |
| Rest Assured | Testes de integração |
| Maven | Build e gerenciamento de dependências |

---

## Principais funcionalidades

A API possui recursos para gerenciamento de:

- cozinhas;
- restaurantes;
- produtos e fotos;
- cidades e estados;
- formas de pagamento;
- usuários, grupos e permissões;
- pedidos e fluxo de pedidos;
- responsáveis por restaurantes;
- relatórios de vendas;
- upload e armazenamento local de fotos;
- envio de e-mails;
- documentação OpenAPI;
- versionamento `/v1` e `/v2`.

Além disso, a aplicação implementa:

- OAuth2 Authorization Code;
- Refresh Token;
- Client Credentials;
- PKCE com `S256`;
- JWT com claims customizadas;
- scopes `READ` e `WRITE`;
- authorities de domínio;
- login customizado;
- tela customizada de consentimento;
- persistência JDBC de clients OAuth2;
- persistência JDBC de autorizações;
- persistência JDBC de consentimentos;
- listagem de aplicações autorizadas;
- revogação de consentimentos e autorizações.

---

## Arquitetura de segurança

A aplicação atua simultaneamente como:

```text
┌───────────────────────────────────────┐
│             AlgaFood API              │
│                                       │
│  ┌─────────────────────────────────┐  │
│  │     Authorization Server        │  │
│  │ /oauth2/authorize               │  │
│  │ /oauth2/token                   │  │
│  │ /oauth2/revoke                  │  │
│  └─────────────────────────────────┘  │
│                  │ JWT                │
│                  ▼                    │
│  ┌─────────────────────────────────┐  │
│  │       Resource Server           │  │
│  │ /v1/**                          │  │
│  │ /v2/**                          │  │
│  └─────────────────────────────────┘  │
└───────────────────────────────────────┘
```

A autorização dos endpoints combina:

- scopes OAuth2 (`SCOPE_READ` e `SCOPE_WRITE`);
- authorities de negócio;
- Method Security;
- anotações customizadas `@CheckSecurity`.

---

## Claims customizadas do JWT

Para autenticação de usuário, o access token pode conter:

```json
{
  "usuario_id": "1",
  "nome_completo": "João da Silva",
  "authorities": [
    "EDITAR_COZINHAS",
    "EDITAR_RESTAURANTES"
  ]
}
```

Para clients do fluxo `client_credentials`, as authorities podem ser configuradas diretamente no `RegisteredClient`.

---

## Requisitos

Antes de executar o projeto, tenha instalado:

- Java 17;
- Maven 3.8+;
- MySQL;
- Git.

Verifique:

```bash
java -version
mvn -version
```

---

## Banco de dados

Por padrão, o perfil de desenvolvimento utiliza:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/algafood?createDatabaseIfNotExist=true&useTimezone=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

Esses valores podem ser sobrescritos pelas variáveis:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

As migrações são executadas automaticamente pelo Flyway.

Atualmente o projeto contém migrações até:

```text
V014__cria-tabelas-oauth2-authorization-e-consent.sql
```

No perfil `dev`, os dados de desenvolvimento também são carregados a partir de:

```text
classpath:db/testdata
```

---

## Executando localmente

Clone o repositório:

```bash
git clone <URL_DO_SEU_REPOSITORIO>
cd algafood-api
```

Execute:

```bash
mvn spring-boot:run
```

Ou gere o build:

```bash
mvn clean verify
```

Depois:

```bash
java -jar target/algafood-api-0.0.1-SNAPSHOT.jar
```

A aplicação utiliza por padrão:

```text
http://localhost:8080
```

O profile default é:

```text
dev
```

---

## Profiles

### Desenvolvimento

```text
dev
```

Características:

- SQL visível no console;
- dados de teste carregados pelo Flyway;
- e-mail fake;
- clients OAuth2 de desenvolvimento criados automaticamente;
- Swagger UI configurado para OAuth2.

### Produção

```text
prod
```

Ative com:

```bash
java -jar target/algafood-api-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod
```

No ambiente de produção, configure as variáveis necessárias.

---

## Variáveis de ambiente de produção

| Variável | Descrição |
|---|---|
| `DB_URL` | URL JDBC do MySQL |
| `DB_USERNAME` | Usuário do banco |
| `DB_PASSWORD` | Senha do banco |
| `MAIL_HOST` | Servidor SMTP |
| `MAIL_PORT` | Porta SMTP |
| `MAIL_FROM` | Remetente dos e-mails |
| `AUTH_SERVER_ISSUER` | Issuer do Authorization Server |
| `JWT_KEYSTORE_LOCATION` | Localização do keystore |
| `JWT_KEYSTORE_PASSWORD` | Senha do keystore |
| `JWT_KEYSTORE_ALIAS` | Alias da chave |
| `ALGAFOOD_STORAGE_LOCAL_DIRETORIO_FOTOS` | Diretório de fotos |

> Não utilize as credenciais de desenvolvimento em produção.

---

## OAuth2 — clients de desenvolvimento

Os clients abaixo são inicializados apenas no profile `dev`.

| Client | Grant Types | Autenticação | Observação |
|---|---|---|---|
| `swagger-ui` | Authorization Code, Refresh Token | Basic | Swagger UI |
| `algafood-web` | Authorization Code, Refresh Token | Basic | Exige consentimento |
| `foodanalytics` | Authorization Code, Refresh Token | Public client | PKCE S256 obrigatório |
| `faturamento` | Client Credentials | Basic | Authorities de pedidos e relatórios |
| `checktoken` | Client Credentials | Basic | Client auxiliar |

### Credenciais de desenvolvimento

```text
swagger-ui
secret: swagger123
```

```text
algafood-web
secret: web123
```

```text
faturamento
secret: faturamento123
```

```text
checktoken
secret: check123
```

O `foodanalytics` é um **public client** e não possui `client_secret`.

---

## Authorization Code

Endpoints:

```text
GET  /oauth2/authorize
POST /oauth2/token
```

Exemplo de configuração:

```text
Client ID: algafood-web
Client Secret: web123
Scopes: READ WRITE
Callback: https://oauth.pstmn.io/v1/callback
```

O client exige consentimento explícito.

---

## Refresh Token

Exemplo:

```bash
curl -X POST 'http://localhost:8080/oauth2/token' \
  -u 'algafood-web:web123' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  --data-urlencode 'grant_type=refresh_token' \
  --data-urlencode 'refresh_token=SEU_REFRESH_TOKEN'
```

Os refresh tokens são rotacionados:

```text
reuseRefreshTokens(false)
```

---

## Client Credentials

Exemplo para o client `faturamento`:

```bash
curl -X POST 'http://localhost:8080/oauth2/token' \
  -u 'faturamento:faturamento123' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  --data-urlencode 'grant_type=client_credentials' \
  --data-urlencode 'scope=READ WRITE'
```

Esse client recebe authorities específicas:

```text
CONSULTAR_PEDIDOS
GERAR_RELATORIOS
```

---

## PKCE

O client:

```text
foodanalytics
```

é configurado como public client:

```text
ClientAuthenticationMethod.NONE
```

e exige:

```text
code_challenge_method=S256
```

Sem um `code_verifier` válido, o token não é emitido.

---

## Consentimentos OAuth2

A aplicação possui uma tela própria para gerenciar os clients autorizados:

```text
http://localhost:8080/oauth2/consents
```

Nela o usuário pode:

- visualizar aplicações autorizadas;
- visualizar scopes concedidos;
- revogar o acesso de um client.

Ao revogar, são removidos o consentimento e as autorizações persistidas para aquele usuário/client.

---

## Login

Página customizada:

```text
http://localhost:8080/login
```

Exemplo de usuário de desenvolvimento:

```text
E-mail: joao.ger@algafood.com.br
Senha: 123
```

> Essa credencial pertence exclusivamente ao ambiente de desenvolvimento.

---

## Swagger / OpenAPI

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

Também pode ser acessado em:

```text
http://localhost:8080/swagger-ui/index.html
```

A documentação está agrupada por versão:

```text
AlgaFood API v1
AlgaFood API v2
```

OpenAPI:

```text
/v3/api-docs/AlgaFood%20API%20v1
/v3/api-docs/AlgaFood%20API%20v2
```

O Swagger possui suporte ao fluxo OAuth2 Authorization Code por meio do client:

```text
swagger-ui
```

---

## API versionada

### V1

```text
/v1/**
```

Contém a API principal.

### V2

```text
/v2/**
```

Atualmente contém versões alternativas dos recursos de cidades e cozinhas.

O Root Entry Point da V1 é ocultado da documentação OpenAPI.

---

## Alguns endpoints

```text
GET    /v1/cozinhas
POST   /v1/cozinhas

GET    /v1/restaurantes
POST   /v1/restaurantes

GET    /v1/pedidos
POST   /v1/pedidos

GET    /v1/usuarios
POST   /v1/usuarios

GET    /v1/estatisticas/vendas-diarias
```

A lista completa está disponível no Swagger UI.

---

## Relatórios

O projeto utiliza JasperReports para geração de relatórios.

Exemplo:

```text
GET /v1/estatisticas/vendas-diarias
```

Pode produzir:

```text
application/json
application/pdf
```

Para PDF:

```http
Accept: application/pdf
```

---

## Fotos de produtos

A API suporta upload de fotos de produtos com validação de:

- tamanho;
- content type;
- multipart/form-data.

O armazenamento atual é local e configurável por:

```text
ALGAFOOD_STORAGE_LOCAL_DIRETORIO_FOTOS
```

---

## E-mails

O projeto possui implementações para:

- fake;
- sandbox;
- SMTP.

No profile `dev`:

```properties
algafood.email.impl=fake
```

Os templates ficam em:

```text
src/main/resources/templates/emails
```

---

## Tratamento de erros

A API possui tratamento centralizado de exceções e respostas padronizadas.

Exemplo conceitual:

```json
{
  "status": 400,
  "timestamp": "2026-09-13T12:00:00Z",
  "type": "https://algafood.com.br/dados-invalidos",
  "title": "Dados inválidos",
  "detail": "Um ou mais campos estão inválidos.",
  "userMessage": "Um ou mais campos estão inválidos."
}
```

O schema `Problema` também é documentado no OpenAPI.

---

## HATEOAS

Os recursos da API V1 utilizam Spring HATEOAS para exposição de links relacionados às operações disponíveis.

---

## Testes

O projeto possui testes de integração para recursos como cozinhas e restaurantes, incluindo cenários de autenticação e autorização.

Execute:

```bash
mvn clean verify
```

Resultado esperado:

```text
Failures: 0
Errors: 0
BUILD SUCCESS
```

---

## Estrutura do projeto

```text
src
├── main
│   ├── java/com/algaworks/algafood
│   │   ├── api
│   │   │   ├── v1
│   │   │   └── v2
│   │   ├── core
│   │   │   ├── security
│   │   │   ├── springdoc
│   │   │   ├── email
│   │   │   └── storage
│   │   ├── domain
│   │   │   ├── model
│   │   │   ├── repository
│   │   │   └── service
│   │   └── infrastructure
│   └── resources
│       ├── db
│       │   ├── migration
│       │   └── testdata
│       ├── keystores
│       ├── relatorios
│       └── templates
└── test
```

---

## Migração para Spring Boot 3

O projeto utiliza a stack Jakarta compatível com Spring Boot 3:

```text
jakarta.persistence
jakarta.validation
jakarta.servlet
```

Também utiliza:

- SpringDoc 2.x;
- Spring Security 6;
- Spring Authorization Server moderno;
- Java 17.

O uso de `javax.sql.DataSource` em infraestrutura/testes é esperado, pois `javax.sql` pertence ao Java SE e não faz parte da migração Jakarta EE.

---

## Referências

Projeto desenvolvido com base no curso **Especialista Spring REST**, da [AlgaWorks](https://algaworks.com/).

Repositório de referência do curso:

https://github.com/algaworks/curso-especialista-spring-rest

Documentações oficiais:

- Spring Boot: https://spring.io/projects/spring-boot
- Spring Security: https://spring.io/projects/spring-security
- Spring Authorization Server: https://spring.io/projects/spring-authorization-server
- SpringDoc OpenAPI: https://springdoc.org/
- Flyway: https://documentation.red-gate.com/flyway
- JasperReports: https://community.jaspersoft.com/

---

## Autor

**Elton Riva**

GitHub: https://github.com/EltonRiva1
