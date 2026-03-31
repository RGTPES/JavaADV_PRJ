package service.impl;

import dao.dao.CouponDAO;
import dao.impl.CouponDAOImpl;
import model.Coupon;
import service.service.CouponService;

import java.util.List;

public class CouponServiceImpl implements CouponService {
    private final CouponDAO couponDAO = new CouponDAOImpl();

    @Override
    public List<Coupon> getAllCoupons() {
        return couponDAO.findAll();
    }

    @Override
    public Coupon getCouponById(int id) {
        if (id <= 0) {
            return null;
        }
        return couponDAO.findById(id);
    }

    @Override
    public Coupon getCouponByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        return couponDAO.findByCode(code);
    }

    @Override
    public Coupon getValidCouponByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        return couponDAO.findValidByCode(code);
    }

    @Override
    public boolean addCoupon(Coupon coupon) {
        if (!isValid(coupon)) {
            return false;
        }
        return couponDAO.insert(coupon);
    }

    @Override
    public boolean updateCoupon(Coupon coupon) {
        if (coupon == null || coupon.getCouponId() <= 0 || !isValid(coupon)) {
            return false;
        }
        return couponDAO.update(coupon);
    }

    @Override
    public boolean deleteCoupon(int id) {
        if (id <= 0) {
            return false;
        }
        return couponDAO.delete(id);
    }

    private boolean isValid(Coupon c) {
        if (c == null) return false;
        if (c.getCouponCode() == null || c.getCouponCode().trim().isEmpty()) return false;
        if (c.getDiscountPercent() <= 0 || c.getDiscountPercent() > 100) return false;
        if (c.getQuantity() < 0) return false;
        if (c.getStartDate() == null || c.getStartDate().trim().isEmpty()) return false;
        if (c.getEndDate() == null || c.getEndDate().trim().isEmpty()) return false;
        if (c.getStatus() == null || c.getStatus().trim().isEmpty()) return false;
        return true;
    }
}