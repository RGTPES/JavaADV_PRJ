package dao.dao;

import model.Order;

import java.util.List;
public interface OrderDAO {
    List<Order> findAll();
    List<Order> findByStatus(String status);
    Order findById(int orderId);
    boolean updateStatus(int orderId, String status);
}