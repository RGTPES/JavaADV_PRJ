package presentation;

import model.Product;
import model.Users;
import service.impl.CartServiceImpl;
import service.impl.ProductServiceImpl;

import java.util.List;

public class CustomerMenu {

    private final ProductMenu productMenu;
    private final ProductServiceImpl productService;
    private final CartServiceImpl cartService;

    public CustomerMenu() {
        productMenu = new ProductMenu();
        productService = new ProductServiceImpl();
        cartService = new CartServiceImpl();
    }

    public void show(Users user) {
        int choice;
        do {
            System.out.println("\n========== Customer menu ==========");
            System.out.println("Xin chao: " + user.getFullName());
            System.out.println("1. Xem san pham");
            System.out.println("2. Them san pham vao gio hang");
            System.out.println("3. Xac nhan dat hang");
            System.out.println("0. Dang xuat");
            choice = inputInt("Moi ban nhap lua chon: ");
            switch (choice) {
                case 1:
                    showProducts();
                    break;
                case 2:
                    addToCart();
                    break;
                case 3:
                    checkout(user);
                    break;
                case 0:
                    System.out.println("Dang xuat thanh cong.");
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        } while (choice != 0);
    }
    // ===== Xem sản phẩm =====
    private void showProducts() {
        List<Product> list = productService.getAllProducts();
        productMenu.showListHaveProduct(list);
    }
    // ===== Thêm vào giỏ =====
    private void addToCart() {
        int productId = inputInt("Nhap ID san pham: ");
        int quantity = inputInt("Nhap so luong: ");
        Product product = productService.findById(productId);
        if (product == null) {
            System.out.println("San pham khong ton tai!");
            return;
        }
        if (product.getStock() < quantity) {
            System.out.println("Khong du hang trong kho!");
            return;
        }
        cartService.addToCart(product, quantity);
        System.out.println("Them vao gio hang thanh cong!");
    }

    // ===== Đặt hàng =====
    private void checkout(Users user) {
        if (cartService.cartIsEmpty()) {
            System.out.println("Gio hang dang rong!");
            return;
        }

        cartService.viewCart();

        boolean confirm = util.InputUtil.inputYesNo("Ban co chac muon dat hang khong?");
        if (!confirm) return;

        cartService.checkout(user.getUserId());
    }



    private int inputInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(util.InputUtil.inputString("").trim());
            } catch (Exception e) {
                System.out.println("Nhap so hop le!");
            }
        }
    }
}