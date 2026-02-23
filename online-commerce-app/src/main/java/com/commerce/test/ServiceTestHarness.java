package com.commerce.test;
import com.commerce.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServiceTestHarness {
    // Replace this with your actual Railway URL
    private static final String BASE_URL = "https://usc-csce548-blake-seekings-production.up.railway.app";
    private static final HttpClient client = HttpClient.newHttpClient();
    public static void test() throws Exception {

        System.out.println("=== STARTING DATA LIFECYCLE TESTS ===\n");

         // 1. Category & Product
        Category cat = new Category(0, "Electronics", "Gadgets and gear");
        int catId = POST(cat, "categories");
        System.out.println("Category Created");
        GET("categories", catId);

        Product prod = new Product(0, "Smartphone", "Latest model", new BigDecimal(999.99), catId);
        int prodId = POST(prod, "products");
        System.out.println("Product Created");
        GET("products", prodId);

        // 2. Customer
        Customer cust = new Customer(0, "John", "Doe", "john@doe.com", "555-1234", "101 Main St");
        int custId = POST(cust, "customers");
        System.out.println("Customer Created");
        GET("customers", custId);

        // 3. Order & OrderItem
        Order ord = new Order(0, custId, "2023-10-27", new BigDecimal(1050.00), "Pending");
        int ordId = POST(ord, "orders");
        System.out.println("Order Created");
        GET("orders", ordId);

        OrderItem item = new OrderItem(0, ordId, prodId, 1, new BigDecimal(999.99));
        int itemId = POST(item, "orderitems");
        System.out.println("Order Item Created");
        GET("orderitems", itemId);


        // 4. Payment & Shipment
        Payment pay = new Payment(0, ordId, "2023-10-27", new BigDecimal(1050.00), "Credit Card");
        int payId = POST(pay, "payments");
        System.out.println("Payment Created");
        GET("payments", payId);


        Shipment ship = new Shipment(0, ordId, "101 Main St", "Processing");
        int shipId = POST(ship, "shipments");
        System.out.println("Shipment Created");
        GET("shipments", shipId);


        // --- UPDATE TEST ---
        System.out.println("\n--- PERFORMING UPDATES ---");
        cust.setLastName("Smith");
        PUT(cust, "customers", custId);
        GET("customers", custId);

        cat.setDescription("High-end gadgets and hardware");
        PUT(cat, "categories", catId);
        GET("categories", catId);

        prod.setPrice(new BigDecimal("1150.99"));
        PUT(prod, "products", prodId);
        GET("products", prodId);

        ord.setStatus("Shipped");
        PUT(ord, "orders", ordId);
        GET("orders", ordId);

        item.setQuantity(2);
        PUT(item, "orderitems", itemId);
        GET("orderitems", itemId);

        pay.setPaymentMethod("PayPal");
        PUT(pay, "payments", payId);
        GET("payments", payId);

        ship.setStatus("Delivered");
        PUT(ship, "shipments", shipId);
        GET("shipments", shipId);
        
        // --- DELETE TEST ---
        System.out.println("\n--- PERFORMING DELETIONS ---");
        DELETE("shipments", shipId);
        System.out.println("Shipment " + shipId + " deleted");
        
        DELETE("payments", payId);
        System.out.println("Payment " + payId + " deleted");

        // 3. ORDER ITEM
        DELETE("orderitems", itemId);
        System.out.println("Order Item " + itemId + " deleted");

        // 4. PRODUCT
        DELETE("products", prodId);
        System.out.println("Product " + prodId + " deleted");

        // 5. ORDER
        DELETE("orders", ordId);
        System.out.println("Order " + ordId + " deleted");

        // 6. CATEGORY
        DELETE("categories", catId);
        System.out.println("Category " + catId + " deleted");

        // 7. CUSTOMER
        DELETE("customers", custId);
        System.out.println("Customer " + custId + " deleted");



    }

    private static String toJson(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            // Log the error (as a Database Engineer, you know how vital logs are!)
            System.err.println("Error serializing Customer to JSON: " + e.getMessage());
            return "{}";
        }
    }

    private static void GET(String table) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + table))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            System.out.println(response.body());
        } else {
            System.out.println("❌ Failed to fetch. Status: " + response.statusCode());
        }
    }

    private static void GET(String table, int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + table + "/" + id))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            System.out.println(response.body());
        } else {
            System.out.println("❌ Failed to fetch. Status: " + response.statusCode());
        }

    }

    private static void PUT(Object obj, String table, int id) throws Exception {
        String json = toJson(obj);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/" + table + "/" + id))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(json))
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static void DELETE(String table, int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + table + "/" + id))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static int POST(Object obj, String table) throws Exception {
        String json = toJson(obj);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + table))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return Integer.parseInt(response.body());
    }
}