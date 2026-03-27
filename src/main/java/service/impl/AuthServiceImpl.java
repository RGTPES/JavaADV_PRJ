package service.impl;
import dao.impl.UserDAOImpl;
import dao.dao.UserDao;
import model.Users;
import util.PasswordUtil;
import service.service.AuthService;
import util.ValidationUtil;

public class AuthServiceImpl implements AuthService {
    private final UserDao userDAO = new UserDAOImpl();
    @Override
    public boolean register(Users user) {
        if (user == null) {
            return false;
        }

        user.setFullName(safeTrim(user.getFullName()));
        user.setEmail(safeTrim(user.getEmail()).toLowerCase());
        user.setPhone(safeTrim(user.getPhone()));
        user.setPassword(safeTrim(user.getPassword()));
        user.setAddress(safeTrim(user.getAddress()));

        if (ValidationUtil.isEmpty(user.getFullName())
                || ValidationUtil.isEmpty(user.getEmail())
                || ValidationUtil.isEmpty(user.getPhone())
                || ValidationUtil.isEmpty(user.getPassword())
                || ValidationUtil.isEmpty(user.getAddress())) {
            return false;
        }

        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            return false;
        }

        if (!ValidationUtil.isValidPhone(user.getPhone())) {
            return false;
        }

        if (!ValidationUtil.isLengthValid(user.getPassword(), 6, 50)) {
            return false;
        }

        if (userDAO.existsByEmail(user.getEmail()) || userDAO.existsByPhone(user.getPhone())) {
            return false;
        }

        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        return userDAO.register(user);
    }
    @Override
    public Users login(String email, String password) {
        email = safeTrim(email).toLowerCase();
        password = safeTrim(password);

        if (isBlank(email) || isBlank(password)) {
            return null;
        }

        Users user = userDAO.login(email);
        if (user == null) {
            return null;
        }

        if (PasswordUtil.checkPassword(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }
}
