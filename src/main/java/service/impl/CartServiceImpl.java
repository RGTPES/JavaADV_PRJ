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
    public boolean addToCart(int userId, Product p, int quantity) {
        return cartDao.addToCart(userId, p, quantity);
    }

    @Override
    public boolean cartIsEmpty(int userId) {
        return cartDao.cartIsEmpty(userId);
    }

    @Override
    public List<CartItem> viewCart(int userId) {
        return cartDao.viewCart(userId);
    }

    @Override
    public boolean checkout(int userId, String couponCode) {
        return cartDao.checkout(userId, couponCode);
    }
}