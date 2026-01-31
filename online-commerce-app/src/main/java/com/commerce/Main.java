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

        try (Connection conn = DatabaseUtil.getConnection()) {
            // Disable auto-commit for transactional testing
            conn.setAutoCommit(false);

            try {

                // Initialize all DAOs
                CustomerDAO customerDAO = new CustomerDAO();
                CategoryDAO categoryDAO = new CategoryDAO();
                ProductDAO productDAO = new ProductDAO();
                OrderDAO orderDAO = new OrderDAO();
                OrderItemDAO itemDAO = new OrderItemDAO();
                PaymentDAO paymentDAO = new PaymentDAO();
                ShipmentDAO shipmentDAO = new ShipmentDAO();


                // --- 1. THE CREATE TESTS ---
                Customer user = new Customer();
                user.setFirstName("Alan");
                user.setLastName("Turing");
                user.setEmail("alan.turing@enigma.com");
                user.setAddress("Bletchley Park");
                customerDAO.create(user);
                System.out.println("[PASS] Customer Created");

                Category electronics = new Category();
                electronics.setName("Peripherals");
                electronics.setDescription("Keyboards and Mice");
                categoryDAO.create(electronics);
                System.out.println("[PASS] Category Created");

                Product mouse = new Product();
                mouse.setName("Gaming Mouse");
                mouse.setDescription("12000 DPI Optical Mouse");
                mouse.setPrice(new BigDecimal("59.99"));
                mouse.setCategoryId(1); // Assuming ID 1 for test
                productDAO.create(mouse);
                System.out.println("[PASS] Product Created");

                Order order = new Order();
                order.setCustomerId(1); 
                order.setTotalAmount(new BigDecimal("59.99"));
                order.setStatus("Pending");
                int generatedOrderId = orderDAO.create(order);
                System.out.println("[PASS] Order Created, ID: ");

                OrderItem item = new OrderItem();
                item.setOrderId(generatedOrderId);
                item.setProductId(1);
                item.setQuantity(1);
                item.setUnitPrice(new BigDecimal("59.99"));
                itemDAO.create(item);
                System.out.println("[PASS] Order Item Created");

                Payment payment = new Payment();
                payment.setOrderId(generatedOrderId);
                payment.setAmount(new BigDecimal("59.99"));
                payment.setPaymentMethod("Credit Card");
                paymentDAO.create(payment);
                System.out.println("[PASS] Payment Recorded");

                Shipment shipment = new Shipment();
                shipment.setOrderId(generatedOrderId);
                shipment.setAddress("Bletchley Park");
                shipment.setStatus("Processing");
                shipmentDAO.create(shipment);
                System.out.println("[PASS] Shipment Created");

                // --- 2. THE READ/UPDATE TESTS ---
                Customer toUpdate = customerDAO.read(1);
                toUpdate.setPhone("123-456-7890");
                customerDAO.update(toUpdate);
                System.out.println("[PASS] Customer Read and Update successful");

                Category categoryUpdate = categoryDAO.read(1);
                categoryUpdate.setDescription("TVs and Laptops");
                categoryDAO.update(categoryUpdate);
                System.out.println("[PASS] Category Read and Update successful");

                Product product = productDAO.read(1);
                product.setPrice(new BigDecimal("149.99"));
                product.setDescription("1 DPI Mouse");
                productDAO.update(product);
                System.out.println("[PASS] Product Read and Update successful");

                Order orderU = orderDAO.read(1);
                orderU.setStatus("Shipped");
                orderU.setTotalAmount(new BigDecimal("199.99"));
                orderDAO.update(orderU);
                System.out.println("[PASS] Order Read and Update successful");

                OrderItem itemU = itemDAO.read(1);
                itemU.setQuantity(5);
                itemU.setUnitPrice(new BigDecimal("45.00"));
                itemDAO.update(itemU);
                System.out.println("[PASS] OrderItem Read and Update successful");

                Payment paymentU = paymentDAO.read(1);
                paymentU.setPaymentMethod("PayPal");
                paymentU.setAmount(new BigDecimal("250.00"));
                paymentDAO.update(paymentU);
                System.out.println("[PASS] Payment Read and Update successful");

                Shipment shipmentU = shipmentDAO.read(1);
                shipmentU.setStatus("Delivered");
                shipmentU.setAddress("789 New Delivery St");
                shipmentDAO.update(shipmentU);
                System.out.println("[PASS] Shipment Read and Update successful");


                // --- 3. THE DELETE TESTS (Reverse Order) ---
                
                // Delete Shipment
                shipmentDAO.delete(1);
                System.out.println("[PASS] Shipment Deleted");

                // Delete Payment
                paymentDAO.delete(1);
                System.out.println("[PASS] Payment Deleted");

                // Delete Order Item
                itemDAO.delete(1);
                System.out.println("[PASS] Order Item Deleted");

                // Delete Order
                orderDAO.delete(1);
                System.out.println("[PASS] Order Deleted");

                // Delete Product
                productDAO.delete(1);
                System.out.println("[PASS] Product Deleted");

                // Delete Customer
                customerDAO.delete(1);
                System.out.println("[PASS] Customer Deleted");

                // Delete Category
                categoryDAO.delete(1);
                System.out.println("[PASS] Category Deleted");

                // Commit the entire transaction
                conn.commit();
                System.out.println("\n=== ALL CRUD TESTS PASSED ===");

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Test Failed. Transaction rolled back.");
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}