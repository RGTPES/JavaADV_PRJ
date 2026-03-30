package presentation;

import model.Users;

public class AdminMenu {
    private final ProductMenu productMenu;
    private final OrderMenu orderMenu;

    public AdminMenu() {
        productMenu = new ProductMenu();
        orderMenu = new OrderMenu();
    }

    public void show(Users user) {
        int choice;
        do {
            System.out.println("\n========== Admin menu ==========");
            System.out.println("Xin chao: " + user.getFullName());
            System.out.println("1. Quan ly san pham");
            System.out.println("2. Quan ly don hang");
            System.out.println("0. Dang xuat");
            System.out.print("Chon: ");

            choice = inputInt();

            switch (choice) {
                case 1:
                    productMenu.displayMenu();
                    break;
                case 2:
                    orderMenu.displayMenu();
                    break;
                case 0:
                    System.out.println("Dang xuat thanh cong.");
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        } while (choice != 0);
    }

    private int inputInt() {
        while (true) {
            try {
                return Integer.parseInt(util.InputUtil.inputString("").trim());
            } catch (Exception e) {
                System.out.print("Vui long nhap so: ");
            }
        }
    }
}