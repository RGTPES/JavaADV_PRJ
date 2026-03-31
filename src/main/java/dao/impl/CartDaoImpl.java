package dao.impl;

import dao.dao.CartDao;
import model.CartItem;
import model.Product;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartDaoImpl implements CartDao {

    private static final Map<Integer, List<CartItem>> userCarts = new HashMap<>();

    private List<CartItem> getUserCart(int userId) {
        return userCarts.computeIfAbsent(userId, k -> new ArrayList<>());
    }

    @Override
    public boolean addToCart(int userId, Product p, int quantity) {
        if (p == null || quantity <= 0) return false;

        List<CartItem> cart = getUserCart(userId);

        for (CartItem item : cart) {
            if (item.getProductId() == p.getProductId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return true;
            }
        }

        CartItem newItem = new CartItem(
                p.getProductId(),
                p.getProductName(),
                p.getPrice(),
                quantity
        );
        cart.add(newItem);
        return true;
    }

    @Override
    public boolean cartIsEmpty(int userId) {
        return getUserCart(userId).isEmpty();
    }

    @Override
    public List<CartItem> viewCart(int userId) {
        List<CartItem> cart = getUserCart(userId);

        if (cart.isEmpty()) {
            System.out.println("Gio hang dang rong!");
            return cart;
        }

        double total = 0;
        System.out.println("\n===== GIO HANG =====");
        for (CartItem item : cart) {
            System.out.println("ID: " + item.getProductId()
                    + " | Ten: " + item.getProductName()
                    + " | Gia: " + item.getPrice()
                    + " | So luong: " + item.getQuantity()
                    + " | Thanh tien: " + item.getSubtotal());
            total += item.getSubtotal();
        }
        System.out.println("Tong tien: " + total);

        return cart;
    }

    @Override
    public void checkout(int userId) {
        List<CartItem> cart = getUserCart(userId);

        if (cart.isEmpty()) {
            System.out.println("gio hang rong!");
            return;
        }

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            double total = 0;
            for (CartItem item : cart) {
                total += item.getSubtotal();
            }

            String sqlOrder = "insert into orders(user_id, total_amount, status) values (?, ?, 'PENDING')";
            PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, userId);
            psOrder.setDouble(2, total);
            psOrder.executeUpdate();

            ResultSet rs = psOrder.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            for (CartItem item : cart) {
                String sqlDetail = "insert into order_details(order_id, product_id, quantity, price) values (?, ?, ?, ?)";
                PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                psDetail.setInt(1, orderId);
                psDetail.setInt(2, item.getProductId());
                psDetail.setInt(3, item.getQuantity());
                psDetail.setDouble(4, item.getPrice());
                psDetail.executeUpdate();
            }

            conn.commit();
            cart.clear();
            System.out.println("gui don hang thanh cong, cho admin duyet");
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}