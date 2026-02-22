package com.commerce.test;

import com.commerce.business.BusinessManager;
import com.commerce.dao.*;
import com.commerce.model.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class Testing {

    public static void main(String[] args) throws SQLException {
    //    DatabaseHarness();
        BusinessHarness();
    }

    public static void BusinessHarness() throws SQLException {
        BusinessManager bm = new BusinessManager(new DataProvider());

        System.out.println("=== STARTING DATA LIFECYCLE TESTS ===\n");

        // 1. Category & Product
        Category cat = new Category(0, "Electronics", "Gadgets and gear");
        bm.saveCategory(cat);
        int catId = bm.getAllCategories().size();
        cat.setCategoryId(catId); 
        System.out.println("Created Category: " + bm.getCategoryById(catId).getName());

        Product prod = new Product(0, "Smartphone", "Latest model", new BigDecimal(999.99), catId);
        bm.saveProduct(prod);
        int prodId = bm.getAllProducts().size();
        prod.setProductId(prodId);
        System.out.println("Created Product: " + bm.getProductById(prodId).getName());

        // 2. Customer
        Customer cust = new Customer(0, "John", "Doe", "john@doe.com", "555-1234", "101 Main St");
        bm.saveCustomer(cust);
        int custId = bm.getAllCustomers().size();
        cust.setCustomerId(custId);
        System.out.println("Created Customer: " + bm.getCustomerById(custId).getFirstName());

        // 3. Order & OrderItem
        Order ord = new Order(0, custId, "2023-10-27", new BigDecimal(1050.00), "Pending");
        bm.saveOrder(ord);
        int ordId = bm.getAllOrders().size();
        ord.setOrderId(ordId);
        System.out.println("Created Order ID: " + bm.getOrderById(ordId).getOrderId());

        OrderItem item = new OrderItem(0, ordId, prodId, 1, new BigDecimal(999.99));
        bm.saveOrderItem(item);
        int itemId = bm.getAllOrderItems().size();
        item.setOrderItemId(itemId);
        System.out.println("Created OrderItem for Product ID: " + bm.getOrderItemById(itemId).getProductId());

        // 4. Payment & Shipment
        Payment pay = new Payment(0, ordId, "2023-10-27", new BigDecimal(1050.00), "Credit Card");
        bm.savePayment(pay);
        int payId = bm.getAllPayments().size();
        pay.setPaymentId(payId);
        System.out.println("Created Payment for Amount: $" + bm.getPaymentById(payId).getAmount());

        Shipment ship = new Shipment(0, ordId, "101 Main St", "Processing");
        bm.saveShipment(ship);
        int shipId = bm.getAllShipments().size();
        ship.setShipmentId(shipId);
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


    public static void DatabaseHarness() {
        System.out.println("=== Starting Full E-Commerce Data Layer Test ===");
        try {
            // Initalize DataProvider
            DataProvider dp = new DataProvider();

            // --- 1. THE CREATE TESTS ---
            Customer user = new Customer();
            user.setFirstName("Alan");
            user.setLastName("Turing");
            user.setEmail("alan.turing@enigma.com");
            user.setAddress("Bletchley Park");
            dp.customer.create(user);
            System.out.println("[PASS] Customer Created");

            Category electronics = new Category();
            electronics.setName("Peripherals");
            electronics.setDescription("Keyboards and Mice");
            dp.category.create(electronics);
            System.out.println("[PASS] Category Created");

            Product mouse = new Product();
            mouse.setName("Gaming Mouse");
            mouse.setDescription("12000 DPI Optical Mouse");
            mouse.setPrice(new BigDecimal("59.99"));
            mouse.setCategoryId(1); // Assuming ID 1 for test
            dp.product.create(mouse);
            System.out.println("[PASS] Product Created");

            Order order = new Order();
            order.setCustomerId(1); 
            order.setTotalAmount(new BigDecimal("59.99"));
            order.setStatus("Pending");
            int generatedOrderId = dp.order.create(order);
            System.out.println("[PASS] Order Created, ID: ");

            OrderItem item = new OrderItem();
            item.setOrderId(generatedOrderId);
            item.setProductId(1);
            item.setQuantity(1);
            item.setUnitPrice(new BigDecimal("59.99"));
            dp.orderItem.create(item);
            System.out.println("[PASS] Order Item Created");

            Payment payment = new Payment();
            payment.setOrderId(generatedOrderId);
            payment.setAmount(new BigDecimal("59.99"));
            payment.setPaymentMethod("Credit Card");
            dp.payment.create(payment);
            System.out.println("[PASS] Payment Recorded");

            Shipment shipment = new Shipment();
            shipment.setOrderId(generatedOrderId);
            shipment.setAddress("Bletchley Park");
            shipment.setStatus("Processing");
            dp.shipment.create(shipment);
            System.out.println("[PASS] Shipment Created");

            // --- 2. THE READ/UPDATE TESTS ---
            Customer toUpdate = dp.customer.read(1);
            toUpdate.setPhone("123-456-7890");
            dp.customer.update(toUpdate);
            System.out.println("[PASS] Customer Read and Update successful");

            Category categoryUpdate = dp.category.read(1);
            categoryUpdate.setDescription("TVs and Laptops");
            dp.category.update(categoryUpdate);
            System.out.println("[PASS] Category Read and Update successful");

            Product product = dp.product.read(1);
            product.setPrice(new BigDecimal("149.99"));
            product.setDescription("1 DPI Mouse");
            dp.product.update(product);
            System.out.println("[PASS] Product Read and Update successful");

            Order orderU = dp.order.read(1);
            orderU.setStatus("Shipped");
            orderU.setTotalAmount(new BigDecimal("199.99"));
            dp.order.update(orderU);
            System.out.println("[PASS] Order Read and Update successful");

            OrderItem itemU = dp.orderItem.read(1);
            itemU.setQuantity(5);
            itemU.setUnitPrice(new BigDecimal("45.00"));
            dp.orderItem.update(itemU);
            System.out.println("[PASS] OrderItem Read and Update successful");

            Payment paymentU = dp.payment.read(1);
            paymentU.setPaymentMethod("PayPal");
            paymentU.setAmount(new BigDecimal("250.00"));
            dp.payment.update(paymentU);
            System.out.println("[PASS] Payment Read and Update successful");

            Shipment shipmentU = dp.shipment.read(1);
            shipmentU.setStatus("Delivered");
            shipmentU.setAddress("789 New Delivery St");
            dp.shipment.update(shipmentU);
            System.out.println("[PASS] Shipment Read and Update successful");


            // --- 3. THE DELETE TESTS (Reverse Order) ---
            
            // Delete Shipment
            dp.shipment.delete(1);
            System.out.println("[PASS] Shipment Deleted");

            // Delete Payment
            dp.payment.delete(1);
            System.out.println("[PASS] Payment Deleted");

            // Delete Order Item
            dp.orderItem.delete(1);
            System.out.println("[PASS] Order Item Deleted");

            // Delete Order
            dp.order.delete(1);
            System.out.println("[PASS] Order Deleted");

            // Delete Product
            dp.product.delete(1);
            System.out.println("[PASS] Product Deleted");

            // Delete Customer
            dp.customer.delete(1);
            System.out.println("[PASS] Customer Deleted");

            // Delete Category
            dp.category.delete(1);
            System.out.println("[PASS] Category Deleted");

            System.out.println("\n=== ALL CRUD TESTS PASSED ===");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}