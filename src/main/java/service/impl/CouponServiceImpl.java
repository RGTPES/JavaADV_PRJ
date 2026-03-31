package service.impl;

import dao.dao.CouponDAO;
import dao.impl.CouponDAOImpl;
import model.Coupon;
import service.service.CouponService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CouponServiceImpl implements CouponService {
    private final CouponDAO couponDAO = new CouponDAOImpl();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        if (!isValidForInsert(coupon)) {
            return false;
        }

        normalizeCoupon(coupon);

        Coupon existing = couponDAO.findByCode(coupon.getCouponCode());
        if (existing != null) {
            System.out.println("Ma coupon da ton tai");
            return false;
        }

        return couponDAO.insert(coupon);
    }

    @Override
    public boolean updateCoupon(Coupon coupon) {
        if (!isValidForUpdate(coupon)) {
            return false;
        }

        Coupon old = couponDAO.findById(coupon.getCouponId());
        if (old == null) {
            System.out.println("Khong tim thay coupon");
            return false;
        }

        normalizeCoupon(coupon);

        Coupon existing = couponDAO.findByCode(coupon.getCouponCode());
        if (existing != null && existing.getCouponId() != coupon.getCouponId()) {
            System.out.println("Ma coupon da ton tai");
            return false;
        }

        return couponDAO.update(coupon);
    }

    @Override
    public boolean deleteCoupon(int id) {
        if (id <= 0) {
            System.out.println("Id khong hop le");
            return false;
        }
        return couponDAO.delete(id);
    }

    private boolean isValidForInsert(Coupon coupon) {
        if (coupon == null) {
            System.out.println("Coupon khong hop le");
            return false;
        }

        if (coupon.getCouponCode() == null || coupon.getCouponCode().trim().isEmpty()) {
            System.out.println("Ma coupon khong duoc de trong");
            return false;
        }

        if (coupon.getDiscountPercent() <= 0 || coupon.getDiscountPercent() > 100) {
            System.out.println("Phan tram giam phai > 0 va <= 100.");
            return false;
        }

        if (coupon.getQuantity() <= 0) {
            System.out.println("So luong phai > 0.");
            return false;
        }


        if (coupon.getMinOrderAmount() < 0) {
            System.out.println("Don toi thieu khong duoc am.");
            return false;
        }

        if (!isValidStatus(coupon.getStatus())) {
            System.out.println("Status chi duoc la ACTIVE hoac INACTIVE.");
            return false;
        }

        if (!isValidDateTime(coupon.getStartDate())) {
            System.out.println("Start date sai dinh dang yyyy-MM-dd HH:mm:ss");
            return false;
        }

        if (!isValidDateTime(coupon.getEndDate())) {
            System.out.println("End date sai dinh dang yyyy-MM-dd HH:mm:ss");
            return false;
        }

        LocalDateTime start = LocalDateTime.parse(coupon.getStartDate(), FORMATTER);
        LocalDateTime end = LocalDateTime.parse(coupon.getEndDate(), FORMATTER);

        if (end.isBefore(start)) {
            System.out.println("End date phai sau hoac bang start date.");
            return false;
        }

        return true;
    }

    private boolean isValidForUpdate(Coupon coupon) {
        if (coupon == null) {
            System.out.println("Coupon khong hop le");
            return false;
        }

        if (coupon.getCouponId() <= 0) {
            System.out.println("Id coupon khong hop le");
            return false;
        }

        if (coupon.getCouponCode() == null || coupon.getCouponCode().trim().isEmpty()) {
            System.out.println("Ma coupon khong duoc de trong");
            return false;
        }

        if (coupon.getDiscountPercent() <= 0 || coupon.getDiscountPercent() > 100) {
            System.out.println("Phan tram giam phai > 0 va <= 100.");
            return false;
        }

        if (coupon.getQuantity() <= 0) {
            System.out.println("So luong phai > 0.");
            return false;
        }

        if (coupon.getMinOrderAmount() < 0) {
            System.out.println("Don toi thieu khong duoc am.");
            return false;
        }

        if (!isValidStatus(coupon.getStatus())) {
            System.out.println("Status chi duoc la ACTIVE hoac INACTIVE.");
            return false;
        }

        if (!isValidDateTime(coupon.getStartDate())) {
            System.out.println("Start date sai dinh dang yyyy-MM-dd HH:mm:ss");
            return false;
        }

        if (!isValidDateTime(coupon.getEndDate())) {
            System.out.println("End date sai dinh dang yyyy-MM-dd HH:mm:ss");
            return false;
        }

        LocalDateTime start = LocalDateTime.parse(coupon.getStartDate(), FORMATTER);
        LocalDateTime end = LocalDateTime.parse(coupon.getEndDate(), FORMATTER);

        if (end.isBefore(start)) {
            System.out.println("End date phai sau hoac bang start date.");
            return false;
        }

        return true;
    }

    private boolean isValidStatus(String status) {
        if (status == null) {
            return false;
        }
        String s = status.trim().toUpperCase();
        return s.equals("ACTIVE") || s.equals("INACTIVE");
    }

    private boolean isValidDateTime(String dateTime) {
        try {
            LocalDateTime.parse(dateTime, FORMATTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void normalizeCoupon(Coupon coupon) {
        coupon.setCouponCode(coupon.getCouponCode().trim().toUpperCase());
        coupon.setStatus(coupon.getStatus().trim().toUpperCase());
    }
}