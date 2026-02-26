package com.commerce.dao;

import com.commerce.model.Order;
import com.commerce.model.OrderItem;
import com.commerce.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public Order create(Order o) throws SQLException {
        String sql = "INSERT INTO Orders (customer_id, order_date, total_amount, status) VALUES (?,?,?,?)";
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, o.getCustomerId());
            ps.setString(2, o.getOrderDate());
            ps.setBigDecimal(3, o.getTotalAmount());
            ps.setString(4, o.getStatus());
            ps.executeUpdate();
            
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                o.setOrderId(keys.getInt(1));
            }
            
            return o;
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
                    o.setOrderDate(rs.getString("order_date"));
                    o.setTotalAmount(rs.getBigDecimal("total_amount"));
                    o.setStatus(rs.getString("status"));
                    return o;
                }
            }
        }
        return null;
    }

    public List<Order> readAll() throws SQLException {
        String sql = "SELECT * FROM Orders";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("order_id"));
                o.setCustomerId(rs.getInt("customer_id"));
                o.setOrderDate(rs.getString("order_date"));
                o.setTotalAmount(rs.getBigDecimal("total_amount"));
                o.setStatus(rs.getString("status"));
                list.add(o);
            }
        }
        return list;
    }

    public void update(Order o) throws SQLException {
        String sql = "UPDATE Orders SET order_date=?, status=?, total_amount=? WHERE order_id=?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getOrderDate());
            ps.setString(2, o.getStatus());
            ps.setBigDecimal(3, o.getTotalAmount());
            ps.setInt(4, o.getOrderId());
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

    public Order readWithItems(int id) throws SQLException {
        Order order = read(id); // Your existing method
        
        if (order != null) {
            String sql = "SELECT * FROM order_items WHERE order_id = ?";
            try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                        rs.getInt("order_item_id"),
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getBigDecimal("unit_price")
                    );
                    order.getItems().add(item);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return order;
    }
}