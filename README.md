# 📚 LiterAlura

Aplicação Java de linha de comando desenvolvida como parte do **Challenge LiterAlura**.

O projeto tem como objetivo a construção de um catálogo de livros a partir da **API Gutendex**, com persistência de dados em **PostgreSQL**, utilizando **Spring Boot** e **Spring Data JPA**.

---

## 🎯 Objetivo do Projeto

Este projeto foi desenvolvido com foco no aprimoramento das seguintes competências:

- Consumo de APIs externas
- Mapeamento de dados com DTOs
- Persistência de dados com Spring Data JPA
- Modelagem e relacionamento entre entidades
- Criação de consultas personalizadas
- Organização de um projeto Spring Boot em camadas

---

## 🚀 Funcionalidades

A aplicação disponibiliza um menu interativo no terminal com as seguintes opções:


<img width="386" height="269" alt="image" src="https://github.com/user-attachments/assets/44518c9c-8695-4e4d-9b23-b4d0e7835e64" />

1. **Buscar livros por título**
   - Consulta a API Gutendex
   - Exibe até 5 resultados
   - Permite salvar um livro selecionado pelo ID
   
2. **Listar livros registrados**
   - Exibe título, autores, idioma e quantidade de downloads

3. **Listar autores registrados**
   - Mostra dados do autor e os livros associados

4. **Listar autores vivos em determinado ano**
   - Filtra autores com base no ano informado

5. **Buscar livros por idioma**
   - Filtra livros cadastrados por idioma (en, pt, fr, es)

0. **Encerrar a aplicação**




---

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- API Gutendex
- Maven
- Jackson

---
##🌐 API Utilizada

Gutendex API
https://gutendex.com/books/
<img width="779" height="778" alt="image" src="https://github.com/user-attachments/assets/b1169d40-6ba8-4387-95e5-31f00416a643" />



## 🧱 Estrutura do Projeto

<img width="363" height="486" alt="image" src="https://github.com/user-attachments/assets/b585efef-d13c-4e0c-97e0-4e8cfe46f690" />


    
---

## ⚙️ Configuração do Banco de Dados

Crie um banco de dados PostgreSQL e configure o arquivo `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true






