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
import java.util.List;

public class CartDaoImpl implements CartDao {
    private final List<CartItem> cart = new ArrayList<>();
    @Override
    public boolean addToCart(Product p, int quantity) {
        if (p == null || quantity <= 0) return false;

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
    public boolean cartIsEmpty() {
        return cart.isEmpty();
    }
    @Override
    public List<CartItem> viewCart() {
        return cart;
    }
    @Override
    public void checkout(int userId) {
        if (cart.isEmpty()) {
            System.out.println("gio hang rong!");
            return;
        }
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            // 1. tính tổng tiền
            double total = 0;
            for (CartItem item : cart) {
                total += item.getSubtotal();
            }

            // 2. insert order
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

            // 3. insert order_detail
            for (CartItem item : cart) {
                // insert detail
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
