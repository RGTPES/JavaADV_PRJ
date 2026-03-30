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
        return orderDAO.findByStatus(status);
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

        String currentStatus = currentOrder.getStatus();

        if (!isValidTransition(currentStatus, newStatus)) {
            return false;
        }

        return orderDAO.updateStatus(orderId, newStatus);
    }

    private boolean isValidStatus(String status) {
        return "PENDING".equalsIgnoreCase(status)
                || "SHIPPING".equalsIgnoreCase(status)
                || "DELIVERED".equalsIgnoreCase(status)
                || "CANCELLED".equalsIgnoreCase(status);
    }

    private boolean isValidTransition(String currentStatus, String newStatus) {
        currentStatus = currentStatus.toUpperCase();
        newStatus = newStatus.toUpperCase();

        if (currentStatus.equals(newStatus)) {
            return true;
        }

        switch (currentStatus) {
            case "PENDING":
                return newStatus.equals("SHIPPING") || newStatus.equals("CANCELLED");
            case "SHIPPING":
                return newStatus.equals("DELIVERED");
            case "DELIVERED":
            case "CANCELLED":
                return false;
            default:
                return false;
        }
    }
}