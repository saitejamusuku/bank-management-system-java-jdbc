# Bank Management System Version 1.0

A console-based **Bank Management System** developed using **Java, Maven, JDBC, and MySQL**.

This project implements a layered architecture using DAO, Service, Model, and Utility layers to provide clean separation of responsibilities and maintainable code.

---

## 🚀 Technologies Used

- Java
- Maven Framework
- JDBC (Java Database Connectivity)
- MySQL Database
- Git & GitHub

---

## 📁 Project Structure

```
BankManagementSystem/
│
├── src/main/java
│   │
│   ├── app
│   │      Main.java
│   │
│   ├── config
│   │      DBConnection.java
│   │
│   ├── dao
│   │      UserDAO.java
│   │      AccountDAO.java
│   │      TransactionDAO.java
│   │
│   ├── daoimpl
│   │      UserDAOImpl.java
│   │      AccountDAOImpl.java
│   │      TransactionDAOImpl.java
│   │
│   ├── model
│   │      User.java
│   │      Account.java
│   │      Transaction.java
│   │
│   ├── service
│   │      UserService.java
│   │      AccountService.java
│   │      TransactionService.java
│   │
│   ├── util
│   │      InputUtil.java
│   │
│   └── exception
│          BankException.java
│
├── pom.xml
└── README.md
```

---

# 🏗️ Project Architecture

The project follows a layered architecture.

## 1. Model Layer

Contains Java POJO classes representing database entities.

Classes:

- User.java
- Account.java
- Transaction.java

Responsibilities:

- Store application data
- Represent database tables

---

## 2. DAO Layer

DAO (Data Access Object) interfaces define database operations.

Classes:

- UserDAO.java
- AccountDAO.java
- TransactionDAO.java

Responsibilities:

- Define CRUD operation methods
- Provide database operation contracts

---

## 3. DAO Implementation Layer

Contains JDBC implementations of DAO interfaces.

Classes:

- UserDAOImpl.java
- AccountDAOImpl.java
- TransactionDAOImpl.java

Responsibilities:

- Establish database connection
- Execute SQL queries
- Perform insert, update, delete, and fetch operations

---

## 4. Service Layer

Contains business logic.

Classes:

- UserService.java
- AccountService.java
- TransactionService.java

Responsibilities:

- Validate user inputs
- Apply banking rules
- Communicate between DAO and application layers

---

## 5. Configuration Layer

Contains database connection configuration.

Class:

```
DBConnection.java
```

Responsibilities:

- Create JDBC connection
- Manage MySQL database connectivity

---

## 6. Utility Layer

Contains reusable helper classes.

Class:

```
InputUtil.java
```

Responsibilities:

- Handle user input
- Provide common utility methods

---

## 7. Exception Layer

Contains custom application exceptions.

Class:

```
BankException.java
```

Responsibilities:

- Handle banking-related errors
- Provide meaningful error messages

---

# ✨ Features

## User Module

- User registration
- User validation
- Store user information in MySQL database

## Account Module

- Create bank accounts
- View account details
- Manage account information

## Transaction Module

- Deposit money
- Withdraw money
- Transfer funds
- View transaction history

---

# 🗄️ Database

Database: **MySQL**

The application uses JDBC to communicate with the MySQL database.

Database operations:

- Insert records
- Update records
- Delete records
- Retrieve records

---

# 📦 Maven Dependency

The project uses Maven for dependency management.

MySQL JDBC Driver:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.4.0</version>
</dependency>
```

---

# ⚙️ How to Run

## 1. Clone Repository

```bash
git clone https://github.com/<username>/bank-management-system-java-jdbc.git
```

---

## 2. Configure Database

Update MySQL credentials in:

```
DBConnection.java
```

Example:

```java
private static final String URL = "jdbc:mysql://localhost:3306/bankdb";
private static final String USER = "root";
private static final String PASSWORD = "password";
```

---

## 3. Build Project Using Maven

```bash
mvn clean install
```

---

## 4. Run Application

Execute:

```
Main.java
```

---

# 🔮 Future Enhancements

- User login authentication
- Password encryption
- Admin module
- Account statements
- Interest calculation
- GUI implementation using JavaFX/Swing

---

# 👨‍💻 Author

**Saiteja**
