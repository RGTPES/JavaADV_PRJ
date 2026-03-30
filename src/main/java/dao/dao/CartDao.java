package dao.dao;
import model.CartItem;
import model.Product;
import java.util.List;
public interface CartDao {
    boolean addToCart(Product p, int quantity);

    boolean cartIsEmpty();

    List<CartItem> viewCart();

    void checkout(int userId);
}