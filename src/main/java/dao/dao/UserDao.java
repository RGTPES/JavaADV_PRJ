package dao.dao;

import model.Users;

public interface UserDao {
    boolean register(Users user);
    Users login(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
