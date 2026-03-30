package service.impl;

import dao.dao.OrderDAO;
import dao.impl.OrderDAOImpl;
import model.Order;
import service.service.OrderService;

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
    public boolean updateOrderStatus(int orderId, String newStatus) {
        if (orderId <= 0 || !isValidStatus(newStatus)) {
            return false;
        }

        Order currentOrder = orderDAO.findById(orderId);
        if (currentOrder == null) {
            return false;
        }

        String currentStatus = currentOrder.getStatus().trim().toUpperCase();
        String nextStatus = newStatus.trim().toUpperCase();

        if (!isValidTransition(currentStatus, nextStatus)) {
            return false;
        }

        return orderDAO.updateStatus(orderId, nextStatus);
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