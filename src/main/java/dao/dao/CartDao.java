package dao.dao;

import model.CartItem;
import model.Product;

import java.util.List;

public interface CartDao {
    boolean addToCart(int userId, Product p, int quantity);
    boolean cartIsEmpty(int userId);
    List<CartItem> viewCart(int userId);
    boolean checkout(int userId, String couponCode);
}