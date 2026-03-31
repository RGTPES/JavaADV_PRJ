package service.service;

import model.CartItem;
import model.Product;

import java.util.List;

public interface CartService {
    boolean addToCart(int userId, Product p, int quantity);
    boolean cartIsEmpty(int userId);
    List<CartItem> viewCart(int userId);
    void checkout(int userId);
}