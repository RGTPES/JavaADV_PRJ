package main;

import model.Users;
import presentation.AdminMenu;
import presentation.AuthMenu;
import presentation.CustomerMenu;

import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final AuthMenu authMenu = new AuthMenu();
    private static final AdminMenu adminMenu = new AdminMenu();
    private static final CustomerMenu customerMenu = new CustomerMenu();

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
                            adminMenu.show(user);
                        } else {
                            customerMenu.show(user);
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