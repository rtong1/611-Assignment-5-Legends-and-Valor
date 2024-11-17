/**
 * Singleton input Handler that handles all user's input
 */

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputHandler {
    private static InputHandler instance;
    private Scanner scanner;

    // Private constructor to prevent direct instantiation
    private InputHandler() {
        scanner = new Scanner(System.in);
    }

    // Public method to provide access to the single instance
    public static InputHandler getInstance() {
        if (instance == null) {
            instance = new InputHandler();
        }
        return instance;
    }

    public String getCommand() {
        System.out.print("Enter command: ");
        return scanner.nextLine();
    }

    public int getIntInput(String prompt) {
        int input = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                input = scanner.nextInt();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // Clear the invalid input
            }
        }
        scanner.nextLine();
        return input;
    }

    public double getDoubleInput(String prompt) {
        double input = 0.0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                input = scanner.nextDouble();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear the invalid input
            }
        }
        scanner.nextLine();
        return input;
    }
}