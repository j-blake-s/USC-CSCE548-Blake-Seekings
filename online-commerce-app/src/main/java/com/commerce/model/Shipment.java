package com.commerce.model;

public class Shipment {
    private int shipmentId;
    private int orderId;
    private String address;
    private String status;

    public Shipment() {}

    public int getShipmentId() { return shipmentId; }
    public void setShipmentId(int shipmentId) { this.shipmentId = shipmentId; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}