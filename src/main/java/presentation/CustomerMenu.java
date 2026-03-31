package presentation;

import model.CartItem;
import model.Order;
import model.Product;
import model.Users;
import service.impl.CartServiceImpl;
import service.impl.OrderServiceImpl;
import service.impl.ProductServiceImpl;
import util.InputUtil;

import java.util.List;
import java.util.Scanner;

public class CustomerMenu {
    private final Scanner sc = new Scanner(System.in);
    private final ProductMenu productMenu;
    private final ProductServiceImpl productService;
    private final CartServiceImpl cartService;
    private final OrderServiceImpl orderService;

    public CustomerMenu() {
        productMenu = new ProductMenu();
        productService = new ProductServiceImpl();
        cartService = new CartServiceImpl();
        orderService = new OrderServiceImpl();
    }

    public void show(Users user) {
        int choice;
        do {
            System.out.println("\n========== Customer menu ==========");
            System.out.println("Xin chao: " + user.getFullName());
            System.out.println("1. Xem san pham");
            System.out.println("2. Them san pham vao gio hang");
            System.out.println("3. Xem gio hang");
            System.out.println("4. Xac nhan dat hang");
            System.out.println("5. Tim kiem san pham nang cao");
            System.out.println("6. Xem san pham gia tang dan");
            System.out.println("7. Xem san pham gia giam dan");
            System.out.println("8. Xem lich su don hang");
            System.out.println("9. Xem chi tiet 1 don hang");
            System.out.println("0. Dang xuat");

            choice = inputInt("Moi ban nhap lua chon: ");

            switch (choice) {
                case 1:
                    showProducts();
                    break;
                case 2:
                    addToCart(user);
                    break;
                case 3:
                    viewCart(user);
                    break;
                case 4:
                    checkout(user);
                    break;
                case 5:
                    searchAdvancedForCustomer();
                    break;
                case 6:
                    productMenu.showListHaveProduct(productService.sortByPriceAsc());
                    break;
                case 7:
                    productMenu.showListHaveProduct(productService.sortByPriceDesc());
                    break;
                case 8:
                    showMyOrders(user);
                    break;
                case 9:
                    showOrderDetail(user);
                    break;
                case 0:
                    System.out.println("Dang xuat thanh cong.");
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        } while (choice != 0);
    }

    private void showProducts() {
        List<Product> list = productService.getAllProducts();
        productMenu.showListHaveProduct(list);
    }

    private void addToCart(Users user) {
        int productId = inputInt("Nhap ID san pham: ");
        int quantity = inputInt("Nhap so luong: ");

        Product product = productService.findById(productId);
        if (product == null) {
            System.out.println("San pham khong ton tai!");
            return;
        }

        if (quantity <= 0) {
            System.out.println("So luong phai lon hon 0!");
            return;
        }

        if (product.getStock() < quantity) {
            System.out.println("Khong du hang trong kho!");
            return;
        }

        boolean result = cartService.addToCart(user.getUserId(), product, quantity);
        System.out.println(result ? "Them vao gio hang thanh cong!" : "Them vao gio hang that bai!");
    }

    private void viewCart(Users user) {
        if (cartService.cartIsEmpty(user.getUserId())) {
            System.out.println("Gio hang dang rong!");
            return;
        }
        cartService.viewCart(user.getUserId());
    }

    private void checkout(Users user) {
        if (cartService.cartIsEmpty(user.getUserId())) {
            System.out.println("Gio hang dang rong!");
            return;
        }

        cartService.viewCart(user.getUserId());
        boolean confirm = InputUtil.inputYesNo("Ban co chac muon dat hang khong?");
        if (!confirm) {
            System.out.println("Da huy dat hang.");
            return;
        }

        cartService.checkout(user.getUserId());
    }

    private void searchAdvancedForCustomer() {
        System.out.println("\n----- Tim kiem nang cao -----");
        System.out.print("Nhap tu khoa ten san pham (bo trong neu khong loc): ");
        String keyword = sc.nextLine().trim();
        if (keyword.isEmpty()) {
            keyword = null;
        }

        Double minPrice = inputOptionalDouble("Nhap gia tu (bo trong neu khong loc): ");
        Double maxPrice = inputOptionalDouble("Nhap gia den (bo trong neu khong loc): ");
        Integer categoryId = inputOptionalInt("Nhap category id (bo trong neu khong loc): ");
        Boolean inStock = inputOptionalYesNo("Chi lay san pham con hang? (y/n, Enter neu bo qua): ");

        System.out.print("Sap xep gia (ASC/DESC, Enter neu khong): ");
        String sortByPrice = sc.nextLine().trim().toUpperCase();
        if (!sortByPrice.equals("ASC") && !sortByPrice.equals("DESC")) {
            sortByPrice = null;
        }

        List<Product> list = productService.searchAdvanced(
                keyword, minPrice, maxPrice, categoryId, inStock, sortByPrice
        );
        productMenu.showListHaveProduct(list);
    }

    private void showMyOrders(Users user) {
        List<Order> orders = orderService.getOrdersByUserId(user.getUserId());
        if (orders == null || orders.isEmpty()) {
            System.out.println("Ban chua co don hang nao.");
            return;
        }

        System.out.println("\n========== Lich su don hang ==========");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-10s %-10s %-15s %-15s %-25s%n",
                "Order ID", "User ID", "Total", "Status", "Created At");
        System.out.println("--------------------------------------------------------------------------------");

        for (Order order : orders) {
            System.out.printf("%-10d %-10d %-15.2f %-15s %-25s%n",
                    order.getOrderId(),
                    order.getUserId(),
                    order.getTotalAmount(),
                    order.getStatus(),
                    order.getCreatedAt());
        }

        System.out.println("--------------------------------------------------------------------------------");
    }

    private void showOrderDetail(Users user) {
        List<Order> orders = orderService.getOrdersByUserId(user.getUserId());
        if (orders == null || orders.isEmpty()) {
            System.out.println("Ban chua co don hang nao.");
            return;
        }

        int orderId = inputInt("Nhap ma don hang muon xem chi tiet: ");
        boolean found = false;

        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                found = true;
                System.out.println("\n========== Chi tiet don hang ==========");
                System.out.println("Ma don hang: " + order.getOrderId());
                System.out.println("Ma nguoi dung: " + order.getUserId());
                System.out.println("Tong tien: " + order.getTotalAmount());
                System.out.println("Trang thai: " + order.getStatus());
                System.out.println("Ngay tao: " + order.getCreatedAt());
                break;
            }
        }

        if (!found) {
            System.out.println("Khong tim thay don hang nay trong lich su cua ban.");
        }
    }

    private int inputInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Nhap so hop le!");
            }
        }
    }

    private Double inputOptionalDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                String value = sc.nextLine().trim();
                if (value.isEmpty()) {
                    return null;
                }
                return Double.parseDouble(value);
            } catch (Exception e) {
                System.out.println("Nhap so hop le.");
            }
        }
    }

    private Integer inputOptionalInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                String value = sc.nextLine().trim();
                if (value.isEmpty()) {
                    return null;
                }
                return Integer.parseInt(value);
            } catch (Exception e) {
                System.out.println("Nhap so nguyen hop le.");
            }
        }
    }

    private Boolean inputOptionalYesNo(String message) {
        while (true) {
            System.out.print(message);
            String value = sc.nextLine().trim().toLowerCase();
            if (value.isEmpty()) {
                return null;
            }
            if (value.equals("y")) {
                return true;
            }
            if (value.equals("n")) {
                return false;
            }
            System.out.println("Chi nhap y, n hoac Enter.");
        }
    }
}