package com.commerce;

import com.commerce.dao.*;
import com.commerce.model.*;
import com.commerce.util.DatabaseUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
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