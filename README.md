# GeoMottu - Sistema de Gerenciamento de Frotas 🛵

![Status: Concluído](https://img.shields.io/badge/status-concluído-green)
![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?logo=spring)
![Oracle DB](https://img.shields.io/badge/Oracle-Database-red?logo=oracle)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Frontend-green?logo=thymeleaf)

O **GeoMottu** é uma aplicação web completa (full-stack) desenvolvida para simular um sistema robusto de gerenciamento de frotas de motocicletas. Seu principal objetivo é atender às necessidades da empresa Mottu, oferecendo uma plataforma centralizada para o controle logístico e operacional de sua frota. 

A aplicação é construída em `Java 17` utilizando o framework `Spring Boot 3`, com uma interface de usuário renderizada no servidor via `Thymeleaf`. Para persistência de dados, emprega Spring Data JPA e `Azure SQL Database`, com migrações de schema gerenciadas por `Flyway`. A segurança é um pilar central, implementada com `Spring Security` para autenticação e autorização baseadas em perfis (ADMIN e USER). 

---

## Benefícios

* **Centralização da Informação:** Consolida dados cruciais de filiais, pátios e frotas em uma única plataforma, proporcionando uma visão unificada e em tempo real da operação. 

* **Controle Eficiente de Inventário:** Permite o rastreamento preciso de cada motocicleta, seu status operacional (Livre, Alugada, Manutenção) e localização (pátio atual), otimizando a alocação de recursos e a programação de manutenções. 

* **Segurança e Segregação de Acesso:** Implementação robusta de Spring Security, garantindo que cada usuário acesse apenas as informações e funcionalidades pertinentes ao seu perfil e filial, protegendo dados sensíveis e prevenindo acessos indevidos. 

* **Tomada de Decisão Baseada em Dados:** O dashboard administrativo fornece insights rápidos e visualmente intuitivos sobre a saúde da frota, como a distribuição de motos por status, tipo e a taxa de ocupação dos pátios, auxiliando gestores na tomada de decisões estratégicas. 

* **Escalabilidade e Robustez na Nuvem:** A implantação na Azure, utilizando App Services e Azure SQL Database (Serverless), garante que a aplicação seja escalável e resiliente, pronta para crescer com a demanda da Mottu, com otimização de custos em períodos de baixa utilização. 

---

## 🧑‍💻 Autores

<div align="center">

| Nome | RM |
| :--- | :--- |
| **Wesley Sena dos Santos** | 558043 |
| **Vanessa Yukari Iwamoto** | 558092 |
| **Samara Victoria Ferraz dos Santos** | 558719 |

</div>

---

## 📋 Índice

1.  [Visão Geral e Objetivo do Projeto](#-visão-geral-e-objetivo-do-projeto)
2.  [Funcionalidades Principais](#-funcionalidades-principais)
3.  [Arquitetura do Sistema](#️-arquitetura-do-sistema)
4.  [Detalhes de Segurança](#-detalhes-de-segurança)
5.  [Tecnologias Utilizadas](#️-tecnologias-utilizadas)
6.  [Como Executar Localmente](#-como-executar-localmente)
7.  [Como Rodar os Testes](#-como-rodar-os-testes)
8.  [Credenciais de Acesso](#-credenciais-de-acesso)
9. [Próximos Passos (Roadmap)](#-próximos-passos-roadmap)
10. [Links](#links)

---

## 🌟 Visão Geral e Objetivo do Projeto

A gestão de uma frota de veículos distribuída geograficamente apresenta desafios complexos, como controle de inventário, alocação de recursos e visibilidade operacional. O **GeoMottu** foi concebido para resolver esses desafios, oferecendo uma plataforma centralizada e segura para:

* **Gerenciar a estrutura física da empresa:** Cadastro e controle de Filiais e seus respectivos Pátios.
* **Controlar a frota de veículos:** Inventário detalhado de todas as motocicletas, incluindo status operacional (Livre, Alugada, Manutenção) e localização (pátio atual).
* **Segregar permissões:** Garantir que usuários operacionais (`USER`) possam apenas gerenciar recursos da sua própria filial, enquanto administradores (`ADMIN`) tenham visão e controle total sobre o sistema.
* **Visualizar dados operacionais:** Oferecer um Dashboard para administradores com métricas importantes sobre a saúde da frota e da operação.

---

## ✨ Funcionalidades Principais

-   [x] **Autenticação & Autorização:** Sistema de login seguro com perfis `ADMIN` e `USER`, utilizando Spring Security.
-   [x] **CRUD Completo e Seguro:** Operações de Criar, Ler, Atualizar e Deletar para todas as entidades principais:
    -   **Filiais:** Gerenciamento exclusivo para `ADMIN`.
    -   **Pátios:** `ADMIN` gerencia todos; `USER` gerencia apenas os da sua filial.
    -   **Motos:** `ADMIN` gerencia todas; `USER` gerencia apenas as da sua filial.
    -   **Usuários:** `ADMIN` pode gerenciar perfis e filiais de outros usuários.
-   [x] **Validações de Negócio:**
    -   Impedimento de cadastro de motos em pátios com capacidade esgotada.
    -   Validação de unicidade de campos críticos (placa, chassi, nome de usuário).
-   [x] **Dashboard Administrativo Interativo:**
    -   Cards com estatísticas em tempo real (total de usuários, pátios, motos).
    -   Gráficos dinâmicos (distribuição por status e por modelo).
    -   Tabela com ranking de pátios por ocupação.
-   [x] **Busca Dinâmica:** Funcionalidade de busca por placa/chassi na lista de motos e por nome na lista de usuários.

---

## 🏗️ Arquitetura do Sistema

A aplicação segue uma arquitetura em camadas (Layered Architecture), padrão em projetos Spring, para garantir a separação de responsabilidades e a manutenibilidade do código.

+-------------------+      +--------------------+      +-----------------------+
|  Frontend         |----->|  Controller Layer  |----->|  Service Layer        |
|  (Thymeleaf, JS)  |      |  (API Endpoints)   |      |  (Business Logic)     |
+-------------------+      +--------------------+      +-----------------------+
|
|
+-------------------+      +--------------------+      +----v------------------+
|  Database         |<-----|  Repository Layer  |<-----|  Domain Model         |
|  (Oracle)         |      |  (Spring Data JPA) |      |  (Entities & DTOs)    |
+-------------------+      +--------------------+      +-----------------------+

* **Controller Layer:** Recebe as requisições HTTP, valida os dados de entrada (usando DTOs) e delega a lógica de negócio para a camada de serviço.
* **Service Layer:** Contém as regras de negócio centrais, orquestra as operações e garante a segurança das transações.
* **Repository Layer:** Abstrai o acesso aos dados, utilizando Spring Data JPA para interagir com o banco de dados.
* **Domain Model:** Representa os dados da aplicação através de Entidades (mapeadas para o banco) e DTOs (Data Transfer Objects) para comunicação segura entre as camadas.

---

## 🛡️ Detalhes de Segurança

A segurança é um pilar central da aplicação, implementada com Spring Security.
* **Autenticação:** Baseada em formulário, com senhas armazenadas de forma segura usando o algoritmo **BCrypt**.
* **Autorização:** Controle de acesso baseado em perfis (`ROLE_ADMIN`, `ROLE_USER`). Endpoints e métodos de serviço são protegidos para garantir que apenas usuários autorizados possam executar operações críticas.
* **Proteção CSRF:** Todos os formulários que alteram o estado do sistema (`POST`) são protegidos contra ataques Cross-Site Request Forgery.
* **Controle de Acesso por Filial:** A lógica de serviço garante que usuários `USER` só possam visualizar e manipular dados (pátios, motos) pertencentes à sua própria filial.

---

## 🛠️ Tecnologias Utilizadas

#### **Backend**
* **Java 21**
* **Spring Boot 3.x**
* **Spring Web (MVC)**
* **Spring Data JPA**
* **Spring Security**
* **Lombok**

#### **Frontend**
* **Thymeleaf**
* **HTML5 / CSS3**
* **Bootstrap 5**
* **JavaScript (ES6)**
* **Chart.js**

#### **Testes**
* **JUnit 5**
* **Mockito**

#### **Banco de Dados & Ferramentas**
* **Oracle Database**
* **Flyway** (Versionamento de Banco de Dados)
* **Maven** (Gerenciamento de Dependências)

---

## 🚀 Como Executar Localmente

### **Pré-requisitos**
* Java JDK 21 ou superior
* Apache Maven 3.8 ou superior
* Uma instância do Oracle Database acessível
* Git

### **Configuração e Execução**
1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/GeoMottuJava.git](https://github.com/seu-usuario/GeoMottuJava.git)
    cd GeoMottuJava
    ```
2.  **Configure o Banco de Dados:**
    * Abra o arquivo `src/main/resources/application.properties`.
    * Altere as propriedades `spring.datasource.url`, `spring.datasource.username`, e `spring.datasource.password` com as credenciais do seu banco de dados Oracle.
3.  **Execute a Aplicação:**
    ```bash
    mvn spring-boot:run
    ```
4.  **Acesse:** A aplicação estará disponível em `http://localhost:8080`.

---
## Como Rodar os Testes

1. **Entre na pasta do projeto**
    ```bash
        cd GeoMottuJava
    ```
2. **Abra um terminal de sua escolha**
3. **Execute o comando de testes**
    ```bash
        mvn test
    ```
---

## 🔑 Credenciais de Acesso

Use os seguintes usuários (criados via migração do Flyway) para testar:

| Perfil | Usuário | Senha |
| :--- | :--- | :--- |
| **Administrador** | `admin` | `@Admin123` |
| **Usuário Comum** | `joao.silva` | `$User5432` |

---

## 🔮 Próximos Passos (Roadmap)
O projeto está pronto para ser expandido com novas funcionalidades:
-   `[ ]` **Implementar Histórico de Alterações da Moto (Auditoria)**
-   `[ ]` **Implementar Mapa Interativo de Filiais e Pátios**
-   `[ ]` **Implementar Geração de Relatórios da Frota em PDF/Excel**
-   `[ ]` **Implementar Sistema de Recuperação de Senha com Token**

---

## 🔗 Links

[![Deploy Online](https://img.shields.io/badge/🌍%20Abrir%20Aplicação-000?style=for-the-badge&logo=vercel)](https://geomottujava.onrender.com)
[![Vídeo de Demonstração](https://img.shields.io/badge/▶️%20Ver%20Demonstração-FF0000?style=for-the-badge&logo=youtube)](https://youtu.be/KOplYBdECnc) 
