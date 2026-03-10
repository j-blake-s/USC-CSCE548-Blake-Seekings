# Online Commerce Admin Dashboard

A high-performance, full-stack management platform for e-commerce operations. This application provides a centralized administrative interface to manage customers, products, categories, orders, and logistics.

---

## 🚀 What the Project Does

The project is a multi-layered Java application that serves as a complete back-office solution for online retailers. It utilizes a modern tech stack to provide real-time data management through a responsive web dashboard.

* **Comprehensive CRUD Operations**: Full capability to Create, Read, Update, and Delete records for all core business objects including Customers, Products, Categories, Orders, Order Items, Payments, and Shipments.
* **Real-time Search**: Purpose-built search-by-ID functionality for every database table, allowing for instant retrieval of specific records.
* **Dynamic UI**: A single-page dashboard experience powered by HTMX and Alpine.js, enabling partial page updates without full browser refreshes.
* **Relational Data Management**: Handles complex database relationships, such as linking payments and shipments to specific orders, and products to categories.

## 💡 Why the Project is Useful

This project bridges the gap between complex database architecture and user-friendly management.

* **Operational Efficiency**: Instead of manual SQL queries, administrators can use a visual interface to manage live production data on platforms like Railway.
* **Scalable Architecture**: Built with a strict separation of concerns, featuring a Data layer, Business layer, and Service layer for easy maintainability and scaling.
* **Modern Web Stack**: Demonstrates the use of Javalin and HTMX to create "Single Page Application" (SPA) behavior with the simplicity of server-side rendering.
* **Reliability**: Includes robust exception handling for database constraints, preventing accidental data corruption (e.g., preventing the deletion of customers with active orders).

## 🛠️ Tech Stack

* **Backend**: Java, Javalin (Web Framework).
* **Database**: MySQL.
* **Frontend**: Thymeleaf (Templating), Tailwind CSS (Styling), HTMX (AJAX/Partial Swaps), Alpine.js (State Management).
* **Hosting**: Railway.


## 🚀 Deployment Guide

Follow these steps to set up the environment and run the **Online Commerce App** on your local machine.

### 1. Prerequisites & Repository Setup
* **Source Code**: Download or clone the GitHub repository. All necessary Java source code is located within the `online-commerce-app` folder.
* **Environment**: The developer recommends using **Visual Studio Code** with the appropriate Java extensions. This allows you to run files simply by clicking the **Play** arrow in the top-right corner.

### 2. Database Configuration (MySQL Workbench)
Before running the application, you must initialize the data layer:
* **Connection**: Open MySQL Workbench and create a new connection. Use any name you prefer, but ensure it is locally hosted on **port 3306**.
* **Initialization**: Locate the `db_creation.sql` and `db_insert.sql` files in the root directory. Run both scripts to create and populate a database named `railway`.

### 3. Application Configuration
You must link the Java source code to your local database instance:
* **Database Utility**: Navigate to `src/main/java/com/commerce/util/DatabaseUtil.java`.
* **Connection String**: Update the `url` variable to:
    `jdbc:mysql://localhost:3306/railway?allowPublicKeyRetrieval=true&useSSL=false`.
* **Credentials**: Ensure the `username` and `password` variables match the environment you created in MySQL Workbench.

### 4. Testing & Execution
Once the configuration is complete, you can verify the layers of the application:
* **Verify Data/Business Layers**: Navigate to `src/main/java/com/commerce/test/Testing.java` and run the `DatabaseHarness` and `BusinessHarness`.
* **Launch the Application**: Open `src/main/java/com/commerce/app/MainApp.java` and run the file. This serves as the engine for the entire project and will host the app locally.

### 5. Accessing the Dashboard
While **MainApp.java** is running, you can interact with the client layer:
* **URL**: Navigate to [http://localhost:7070/dashboard](http://localhost:7070/dashboard) in your web browser.
* **Functionality**: From this interface, you can view all tables, insert new records, and edit or delete existing entries.
