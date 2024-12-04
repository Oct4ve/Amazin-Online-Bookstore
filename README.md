Amazin Online Bookstore

The Amazin Online Bookstore is a Java-based Spring Boot application that serves as an online bookstore. This application manages submitted books and provides an interface for users to interact with them using REST API. It also provides a front-end display using Thymeleaf. Users can sign up/log in, add and remove books from the database (in the future only special users will be able to do this), add/remove books from their cart and view all the books in the database.

Object Classes:
Book: This class represents the Book entity and contains attributes such as title, ISBN, description, author, publish date and price.
User: This class represents the User entity and represents a user with a username and password. Each user has a shopping cart created for them upon sign up.
ShoppingCart: This represents a user's cart. One is created for every user upon sign up. The cart is able to contain books and is persisted between sessions.

Controller:
ThymeLeafController: A controller which controls the handling of HTML requests for all features of the application.

Repository Interfaces:
BookRepository: An interface that extends the CrudRepository to provide CRUD operations for the Book entity.
CartRepository:An interface extending the CrudRepository to provide persistence for created shopping carts.
UserRepository: An interface extending the CrudRepository to provide persistence for created users

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

![image](https://github.com/user-attachments/assets/8a801513-fb7b-444e-9973-1bfd0e1780c1)

Plan for next Sprint

For Milestone 3 our plan is:
- Add covers for books
- Add a quantity attribute to books so we can have multiple of the same books
- Make the UI nicer and the interface should not have any dangling links to non-implemented features
- Book recommendations and add the values needed for Jaccard Distance
- UI and Integration tests for the new implemented features
- Make recieve_date work for what date the book was added to the site

