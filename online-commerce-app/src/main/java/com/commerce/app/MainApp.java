package com.commerce.app;

import io.javalin.Javalin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.commerce.business.BusinessManager;
import com.commerce.dao.DataProvider;
import com.commerce.model.*;
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
        app.get("/forms/customer", ctx -> {
            String id = ctx.queryParam("id");
            Map<String, Object> model = new HashMap<>();
                          
        
            if (id != null) {
                // Fetch existing data for Edit mode
                model.put("c", bm.getCustomerById(Integer.parseInt(id)));
            } 
            else {
                // Pass a null/empty object for Create mode
                model.put("c", new Customer()); 
            }
            ctx.render("templates/forms/customer-form.html", model);

        });

        app.get("/forms/product", ctx -> {
            String id = ctx.queryParam("id");
            Map<String, Object> model = new HashMap<>();
            
            if (id != null && !id.isEmpty()) {
                // Edit mode: Fetch existing product
                model.put("p", bm.getProductById(Integer.parseInt(id)));
            } else {
                // Create mode: Pass a new object
                model.put("p", new Product()); 
            }
            ctx.render("templates/forms/product-form.html", model);
        });

        // Category Form Route
        app.get("/forms/category", ctx -> {
            String id = ctx.queryParam("id");
            Map<String, Object> model = new HashMap<>();
            model.put("cat", (id != null) ? bm.getCategoryById(Integer.parseInt(id)) : new Category());
            ctx.render("templates/forms/category-form.html", model);
        });

        // Order Form Route
        app.get("/forms/order", ctx -> {
            String id = ctx.queryParam("id");
            Map<String, Object> model = new HashMap<>();
            if (id != null) {
                Order o = bm.getOrderById(Integer.parseInt(id));
                model.put("o", o);
            } else {
                model.put("o", new Order());
            }
            ctx.render("templates/forms/order-form.html", model);
        });

        app.get("/forms/orderitem", ctx -> {
            String id = ctx.queryParam("id");
            Map<String, Object> model = new HashMap<>();
            
            if (id != null && !id.isEmpty()) {
                // Edit mode
                model.put("item", bm.getOrderItemById(Integer.parseInt(id)));
            } else {
                // Create mode
                model.put("item", new OrderItem()); 
            }
            ctx.render("templates/forms/orderitem-form.html", model);
        });

        app.get("/forms/payment", ctx -> {
            String id = ctx.queryParam("id");
            Map<String, Object> model = new HashMap<>();
            model.put("pay", (id != null) ? bm.getPaymentById(Integer.parseInt(id)) : new Payment());
            ctx.render("templates/forms/payment-form.html", model);
        });

        app.get("/forms/shipment", ctx -> {
            String id = ctx.queryParam("id");
            Map<String, Object> model = new HashMap<>();
            model.put("s", (id != null) ? bm.getShipmentById(Integer.parseInt(id)) : new Shipment());
            ctx.render("templates/forms/shipment-form.html", model);
        });

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
        app.get("/customers", ctx -> {
            String searchId = ctx.queryParam("searchId");
            Map<String, Object> model = new HashMap<>();
            
            if (searchId != null && !searchId.isEmpty()) {
                Customer c = bm.getCustomerById(Integer.parseInt(searchId));
                model.put("customers", (c != null) ? List.of(c) : List.of());
            } else {
                model.put("customers", bm.getAllCustomers());
            }
            model.put("products", new ArrayList<>());
            model.put("orders", new ArrayList<>());
            model.put("orderItems", new ArrayList<>());
            model.put("categories", new ArrayList<>());
            model.put("payments", new ArrayList<>());
            model.put("shipments", new ArrayList<>());
            ctx.render("templates/dashboard.html", model);
        });
        app.post("/customers", CustomerController::create);
        app.put("/customers/{id}", CustomerController::update);
        app.delete("/customers/{id}", CustomerController::deleteOne);

        // --- PRODUCT ROUTES ---
        app.get("/products", ctx -> {
            String searchId = ctx.queryParam("searchId");
            Map<String, Object> model = new HashMap<>();
            
            // Handle the Search Logic for Products
            List<Product> productResult = new ArrayList<>();
            if (searchId != null && !searchId.isEmpty()) {
                try {
                    // Use your business manager to get the specific product
                    Product p = bm.getProductById(Integer.parseInt(searchId));
                    if (p != null) productResult.add(p);
                } catch (NumberFormatException e) {
                    // Non-numeric input returns an empty list
                }
            } else {
                productResult = bm.getAllProducts();
            }
            model.put("products", productResult);
            
            // Provide empty lists for other models to prevent Thymeleaf 500 errors
            model.put("customers", new ArrayList<>());
            model.put("orders", new ArrayList<>());
            model.put("orderItems", new ArrayList<>());
            model.put("categories", new ArrayList<>());
            model.put("payments", new ArrayList<>());
            model.put("shipments", new ArrayList<>());

            ctx.render("templates/dashboard.html", model);
        });

        // app.get("/products/{id}", ProductController::getOne);
        app.post("/products", ProductController::create);
        app.put("/products/{id}", ProductController::update);
        app.delete("/products/{id}", ProductController::deleteOne);

        // --- CATEGORY ROUTES ---
        app.get("/categories", ctx -> {
            String searchId = ctx.queryParam("searchId");
            Map<String, Object> model = new HashMap<>();
            
            // 1. Handle the Search Logic for Categories
            List<Category> categoryResult = new ArrayList<>();
            if (searchId != null && !searchId.isEmpty()) {
                try {
                    Category cat = bm.getCategoryById(Integer.parseInt(searchId));
                    if (cat != null) categoryResult.add(cat);
                } catch (NumberFormatException e) {
                    // Return empty list if input is not a valid number
                }
            } else {
                categoryResult = bm.getAllCategories();
            }
            model.put("categories", categoryResult);

            // 2. Prevent 500 Errors by providing empty lists for other objects 
            // referenced in dashboard.html
            model.put("customers", new ArrayList<>());
            model.put("products", new ArrayList<>());
            model.put("orders", new ArrayList<>());
            model.put("orderItems", new ArrayList<>());
            model.put("payments", new ArrayList<>());
            model.put("shipments", new ArrayList<>());

            ctx.render("templates/dashboard.html", model);
        });
        app.post("/categories", CategoryController::create);
        app.put("/categories/{id}", CategoryController::update);
        app.delete("/categories/{id}", CategoryController::deleteOne);

        // --- ORDER ROUTES ---
        app.get("/orders", ctx -> {
            String searchId = ctx.queryParam("searchId");
            Map<String, Object> model = new HashMap<>();
            
            // 1. Handle the Search Logic for Orders
            List<Order> orderResult = new ArrayList<>();
            if (searchId != null && !searchId.isEmpty()) {
                try {
                    Order o = bm.getOrderById(Integer.parseInt(searchId));
                    if (o != null) orderResult.add(o);
                } catch (NumberFormatException e) {
                    // Return empty list if input is not a valid number
                }
            } else {
                orderResult = bm.getAllOrders();
            }
            model.put("orders", orderResult);

            // 2. STUB data to prevent Thymeleaf errors in dashboard.html
            model.put("customers", new ArrayList<>());
            model.put("products", new ArrayList<>());
            model.put("orderItems", new ArrayList<>());
            model.put("categories", new ArrayList<>());
            model.put("payments", new ArrayList<>());
            model.put("shipments", new ArrayList<>());

            ctx.render("templates/dashboard.html", model);
        });
        app.post("/orders", OrderController::create);
        app.put("/orders/{id}", OrderController::update);
        app.delete("/orders/{id}", OrderController::deleteOne);

        // --- ORDER ITEM ROUTES ---
        app.get("/orderitems", ctx -> {
            String searchId = ctx.queryParam("searchId");
            Map<String, Object> model = new HashMap<>();
            
            // 1. Handle the Search Logic for Order Items
            List<OrderItem> orderItemResult = new ArrayList<>();
            if (searchId != null && !searchId.isEmpty()) {
                try {
                    OrderItem item = bm.getOrderItemById(Integer.parseInt(searchId));
                    if (item != null) orderItemResult.add(item);
                } catch (NumberFormatException e) {
                    // Non-numeric input results in an empty list
                }
            } else {
                orderItemResult = bm.getAllOrderItems();
            }
            model.put("orderItems", orderItemResult);

            // 2. STUB data for other tables required by dashboard.html
            model.put("customers", new ArrayList<>());
            model.put("products", new ArrayList<>());
            model.put("orders", new ArrayList<>());
            model.put("categories", new ArrayList<>());
            model.put("payments", new ArrayList<>());
            model.put("shipments", new ArrayList<>());

            ctx.render("templates/dashboard.html", model);
        });
        app.post("/orderitems", OrderItemController::create);
        app.put("/orderitems/{id}", OrderItemController::update);
        app.delete("/orderitems/{id}", OrderItemController::deleteOne);

        // --- PAYMENT ROUTES ---
        app.get("/payments", ctx -> {
            String searchId = ctx.queryParam("searchId");
            Map<String, Object> model = new HashMap<>();
            
            // 1. Handle the Search Logic for Payments
            List<Payment> paymentResult = new ArrayList<>();
            if (searchId != null && !searchId.isEmpty()) {
                try {
                    Payment pay = bm.getPaymentById(Integer.parseInt(searchId));
                    if (pay != null) paymentResult.add(pay);
                } catch (NumberFormatException e) {
                    // Non-numeric input results in an empty list
                }
            } else {
                paymentResult = bm.getAllPayments();
            }
            model.put("payments", paymentResult);

            // 2. STUB data for other tables required by dashboard.html
            model.put("customers", new ArrayList<>());
            model.put("products", new ArrayList<>());
            model.put("orders", new ArrayList<>());
            model.put("orderItems", new ArrayList<>());
            model.put("categories", new ArrayList<>());
            model.put("shipments", new ArrayList<>());

            ctx.render("templates/dashboard.html", model);
        });
        app.post("/payments", PaymentController::create);
        app.put("/payments/{id}", PaymentController::update);
        app.delete("/payments/{id}", PaymentController::deleteOne);

        // --- SHIPMENT ROUTES ---
        app.get("/shipments", ctx -> {
            String searchId = ctx.queryParam("searchId");
            Map<String, Object> model = new HashMap<>();
            
            // 1. Handle the Search Logic for Shipments
            List<Shipment> shipmentResult = new ArrayList<>();
            if (searchId != null && !searchId.isEmpty()) {
                try {
                    Shipment s = bm.getShipmentById(Integer.parseInt(searchId));
                    if (s != null) shipmentResult.add(s);
                } catch (NumberFormatException e) {
                    // Non-numeric input results in an empty list
                }
            } else {
                shipmentResult = bm.getAllShipments();
            }
            model.put("shipments", shipmentResult);

            // 2. STUB data for other tables to satisfy dashboard.html requirements
            model.put("customers", new ArrayList<>());
            model.put("products", new ArrayList<>());
            model.put("orders", new ArrayList<>());
            model.put("orderItems", new ArrayList<>());
            model.put("categories", new ArrayList<>());
            model.put("payments", new ArrayList<>());

            ctx.render("templates/dashboard.html", model);
        });
        app.post("/shipments", ShipmentController::create);
        app.put("/shipments/{id}", ShipmentController::update);
        app.delete("/shipments/{id}", ShipmentController::deleteOne);

        System.out.println("All routes registered. Testing at http://localhost:7070/dashboard");
        
   
    }
}