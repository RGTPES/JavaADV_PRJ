package presentation;

import model.Product;
import service.impl.ProductServiceImpl;

import java.util.List;
import java.util.Scanner;

public class ProductMenu {
    private final Scanner sc = new Scanner(System.in);
    private final ProductServiceImpl productService = new ProductServiceImpl();
    private static final int PAGE_SIZE = 5;

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n========== Quan ly san pham ==========");
            System.out.println("1. Hien thi danh sach san pham co phan trang");
            System.out.println("2. Them moi san pham");
            System.out.println("3. Sua thong tin san pham");
            System.out.println("4. Xoa san pham");
            System.out.println("5. Tim kiem san pham theo ten");
            System.out.println("6. Sap xep gia tang dan");
            System.out.println("7. Sap xep gia giam dan");
            System.out.println("8. Tim kiem nang cao");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");

            choice = inputInt();

            switch (choice) {
                case 1:
                    showProductsByPage();
                    break;
                case 2:
                    addProduct();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    searchProduct();
                    break;
                case 6:
                    showList(productService.sortByPriceAsc());
                    break;
                case 7:
                    showList(productService.sortByPriceDesc());
                    break;
                case 8:
                    searchAdvanced();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        } while (choice != 0);
    }

    private void showProductsByPage() {
        int totalPages = productService.getTotalPages(PAGE_SIZE);
        if (totalPages == 0) {
            System.out.println("Khong co san pham nao.");
            return;
        }

        int page;
        do {
            System.out.print("Nhap trang (1 - " + totalPages + "): ");
            page = inputInt();
        } while (page < 1 || page > totalPages);
        List<Product> list = productService.getAll(page, PAGE_SIZE);
        showList(list);
        System.out.println("Trang " + page + "/" + totalPages);
    }

    private void addProduct() {
        Product p = new Product();

        System.out.print("Nhap ten san pham: ");
        p.setProductName(inputRequiredString());

        System.out.print("Nhap dung luong: ");
        p.setStorage(inputRequiredString());

        System.out.print("Nhap mau sac: ");
        p.setColor(inputRequiredString());

        System.out.print("Nhap gia: ");
        p.setPrice(inputDouble());

        System.out.print("Nhap so luong kho: ");
        p.setStock(inputInt());

        System.out.print("Nhap mo ta: ");
        p.setDescription(inputRequiredString());

        System.out.print("Nhap category id: ");
        p.setCategoryId(inputInt());

        if (p.getPrice() <= 0) {
            System.out.println("Gia phai lon hon 0.");
            return;
        }

        if (p.getStock() <= 0) {
            System.out.println("So luong kho phai lon hon 0.");
            return;
        }

        boolean result = productService.addProduct(p);
        System.out.println(result ? "Them san pham thanh cong." : "Them san pham that bai.");
    }

    private void updateProduct() {
        System.out.print("Nhap id san pham can sua: ");
        int id = inputInt();

        Product oldProduct = productService.findById(id);
        if (oldProduct == null) {
            System.out.println("Khong tim thay san pham.");
            return;
        }

        System.out.println("----- Thong tin cu -----");
        printProduct(oldProduct);

        Product newProduct = new Product();
        newProduct.setProductId(oldProduct.getProductId());

        System.out.print("Nhap ten moi: ");
        newProduct.setProductName(inputRequiredString());

        System.out.print("Nhap dung luong moi: ");
        newProduct.setStorage(inputRequiredString());

        System.out.print("Nhap mau moi: ");
        newProduct.setColor(inputRequiredString());

        System.out.print("Nhap gia moi: ");
        newProduct.setPrice(inputDouble());

        System.out.print("Nhap kho moi: ");
        newProduct.setStock(inputInt());

        System.out.print("Nhap mo ta moi: ");
        newProduct.setDescription(inputRequiredString());

        System.out.print("Nhap category id moi: ");
        newProduct.setCategoryId(inputInt());

        if (newProduct.getPrice() <= 0) {
            System.out.println("Gia phai lon hon 0.");
            return;
        }

        if (newProduct.getStock() <= 0) {
            System.out.println("So luong kho phai lon hon 0.");
            return;
        }

        boolean result = productService.updateProduct(newProduct);
        System.out.println(result ? "Cap nhat thanh cong." : "Cap nhat that bai.");
    }

    private void deleteProduct() {
        System.out.print("Nhap id san pham can xoa: ");
        int id = inputInt();

        Product p = productService.findById(id);
        if (p == null) {
            System.out.println("Khong tim thay san pham.");
            return;
        }

        printProduct(p);
        System.out.print("Xac nhan xoa (Y/N): ");
        String confirm = sc.nextLine().trim();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Da huy thao tac.");
            return;
        }

        boolean result = productService.deleteProduct(id);
        System.out.println(result ? "Xoa thanh cong." : "Xoa that bai.");
    }

    public void searchProduct() {
        System.out.print("Nhap ten can tim: ");
        String keyword = sc.nextLine().trim();
        showList(productService.searchByName(keyword));
    }

    private void searchAdvanced() {
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

        showList(list);
    }

    public List<Product> sortAsc() {
        return productService.sortByPriceAsc();
    }

    public List<Product> sortDesc() {
        return productService.sortByPriceDesc();
    }

    public void showList(List<Product> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("Khong co du lieu.");
            return;
        }

        for (Product p : list) {
            printProduct(p);
            System.out.println("--------------------------------");
        }
    }

    public void showListHaveProduct(List<Product> list) {
        boolean hasProduct = false;

        for (Product p : list) {
            if (p.getStock() > 0) {
                printProduct(p);
                System.out.println("--------------------------------");
                hasProduct = true;
            }
        }

        if (!hasProduct) {
            System.out.println("Khong co san pham con hang.");
        }
    }

    private void printProduct(Product p) {
        System.out.println("Id: " + p.getProductId());
        System.out.println("Ten: " + p.getProductName());
        System.out.println("Dung luong: " + p.getStorage());
        System.out.println("Mau: " + p.getColor());
        System.out.println("Gia: " + String.format("%.0f", p.getPrice()));
        System.out.println("Kho: " + p.getStock());
        System.out.println("Mo ta: " + p.getDescription());
        System.out.println("Category id: " + p.getCategoryId());
        System.out.println("Trang thai: " + p.getStatus());
        System.out.println("Ngay tao: " + p.getCreatedAt());
    }

    private int inputInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Vui long nhap so nguyen: ");
            }
        }
    }

    private double inputDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Vui long nhap so: ");
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

    private String inputRequiredString() {
        while (true) {
            String value = sc.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.print("Truong nay khong duoc de trong. Nhap lai: ");
        }
    }
}