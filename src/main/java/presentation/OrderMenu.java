package presentation;

import model.Order;
import service.impl.OrderServiceImpl;
import util.InputUtil;

import java.util.List;

public class OrderMenu {
    private final OrderServiceImpl orderService;

    public OrderMenu() {
        orderService = new OrderServiceImpl();
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n========== Quan ly don hang ==========");
            System.out.println("1. Hien thi tat ca don hang");
            System.out.println("2. Tim don hang theo trang thai");
            System.out.println("3. Cap nhat trang thai don hang");
            System.out.println("0. Quay lai");

            choice = inputInt("Chon chuc nang: ");

            switch (choice) {
                case 1:
                    showAllOrders();
                    break;
                case 2:
                    showOrdersByStatus();
                    break;
                case 3:
                    updateOrderStatus();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        } while (choice != 0);
    }

    private void showAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        showOrderList(orders);
    }

    private void showOrdersByStatus() {
        String status = InputUtil.inputString("Nhap trang thai (PENDING/SHIPPING/DELIVERED/CANCELLED): ")
                .trim()
                .toUpperCase();

        List<Order> orders = orderService.getOrdersByStatus(status);
        showOrderList(orders);
    }

    private void updateOrderStatus() {
        int orderId = inputInt("Nhap ma don hang: ");
        String newStatus = InputUtil.inputString("Nhap trang thai moi (PENDING/SHIPPING/DELIVERED/CANCELLED): ")
                .trim()
                .toUpperCase();

        boolean success = orderService.updateOrderStatus(orderId, newStatus);
        if (success) {
            System.out.println("Cap nhat trang thai don hang thanh cong.");
        } else {
            System.out.println("Cap nhat that bai. Kiem tra ma don hang hoac quy tac chuyen trang thai.");
        }
    }

    public void showMyOrders(int userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        showOrderList(orders);
    }

    private void showOrderList(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            System.out.println("Khong co don hang nao.");
            return;
        }

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.printf("%-10s %-10s %-15s %-15s %-25s%n",
                "Order ID", "User ID", "Total", "Status", "Created At");
        System.out.println("--------------------------------------------------------------------------------");

        for (Order order : orders) {
            System.out.printf("%-10d %-10d %-15.2f %-15s %-25s%n",
                    order.getOrderId(),
                    order.getUserId(),
                    order.getTotalAmount(),
                    order.getStatus(),
                    order.getCreatedAt());
        }

        System.out.println("--------------------------------------------------------------------------------");
    }

    private int inputInt(String message) {
        while (true) {
            try {
                return Integer.parseInt(InputUtil.inputString(message).trim());
            } catch (Exception e) {
                System.out.println("Vui long nhap so hop le.");
            }
        }
    }
}