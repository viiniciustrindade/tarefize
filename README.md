# 📝 To-Do List API

> **API disponível em:** [https://todolist-api-8hia.onrender.com/swagger-ui/index.html](https://todolist-api-8hia.onrender.com/swagger-ui/index.html)

Uma API REST robusta para gerenciamento de tarefas, desenvolvida com **Spring Boot 3**. Este projeto foi construído focando em boas práticas de desenvolvimento, segurança com JWT e arquitetura limpa, permitindo que usuários gerenciem suas tarefas de forma organizada e segura.

## 🚀 Tecnologias Utilizadas

* **Java 21**
* **Spring Boot 3**
* **Spring Security** (Autenticação via JWT)
* **Spring Data JPA** (Persistência de dados)
* **PostgreSQL** (Banco de dados relacional)
* **Bean Validation** (Validação de dados com Hibernate Validator)
* **Lombok** (Redução de código boilerplate)
* **Swagger/OpenAPI** (Documentação interativa da API)

## 📌 Funcionalidades Principais

* **Autenticação de Usuários:** Sistema de cadastro e login com senhas criptografadas (BCrypt).
* **Gestão de Tarefas (CRUD):** Criação, listagem, atualização e exclusão de tarefas.
* **Isolamento de Dados:** Cada usuário tem acesso exclusivo apenas às suas próprias tarefas.
* **Validação de Domínio:** Sistema de prioridades (range de 1 a 5) validado na camada de entrada.
* **Tratamento de Erros:** Handler global para captura de exceções de negócio e erros de validação, retornando respostas padronizadas.

## 🏗️ Arquitetura e Boas Práticas

O projeto segue os princípios da **Arquitetura em Camadas** e **SOLID**, garantindo facilidade de manutenção e testabilidade.



* **Controller:** Gerencia as rotas e valida a entrada de dados com `@Valid`.
* **Service:** Centraliza as regras de negócio e orquestração de persistência.
* **Repository:** Interface de comunicação com o banco de dados.
* **DTO (Data Transfer Objects):** Records utilizados para trafegar dados com segurança, evitando a exposição de entidades.

## 📖 Como Executar o Projeto

1. **Clonar o repositório:**
```bash
   git clone https://github.com/viiniciustrindade/todolist-api.git
```
2. **Configurar o Banco de Dados:**

   Ajuste as credenciais do PostgreSQL no arquivo `src/main/resources/application.properties`.


3. **Executar a aplicação:**
```bash
./mvnw spring-boot:run
```


4. **Acessar a Documentação:**
   Abra o navegador em `http://localhost:8081/swagger-ui.html` para testar os endpoints através do Swagger.


## 🧪 Qualidade e Testes

O projeto conta com uma suíte de testes automatizados para garantir a confiabilidade das regras de negócio e a integridade dos endpoints:

* **Testes de Integração:** Implementados com `MockMvc` para validar o ciclo completo das requisições (Controller -> Service -> Repository), garantindo que a autenticação JWT e as permissões de acesso estejam funcionando corretamente.
* **Perfis de Teste:** Uso de `@ActiveProfiles("test")` para isolar o ambiente de desenvolvimento do ambiente de testes.
* **Validação de Erros:** Testes específicos para garantir que o `GlobalExceptionHandler` retorne os códigos HTTP e mensagens corretas em cenários de exceção.

## ☁️ Deploy e CI/CD

A aplicação está totalmente produtiva e integrada com ferramentas modernas de automação:

* **CI/CD (GitHub Actions):** Pipeline configurado para executar automaticamente toda a suíte de testes a cada `push` ou `pull request`. O deploy só é liberado se todos os testes passarem.
* **Hospedagem (Render):** API hospedada no Render, conectada a um banco de dados PostgreSQL gerenciado.
* **Segurança em Produção:** Uso rigoroso de variáveis de ambiente para proteção de segredos (JWT Secret, Database Credentials), seguindo as melhores práticas de segurança do mercado.
