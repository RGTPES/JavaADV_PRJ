package service.service;

import model.CartItem;
import model.Product;

import java.util.List;

public interface CartService {
    public boolean addToCart(Product p, int quantity);
    public boolean cartIsEmpty();
    public List<CartItem> viewCart();
    public void checkout(int userId);

}
