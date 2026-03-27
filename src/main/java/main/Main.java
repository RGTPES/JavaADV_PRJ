package main;


import model.Users;
import presentation.AuthMenu;
import presentation.ProductMenu;

import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final AuthMenu authMenu = new AuthMenu();
    private static final ProductMenu productMenu = new ProductMenu();

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n========== He thong smartphone store ==========");
            System.out.println("1. Dang ky");
            System.out.println("2. Dang nhap");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");

            int choice = inputInt();

            switch (choice) {
                case 1:
                    authMenu.register();
                    break;
                case 2:
                    Users user = authMenu.login();
                    if (user != null) {
                        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                            adminMenu(user);
                        } else {
//                            customerMenu(user);
                        }
                    }
                    break;
                case 0:
                    System.out.println("Tam biet.");
                    return;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        }
    }

    // ================= ADMIN MENU =================
    private static void adminMenu(Users user) {
        int choice;
        do {
            System.out.println("\n========== Admin menu ==========");
            System.out.println("Xin chao: " + user.getFullName());
            System.out.println("1. Quan ly san pham");
            System.out.println("0. Dang xuat");
            System.out.print("Chon: ");

            choice = inputInt();

            switch (choice) {
                case 1:
                    productMenu.displayMenu();
                    break;
                case 0:
                    System.out.println("Dang xuat thanh cong.");
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
            }

        } while (choice != 0);
    }

    // ================= CUSTOMER MENU =================
//    private static void customerMenu(Users user) {
//        int choice;
//        do {
//            System.out.println("\n========== Customer menu ==========");
//            System.out.println("Xin chao: " + user.getFullName());
//            System.out.println("1. Xem san pham");
//            System.out.println("2. Tim kiem san pham");
//            System.out.println("3. Sap xep gia tang dan");
//            System.out.println("4. Sap xep gia giam dan");
//            System.out.println("0. Dang xuat");
//            System.out.print("Chon: ");
//
//            choice = inputInt();
//
//            switch (choice) {
//                case 1:
//                    productMenu.displayMenu(); // co the dung chung (hoac tach rieng neu muon)
//                    break;
//                case 2:
//                    productMenu.searchProduct();
//                    break;
//                case 3:
//                    productMenu.showList(productMenu.sortAsc());
//                    break;
//                case 4:
//                    productMenu.showList(productMenu.sortDesc());
//                    break;
//                case 0:
//                    System.out.println("Dang xuat thanh cong.");
//                    break;
//                default:
//                    System.out.println("Lua chon khong hop le.");
//            }
//
//        } while (choice != 0);
//    }

    // ================= INPUT =================
    private static int inputInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Vui long nhap so: ");
            }
        }
    }
}
