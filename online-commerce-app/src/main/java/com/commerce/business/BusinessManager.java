package com.commerce.business;

import java.sql.SQLException;
import java.util.List;

import com.commerce.dao.DataProvider;
import com.commerce.model.Customer;
import com.commerce.model.Category;
import com.commerce.model.Order;
import com.commerce.model.OrderItem;
import com.commerce.model.Payment;
import com.commerce.model.Product;
import com.commerce.model.Shipment;

public class BusinessManager {
    private DataProvider db = new DataProvider();

    public BusinessManager(DataProvider db_) {
        db = db_;
    }


    // CUSTOMER
    public Customer saveCustomer(Customer obj) throws SQLException {
        if (obj.getCustomerId() == 0) obj = db.customer.create(obj);
        else db.customer.update(obj);
        return obj;
    }
    
    public void deleteCustomer(int id) throws SQLException { db.customer.delete(id); }
    public Customer getCustomerById(int id) throws SQLException { return db.customer.read(id); }
    public List<Customer> getAllCustomers() throws SQLException { return db.customer.readAll(); }


    // CATEGORY
    public Category saveCategory(Category obj) throws SQLException  {
        if (obj.getCategoryId() == 0) obj = db.category.create(obj);
        else db.category.update(obj);
        return obj;
    }
    public void deleteCategory(int id) throws SQLException { db.category.delete(id); }
    public Category getCategoryById(int id) throws SQLException { return db.category.read(id); }
    public List<Category> getAllCategories() throws SQLException { return db.category.readAll(); }



    // ORDER
    public Order saveOrder(Order obj) throws SQLException  {
        if (obj.getOrderId() == 0) obj = db.order.create(obj);
        else db.order.update(obj);
        return obj;
    }
    public void deleteOrder(int id) throws SQLException{ db.order.delete(id); }
    public Order getOrderById(int id) throws SQLException { return db.order.read(id); }
    public List<Order> getAllOrders() throws SQLException { return db.order.readAll(); }


    // PRODUCT
    public Product saveProduct(Product obj) throws SQLException  {
        if (obj.getProductId() == 0) obj = db.product.create(obj);
        else db.product.update(obj);
        return obj;
    }
    public void deleteProduct(int id) throws SQLException { db.product.delete(id); }
    public Product getProductById(int id) throws SQLException { return db.product.read(id); }
    public List<Product> getAllProducts() throws SQLException { return db.product.readAll(); }

    // ORDER ITEM
    public OrderItem saveOrderItem(OrderItem obj) throws SQLException  {
        if (obj.getOrderItemId() == 0) obj = db.orderItem.create(obj);
        else db.orderItem.update(obj);
        return obj;
    }
    public void deleteOrderItem(int id) throws SQLException { db.orderItem.delete(id); }
    public OrderItem getOrderItemById(int id) throws SQLException { return db.orderItem.read(id); }
    public List<OrderItem> getAllOrderItems() throws SQLException { return db.orderItem.readAll(); }

    // PAYMENT
    public Payment savePayment(Payment obj) throws SQLException  {
        if (obj.getPaymentId() == 0) obj = db.payment.create(obj);
        else db.payment.update(obj);
        return obj;
    }
    public void deletePayment(int id) throws SQLException { db.payment.delete(id); }
    public Payment getPaymentById(int id) throws SQLException { return db.payment.read(id); }
    public List<Payment> getAllPayments() throws SQLException { return db.payment.readAll(); }


    // SHIPMENT
    public Shipment saveShipment(Shipment obj) throws SQLException {
        if (obj.getShipmentId() == 0) obj = db.shipment.create(obj);
        else db.shipment.update(obj);
        return obj;
    }
    public void deleteShipment(int id) throws SQLException { db.shipment.delete(id); }
    public Shipment getShipmentById(int id) throws SQLException { return db.shipment.read(id); }
    public List<Shipment> getAllShipments() throws SQLException { return db.shipment.readAll(); }



}
