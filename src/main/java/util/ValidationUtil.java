package util;
import java.util.regex.Pattern;

public class ValidationUtil {

    // regex email cơ bản
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}$";

    // regex số điện thoại (10 chữ số)
    private static final String PHONE_REGEX = "^\\d{10}$";

    // kiểm tra rỗng
    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    // kiểm tra email hợp lệ
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return false;
        return Pattern.matches(EMAIL_REGEX, email);
    }

    // kiểm tra số điện thoại
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return false;
        return Pattern.matches(PHONE_REGEX, phone);
    }

    // kiểm tra số > 0
    public static boolean isPositiveNumber(double value) {
        return value > 0;
    }

    // kiểm tra số >= 0 (dùng cho stock)
    public static boolean isNonNegative(int value) {
        return value >= 0;
    }

    // kiểm tra độ dài chuỗi
    public static boolean isLengthValid(String input, int min, int max) {
        if (isEmpty(input)) return false;
        int len = input.length();
        return len >= min && len <= max;
    }
}
