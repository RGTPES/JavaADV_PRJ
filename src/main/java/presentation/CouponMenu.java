package presentation;

import model.Coupon;
import service.impl.CouponServiceImpl;

import java.util.List;
import java.util.Scanner;

import static util.InputUtil.inputInt;

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

            choice = inputInt("Chon chuc nang");

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
        System.out.println("\n------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-15s %-8s %-8s %-12s %-22s %-22s %-10s%n",
                "ID", "Code", "%", "Qty", "MinOrder", "Start", "End", "Status");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");

        for (Coupon c : list) {
            System.out.printf("%-5d %-15s %-8.2f %-8d  %-12.2f %-22s %-22s %-10s%n",
                    c.getCouponId(),
                    c.getCouponCode(),
                    c.getDiscountPercent(),
                    c.getQuantity(),
                    c.getMinOrderAmount(),
                    c.getStartDate(),
                    c.getEndDate(),
                    c.getStatus());
        }

        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
    }

    private void addCoupon() {
        Coupon c = new Coupon();

        System.out.print("Nhap ma coupon: ");
        c.setCouponCode(sc.nextLine().trim().toUpperCase());

        System.out.print("Nhap phan tram giam: ");
        c.setDiscountPercent(inputDouble());

        c.setQuantity(inputInt("Nhap so luong: "));


        System.out.print("Nhap don toi thieu: ");
        c.setMinOrderAmount(inputDouble());

        System.out.print("Nhap start_date (yyyy-MM-dd): ");
        c.setStartDate(sc.nextLine().trim() + " 00:00:00");

        System.out.print("Nhap end_date (yyyy-MM-dd): ");
        c.setEndDate(sc.nextLine().trim() + " 23:59:59");

        System.out.print("Nhap status (ACTIVE/INACTIVE): ");
        c.setStatus(sc.nextLine().trim().toUpperCase());

        boolean result = couponService.addCoupon(c);
        System.out.println(result ? "Them coupon thanh cong." : "Them coupon that bai.");
    }

    private void updateCoupon() {
        int id = inputInt("Nhap id coupon can sua ");

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

        c.setQuantity(inputInt("Nhap so luong moi "));


        System.out.print("Nhap don toi thieu moi: ");
        c.setMinOrderAmount(inputDouble());

        System.out.print("Nhap start_date moi (yyyy-MM-dd): ");
        c.setStartDate(sc.nextLine().trim() + " 00:00:00");

        System.out.print("Nhap end_date moi (yyyy-MM-dd): ");
        c.setEndDate(sc.nextLine().trim() + " 23:59:59");

        System.out.print("Nhap status moi (ACTIVE/INACTIVE): ");
        c.setStatus(sc.nextLine().trim().toUpperCase());

        boolean result = couponService.updateCoupon(c);
        System.out.println(result ? "Cap nhat coupon thanh cong." : "Cap nhat coupon that bai.");
    }

    private void deleteCoupon() {
        int id = inputInt("Nhap id coupon can xoa ");

        boolean result = couponService.deleteCoupon(id);
        System.out.println(result ? "Xoa coupon thanh cong." : "Xoa coupon that bai.");
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