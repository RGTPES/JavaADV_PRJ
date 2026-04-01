package dao.impl;

import dao.dao.CartDao;
import model.CartItem;
import model.Coupon;
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
        if (p == null || quantity <= 0) {
            return false;
        }
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
            System.out.println(
                    "ID: " + item.getProductId()
                            + " | Ten: " + item.getProductName()
                            + " | Gia: " + item.getPrice()
                            + " | So luong: " + item.getQuantity()
                            + " | Thanh tien: " + item.getSubtotal()
            );
            total += item.getSubtotal();
        }

        System.out.println("Tong tien tam tinh: " + total);
        return cart;
    }

    @Override
    public boolean checkout(int userId, String couponCode) {
        List<CartItem> cart = getUserCart(userId);

        if (cart.isEmpty()) {
            System.out.println("Gio hang rong!");
            return false;
        }

        Connection conn = null;

        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            double subtotal = 0;

            for (CartItem item : cart) {
                String checkStockSql = "select stock from products where product_id = ?";

                try (PreparedStatement psCheck = conn.prepareStatement(checkStockSql)) {
                    psCheck.setInt(1, item.getProductId());

                    try (ResultSet rs = psCheck.executeQuery()) {
                        if (!rs.next()) {
                            System.out.println("San pham ID " + item.getProductId() + " khong ton tai.");
                            conn.rollback();
                            return false;
                        }

                        int stock = rs.getInt("stock");
                        if (stock < item.getQuantity()) {
                            System.out.println("San pham ID " + item.getProductId() + " khong du ton kho.");
                            conn.rollback();
                            return false;
                        }
                    }
                }

                subtotal += item.getSubtotal();
            }

            double discountAmount = 0;
            Coupon appliedCoupon = null;
            String normalizedCouponCode = null;

            if (couponCode != null && !couponCode.trim().isEmpty()) {
                String couponSql = """
                        select * from coupons
                        where code = ?
                          and status = 'ACTIVE'
                          and quantity > 0
                          and now() between start_time and end_time
                        """;

                try (PreparedStatement psCoupon = conn.prepareStatement(couponSql)) {
                    normalizedCouponCode = couponCode.trim().toUpperCase();
                    psCoupon.setString(1, normalizedCouponCode);

                    try (ResultSet rs = psCoupon.executeQuery()) {
                        if (!rs.next()) {
                            System.out.println("Coupon khong hop le hoac da het han.");
                            conn.rollback();
                            return false;
                        }

                        appliedCoupon = new Coupon();
                        appliedCoupon.setCouponId(rs.getInt("coupon_id"));
                        appliedCoupon.setCouponCode(rs.getString("code"));
                        appliedCoupon.setDiscountPercent(rs.getDouble("discount_percent"));
                        appliedCoupon.setQuantity(rs.getInt("quantity"));
                        appliedCoupon.setMinOrderAmount(rs.getDouble("min_order_amount"));
                        appliedCoupon.setStartDate(rs.getString("start_time"));
                        appliedCoupon.setEndDate(rs.getString("end_time"));
                        appliedCoupon.setStatus(rs.getString("status"));
                    }
                }

                if (subtotal < appliedCoupon.getMinOrderAmount()) {
                    System.out.println("Don hang chua du gia tri toi thieu de ap dung coupon.");
                    conn.rollback();
                    return false;
                }

                discountAmount = subtotal * appliedCoupon.getDiscountPercent() / 100.0;
            }

            double finalTotal = subtotal - discountAmount;
            if (finalTotal < 0) {
                finalTotal = 0;
            }

            String sqlOrder = """
                    insert into orders(user_id, total_amount, status, coupon_code, discount_amount)
                    values (?, ?, 'PENDING', ?, ?)
                    """;

            int orderId;

            try (PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setInt(1, userId);
                psOrder.setDouble(2, finalTotal);

                if (appliedCoupon != null) {
                    psOrder.setString(3, normalizedCouponCode);
                } else {
                    psOrder.setNull(3, java.sql.Types.VARCHAR);
                }

                psOrder.setDouble(4, discountAmount);
                psOrder.executeUpdate();

                try (ResultSet rs = psOrder.getGeneratedKeys()) {
                    if (!rs.next()) {
                        System.out.println("Khong tao duoc don hang.");
                        conn.rollback();
                        return false;
                    }
                    orderId = rs.getInt(1);
                }
            }

            String sqlDetail = "insert into order_details(order_id, product_id, quantity, price) values (?, ?, ?, ?)";
            String sqlUpdateStock = "update products set stock = stock - ? where product_id = ? and stock >= ?";

            for (CartItem item : cart) {
                try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {
                    psDetail.setInt(1, orderId);
                    psDetail.setInt(2, item.getProductId());
                    psDetail.setInt(3, item.getQuantity());
                    psDetail.setDouble(4, item.getPrice());
                    psDetail.executeUpdate();
                }

                try (PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock)) {
                    psStock.setInt(1, item.getQuantity());
                    psStock.setInt(2, item.getProductId());
                    psStock.setInt(3, item.getQuantity());

                    int updated = psStock.executeUpdate();
                    if (updated <= 0) {
                        System.out.println("Cap nhat ton kho that bai cho san pham ID " + item.getProductId());
                        conn.rollback();
                        return false;
                    }
                }
            }

            if (appliedCoupon != null) {
                String sqlDecreaseCoupon = """
                        update coupons
                        set quantity = quantity - 1
                        where coupon_id = ? and quantity > 0
                        """;

                try (PreparedStatement psCouponUpdate = conn.prepareStatement(sqlDecreaseCoupon)) {
                    psCouponUpdate.setInt(1, appliedCoupon.getCouponId());

                    int updated = psCouponUpdate.executeUpdate();
                    if (updated <= 0) {
                        System.out.println("Khong the tru so luong coupon.");
                        conn.rollback();
                        return false;
                    }
                }
            }

            conn.commit();
            cart.clear();

            System.out.println("Dat hang thanh cong.");
            System.out.println("Tam tinh: " + subtotal);
            System.out.println("Giam gia: " + discountAmount);
            System.out.println("Thanh toan: " + finalTotal);
            System.out.println("Don hang dang o trang thai PENDING.");

            return true;
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}