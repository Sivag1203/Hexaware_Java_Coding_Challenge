package org.example.dao;

import org.example.entity.Clothing;
import org.example.entity.Electronics;
import org.example.entity.Product;
import org.example.entity.User;
import org.example.exception.OrderNotFoundException;
import org.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderProcessor implements IOrderManagementRepository {

    @Override
    public void createUser(User user) {
        try {
            Connection conn = DBUtil.getConnection();
            String query = "INSERT INTO users VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, user.getUserId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.executeUpdate();
            conn.close();
        } catch (Exception e) {
            System.out.println("Error in createUser: " + e.getMessage());
        }
    }

    @Override
    public void createProduct(User user, Product product) {
        try {
            Connection conn = DBUtil.getConnection();

            // Check if user is admin
            String checkQuery = "SELECT role FROM users WHERE userId = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, user.getUserId());
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next() || !rs.getString("role").equalsIgnoreCase("Admin")) {
                System.out.println("Only Admin can add products.");
                conn.close();
                return;
            }

            String query = "INSERT INTO products VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, product.getProductId());
            stmt.setString(2, product.getProductName());
            stmt.setString(3, product.getDescription());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getQuantityInStock());
            stmt.setString(6, product.getType());

            if (product.getType().equalsIgnoreCase("Electronics")) {
                Electronics e = (Electronics) product;
                stmt.setString(7, e.getBrand());
                stmt.setInt(8, e.getWarrantyPeriod());
                stmt.setString(9, null);
                stmt.setString(10, null);
            } else if (product.getType().equalsIgnoreCase("Clothing")) {
                Clothing c = (Clothing) product;
                stmt.setString(7, null);
                stmt.setInt(8, 0);
                stmt.setString(9, c.getSize());
                stmt.setString(10, c.getColor());
            } else {
                stmt.setString(7, null);
                stmt.setInt(8, 0);
                stmt.setString(9, null);
                stmt.setString(10, null);
            }

            stmt.executeUpdate();
            conn.close();
        } catch (Exception e) {
            System.out.println("Error in createProduct: " + e.getMessage());
        }
    }

    @Override
    public void createOrder(User user, List<Product> products) {
        try {
            Connection conn = DBUtil.getConnection();

            // Check if user exists
            String checkUser = "SELECT * FROM users WHERE userId = ?";
            PreparedStatement userStmt = conn.prepareStatement(checkUser);
            userStmt.setInt(1, user.getUserId());
            ResultSet rs = userStmt.executeQuery();

            if (!rs.next()) {
                createUser(user); // add user if not exists
            }

            // Insert order
            String orderQuery = "INSERT INTO orders(userId) VALUES (?)";
            PreparedStatement orderStmt = conn.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, user.getUserId());
            orderStmt.executeUpdate();

            ResultSet generated = orderStmt.getGeneratedKeys();
            generated.next();
            int orderId = generated.getInt(1);

            // Insert order items
            for (Product p : products) {
                String itemQuery = "INSERT INTO order_items(orderId, productId, quantity) VALUES (?, ?, ?)";
                PreparedStatement itemStmt = conn.prepareStatement(itemQuery);
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, p.getProductId());
                itemStmt.setInt(3, 1);
                itemStmt.executeUpdate();
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("Error in createOrder: " + e.getMessage());
        }
    }

    @Override
    public void cancelOrder(int userId, int orderId) {
        try {
            Connection conn = DBUtil.getConnection();

            String checkQuery = "SELECT * FROM orders WHERE orderId = ? AND userId = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, orderId);
            checkStmt.setInt(2, userId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                throw new OrderNotFoundException("Order not found.");
            }

            String deleteItems = "DELETE FROM order_items WHERE orderId = ?";
            PreparedStatement delItemStmt = conn.prepareStatement(deleteItems);
            delItemStmt.setInt(1, orderId);
            delItemStmt.executeUpdate();

            String deleteOrder = "DELETE FROM orders WHERE orderId = ?";
            PreparedStatement delOrderStmt = conn.prepareStatement(deleteOrder);
            delOrderStmt.setInt(1, orderId);
            delOrderStmt.executeUpdate();

            conn.close();
        } catch (OrderNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error in cancelOrder: " + e.getMessage());
        }
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        try {
            Connection conn = DBUtil.getConnection();
            String query = "SELECT * FROM products";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("productId"),
                        rs.getString("productName"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantityInStock"),
                        rs.getString("type")
                );
                list.add(p);
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("Error in getAllProducts: " + e.getMessage());
        }

        return list;
    }

    @Override
    public List<Product> getOrderByUser(User user) {
        List<Product> list = new ArrayList<>();
        try {
            Connection conn = DBUtil.getConnection();
            String query = "SELECT p.* FROM products p " +
                    "JOIN order_items oi ON p.productId = oi.productId " +
                    "JOIN orders o ON oi.orderId = o.orderId " +
                    "WHERE o.userId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, user.getUserId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("productId"),
                        rs.getString("productName"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantityInStock"),
                        rs.getString("type")
                );
                list.add(p);
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("Error in getOrderByUser: " + e.getMessage());
        }

        return list;
    }
}
