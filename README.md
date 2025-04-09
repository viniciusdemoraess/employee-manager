# Teste Prático - API REST com Autenticação, PostgreSQL e MinIO

## 👤 Dados do Candidato

**Nome completo:** Vinicius de Moraes Espirito Santos Oliveira
**Email:** viniciusdemoraespro@gmail.com  
**Cargo:** DESENVOLVEDOR JAVA (BACK-END)

---

## 🧾 Sumário

- [📋 Descrição do Projeto](#descrição-do-projeto)
- [📦 Tecnologias Utilizadas](#tecnologias-utilizadas)
- [🚀 Como Executar o Projeto](#como-executar-o-projeto)
- [🔐 Autenticação](#autenticação)
- [📁 Endpoints e Funcionalidades](#endpoints-e-funcionalidades)
- [🗃️ Banco de Dados](#banco-de-dados)
- [🧪 Como Testar](#como-testar)
- [📌 Observações Importantes](#observações-importantes)

---

## 📋 Descrição do Projeto

Este projeto consiste em uma API RESTful desenvolvida para gerenciar dados de servidores efetivos e temporários, unidades e lotações. Os dados são persistidos em um banco PostgreSQL e as fotos são armazenadas em um servidor S3 MinIO. A aplicação inclui autenticação com expiração, renovação de token, CORS configurado, paginação e upload de imagens.

---

## 📦 Tecnologias Utilizadas

- Java 21 + Spring Boot
- Spring Security + JWT
- PostgreSQL (última versão)
- MinIO (S3-compatible storage)
- Docker e Docker Compose
- JPA / Hibernate
- Swagger (OpenAPI)
- Lombok
- Nginx


---

## 🚀 Como Executar o Projeto

### 1. Clone o repositório:
```bash

git clone https://github.com/viniciusdemoraess/employee-manager.git
cd employee-manager

```

### 2. Execute os contêineres com Docker Compose:

```bash
docker-compose up --build
```

Esse comando irá iniciar:

- A aplicação Spring Boot.

- O banco de dados PostgreSQL.

- O servidor MinIO.

- O proxy reverso Nginx.

### 3. Acesse a documentação do Swagger:

```bash
http://localhost:8080/employee-manager/v0.0.1/swagger-ui/index.html
```

## 🔐 Autenticação

A aplicação utiliza JWT para autenticação.

- O token tem validade de 5 minutos

- Um endpoint de refresh permite renovação do token

- CORS configurado para aceitar apenas o domínio local (ex: http://localhost:8080)

### Endpoints:

```bash
POST /auth/login → retorna token JWT

POST /auth/refresh → renova o token
```

### Foi criado um usuário padrão necessário para login na aplicação, copie e cole o seguinte usuário no endpoint de `/auth/login`:

```bash
{
  "email": "admin@admin.com",
  "password": "123456"
}

```

## 📁 Endpoints e Funcionalidades

CRUDs completos com POST, PUT, GET, DELETE para:

- Servidor Efetivo

- Servidor Temporário

- Unidade

- Lotação

### Endpoints Específicos:

#### GET /servidores-efetivos/unidade/{unidId}
→ Lista servidores efetivos lotados em uma unidade específica com:
Nome, idade, unidade de lotação e link da fotografia

#### GET /enderecos-funcionais?nome={parteNome}
→ Retorna endereço funcional com base em parte do nome do servidor efetivo

### Upload de Fotografia:

#### POST /servidores-efetivos

→ Cria um servidor efetivo.
→ Realiza o upload de uma ou mais fotografias para o MinIO
→ Link temporário de 5 minutos para visualização das imagens

## 🗃️ Banco de Dados
O banco de dados PostgreSQL é iniciado em container Docker. As entidades estão de acordo com o diagrama fornecido e o schema é gerado automaticamente via JPA.

## 🧪 Como Testar

Faça login no sistema via /auth/login

Use o token JWT nas requisições protegidas (no Swagger ou via Postman)

Teste os endpoints disponíveis

Faça upload de imagens e veja os links temporários gerados

Use a paginação via query params:
Ex: GET /servidores?page=0&size=10

## 📌 Observações Importantes

- A autenticação expira em 5 minutos e exige renovação

- O acesso aos endpoints está limitado por CORS ao domínio do frontend

- O sistema utiliza Docker Compose para orquestrar todos os serviços

- Todas as dependências e scripts estão incluídos no projeto

🐳 Docker Compose

Exemplo de serviços definidos:

```bash
version: '3.8'

services:
  minio:
    image: minio/minio:latest
    container_name: minio
    environment:
      MINIO_ROOT_USER: minioaccesskey
      MINIO_ROOT_PASSWORD: miniosecretkey
      MINIO_BUCKETS: "servidores"
      MINIO_ADDRESS: ":9000"
      MINIO_CONSOLE_ADDRESS: ":9001"
    ports:
      - "9000:9000"
      - "9001:9001"
    command: server --address 0.0.0.0:9000 /data
    volumes:
      - minio_data:/data

  nginx-proxy:
    image: nginx:alpine
    container_name: nginx-proxy
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro
    ports:
      - "9002:9002"
    depends_on:
      - minio

  postgres:
    image: postgres:latest
    container_name: postgres
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgrespassword
      POSTGRES_DB: servidoresdb
    ports:
      - "5432:5432"

  app:
    build: .
    container_name: employee_manager_app
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/servidoresdb
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgrespassword
      MINIO_URL: http://minio:9000
      MINIO_ACCESS_KEY: minioaccesskey
      MINIO_SECRET_KEY: miniosecretkey
      DB_HOST: postgres
      DB_PORT: 5432
    depends_on:
      - postgres
      - minio


volumes:
  minio_data:
    driver: local

```