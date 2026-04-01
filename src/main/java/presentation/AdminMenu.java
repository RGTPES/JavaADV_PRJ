package presentation;

import model.Users;

import static util.InputUtil.inputInt;

public class AdminMenu {
    private final ProductMenu productMenu;
    private final OrderMenu orderMenu;
    private final CouponMenu couponMenu;
    private final ReportMenu reportMenu;
    private final CategoryMenu categoryMenu;

    public AdminMenu() {
        productMenu = new ProductMenu();
        orderMenu = new OrderMenu();
        couponMenu = new CouponMenu();
        reportMenu = new ReportMenu();
        categoryMenu = new CategoryMenu();
    }

    public void show(Users user) {
        int choice;
        do {
            System.out.println("\n========== Admin menu ==========");
            System.out.println("Xin chao: " + user.getFullName());
            System.out.println("1. Quan ly san pham");
            System.out.println("2. Quan ly don hang");
            System.out.println("3. Quan ly coupon");
            System.out.println("4. Quan ly categories");
            System.out.println("5. Top 5 san pham ban chay nhat thang");
            System.out.println("0. Dang xuat");

            choice = inputInt("Chon chuc nang");

            switch (choice) {
                case 1:
                    productMenu.displayMenu();
                    break;
                case 2:
                    orderMenu.displayMenu();
                    break;
                case 3:
                    couponMenu.displayMenu();
                    break;
                case 4:
                    categoryMenu.showCategoryMenu();
                    break;
                case 5:
                    reportMenu.showTop5BestSellingProductsThisMonth();
                    break;
                case 0:
                    System.out.println("Dang xuat thanh cong.");
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        } while (choice != 0);
    }
}