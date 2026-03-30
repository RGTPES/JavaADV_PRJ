package service.impl;

import dao.dao.CartDao;
import dao.impl.CartDaoImpl;
import model.CartItem;
import model.Product;
import service.service.CartService;

import java.util.List;

public class CartServiceImpl implements CartService {
    private final CartDao cartDao = new CartDaoImpl();
    @Override
    public boolean addToCart(Product p, int quantity) {
        return cartDao.addToCart(p, quantity);
    }
    @Override
    public boolean cartIsEmpty(){
        return cartDao.cartIsEmpty();
    }
    @Override
    public List<CartItem> viewCart() {
        return cartDao.viewCart();
    }
    @Override
    public void checkout(int userId) {
        cartDao.checkout(userId);
    }
}