Amazin Online Bookstore

The Amazin Online Bookstore is a Java-based Spring Boot application that serves as an online bookstore. This application manages submitted books and provides an interface for users to interact with them using REST API. It also provides a front-end display using Thymeleaf.

Classes Book: This class represents the Book entity and contains attributes such as title, ISBN, description, author, publish date and price.

BookController: A REST controller that manages CRUD operations for books.

BookRepository: An interface that extends the CrudRepository to provide CRUD operations for the Book entity.

ShoppingCart: This class simulates a shopping cart that holds a list of books. It provides methods for managing the cart, such as adding, removing, and calculating the total cost of books.

Database Schemas 

Book

| Column       | Data Type        | Constraints                 |
|--------------|------------------|-----------------------------|
| id           | BIGINT           | PRIMARY KEY, AUTO_INCREMENT |
| title        | VARCHAR(255)     | NOT NULL                    |
| description  | TEXT             | NULLABLE                    |
| author       | VARCHAR(255)     | NOT NULL                    |
| isbn         | VARCHAR(20)      | UNIQUE, NOT NULL            |
| publishDate  | DATE             | NULLABLE                    |
| receiveDate  | DATE             | NULLABLE                    |
| price        | DECIMAL(10,2)    | NOT NULL                    |

Shopping Cart

| Column   | Data Type | Constraints                               |
|----------|-----------|-------------------------------------------|
| id       | BIGINT    | PRIMARY KEY, AUTO_INCREMENT               |
| user_id  | BIGINT    | UNIQUE, FOREIGN KEY REFERENCES `users(id)`|

Shopping Cart Additions

| Column             | Data Type | Constraints                                             |
|--------------------|-----------|---------------------------------------------------------|
| shopping_cart_id   | BIGINT    | PRIMARY KEY, FOREIGN KEY REFERENCES `shopping_carts(id)`|
| book_id            | BIGINT    | PRIMARY KEY, FOREIGN KEY REFERENCES `books(id)`         |

User

| Column        | Data Type              | Constraints                                        |
|---------------|------------------------|----------------------------------------------------|
| id            | BIGINT                 | PRIMARY KEY, AUTO_INCREMENT                        |
| username      | VARCHAR(255)           | UNIQUE, NOT NULL                                   |
| password      | VARCHAR(255)           | NOT NULL                                           |
| permissions   | ENUM('BASIC', 'ADMIN') | NOT NULL                                           |
| cart_id       | BIGINT                 | UNIQUE, FOREIGN KEY REFERENCES `shopping_carts(id)`|

UML Diagram

![image](https://github.com/user-attachments/assets/de9a1824-5f82-4312-830b-d96c65d85fdb)

