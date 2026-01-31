package com.commerce.dao;

import com.commerce.model.Order;
import com.commerce.util.DatabaseUtil;

import java.sql.*;

public class OrderDAO {
    public int create(Order o) throws SQLException {
        String sql = "INSERT INTO Orders (customer_id, total_amount, status) VALUES (?,?,?)";
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, o.getCustomerId());
            ps.setBigDecimal(2, o.getTotalAmount());
            ps.setString(3, o.getStatus());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    public Order read(int id) throws SQLException {
        String sql = "SELECT * FROM Orders WHERE order_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order o = new Order();
                    o.setOrderId(rs.getInt("order_id"));
                    o.setCustomerId(rs.getInt("customer_id"));
                    o.setTotalAmount(rs.getBigDecimal("total_amount"));
                    o.setStatus(rs.getString("status"));
                    return o;
                }
            }
        }
        return null;
    }

    public void update(Order o) throws SQLException {
        String sql = "UPDATE Orders SET status=?, total_amount=? WHERE order_id=?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getStatus());
            ps.setBigDecimal(2, o.getTotalAmount());
            ps.setInt(3, o.getOrderId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Orders WHERE order_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}