package service.service;

import model.Coupon;
import java.util.List;

public interface CouponService {
    List<Coupon> getAllCoupons();
    Coupon getCouponById(int id);
    Coupon getCouponByCode(String code);
    Coupon getValidCouponByCode(String code);

    boolean addCoupon(Coupon coupon);
    boolean updateCoupon(Coupon coupon);
    boolean deleteCoupon(int id);
}