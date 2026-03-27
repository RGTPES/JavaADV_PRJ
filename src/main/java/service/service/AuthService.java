package service.service;
import model.Users;
public interface AuthService {
    boolean register(Users user);

    Users login(String email, String password);
}
