package com.commerce.dao;

public class DataProvider {
    
    public CustomerDAO customer;
    public CategoryDAO category;
    public OrderDAO order;
    public OrderItemDAO orderItem;
    public PaymentDAO payment;
    public ProductDAO product;
    public ShipmentDAO shipment;

    public DataProvider() {
        customer = new CustomerDAO();
        category = new CategoryDAO();
        order = new OrderDAO();
        orderItem = new OrderItemDAO();
        payment = new PaymentDAO();
        product = new ProductDAO();
        shipment = new ShipmentDAO();
    }
}
