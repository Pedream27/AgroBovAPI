# AgroBovAPI

[![Status da Construção](https://img.shields.io/badge/Status-Working-brightgreen)](https://github.com/seu-usuario/seu-repositorio/actions)
[![Licença MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Versão Java](https://img.shields.io/badge/Java-17%2B-orange)](https://www.oracle.com/java/technologies/downloads/)
[![Versão Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-green)](https://spring.io/projects/spring-boot)

Esta API RESTful permite o gerenciamento de registros de vacas e suas produções diárias de leite, incluindo a análise de tendências de produção, com suporte a HATEOAS para facilitar a navegação e o consumo por clientes frontend.

## Sumário

* [Visão Geral](#visão-geral)
* [Tecnologias Utilizadas](#tecnologias-utilizadas)
* [Pré-requisitos](#pré-requisitos)
* [Como Rodar a Aplicação](#como-rodar-a-aplicação)
    * [1. Usando Docker Compose (Recomendado)](#1-usando-docker-compose-recomendado)
    * [2. Rodando Diretamente com Maven](#2-rodando-diretamente-com-maven)
* [Conceitos Importantes para o Frontend](#conceitos-importantes-para-o-frontend)
    * [HATEOAS](#hateoas)
    * [Estrutura de Links](#estrutura-de-links)
    * [Códigos de Status HTTP](#códigos-de-status-http)
    * [Tratamento de Erros](#tratamento-de-erros)
    * [CORS (Cross-Origin Resource Sharing)](#cors-cross-origin-resource-sharing)
    * [Formato de Datas](#formato-de-datas)
* [Documentação da API](#documentação-da-api)
    * [Recurso: Vacas](#recurso-vacas)
        * [`GET /api/vacas`](#get-apivacas)
        * [`GET /api/vacas/{id}`](#get-apivacasid)
        * [`POST /api/vacas`](#post-apivacas)
        * [`PUT /api/vacas/{id}`](#put-apivacasid)
        * [`DELETE /api/vacas/{id}`](#delete-apivacasid)
    * [Recurso: Produção de Leite](#recurso-produção-de-leite)
        * [`POST /api/producao-leite/vaca/{vacaId}`](#post-apiproducao-leitevacaid)
        * [`GET /api/producao-leite/vaca/{vacaId}`](#get-apiproducao-leitevacaid)
        * [`GET /api/producao-leite/vaca/{vacaId}/analise-15-dias`](#get-apiproducao-leitevacaidanalise-15-dias)
* [Contribuição](#contribuição)
* [Licença](#licença)

---

## Visão Geral

Este projeto é uma API RESTful desenvolvida em **Spring Boot** para gerenciar informações sobre vacas e suas produções de leite. Ele permite registrar novas vacas, suas produções diárias e oferece uma análise da tendência de produção nos últimos 15 dias. A API utiliza **HATEOAS** para fornecer links contextuais nas respostas, tornando-a mais navegável e robusta para clientes frontend.

## Tecnologias Utilizadas

* **Java 17+**
* **Spring Boot 3.x**
* **Spring Data JPA**
* **PostgreSQL** (como banco de dados)
* **Spring HATEOAS** (para links de hypermedia)
* **Lombok** (para redução de boilerplate)
* **Maven** (para gerenciamento de dependências e build)
* **Docker / Docker Compose** (para ambiente de desenvolvimento e deploy)

## Pré-requisitos

Para rodar a aplicação localmente, você precisará de:

* [JDK 17 ou superior](https://www.oracle.com/java/technologies/downloads/)
* [Apache Maven](https://maven.apache.org/download.cgi)
* [Docker e Docker Compose](https://docs.docker.com/get-docker/) (recomendado)
* Um cliente REST para testar as APIs (ex: [Postman](https://www.postman.com/downloads/), [Insomnia](https://insomnia.rest/download/), `curl`)

## Como Rodar a Aplicação

### 1. Usando Docker Compose (Recomendado)

Esta é a maneira mais fácil e recomendada para iniciar a aplicação e o banco de dados, pois gerencia todas as dependências do ambiente.

1. **Clone o repositório** (se ainda não o fez):

    ```bash
    git clone https://github.com/seu-usuario/seu-repositorio.git
    cd seu-repositorio
    ```
    *(Substitua `seu-usuario/seu-repositorio` pelo caminho real do seu projeto.)*

2. **Verifique e Configure as Credenciais do Banco de Dados:**

    Abra o arquivo `docker-compose.yml` na raiz do seu projeto. Localize o serviço `db` e altere as variáveis de ambiente `POSTGRES_USER` e `POSTGRES_PASSWORD` para suas credenciais seguras.

    ```yaml
    services:
      db:
        environment:
          POSTGRES_DB: vacas_db
          POSTGRES_USER: seu_usuario_postgres # <--- Mude este!
          POSTGRES_PASSWORD: sua_senha_postgres # <--- Mude este!
    ```

3. **Inicie os Serviços:**

    Na raiz do projeto (onde está o `docker-compose.yml`), execute o seguinte comando no seu terminal:

    ```bash
    docker-compose up --build -d
    ```

    * `up`: Inicia os serviços definidos no `docker-compose.yml`.
    * `--build`: Garante que a imagem da sua aplicação Spring Boot seja construída (ou reconstruída) a partir do `Dockerfile`.
    * `-d`: Inicia os contêineres em segundo plano (detached mode), liberando o terminal.

4. **Verifique o Status dos Contêineres:**

    Você pode verificar se os contêineres estão rodando com:

    ```bash
    docker ps
    ```

    Você deverá ver os contêineres `vacas_db_container` e `vacas_app_container` listados como `Up`.

5. **Acesse a API:**

    A API estará disponível em `http://localhost:8080/api`.

6. **Visualizar Logs (Opcional):**

    Para ver os logs do contêiner da aplicação em tempo real:

    ```bash
    docker-compose logs -f app
    ```

    Para ver os logs do contêiner do banco de dados em tempo real:

    ```bash
    docker-compose logs -f db
    ```

7. **Parar e Remover os Serviços:**

    Para parar e remover os contêineres e redes criadas pelo Docker Compose:

    ```bash
    docker-compose down
    ```

    Para parar, remover e também **apagar os dados persistidos do banco de dados** (use com cautela!):

    ```bash
    docker-compose down -v
    ```

### 2. Rodando Diretamente com Maven

Se você preferir rodar a aplicação diretamente em sua máquina (sem Docker), certifique-se de ter um servidor PostgreSQL rodando e configurado localmente.

1. **Configure o Banco de Dados Localmente:**
    * Crie um banco de dados PostgreSQL chamado `vacas_db`.
    * Certifique-se de que o usuário e a senha que você usará para conectar a este banco de dados tenham as permissões necessárias.

2. **Atualize o Arquivo de Propriedades da Aplicação:**

    Abra o arquivo `src/main/resources/application.properties` na raiz do seu projeto e atualize as configurações do banco de dados para apontar para sua instância PostgreSQL local:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/vacas_db
    spring.datasource.username=seu_usuario_postgres # <--- Mude este para seu usuário local
    spring.datasource.password=sua_senha_postgres # <--- Mude este para sua senha local
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.jpa.hibernate.ddl-auto=update # Ou 'create-drop' para testes, 'none' para produção
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    server.port=8080
    ```

3. **Execute a Aplicação:**

    Na raiz do seu projeto (onde está o `pom.xml`), execute os seguintes comandos no seu terminal:

    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

    A aplicação iniciará na porta `8080`.

## Conceitos Importantes para o Frontend

### HATEOAS

HATEOAS (Hypermedia as an Engine of Application State) é um princípio fundamental que significa que as respostas da API incluem **links** que indicam as ações ou recursos relacionados que o cliente pode acessar em seguida. Isso permite que o frontend "descubra" as funcionalidades da API dinamicamente, sem URLs hardcoded, tornando a integração mais flexível e resiliente a mudanças na estrutura da API.

### Estrutura de Links

Os links HATEOAS são incluídos nas respostas JSON sob a chave `_links`. Cada link possui um `rel` (nome do relacionamento) e um `href` (a URL do recurso).

**Exemplo de resposta com links:**

```json
{
    "id": 1,
    "nome": "Mimosa",
    "raca": "Nelore",
    "dataNascimento": "2020-01-15",
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/vacas/1"
        },
        "delete_vaca": {
            "href": "http://localhost:8080/api/vacas/1"
        },
        "producoes_leite": {
            "href": "http://localhost:8080/api/producao-leite/vaca/1"
        },
        "analise_producao_15_dias": {
            "href": "http://localhost:8080/api/producao-leite/vaca/1/analise-15-dias"
        }
    }
}
```

O frontend deve:

- Parsear a resposta JSON.
- Acessar a propriedade `_links`.
- Localizar o link desejado pelo seu `rel` (ex: `delete_vaca`, `producoes_leite`).
- Usar o `href` associado para fazer a próxima requisição.

### Códigos de Status HTTP

A API utiliza códigos de status HTTP padrão para indicar o resultado das operações:

**2xx (Sucesso):**
- `200 OK`: Requisição processada com sucesso.
- `201 Created`: Novo recurso criado com sucesso (resposta a um POST).
- `204 No Content`: Requisição processada com sucesso, mas sem conteúdo para retornar (ex: exclusão).

**4xx (Erro do Cliente):**
- `400 Bad Request`: Requisição malformada, validação falhou, dados inválidos.
- `404 Not Found`: Recurso não encontrado no servidor.
- `409 Conflict`: Conflito com o estado atual do recurso (ex: tentar criar uma vaca com nome já existente).

**5xx (Erro do Servidor):**
- `500 Internal Server Error`: Um erro inesperado ocorreu no servidor.

É crucial que o frontend implemente o tratamento desses códigos para exibir mensagens de erro adequadas ao usuário e gerenciar o fluxo da aplicação.

### Tratamento de Erros

Em caso de erros (`4xx` ou `5xx`), a API pode retornar um corpo de resposta JSON com detalhes do erro (embora o código atual retorne apenas o status e null em alguns casos, idealmente ele retornaria um objeto de erro padronizado). O frontend deve monitorar esses códigos de status HTTP e exibir mensagens de feedback apropriadas ao usuário com base na resposta recebida.

### CORS (Cross-Origin Resource Sharing)

Se o seu frontend estiver sendo executado em um domínio ou porta diferente da API (por exemplo, frontend em http://localhost:3000 e API em http://localhost:8080), você pode encontrar erros de CORS (Cross-Origin Resource Sharing). A API está configurada com `@CrossOrigin(origins = "http://localhost:3000")` nos controladores para permitir requisições de origens comuns de desenvolvimento. Se o seu frontend estiver em outra origem, será necessário ajustar a configuração no código da API (nas anotações `@CrossOrigin` nos controladores, adicionando sua origem, ou configurando globalmente).

### Formato de Datas

Todas as datas utilizadas na API (tanto nas requisições de entrada quanto nas respostas de saída) devem estar no formato `YYYY-MM-DD` (ex: 2025-06-05).

## Documentação da API

**Base URL para todos os endpoints:** `http://localhost:8080/api`

---

### Recurso: Vacas

Este recurso gerencia o registro e as informações básicas das vacas.

#### `GET /api/vacas`

- **Descrição:** Lista todas as vacas registradas no sistema.
- **Método:** GET
- **Parâmetros:** Nenhum

**Resposta (200 OK - Exemplo):**

```json
[
    {
        "id": 1,
        "nome": "Mimosa",
        "raca": "Nelore",
        "dataNascimento": "2020-01-15",
        "producoesLeite": null,
        "_links": {
            "self": { "href": "http://localhost:8080/api/vacas/1" },
            "delete_vaca": { "href": "http://localhost:8080/api/vacas/1" },
            "update_vaca": { "href": "http://localhost:8080/api/vacas/1" },
            "producoes_leite": { "href": "http://localhost:8080/api/producao-leite/vaca/1" },
            "analise_producao_15_dias": { "href": "http://localhost:8080/api/producao-leite/vaca/1/analise-15-dias" }
        }
    },
    {
        "id": 2,
        "nome": "Estrela",
        "raca": "Jersey",
        "dataNascimento": "2021-03-20",
        "producoesLeite": null,
        "_links": { /* ... links para a vaca Estrela ... */ }
    }
]
```

**Resposta (204 No Content):** Retornada se não houver vacas registradas.

---

#### `GET /api/vacas/{id}`

- **Descrição:** Busca os detalhes de uma vaca específica pelo seu ID.
- **Método:** GET
- **Parâmetros de Path:**
    - `id` (long, obrigatório): O ID único da vaca.

**Resposta (200 OK - Exemplo):**

```json
{
    "id": 1,
    "nome": "Mimosa",
    "raca": "Nelore",
    "dataNascimento": "2020-01-15",
    "producoesLeite": null,
    "_links": {
        "self": { "href": "http://localhost:8080/api/vacas/1" },
        "delete_vaca": { "href": "http://localhost:8080/api/vacas/1" },
        "update_vaca": { "href": "http://localhost:8080/api/vacas/1" },
        "all_vacas": { "href": "http://localhost:8080/api/vacas" },
        "producoes_leite": { "href": "http://localhost:8080/api/producao-leite/vaca/1" },
        "analise_producao_15_dias": { "href": "http://localhost:8080/api/producao-leite/vaca/1/analise-15-dias" },
        "registrar_producao": { "href": "http://localhost:8080/api/producao-leite/vaca/1" }
    }
}
```

**Resposta (404 Not Found):** Vaca com o ID especificado não foi encontrada.

---

#### `POST /api/vacas`

- **Descrição:** Cria um novo registro de vaca no sistema.
- **Método:** POST
- **Headers:** `Content-Type: application/json`
- **Corpo da Requisição (JSON - Exemplo):**

    ```json
    {
        "nome": "Pérola",
        "raca": "Holandesa",
        "dataNascimento": "2022-04-01"
    }
    ```

    - `nome` (string, obrigatório, deve ser único)
    - `raca` (string, obrigatório)
    - `dataNascimento` (string, YYYY-MM-DD, opcional)

**Resposta (201 Created):** Objeto Vaca da vaca recém-criada, incluindo seu id e links HATEOAS para self, all_vacas e registrar_producao.

**Resposta (400 Bad Request):** Nome duplicado ou dados de entrada inválidos.

---

#### `PUT /api/vacas/{id}`

- **Descrição:** Atualiza as informações de uma vaca existente.
- **Método:** PUT
- **Parâmetros de Path:**
    - `id` (long, obrigatório): O ID único da vaca a ser atualizada.
- **Headers:** `Content-Type: application/json`
- **Corpo da Requisição (JSON - Exemplo):**

    ```json
    {
        "nome": "Pérola do Leite",
        "raca": "Holandesa",
        "dataNascimento": "2022-04-01"
    }
    ```

    Envie apenas os campos que deseja atualizar.

**Resposta (200 OK):** Objeto Vaca da vaca atualizada, com links HATEOAS.

**Resposta (404 Not Found):** Vaca com o ID especificado não foi encontrada.

**Resposta (400 Bad Request):** Dados de entrada inválidos.

---

#### `DELETE /api/vacas/{id}`

- **Descrição:** Remove um registro de vaca do sistema.
- **Método:** DELETE
- **Parâmetros de Path:**
    - `id` (long, obrigatório): O ID único da vaca a ser deletada.

**Resposta (204 No Content):** Vaca deletada com sucesso.

**Resposta (404 Not Found):** Vaca com o ID especificado não foi encontrada.

---

### Recurso: Produção de Leite

Este recurso gerencia o registro e a análise da produção de leite das vacas.

#### `POST /api/producao-leite/vaca/{vacaId}`

- **Descrição:** Registra a quantidade de leite produzida por uma vaca em uma data específica.
- **Método:** POST
- **Parâmetros de Path:**
    - `vacaId` (long, obrigatório): O ID da vaca à qual a produção de leite pertence.
- **Headers:** `Content-Type: application/json`
- **Corpo da Requisição (JSON - Exemplo):**

    ```json
    {
        "dataRegistro": "2025-06-05",
        "quantidadeLitros": 28.5
    }
    ```

    - `dataRegistro` (string, YYYY-MM-DD, obrigatório)
    - `quantidadeLitros` (double, obrigatório)

**Resposta (201 Created):** Objeto ProducaoLeite da produção registrada, com links HATEOAS para a vaca associada e análises.

**Resposta (404 Not Found):** Vaca com o vacaId especificado não foi encontrada.

**Resposta (400 Bad Request):** Dados de entrada inválidos.

---

#### `GET /api/producao-leite/vaca/{vacaId}`

- **Descrição:** Retorna todas as produções de leite registradas para uma vaca específica, ordenadas por data de registro.
- **Método:** GET
- **Parâmetros de Path:**
    - `vacaId` (long, obrigatório): O ID da vaca.

**Resposta (200 OK - Exemplo):**

```json
[
    {
        "id": 101,
        "vaca": { "id": 1, "nome": "Mimosa", "raca": "Nelore", "dataNascimento": "2020-01-15", "producoesLeite": null },
        "dataRegistro": "2025-06-03",
        "quantidadeLitros": 27.0,
        "_links": {
            "self": { "href": "http://localhost:8080/api/producao-leite/vaca/1" },
            "vaca": { "href": "http://localhost:8080/api/vacas/1" }
        }
    },
    {
        "id": 102,
        "vaca": { "id": 1, "nome": "Mimosa", "raca": "Nelore", "dataNascimento": "2020-01-15", "producoesLeite": null },
        "dataRegistro": "2025-06-04",
        "quantidadeLitros": 28.5,
        "_links": {
            "self": { "href": "http://localhost:8080/api/producao-leite/vaca/1" },
            "vaca": { "href": "http://localhost:8080/api/vacas/1" }
        }
    }
]
```

**Resposta (204 No Content):** Não há produções de leite registradas para esta vaca.

---

#### `GET /api/producao-leite/vaca/{vacaId}/analise-15-dias`

- **Descrição:** Calcula e retorna a produção de leite diária para uma vaca nos últimos 15 dias, indicando se a quantidade aumentou, diminuiu ou permaneceu estável em relação ao dia anterior.
- **Método:** GET
- **Parâmetros de Path:**
    - `vacaId` (long, obrigatório): O ID da vaca.

**Resposta (200 OK - Exemplo):**

```json
[
    {
        "dataRegistro": "2025-05-22",
        "quantidadeLitros": 20.0,
        "variacaoLitros": null,
        "tendencia": "Primeiro registro no período"
    },
    {
        "dataRegistro": "2025-05-23",
        "quantidadeLitros": 22.0,
        "variacaoLitros": 2.0,
        "tendencia": "Aumentou"
    },
    {
        "dataRegistro": "2025-05-24",
        "quantidadeLitros": 21.0,
        "variacaoLitros": -1.0,
        "tendencia": "Diminuiu"
    },
    {
        "dataRegistro": "2025-05-25",
        "quantidadeLitros": 21.0,
        "variacaoLitros": 0.0,
        "tendencia": "Estável"
    }
    // ... mais entradas para os 15 dias
]
```

- `dataRegistro` (string, YYYY-MM-DD): Data do registro.
- `quantidadeLitros` (double): Quantidade de leite produzida naquele dia.
- `variacaoLitros` (double ou null): Diferença na quantidade de litros em relação ao dia anterior. null para o primeiro registro no período.
- `tendencia` (string): Indica se a produção "Aumentou", "Diminuiu", "Estável" ou "Primeiro registro no período".

**Resposta (204 No Content):** Não há produções de leite registradas para esta vaca nos últimos 15 dias.

---

## Contribuição

Sinta-se à vontade para abrir issues ou pull requests! Leia as diretrizes de contribuição antes de começar.

## Licença

Este projeto está licenciado sob a [Licença MIT](LICENSE).
