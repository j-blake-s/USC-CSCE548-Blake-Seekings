package com.commerce.dao;

import com.commerce.model.Shipment;
import com.commerce.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDAO {

    // CREATE
    public void create(Shipment s) throws SQLException {
        String sql = "INSERT INTO Shipments (order_id, address, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, s.getOrderId());
            ps.setString(2, s.getAddress());
            ps.setString(3, s.getStatus());
            ps.executeUpdate();
        }
    }

    // READ (Single by ID)
    public Shipment read(int shipmentId) throws SQLException {
        String sql = "SELECT * FROM Shipments WHERE shipment_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shipmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapShipment(rs);
                }
            }
        }
        return null;
    }

    public List<Shipment> readAll() throws SQLException {
        String sql = "SELECT * FROM Shipments";
        List<Shipment> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Shipment s = new Shipment();
                s.setShipmentId(rs.getInt("shipmentId"));
                s.setOrderId(rs.getInt("orderId"));
                s.setAddress(rs.getString("address"));
                s.setStatus(rs.getString("status"));
                list.add(s);
            }
        }
        return list;
    }

    // READ (By Order ID - Common for Customer Dashboards)
    public Shipment readByOrderId(int orderId) throws SQLException {
        String sql = "SELECT * FROM Shipments WHERE order_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapShipment(rs);
                }
            }
        }
        return null;
    }

    // UPDATE
    public void update(Shipment s) throws SQLException {
        String sql = "UPDATE Shipments SET address = ?, status = ? WHERE shipment_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getAddress());
            ps.setString(2, s.getStatus());
            ps.setInt(3, s.getShipmentId());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void delete(int shipmentId) throws SQLException {
        String sql = "DELETE FROM Shipments WHERE shipment_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shipmentId);
            ps.executeUpdate();
        }
    }

    private Shipment mapShipment(ResultSet rs) throws SQLException {
        Shipment s = new Shipment();
        s.setShipmentId(rs.getInt("shipment_id"));
        s.setOrderId(rs.getInt("order_id"));
        s.setAddress(rs.getString("address"));
        s.setStatus(rs.getString("status"));
        return s;
    }
}