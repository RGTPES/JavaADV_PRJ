package dao.impl;

import dao.dao.CartDao;
import model.CartItem;
import model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartDaoImpl implements CartDao {
    private final List<CartItem> cart = new ArrayList<>();
    @Override
    public boolean addToCart(Product p, int quantity) {
        if (p == null || quantity <= 0) return false;

        for (CartItem item : cart) {
            if (item.getProductId() == p.getProductId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return true;
            }
        }
        CartItem newItem = new CartItem(
                p.getProductId(),
                p.getProductName(),
                p.getPrice(),
                quantity
        );
        cart.add(newItem);
        return true;
    }
    @Override
    public boolean cartIsEmpty() {
        return cart.isEmpty();
    }
    @Override
    public List<CartItem> viewCart() {
        return cart;
    }
}
