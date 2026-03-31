package dao.impl;

import dao.dao.CouponDAO;
import model.Coupon;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CouponDAOImpl implements CouponDAO {

    @Override
    public List<Coupon> findAll() {
        List<Coupon> list = new ArrayList<>();
        String sql = "select * from coupons order by coupon_id desc";

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Coupon findById(int id) {
        String sql = "select * from coupons where coupon_id = ?";

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Coupon findByCode(String code) {
        String sql = "select * from coupons where coupon_code = ?";

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, code.trim().toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Coupon findValidByCode(String code) {
        String sql = """
                select * from coupons
                where coupon_code = ?
                  and status = 'ACTIVE'
                  and quantity > 0
                  and now() between start_date and end_date
                """;

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, code.trim().toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean insert(Coupon coupon) {
        String sql = """
                insert into coupons(coupon_code, discount_percent, quantity, start_date, end_date, status)
                values (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, coupon.getCouponCode().trim().toUpperCase());
            ps.setDouble(2, coupon.getDiscountPercent());
            ps.setInt(3, coupon.getQuantity());
            ps.setString(4, coupon.getStartDate());
            ps.setString(5, coupon.getEndDate());
            ps.setString(6, coupon.getStatus().trim().toUpperCase());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(Coupon coupon) {
        String sql = """
                update coupons
                set coupon_code = ?, discount_percent = ?, quantity = ?, start_date = ?, end_date = ?, status = ?
                where coupon_id = ?
                """;

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, coupon.getCouponCode().trim().toUpperCase());
            ps.setDouble(2, coupon.getDiscountPercent());
            ps.setInt(3, coupon.getQuantity());
            ps.setString(4, coupon.getStartDate());
            ps.setString(5, coupon.getEndDate());
            ps.setString(6, coupon.getStatus().trim().toUpperCase());
            ps.setInt(7, coupon.getCouponId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "delete from coupons where coupon_id = ?";

        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean decreaseQuantity(Connection conn, int couponId) {
        String sql = "update coupons set quantity = quantity - 1 where coupon_id = ? and quantity > 0";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private Coupon map(ResultSet rs) throws Exception {
        Coupon c = new Coupon();
        c.setCouponId(rs.getInt("coupon_id"));
        c.setCouponCode(rs.getString("coupon_code"));
        c.setDiscountPercent(rs.getDouble("discount_percent"));
        c.setQuantity(rs.getInt("quantity"));
        c.setStartDate(rs.getString("start_date"));
        c.setEndDate(rs.getString("end_date"));
        c.setStatus(rs.getString("status"));
        return c;
    }
}