package service.service;

import model.Order;
import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    List<Order> getOrdersByStatus(String status);
    List<Order> getOrdersByUserId(int userId);
    boolean updateOrderStatus(int orderId, String newStatus);
}