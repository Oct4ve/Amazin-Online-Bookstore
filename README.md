Amazin Online Bookstore

The Amazin Online Bookstore is a Java-based Spring Boot application that serves as an online bookstore. This application manages submitted books and provides an interface for users to interact with them using REST API. It also provides a front-end display using Thymeleaf.

Classes Book: This class represents the Book entity and contains attributes such as title, ISBN, description, author, publish date and price.

BookController: A REST controller that manages CRUD operations for books.

BookRepository: An interface that extends the CrudRepository to provide CRUD operations for the Book entity.

ShoppingCart: This class simulates a shopping cart that holds a list of books. It provides methods for managing the cart, such as adding, removing, and calculating the total cost of books.

Database Schema for Book Class

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

UML Diagram

![image](https://github.com/user-attachments/assets/de9a1824-5f82-4312-830b-d96c65d85fdb)

