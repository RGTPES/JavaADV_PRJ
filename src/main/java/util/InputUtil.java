package util;

import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);
    public static String inputString(String message) {
        System.out.println(message);
        return scanner.nextLine().trim();
    }
    public static int inputInt(String message) {
        System.out.println(message);
        while(true){
            try{
                System.out.println(message);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            }catch(Exception e){
                System.out.println("Vui long nhap so nguyen hop le");
            }
        }
    }
    public static boolean inputYesNo(String message) {
        while (true) {
            System.out.print(message + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) {
                return true;
            }
            if (input.equals("n")) {
                return false;
            }
            System.out.println("chi duoc nhap y hoac n!");
        }
    }
}
