# Wishlist API

Esta é uma API REST para gerenciamento de listas de desejos (**Wishlist**) de e-commerce. Foi desenvolvida com **Java 17** e **Spring Boot 3**, utilizando um banco de dados não relacional (**MongoDB**). A API segue princípios de **Clean Architecture** e **Microservices**, proporcionando escalabilidade, manutenibilidade e fácil integração com outras aplicações.

## Funcionalidades

- Adicionar produtos à wishlist do cliente
- Remover produtos da wishlist
- Consultar todos os produtos na wishlist
- Verificar se um produto específico está na wishlist
- Limitar a quantidade de produtos na wishlist (máximo de 20)
- Segurança configurada com autenticação básica (Basic Auth)

**Usuário e senha para testar a API:** `admin` / `admin`

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3**
- **MongoDB** como banco de dados NoSQL
- **JUnit 5** para cobertura completa de testes
- **Swagger** para documentação da API
- **Gradle** como gerenciador de dependências
- **Docker** para facilitar o deploy em containers

## Requisitos

Para rodar a API localmente, você precisará ter:

- **Java 17**
- **Docker** (opcional, mas recomendado)
- **MongoDB** instalado ou em execução via container Docker

## Como Executar

### 1. Rodando Localmente

Se você tem o **MongoDB** instalado localmente, pode iniciar o projeto com os seguintes comandos:

1. Clone o repositório:

    ```bash
    git clone https://github.com/Arionildo/wishlist-api.git
    cd wishlist-api
    ```

2. Configure o MongoDB no arquivo `application.properties`.

3. Execute a aplicação com o Gradle:

    ```bash
    ./gradlew bootRun
    ```

4. A API estará disponível em: [http://localhost:8080/api/v1/wishlist](http://localhost:8080/api/v1/wishlist).

### 2. Rodando com Docker

Caso prefira usar Docker para executar o MongoDB e a aplicação, siga os passos abaixo:

1. Primeiro, certifique-se de ter o Docker instalado.

2. No diretório raiz do projeto, execute o comando para criar e iniciar os containers:

    ```bash
    docker-compose up --build
    ```

3. A aplicação estará rodando em [http://localhost:8080](http://localhost:8080) e o MongoDB no container.

## Documentação da API

A documentação da API pode ser acessada diretamente no Swagger após a execução, em:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Endpoints Principais

- **POST** [`/api/v1/wishlist/{customerId}/add-product/{productId}`](http://localhost:8080/api/v1/wishlist/{customerId}/add-product/{productId}) - Adiciona um produto à wishlist do cliente
- **DELETE** [`/api/v1/wishlist/{customerId}/remove-product/{productId}`](http://localhost:8080/api/v1/wishlist/{customerId}/remove-product/{productId}) - Remove um produto da wishlist
- **GET** [`/api/v1/wishlist/{customerId}`](http://localhost:8080/api/v1/wishlist/{customerId}) - Retorna todos os produtos da wishlist do cliente
- **GET** [`/api/v1/wishlist/{customerId}/has-product/{productId}`](http://localhost:8080/api/v1/wishlist/{customerId}/has-product/{productId}) - Verifica se um produto está na wishlist do cliente
- **DELETE** [`/api/v1/wishlist/{customerId}`](http://localhost:8080/api/v1/wishlist/{customerId}) - Deleta a wishlist do cliente

## Testes

A aplicação possui cobertura completa de testes de unidade e integração, utilizando o JUnit 5. Para rodar os testes:

```bash
./gradlew test
