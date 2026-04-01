package presentation;

import model.Users;
import service.impl.AuthServiceImpl;
import util.ValidationUtil;

import java.util.Scanner;

public class AuthMenu {
    private final Scanner sc = new Scanner(System.in);
    private final AuthServiceImpl authService = new AuthServiceImpl();

    public Users login() {
        System.out.println("\n===== Dang nhap =====");
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Mat khau: ");
        String password = sc.nextLine().trim();
        Users user = authService.login(email, password);
        if (user == null) {
            System.out.println("Dang nhap that bai.");
        } else {
            System.out.println("Dang nhap thanh cong. Xin chao " + user.getFullName() + ".");
        }

        return user;
    }

    public void register() {
        System.out.println("\n===== Dang ky =====");

        Users user = new Users();

        System.out.print("Ho ten: ");
        user.setFullName(sc.nextLine().trim());

        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        if (!ValidationUtil.isValidEmail(email)) {
            System.out.println("Email khong hop le.");
            return;
        }
        user.setEmail(email);

        System.out.print("So dien thoai: ");
        String phone = sc.nextLine().trim();

        if (!ValidationUtil.isValidPhone(phone)) {
            System.out.println("So dien thoai khong hop le.");
            return;
        }
        user.setPhone(phone);

        System.out.print("Mat khau: ");
        user.setPassword(sc.nextLine().trim());

        System.out.print("Dia chi: ");
        user.setAddress(sc.nextLine().trim());

        user.setRole("CUSTOMER");

        boolean result = authService.register(user);
        System.out.println(result ? "Dang ky thanh cong." : "Dang ky that bai.");
    }
}