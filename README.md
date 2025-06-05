
AgroBovAPI


Esta API RESTful permite o gerenciamento de registros de vacas e suas produções diárias de leite, incluindo a análise de tendências de produção, com suporte a HATEOAS para facilitar a navegação.

Sumário
Visão Geral
Tecnologias Utilizadas
Pré-requisitos
Como Rodar a Aplicação
Usando Docker Compose (Recomendado)
Rodando Diretamente com Maven
Conceitos Importantes para o Frontend
HATEOAS
Estrutura de Links
Códigos de Status HTTP
Tratamento de Erros
CORS (Cross-Origin Resource Sharing)
Formato de Datas
Documentação da API
1. Recurso: Vacas
GET /api/vacas - Listar Todas as Vacas
GET /api/vacas/{id} - Buscar Vaca por ID
POST /api/vacas - Criar uma Nova Vaca
PUT /api/vacas/{id} - Atualizar Vaca
DELETE /api/vacas/{id} - Deletar Vaca
2. Recurso: Produção de Leite
POST /api/producao-leite/vaca/{vacaId} - Registrar Produção de Leite
GET /api/producao-leite/vaca/{vacaId} - Listar Produções de Leite por Vaca
GET /api/producao-leite/vaca/{vacaId}/analise-15-dias - Analisar Produção de Leite nos Últimos 15 Dias
Contribuição
Licença
Visão Geral
Este projeto é uma API RESTful desenvolvida em Spring Boot para gerenciar informações sobre vacas e suas produções de leite. Ele permite registrar novas vacas, suas produções diárias e oferece uma análise da tendência de produção nos últimos 15 dias. A API utiliza HATEOAS para fornecer links contextuais nas respostas, tornando-a mais navegável e robusta para clientes frontend.

Tecnologias Utilizadas
Java 17+
Spring Boot 3.x
Spring Data JPA
PostgreSQL (como banco de dados)
Spring HATEOAS (para links de hypermedia)
Lombok (para redução de boilerplate)
Maven (para gerenciamento de dependências e build)
Docker / Docker Compose (para ambiente de desenvolvimento e deploy)
Pré-requisitos
Para rodar a aplicação localmente, você precisará de:

JDK 17 ou superior
Apache Maven
Docker e Docker Compose (recomendado)
Um cliente REST para testar as APIs (ex: Postman, Insomnia, curl)
Como Rodar a Aplicação
Usando Docker Compose (Recomendado)
Esta é a maneira mais fácil e recomendada para iniciar a aplicação e o banco de dados.

Clone o repositório (se ainda não o fez):

Bash

git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
(Substitua seu-usuario/seu-repositorio pelo caminho real do seu projeto).

Verifique a configuração do banco de dados:
Certifique-se de que o arquivo docker-compose.yml na raiz do projeto tenha as credenciais de banco de dados definidas.

YAML

# ... (trecho de docker-compose.yml)
services:
  db:
    environment:
      POSTGRES_DB: vacas_db
      POSTGRES_USER: seu_usuario_postgres # <--- Mude este
      POSTGRES_PASSWORD: sua_senha_postgres # <--- Mude este
# ...
Altere seu_usuario_postgres e sua_senha_postgres para credenciais seguras de sua escolha.

Inicie os serviços:
Na raiz do projeto, execute o seguinte comando:

Bash

docker-compose up --build -d
--build: Garante que a imagem da sua aplicação Spring Boot seja construída (ou reconstruída) a partir do Dockerfile.
-d: Inicia os contêineres em segundo plano (detached mode).
Verifique o status dos contêineres:

Bash

docker ps
Você deverá ver os contêineres vacas_db_container e vacas_app_container rodando.

Acesse a API:
A API estará disponível em http://localhost:8080/api.

Parar e Limpar:
Para parar e remover os contêineres e redes:

Bash

docker-compose down
Para parar, remover e também apagar os dados persistidos do banco de dados (use com cautela!):

Bash

docker-compose down -v
Rodando Diretamente com Maven
Se você preferir rodar a aplicação diretamente em sua máquina sem Docker (certifique-se de ter um PostgreSQL rodando e configurado localmente).

Configure o Banco de Dados:

Crie um banco de dados PostgreSQL chamado vacas_db.
Atualize o arquivo src/main/resources/application.properties com suas credenciais do PostgreSQL:
Properties

spring.datasource.url=jdbc:postgresql://localhost:5432/vacas_db
spring.datasource.username=seu_usuario_postgres # <--- Mude este
spring.datasource.password=sua_senha_postgres # <--- Mude este
Execute a aplicação:
Na raiz do projeto, execute:

Bash

mvn spring-boot:run
A aplicação iniciará na porta 8080.

Conceitos Importantes para o Frontend
HATEOAS
HATEOAS (Hypermedia as an Engine of Application State) significa que as respostas da API incluem links que indicam as ações ou recursos relacionados que o cliente pode acessar em seguida. Isso permite que o frontend "descubra" as funcionalidades da API sem URLs hardcoded, tornando a integração mais flexível e resiliente a mudanças na estrutura da API.

Estrutura de Links
Os links HATEOAS são incluídos nas respostas JSON sob a chave _links. Cada link possui um rel (nome do relacionamento) e um href (a URL do recurso).

Exemplo de resposta com links:

JSON

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
        }
    }
}
O frontend deve parsear a resposta JSON, acessar a propriedade _links, localizar o link desejado pelo seu rel (ex: delete_vaca) e usar o href associado para fazer a próxima requisição.

Códigos de Status HTTP
A API utiliza códigos de status HTTP padrão para indicar o resultado das operações:

2xx (Sucesso): 200 OK, 201 Created, 204 No Content.
4xx (Erro do Cliente): 400 Bad Request, 404 Not Found, 409 Conflict.
5xx (Erro do Servidor): 500 Internal Server Error. É crucial que o frontend implemente o tratamento desses códigos para exibir mensagens de erro adequadas ao usuário.
Tratamento de Erros
Em caso de erros (4xx ou 5xx), a API pode retornar um corpo de resposta JSON com detalhes do erro. O frontend deve monitorar esses códigos e exibir mensagens apropriadas ao usuário.

CORS (Cross-Origin Resource Sharing)
Se o seu frontend estiver sendo executado em um domínio/porta diferente da API (ex: http://localhost:3000 para frontend e http://localhost:8080 para API), erros de CORS podem ocorrer. A API está configurada com @CrossOrigin nos controladores para permitir requisições de origens comuns de desenvolvimento (como http://localhost:3000). Se o seu frontend estiver em outra origem, será necessário ajustar a configuração no código da API (nas anotações @CrossOrigin nos controladores).

Formato de Datas
Todas as datas na API (requisições e respostas) devem estar no formato YYYY-MM-DD (ex: 2025-06-05).

Documentação da API
Base URL: http://localhost:8080/api

1. Recurso: Vacas
Gerencia o registro e as informações básicas das vacas.

GET /api/vacas - Listar Todas as Vacas
Retorna uma lista de todas as vacas registradas, com links HATEOAS.

Método: GET
Parâmetros: Nenhum
Resposta (200 OK):
JSON

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
    }
    // ...
]
Resposta (204 No Content): Nenhuma vaca registrada.
GET /api/vacas/{id} - Buscar Vaca por ID
Retorna os detalhes de uma vaca específica.

Método: GET
Parâmetros de Path:
id (long, obrigatório): O ID da vaca.
Resposta (200 OK): Objeto Vaca com detalhes e links HATEOAS para ações como self, delete_vaca, update_vaca, all_vacas, producoes_leite, analise_producao_15_dias, registrar_producao.
Resposta (404 Not Found): Vaca não encontrada.
POST /api/vacas - Criar uma Nova Vaca
Cria um novo registro de vaca.

Método: POST
Headers: Content-Type: application/json
Corpo da Requisição (JSON):
JSON

{
    "nome": "Pérola",
    "raca": "Holandesa",
    "dataNascimento": "2022-04-01"
}
nome (string, obrigatório, único)
raca (string, obrigatório)
dataNascimento (string, YYYY-MM-DD, opcional)
Resposta (201 Created): Objeto Vaca da vaca criada com links HATEOAS.
Resposta (400 Bad Request): Nome duplicado ou dados inválidos.
PUT /api/vacas/{id} - Atualizar Vaca
Atualiza as informações de uma vaca existente.

Método: PUT
Parâmetros de Path:
id (long, obrigatório): O ID da vaca a ser atualizada.
Headers: Content-Type: application/json
Corpo da Requisição (JSON):
JSON

{
    "nome": "Pérola do Leite",
    "raca": "Holandesa",
    "dataNascimento": "2022-04-01"
}
Envie os campos que deseja atualizar.
Resposta (200 OK): Objeto Vaca atualizada com links HATEOAS.
Resposta (404 Not Found): Vaca não encontrada.
Resposta (400 Bad Request): Dados inválidos.
DELETE /api/vacas/{id} - Deletar Vaca
Remove um registro de vaca do sistema.

Método: DELETE
Parâmetros de Path:
id (long, obrigatório): O ID da vaca a ser deletada.
Resposta (204 No Content): Vaca deletada com sucesso.
Resposta (404 Not Found): Vaca não encontrada.
2. Recurso: Produção de Leite
Gerencia o registro e a análise da produção de leite das vacas.

POST /api/producao-leite/vaca/{vacaId} - Registrar Produção de Leite
Registra a quantidade de leite produzida por uma vaca em uma data específica.

Método: POST
Parâmetros de Path:
vacaId (long, obrigatório): O ID da vaca.
Headers: Content-Type: application/json
Corpo da Requisição (JSON):
JSON

{
    "dataRegistro": "2025-06-05",
    "quantidadeLitros": 28.5
}
dataRegistro (string, YYYY-MM-DD, obrigatório)
quantidadeLitros (double, obrigatório)
Resposta (201 Created): Objeto ProducaoLeite registrado com links HATEOAS para a vaca associada e análises.
Resposta (404 Not Found): Vaca não encontrada.
Resposta (400 Bad Request): Dados inválidos.
GET /api/producao-leite/vaca/{vacaId} - Listar Produções de Leite por Vaca
Retorna todas as produções de leite registradas para uma vaca específica, ordenadas por data.

Método: GET
Parâmetros de Path:
vacaId (long, obrigatório): O ID da vaca.
Resposta (200 OK): Lista de objetos ProducaoLeite, cada um com links HATEOAS.
Resposta (204 No Content): Nenhuma produção de leite encontrada para a vaca.
GET /api/producao-leite/vaca/{vacaId}/analise-15-dias - Analisar Produção de Leite nos Últimos 15 Dias
Calcula e retorna a produção de leite diária para uma vaca nos últimos 15 dias, indicando se a quantidade aumentou, diminuiu ou permaneceu estável em relação ao dia anterior.

Método: GET
Parâmetros de Path:
vacaId (long, obrigatório): O ID da vaca.
Resposta (200 OK): Lista de objetos de análise.
JSON

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
    }
    // ...
]
Resposta (204 No Content): Nenhuma produção de leite encontrada para a vaca nos últimos 15 dias.
