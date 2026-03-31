package presentation;

import model.Coupon;
import service.impl.CouponServiceImpl;

import java.util.List;
import java.util.Scanner;

public class CouponMenu {
    private final Scanner sc = new Scanner(System.in);
    private final CouponServiceImpl couponService = new CouponServiceImpl();

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n========== Quan ly coupon ==========");
            System.out.println("1. Hien thi danh sach coupon");
            System.out.println("2. Them coupon");
            System.out.println("3. Sua coupon");
            System.out.println("4. Xoa coupon");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");

            choice = inputInt();

            switch (choice) {
                case 1:
                    showAll();
                    break;
                case 2:
                    addCoupon();
                    break;
                case 3:
                    updateCoupon();
                    break;
                case 4:
                    deleteCoupon();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        } while (choice != 0);
    }

    private void showAll() {
        List<Coupon> list = couponService.getAllCoupons();
        if (list == null || list.isEmpty()) {
            System.out.println("Khong co coupon.");
            return;
        }

        System.out.println("\n--------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-15s %-10s %-10s %-22s %-22s %-10s%n",
                "ID", "Code", "%", "Qty", "Start", "End", "Status");
        System.out.println("--------------------------------------------------------------------------------------------------");

        for (Coupon c : list) {
            System.out.printf("%-5d %-15s %-10.2f %-10d %-22s %-22s %-10s%n",
                    c.getCouponId(),
                    c.getCouponCode(),
                    c.getDiscountPercent(),
                    c.getQuantity(),
                    c.getStartDate(),
                    c.getEndDate(),
                    c.getStatus());
        }

        System.out.println("--------------------------------------------------------------------------------------------------");
    }

    private void addCoupon() {
        Coupon c = new Coupon();

        System.out.print("Nhap ma coupon: ");
        c.setCouponCode(sc.nextLine().trim().toUpperCase());

        System.out.print("Nhap phan tram giam: ");
        c.setDiscountPercent(inputDouble());

        System.out.print("Nhap so luong: ");
        c.setQuantity(inputInt());

        System.out.print("Nhap start_date (yyyy-MM-dd HH:mm:ss): ");
        c.setStartDate(sc.nextLine().trim());

        System.out.print("Nhap end_date (yyyy-MM-dd HH:mm:ss): ");
        c.setEndDate(sc.nextLine().trim());

        System.out.print("Nhap status (ACTIVE/INACTIVE): ");
        c.setStatus(sc.nextLine().trim().toUpperCase());

        boolean result = couponService.addCoupon(c);
        System.out.println(result ? "Them coupon thanh cong." : "Them coupon that bai.");
    }

    private void updateCoupon() {
        System.out.print("Nhap id coupon can sua: ");
        int id = inputInt();

        Coupon old = couponService.getCouponById(id);
        if (old == null) {
            System.out.println("Khong tim thay coupon.");
            return;
        }

        Coupon c = new Coupon();
        c.setCouponId(id);

        System.out.print("Nhap ma coupon moi: ");
        c.setCouponCode(sc.nextLine().trim().toUpperCase());

        System.out.print("Nhap phan tram giam moi: ");
        c.setDiscountPercent(inputDouble());

        System.out.print("Nhap so luong moi: ");
        c.setQuantity(inputInt());

        System.out.print("Nhap start_date moi (yyyy-MM-dd HH:mm:ss): ");
        c.setStartDate(sc.nextLine().trim());

        System.out.print("Nhap end_date moi (yyyy-MM-dd HH:mm:ss): ");
        c.setEndDate(sc.nextLine().trim());

        System.out.print("Nhap status moi (ACTIVE/INACTIVE): ");
        c.setStatus(sc.nextLine().trim().toUpperCase());

        boolean result = couponService.updateCoupon(c);
        System.out.println(result ? "Cap nhat coupon thanh cong." : "Cap nhat coupon that bai.");
    }

    private void deleteCoupon() {
        System.out.print("Nhap id coupon can xoa: ");
        int id = inputInt();

        boolean result = couponService.deleteCoupon(id);
        System.out.println(result ? "Xoa coupon thanh cong." : "Xoa coupon that bai.");
    }

    private int inputInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Nhap so nguyen hop le: ");
            }
        }
    }

    private double inputDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Nhap so hop le: ");
            }
        }
    }
}