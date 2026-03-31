package dao.dao;

import model.Coupon;

import java.sql.Connection;
import java.util.List;

public interface CouponDAO {
    List<Coupon> findAll();
    Coupon findById(int id);
    Coupon findByCode(String code);
    Coupon findValidByCode(String code);
    boolean insert(Coupon coupon);
    boolean update(Coupon coupon);
    boolean delete(int id);
    boolean decreaseQuantity(Connection conn, int couponId);
}