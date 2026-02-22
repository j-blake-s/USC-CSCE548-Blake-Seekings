package com.commerce.dao;

import com.commerce.model.OrderItem;
import com.commerce.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {
    public void create(OrderItem item) throws SQLException {
        String sql = "INSERT INTO Order_Items (order_id, product_id, quantity, unit_price) VALUES (?,?,?,?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getUnitPrice());
            ps.executeUpdate();
        }
    }


    public List<OrderItem> readAll() throws SQLException {
        String sql = "SELECT * FROM OrderItems";
        List<OrderItem> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                OrderItem oi = new OrderItem();
                oi.setOrderItemId(rs.getInt("orderItemId"));
                oi.setOrderId(rs.getInt("orderId"));
                oi.setProductId(rs.getInt("productId"));
                oi.setQuantity(rs.getInt("quantity"));
                oi.setUnitPrice(rs.getBigDecimal("unitPrice"));
                list.add(oi);
            }
        }
        return list;
    }


    // READ (Single item by its unique ID)
    public OrderItem read(int orderItemId) throws SQLException {
        String sql = "SELECT * FROM Order_Items WHERE order_item_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderItemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapOrderItem(rs);
                }
            }
        }
        return null;
    }

    // READ (All items for a specific Order - Most Common Use Case)
    public List<OrderItem> readByOrderId(int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM Order_Items WHERE order_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapOrderItem(rs));
                }
            }
        }
        return items;
    }

    // UPDATE
    public void update(OrderItem item) throws SQLException {
        String sql = "UPDATE Order_Items SET quantity = ?, unit_price = ? WHERE order_item_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getQuantity());
            ps.setBigDecimal(2, item.getUnitPrice());
            ps.setInt(3, item.getOrderItemId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Order_Items WHERE order_item_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private OrderItem mapOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        return item;
    }
    
}