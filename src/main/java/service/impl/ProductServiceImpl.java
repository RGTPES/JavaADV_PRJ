package service.impl;
import dao.dao.ProductDAO;
import dao.impl.ProductDAOImpl;
import model.Product;
import service.service.ProductService;
import util.ValidationUtil;

import java.util.List;
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO = new ProductDAOImpl();
@Override
    public List<Product> getAll(int page, int pageSize) {
        if (page <= 0 || pageSize <= 0) {
            return List.of();
        }
        return productDAO.findAll(page, pageSize);
    }
@Override
    public int getTotalPages(int pageSize) {
        if (pageSize <= 0) {
            return 0;
        }
        int total = productDAO.countActiveProducts();
        return (int) Math.ceil((double) total / pageSize);
    }
@Override
    public Product findById(int id) {
        return productDAO.findById(id);
    }
@Override
    public boolean addProduct(Product p) {
        if (!isValidProduct(p)) {
            return false;
        }
        return productDAO.insert(p);
    }
@Override
    public boolean updateProduct(Product p) {
        if (p == null || p.getProductId() <= 0) {
            return false;
        }
        if (!isValidProduct(p)) {
            return false;
        }
        return productDAO.update(p);
    }
@Override
    public boolean deleteProduct(int id) {
        if (id <= 0) {
            return false;
        }
        return productDAO.softDelete(id);
    }
@Override
    public List<Product> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }
        return productDAO.searchByName(keyword.trim());
    }
@Override
    public List<Product> sortByPriceAsc() {
        return productDAO.sortByPriceAsc();
    }
@Override
public List<Product> getAllProducts(){
    return productDAO.getAllProducts();

}
@Override
    public List<Product> sortByPriceDesc() {
        return productDAO.sortByPriceDesc();
    }
    private boolean isValidProduct(Product p) {
        if (p == null) {
            return false;
        }
        if (ValidationUtil.isEmpty(p.getProductName())
                || ValidationUtil.isEmpty(p.getStorage())
                || ValidationUtil.isEmpty(p.getColor())
                || ValidationUtil.isEmpty(p.getDescription())) {
            return false;
        }

        if (!ValidationUtil.isPositiveNumber(p.getPrice())) {
            return false;
        }

        if (!ValidationUtil.isPositiveNumber(p.getStock())) {
            return false;
        }

        return p.getCategoryId() > 0;
    }

}