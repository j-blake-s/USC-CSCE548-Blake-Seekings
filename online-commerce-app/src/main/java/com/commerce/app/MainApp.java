package com.commerce.app;

import io.javalin.Javalin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.commerce.business.BusinessManager;
import com.commerce.dao.DataProvider;
import com.commerce.service.*;

import io.javalin.rendering.template.JavalinThymeleaf; // Import the renderer

/**
 * HOSTING DETAILS:
 * Platform: Railway (https://railway.app)
 * * HOW TO HOST:
 * 1. Database: Provisioned a MySQL instance on Railway. Used the 'Variables' 
 * tab to extract the public TCP proxy credentials for schema management.
 * 2. Schema: Connected via MySQL Workbench to create tables using DDL scripts.
 * 3. Application: Linked this Java project to a Railway 'Service'.
 * 4. Environment: Configured the following variables in Railway to allow 
 * the JAR to connect to the database:
 * - DATABASE_URL (Internal/Public connection string)
 * - PORT (Assigned by Railway's dynamic port mapping)
 * 5. Deployment: Set up a CI/CD pipeline where Railway automatically 
 * rebuilds and deploys the app whenever code is pushed to the GitHub repository.
 */
public class MainApp {
    public static void main(String[] args) {

        DataProvider dp = new DataProvider();
        BusinessManager bm = new BusinessManager(dp);

        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "7070"));

        Javalin app = Javalin.create(config -> {
            config.fileRenderer(new JavalinThymeleaf());
            // Optional: Adds a logger to see requests in your VS Code terminal
            config.requestLogger.http((ctx, ms) -> {
                System.out.println(ctx.method() + " " + ctx.path() + " (" + ms + "ms)");
            });
        }).start(port);

        app.exception(SQLException.class, (e, ctx) -> {
            if (e.getMessage().contains("foreign key constraint")) {
                    ctx.status(409).result("Cannot delete: This record is linked to other data.");
                } else {
                    ctx.status(500).result("Database Error");
                }
        });
        
        System.out.println("--- E-Commerce API Started ---");

        // Inside your main method
        // These routes return the blank forms for the modal
        app.get("/forms/customer", ctx -> ctx.render("templates/forms/customer-form.html"));
        app.get("/forms/product", ctx -> ctx.render("templates/forms/product-form.html"));
        app.get("/forms/category", ctx -> ctx.render("templates/forms/category-form.html"));
        app.get("/forms/order", ctx -> ctx.render("templates/forms/order-form.html"));
        app.get("/forms/orderitem", ctx -> ctx.render("templates/forms/orderitem-form.html"));
        app.get("/forms/payment", ctx -> ctx.render("templates/forms/payment-form.html"));
        app.get("/forms/shipment", ctx -> ctx.render("templates/forms/shipment-form.html"));
        
        // 2. The Dashboard Route
        app.get("/dashboard", ctx -> {
            try {
                    Map<String, Object> model = new HashMap<>();
                    model.put("customers", bm.getAllCustomers());
                    model.put("products", bm.getAllProducts());
                    model.put("orders", bm.getAllOrders());
                    model.put("orderItems", bm.getAllOrderItems());
                    model.put("categories", bm.getAllCategories());
                    model.put("payments", bm.getAllPayments());
                    model.put("shipments", bm.getAllShipments());
                    
                    ctx.render("templates/dashboard.html", model);
                } catch (Exception e) {
                    System.err.println("Dashboard Error: " + e.getMessage());
                    ctx.status(500).result("Could not load dashboard data.");
                }
        });


        // --- CUSTOMER ROUTES ---
        app.get("/customers", CustomerController::getAll);
        app.get("/customers/{id}", CustomerController::getOne);
        app.post("/customers", CustomerController::create);
        app.put("/customers/{id}", CustomerController::update);
        app.delete("/customers/{id}", CustomerController::deleteOne);

        // --- PRODUCT ROUTES ---
        app.get("/products", ProductController::getAll);
        app.get("/products/{id}", ProductController::getOne);
        app.post("/products", ProductController::create);
        app.put("/products/{id}", ProductController::update);
        app.delete("/products/{id}", ProductController::deleteOne);

        // --- CATEGORY ROUTES ---
        app.get("/categories", CategoryController::getAll);
        app.get("/categories/{id}", CategoryController::getOne);
        app.post("/categories", CategoryController::create);
        app.put("/categories/{id}", CategoryController::update);
        app.delete("/categories/{id}", CategoryController::deleteOne);

        // --- ORDER ROUTES ---
        app.get("/orders", OrderController::getAll);
        app.get("/orders/{id}", OrderController::getOne);
        app.post("/orders", OrderController::create);
        app.put("/orders/{id}", OrderController::update);
        app.delete("/orders/{id}", OrderController::deleteOne);

        // --- ORDER ITEM ROUTES ---
        app.get("/orderitems", OrderItemController::getAll);
        app.get("/orderitems/{id}", OrderItemController::getOne);
        app.post("/orderitems", OrderItemController::create);
        app.put("/orderitems/{id}", OrderItemController::update);
        app.delete("/orderitems/{id}", OrderItemController::deleteOne);

        // --- PAYMENT ROUTES ---
        app.get("/payments", PaymentController::getAll);
        app.get("/payments/{id}", PaymentController::getOne);
        app.post("/payments", PaymentController::create);
        app.put("/payments/{id}", PaymentController::update);
        app.delete("/payments/{id}", PaymentController::deleteOne);

        // --- SHIPMENT ROUTES ---
        app.get("/shipments", ShipmentController::getAll);
        app.get("/shipments/{id}", ShipmentController::getOne);
        app.post("/shipments", ShipmentController::create);
        app.put("/shipments/{id}", ShipmentController::update);
        app.delete("/shipments/{id}", ShipmentController::deleteOne);

        System.out.println("All routes registered. Testing at http://localhost:7070/dashboard");
        
   
    }
}