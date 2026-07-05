# 🚀 Motor de Pagamentos - API de Alta Concorrência

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1+-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Security](https://img.shields.io/badge/Security-API_Key-E34F26?style=for-the-badge&logo=security&logoColor=white)

Um motor de processamento financeiro assíncrono construído em **Java 21** e **Spring Boot 4.1**. Este projeto foi desenhado do zero para resolver problemas reais de sistemas corporativos: alta volumetria de requisições, garantia absoluta de consistência de dados contra **Condições de Corrida (*Race Conditions*)** e blindagem de segurança nos endpoints.

---

## 🎯 O Problema que este Projeto Resolve

Em sistemas financeiros ingênuos (simples CRUDs), se dois utilizadores tentarem transferir todo o saldo de uma mesma conta exatamente no mesmo milissegundo, a base de dados pode permitir que ambas as transações ocorram, resultando no temido **Gasto Duplo (*Double Spending*)**. 

Além disso, processar regras de negócio pesadas e guardar na base de dados com o pedido HTTP aberto pode causar lentidão severa na experiência do utilizador (*Timeouts*). 

**A solução aplicada aqui:** Uma arquitetura orientada a eventos (*Event-Driven*) com processamento em *background*, travas pessimistas na base de dados e filtros de segurança, garantindo que o dinheiro seja transferido de forma segura, privada e com resposta quase instantânea.

---

## 🧠 Arquitetura e Padrões de Engenharia

Este repositório aplica práticas de desenvolvimento de nível Pleno/Sênior:

* **Segurança First (API Key):**
  A API não está exposta publicamente. Um filtro de segurança (Interceptor/Filter) atua como "Cão de Guarda", barrando imediatamente (Status 401) qualquer pedido que não possua a chave secreta de autorização, simulando o comportamento de um ambiente corporativo atrás de um API Gateway.

* **Domain-Driven Design (DDD) Tático:**
  A lógica de negócio não está espalhada em *Services* anêmicos. As entidades (como `Conta`) possuem regras próprias e métodos que protegem a sua integridade (ex: `validarSaldoSuficiente()`), garantindo um Domínio Rico.
  
* **Pessimistic Locking (Bloqueio Pessimista):**
  Implementado no `Repository` do Spring Data JPA. Quando uma transação inicia, a linha da base de dados referente à conta é bloqueada (`FOR UPDATE`). Nenhum outro pedido pode alterar esse saldo até que a transação atual faça o *commit* ou *rollback*.

* **Processamento Assíncrono (Eventos):**
  A API não processa a transferência no momento em que a recebe. Ela emite um evento via `ApplicationEventPublisher`, retorna um **Status 202 (Accepted)** para o utilizador imediatamente e processa o envio do dinheiro numa *Thread* separada (`@Async`).

* **Padrão Polling de Consulta (Cache em Memória):**
  Para que o cliente saiba o resultado da transação que ocorreu em *background*, implementámos um serviço de cache Thread-Safe utilizando `ConcurrentHashMap`. O cliente recebe um protocolo no POST e consulta o status num endpoint GET, sem sobrecarregar a base de dados principal.

* **Tratamento Global de Erros (RFC 7807):**
  Utilização de `@RestControllerAdvice` para capturar exceções de domínio e retornar JSONs formatados e padronizados, evitando a exposição de *Stack Traces* (dados sensíveis) ao cliente.

---

## 🛠️ Stack Tecnológica

**Core:**
* Java 21
* Spring Boot 4.1+ (Modular Architecture)
* Spring WebMvc
* Spring Data JPA
* Bean Validation

**Base de Dados & Memória:**
* H2 Database (Base de dados em memória para testes e execução rápida)
* ConcurrentHashMap (Estratégia de Cache local para Polling)

**Qualidade & DevOps:**
* JUnit 5 + Spring Boot WebMvc Test (Testes de Integração com `MockMvc` configurados com Header de Segurança)
* SpringDoc OpenAPI (Swagger UI para documentação viva)
* Docker (Containerização)

---

## ⚙️ Como Executar o Projeto

Pode correr esta aplicação de duas maneiras. A porta padrão é a `8080`.

### Opção 1: Via Docker (Recomendado)
Pré-requisitos: Ter o [Docker](https://www.docker.com/) instalado na sua máquina.

```bash
# 1. Faça o build do projeto (Gera o ficheiro .jar ignorando os testes)
./mvnw clean package -DskipTests

# 2. Crie a imagem Docker
docker build -t motor-pagamentos .

# 3. Suba o contêiner mapeando a porta 8080
docker run -p 8080:8080 motor-pagamentos
