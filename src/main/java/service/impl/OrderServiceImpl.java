package service.impl;

import dao.dao.OrderDAO;
import dao.impl.OrderDAOImpl;
import model.Order;
import service.service.OrderService;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        if (!isValidStatus(status)) {
            return List.of();
        }
        return orderDAO.findByStatus(status.trim().toUpperCase());
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        if (userId <= 0) {
            return List.of();
        }
        return orderDAO.findByUserId(userId);
    }

    @Override
    public boolean updateOrderStatus(int orderId, String newStatus) {
        if (orderId <= 0 || !isValidStatus(newStatus)) {
            return false;
        }

        String nextStatus = newStatus.trim().toUpperCase();
        Order currentOrder = orderDAO.findById(orderId);

        if (currentOrder == null) {
            return false;
        }

        String currentStatus = currentOrder.getStatus().trim().toUpperCase();

        if (!isValidTransition(currentStatus, nextStatus)) {
            return false;
        }

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            if (currentStatus.equals("PENDING") && nextStatus.equals("SHIPPING")) {
                String sqlDetail = "select product_id, quantity from order_details where order_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlDetail)) {
                    ps.setInt(1, orderId);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int productId = rs.getInt("product_id");
                            int quantity = rs.getInt("quantity");

                            String checkStockSql = "select stock from products where product_id = ? and status = 'ACTIVE'";
                            try (PreparedStatement psCheck = conn.prepareStatement(checkStockSql)) {
                                psCheck.setInt(1, productId);

                                try (ResultSet rsStock = psCheck.executeQuery()) {
                                    if (!rsStock.next()) {
                                        conn.rollback();
                                        return false;
                                    }

                                    int stock = rsStock.getInt("stock");
                                    if (stock < quantity) {
                                        conn.rollback();
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement("select product_id, quantity from order_details where order_id = ?")) {
                    ps.setInt(1, orderId);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int productId = rs.getInt("product_id");
                            int quantity = rs.getInt("quantity");

                            String decreaseSql = "update products set stock = stock - ? where product_id = ? and stock >= ?";
                            try (PreparedStatement psUpdate = conn.prepareStatement(decreaseSql)) {
                                psUpdate.setInt(1, quantity);
                                psUpdate.setInt(2, productId);
                                psUpdate.setInt(3, quantity);

                                if (psUpdate.executeUpdate() == 0) {
                                    conn.rollback();
                                    return false;
                                }
                            }
                        }
                    }
                }
            }

            if (currentStatus.equals("SHIPPING") && nextStatus.equals("CANCELLED")) {
                try (PreparedStatement ps = conn.prepareStatement("select product_id, quantity from order_details where order_id = ?")) {
                    ps.setInt(1, orderId);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int productId = rs.getInt("product_id");
                            int quantity = rs.getInt("quantity");

                            String increaseSql = "update products set stock = stock + ? where product_id = ?";
                            try (PreparedStatement psUpdate = conn.prepareStatement(increaseSql)) {
                                psUpdate.setInt(1, quantity);
                                psUpdate.setInt(2, productId);

                                if (psUpdate.executeUpdate() == 0) {
                                    conn.rollback();
                                    return false;
                                }
                            }
                        }
                    }
                }
            }

            String updateStatusSql = "update orders set status = ? where order_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateStatusSql)) {
                ps.setString(1, nextStatus);
                ps.setInt(2, orderId);

                if (ps.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isValidStatus(String status) {
        if (status == null) {
            return false;
        }

        String s = status.trim().toUpperCase();
        return s.equals("PENDING")
                || s.equals("SHIPPING")
                || s.equals("DELIVERED")
                || s.equals("CANCELLED");
    }

    private boolean isValidTransition(String currentStatus, String newStatus) {
        if (currentStatus.equals(newStatus)) {
            return true;
        }

        switch (currentStatus) {
            case "PENDING":
                return newStatus.equals("SHIPPING") || newStatus.equals("CANCELLED");
            case "SHIPPING":
                return newStatus.equals("DELIVERED") || newStatus.equals("CANCELLED");
            case "DELIVERED":
            case "CANCELLED":
                return false;
            default:
                return false;
        }
    }
}