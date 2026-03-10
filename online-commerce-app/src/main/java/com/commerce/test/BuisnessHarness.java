package com.commerce.test;

import com.commerce.business.BusinessManager;
import com.commerce.dao.*;
import com.commerce.model.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class BuisnessHarness {

    public static void main(String[] args) throws SQLException {
        BusinessManager bm = new BusinessManager(new DataProvider());

        System.out.println("=== STARTING DATA LIFECYCLE TESTS ===\n");

        // 1. Category & Product
        Category cat = new Category(0, "Electronics", "Gadgets and gear");
        cat = bm.saveCategory(cat);
        int catId = cat.getCategoryId();
        System.out.println("Created Category: " + bm.getCategoryById(catId).getName());

        Product prod = new Product(0, "Smartphone", "Latest model", new BigDecimal(999.99), catId);
        prod = bm.saveProduct(prod);
        int prodId = prod.getProductId();
        System.out.println("Created Product: " + bm.getProductById(prodId).getName());

        // 2. Customer
        Customer cust = new Customer(0, "John", "Doe", "john@doe.com", "555-1234", "101 Main St");
        cust = bm.saveCustomer(cust);
        int custId = cust.getCustomerId();
        System.out.println("Created Customer: " + bm.getCustomerById(custId).getFirstName());

        // 3. Order & OrderItem
        Order ord = new Order(0, custId, "2023-10-27", new BigDecimal(1050.00), "Pending");
        ord = bm.saveOrder(ord);
        int ordId = ord.getOrderId();
        System.out.println("Created Order ID: " + bm.getOrderById(ordId).getOrderId());

        OrderItem item = new OrderItem(0, ordId, prodId, 1, new BigDecimal(999.99));
        item = bm.saveOrderItem(item);
        int itemId = item.getOrderItemId();
        System.out.println("Created OrderItem for Product ID: " + bm.getOrderItemById(itemId).getProductId());

        // 4. Payment & Shipment
        Payment pay = new Payment(0, ordId, "2023-10-27", new BigDecimal(1050.00), "Credit Card");
        pay = bm.savePayment(pay);
        int payId = pay.getPaymentId();
        System.out.println("Created Payment for Amount: $" + bm.getPaymentById(payId).getAmount());

        Shipment ship = new Shipment(0, ordId, "101 Main St", "Processing");
        ship = bm.saveShipment(ship);
        int shipId = ship.getShipmentId();
        System.out.println("Created Shipment Status: " + bm.getShipmentById(shipId).getStatus());

        // --- UPDATE TEST ---
        System.out.println("\n--- PERFORMING UPDATES ---");
        cust.setLastName("Smith");
        bm.saveCustomer(cust); // ID is now 50, so this calls updateCustomer
        System.out.println("Updated Customer: " + bm.getCustomerById(custId).getFirstName() + " " + bm.getCustomerById(custId).getLastName());

        cat.setDescription("High-end gadgets and hardware");
        bm.saveCategory(cat);
        System.out.println("Update Category: " + bm.getCategoryById(catId).getDescription());

        prod.setPrice(new BigDecimal("1150.99"));
        bm.saveProduct(prod);
        System.out.println("Update Product: $" + bm.getProductById(prodId).getPrice());

        ord.setStatus("Shipped");
        bm.saveOrder(ord);
        System.out.println("Update Order: " + bm.getOrderById(ordId).getStatus());

        item.setQuantity(2);
        bm.saveOrderItem(item);
        System.out.println("Update OrderItem Quantity: " + bm.getOrderItemById(itemId).getQuantity());
        
        pay.setPaymentMethod("PayPal");
        bm.savePayment(pay);
        System.out.println("Update Payment Method: " + bm.getPaymentById(payId).getPaymentMethod());

        ship.setStatus("Delivered");
        bm.saveShipment(ship);
        System.out.println("Update Shipment Status: " + bm.getShipmentById(shipId).getStatus());
        
        // --- DELETE TEST ---
        System.out.println("\n--- PERFORMING DELETIONS ---");
        bm.deleteShipment(shipId);
        System.out.println("Shipment deleted. List size: " + bm.getAllShipments().size());
        
        bm.deletePayment(payId);
        System.out.println("Payment deleted. Remaining: " + bm.getAllPayments().size());

        // 3. ORDER ITEM
        bm.deleteOrderItem(itemId);
        System.out.println("OrderItem deleted. Remaining: " + bm.getAllOrderItems().size());

        // 4. PRODUCT
        bm.deleteProduct(prodId);
        System.out.println("Product deleted. Remaining: " + bm.getAllProducts().size());

        // 5. ORDER
        bm.deleteOrder(ordId);
        System.out.println("Order deleted. Remaining: " + bm.getAllOrders().size());

        // 6. CATEGORY
        bm.deleteCategory(catId);
        System.out.println("Category deleted. Remaining: " + bm.getAllCategories().size());

        // 7. CUSTOMER
        bm.deleteCustomer(custId);
        System.out.println("Customer deleted. Remaining: " + bm.getAllCustomers().size());

        System.out.println("\n=== TESTS COMPLETE ===");

    }


}