package service.service;


import model.Product;
import java.util.List;
public interface ProductService {

    List<Product> getAll(int page, int pageSize);

    int getTotalPages(int pageSize);

    Product findById(int id);


    boolean addProduct(Product product);

    boolean updateProduct(Product product);

    boolean deleteProduct(int id);

    List<Product> searchByName(String keyword);

    List<Product> sortByPriceAsc();

    List<Product> sortByPriceDesc();

    List<Product> getAllProducts ();


}
