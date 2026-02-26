package com.commerce.dao;

import com.commerce.model.Payment;
import com.commerce.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    // CREATE
    public Payment create(Payment p) throws SQLException {
        String sql = "INSERT INTO Payments (order_id, payment_date, amount, payment_method) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getOrderId());
            ps.setString(2, p.getPaymentDate());
            ps.setBigDecimal(3, p.getAmount());
            ps.setString(4, p.getPaymentMethod());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                p.setPaymentId(keys.getInt(1));
            }
            
            return p;
        }
    }

    // READ (Single by ID)
    public Payment read(int paymentId) throws SQLException {
        String sql = "SELECT * FROM Payments WHERE payment_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPayment(rs);
                }
            }
        }
        return null;
    }

    public List<Payment> readAll() throws SQLException {
        String sql = "SELECT * FROM Payments";
        List<Payment> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Payment p = new Payment();
                p.setPaymentId(rs.getInt("payment_id"));
                p.setOrderId(rs.getInt("order_id"));
                p.setPaymentDate(rs.getString("payment_date"));
                p.setAmount(rs.getBigDecimal("amount"));
                p.setPaymentMethod(rs.getString("payment_method"));
                list.add(p);
            }
        }
        return list;
    }

    // READ (All payments for a specific Order)
    public List<Payment> readByOrderId(int orderId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments WHERE order_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapPayment(rs));
                }
            }
        }
        return payments;
    }

    // UPDATE
    public void update(Payment p) throws SQLException {
        String sql = "UPDATE Payments SET payment_date =?, amount = ?, payment_method = ? WHERE payment_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getPaymentDate());
            ps.setBigDecimal(2, p.getAmount());
            ps.setString(3, p.getPaymentMethod());
            ps.setInt(4, p.getPaymentId());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void delete(int paymentId) throws SQLException {
        String sql = "DELETE FROM Payments WHERE payment_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            ps.executeUpdate();
        }
    }

    private Payment mapPayment(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));
        p.setOrderId(rs.getInt("order_id"));
        p.setAmount(rs.getBigDecimal("amount"));
        p.setPaymentMethod(rs.getString("payment_method"));
        p.setPaymentDate(rs.getString("payment_date"));
        return p;
    }
}