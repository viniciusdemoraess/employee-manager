# Teste Prático - API REST com Autenticação, PostgreSQL e MinIO

## 👤 Dados do Candidato

**Nome completo:** Vinicius de Moraes Espirito Santos Oliveira

**Email:** viniciusdemoraespro@gmail.com  

**Cargo:** DESENVOLVEDOR JAVA (BACK-END)

**Inscrição:** 9709

---

## 🧾 Sumário

- [📋 Descrição do Projeto](#-descrição-do-projeto)
- [📦 Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [🚀 Como Executar o Projeto](#-como-executar-o-projeto)
- [🔐 Autenticação](#-autenticação)
- [📁 Endpoints e Funcionalidades](#-endpoints-e-funcionalidades-requisitos-específicos)
- [🗃️ Banco de Dados](#banco-de-dados)
- [🧪 Como Testar](#-como-testar)
- [📌 Observações Importantes](#-observações-importantes-sobre-os-pré-requisitos)

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

**Obs:** Docker e docker-compose devem estar instalados previamente.

### 1. Clone o repositório:
```bash

git clone https://github.com/viniciusdemoraess/employee-manager.git
cd employee-manager

```

### 2. Execute os contêineres com Docker Compose:

```bash
docker-compose up --build
```

**Para Docker mais recente:**

```bash
docker compose up --build
```

Esse comando irá iniciar:

- O banco de dados PostgreSQL.

- O servidor MinIO.

- O proxy reverso Nginx.

- A aplicação Spring Boot.

### 3. Acesse a documentação do Swagger:

```bash
http://localhost:8080/employee-manager/v0.0.1/swagger-ui/index.html
```

**Obs: Caso você utilize outra ferramenta para fazer as requisições como o POSTMAN, o endereço base fica: `http://localhost:8080/employee-manager/v0.0.1/`**

**Exemplo:**

https://github.com/user-attachments/assets/cc5922a3-7f47-4183-a74b-60ab35b23aca


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

## 📁 Endpoints e Funcionalidades Requisitos Específicos

- Servidor Efetivo

- Servidor Temporário

- Unidade

- Lotação

#### Ambos os endpoints de CRUD estão bem mapeados no Swagger: POST, PUT, GET, DELETE.
#### Swagger Endpoit: http://localhost:8080/employee-manager/v0.0.1/swagger-ui/index.html


### Requisitos Específicos:

#### Atendendo aos Requisitos Específicos do ANEXO III: GET /servidores-efetivos/unidade/{unidId}

→ Lista servidores efetivos lotados em uma unidade específica com:
Nome, idade, unidade de lotação e link da fotografia

#### Atendendo aos Requisitos Específicos do ANEXO III:  GET /enderecos-funcionais?nome={parteNome}

→ Retorna endereço funcional com base em parte do nome do servidor efetivo

### Atendendo ao item - Upload de Fotografia:

#### Atendendo aos Requisitos Específicos do ANEXO III:  POST /servidores-efetivos e POST /servidores-temporarios

→ Ao criar um servidor Efetivo ou Temporário, existe a possibilidade de Envio de uma ou mais Fotos do servidor.

→ O upload de uma ou mais fotografias é feito para o MinIO.

→ Link temporário de 5 minutos para visualização das imagens.

![image](https://github.com/user-attachments/assets/39d21c3b-a452-42f2-9caa-a09958fa2ee8)

**O botão onde a seta azul indica é onde você pode adicionar a opção de enviar mais de uma foto.**



## Banco de Dados

O banco de dados PostgreSQL é iniciado em container Docker. As entidades estão de acordo com o diagrama fornecido e o schema é gerado automaticamente via JPA.

![image](https://github.com/user-attachments/assets/b9bc54e0-52f4-485e-bceb-51beae750e4e)


## 🧪 Como Testar

Faça login no sistema via /auth/login e adicione o email e senha:
```bash
{
  "email": "admin@admin.com",
  "password": "123456"
}

``` 

Use o token JWT nas requisições protegidas (no Swagger ou via Postman)

Teste os endpoints disponíveis

Faça upload de imagens e veja os links temporários gerados

Use a paginação via query params:
Ex: GET /servidores?page=0&size=10

Exemplo: 

**Login:**
![image](https://github.com/user-attachments/assets/9f7ba938-9b2b-48e7-ae39-ee8ae888566a)

Exemplo de Response:
```bash
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzIiwic3ViIjoiYWRtaW5AYWRtaW4uY29tIiwiaWF0IjoxNzQ0MjAzNTU3LCJleHAiOjE3NDQyMDM4NTd9.7_tD8lspN9NKC8u_RNTGq9DaSZv05P3znqDD6YooMAk",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoicmVmcmVzaCIsInN1YiI6ImFkbWluQGFkbWluLmNvbSIsImlhdCI6MTc0NDIwMzU1NywiZXhwIjoxNzQ0Mjg5OTU3fQ.7WYG9EWCEqW3KkdeE4rbyDpZKU2R45FAvkJsWVz0DAk",
  "expiresIn": 300000
}
```
**Clique aqui:**

![image](https://github.com/user-attachments/assets/683400b2-a944-4805-8a9c-505e8f3de639)

**E adicione o token para liberar as requisições.**

![image](https://github.com/user-attachments/assets/c98e563f-a6bf-4206-a79f-f73714b66607)

**Caso o token expire, voce pode usar o refreshToken para gerar um novo token, porém antes faça o logout:**

![image](https://github.com/user-attachments/assets/683400b2-a944-4805-8a9c-505e8f3de639)

![image](https://github.com/user-attachments/assets/8511eb28-c44d-46e1-9cc4-22ceaaf19271)

![image](https://github.com/user-attachments/assets/9b310c3c-ff20-441e-8ee7-595c39a2d8b0)


**Criação de Unidades:**

![image](https://github.com/user-attachments/assets/3dd751a0-a431-4452-bcd1-ef11a0f2a292)


Exemplo Request:
```bash
{
  "nome": "PERICIA OFICIAL E IDENTIFICACAO TECNICA",
  "sigla": "POLITEC",
  "tipoLogradouro": "casa",
  "logradouro": "Rua Tal Quadra Tal",
  "numero": 23,
  "bairro": "Carumbé",
  "cidade": {
    "nome": "Cuiabá",
    "uf": "MT"
  }
}
```

**Criação Servidor Efetivo:**

https://github.com/user-attachments/assets/e99bee89-bd8e-4e71-8b23-7de95a9035b4

**Listar Servidor Efetivo Por Unidade:**

https://github.com/user-attachments/assets/1c6eb9e4-cb19-4e82-af7e-d637e4178cb4

**Listar Endereco Funcional de Servidor Efetivo:**

https://github.com/user-attachments/assets/c67f93a4-f623-4456-8400-89653c9918b2

**Criar Lotação:**


https://github.com/user-attachments/assets/311bfd80-a9ac-4ed7-b003-47c700bb4e74



**Caso o servidor já esteja lotado na unidade ele nao permite a criação duplicada da lotação.**





## 📌 Observações Importantes sobre os Pré-Requisitos.

- Atendendo ao item 'B' - Pré-Requisitos do Anexo III Foi utilizado Java 21.

- Atendendo ao item 'B' - Pré-Requisitos do Anexo III- Foi executado em container o servidor Min.io.

- Atendendo ao item 'C' - Pré-Requisitos do Anexo III - Foi executado em container o banco de dados PostgreSQL

## 📌 Observações Importantes sobre os Requisitos Gerais.

- Atendendo ao item 'A' - Requisitos Gerais do Anexo III: Foi implementado um mecanismo de autenticação com JWT.

- Atendendo ao item 'B' - Requisitos Gerais do Anexo III: A autenticação expira em 5 minutos e possibilita renovação.

- Atendendo ao item 'C' - Requisitos Gerais do Anexo III: Foram implementados os verbos post, put, get e o delete também.

- Atendendo ao item 'D' - Requisitos Gerais do Anexo III: Todas as consultas contém recursos de paginação.

- Atendendo ao item 'E' - Requisitos Gerais do Anexo III: Os dados produzidos são armazenados no servidor de banco de dados (Postgresql) que foi criado em container.

- Atendendo ao item 'F' - Requisitos Gerais do Anexo III: A solução final foi orquestrada em container, sendo necessário apenas rodar o docker compose para ter acesso a aplicação.

## 📌 Observações Importantes sobre os Requisitos Específicos.

- Atendendo aos Requisitos Específicos do Anexo III: Foi implementado uma API Rest para o diagrama de banco de dados do anexo.

- Atendendo aos Requisitos Específicos do Anexo III: Foi criado um CRUD para Servidor Efetivo, Servidor Temporário, Unidade e Lotação, onde foi contemplado a inclusão e edição dos dados das tabelas relacionadas.

- Atendendo aos Requisitos Específicos do Anexo III: Foi Criado um endpoint que permita consultar os servidores efetivos lotados em determinada unidade parametrizando a consulta pelo atributo unid_id;
Retornar os seguintes campos: Nome, idade, unidade de lotação e fotografia;

- Atendendo aos Requisitos Específicos do Anexo III: Foi Criado um endpoint que permita consultar o endereço funcional (da unidade onde o servidor é lotado) a partir de uma parte do nome do servidor efetivo.

- Atendendo aos Requisitos Específicos do Anexo III: Realizar o upload de uma ou mais fotografias enviando-as para o Min.IO; A recuperação das imagens deverá ser através de links temporários gerados pela biblioteca do Min.IO com tempo de expiração de 5 minutos

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
