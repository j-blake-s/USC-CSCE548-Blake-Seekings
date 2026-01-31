package com.commerce.dao;

import com.commerce.model.Product;
import com.commerce.util.DatabaseUtil;

import java.sql.*;

public class ProductDAO {
    public void create(Product p) throws SQLException {
        String sql = "INSERT INTO Products (name, description, price, category_id) VALUES (?,?,?,?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setBigDecimal(3, p.getPrice());
            ps.setInt(4, p.getCategoryId());
            ps.executeUpdate();
        }
    }

    public Product read(int id) throws SQLException {
        String sql = "SELECT * FROM Products WHERE product_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getInt("product_id"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setCategoryId(rs.getInt("category_id"));
                    return p;
                }
            }
        }
        return null;
    }

    public void update(Product p) throws SQLException {
        String sql = "UPDATE Products SET name=?, description=?, price=?, category_id=? WHERE product_id=?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setBigDecimal(3, p.getPrice());
            ps.setInt(4, p.getCategoryId());
            ps.setInt(5, p.getProductId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Products WHERE product_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}