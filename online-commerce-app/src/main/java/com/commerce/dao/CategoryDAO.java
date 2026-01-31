package com.commerce.dao;

import com.commerce.model.Category;
import com.commerce.util.DatabaseUtil;

import java.sql.*;

public class CategoryDAO {

    public void create(Category c) throws SQLException {
        String sql = "INSERT INTO Categories (name, description) VALUES (?,?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.executeUpdate();
        }
    }

    public Category read(int id) throws SQLException {
        String sql = "SELECT * FROM Categories WHERE category_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Category c = new Category();
                    c.setCategoryId(rs.getInt("category_id"));
                    c.setName(rs.getString("name"));
                    c.setDescription(rs.getString("description"));
                    return c;
                }
            }
        }
        return null;
    }

    public void update(Category c) throws SQLException {
        String sql = "UPDATE Categories SET name=?, description=? WHERE category_id=?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.setInt(3, c.getCategoryId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Categories WHERE category_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}