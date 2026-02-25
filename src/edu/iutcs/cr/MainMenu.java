package edu.iutcs.cr;

import edu.iutcs.cr.util.InputReader;

/**
 * @author Raian Rahman
 * @since 4/19/2024
 */
public class MainMenu {

    private void showMenu() {
        System.out.println("Please select an operation [1-9]: ");
        System.out.println("1. Add new seller");
        System.out.println("2. Add new customer");
        System.out.println("3. Add car");
        System.out.println("4. View inventory");
        System.out.println("5. View seller list");
        System.out.println("6. View buyer list");
        System.out.println();
        System.out.println("7. Add new order");
        System.out.println("8. View all invoices");
        System.out.println();
        System.out.println("9. Save System and Exit");
    }

    public int showAndSelectOperation() {
        showMenu();
        InputReader reader = InputReader.getInstance();
        int selectedOperation = -1;

        while (selectedOperation < 1 || selectedOperation > 9) {
            System.out.print("Enter your choice: ");
            selectedOperation = reader.nextInt();

            if (selectedOperation < 1 || selectedOperation > 9) {
                System.out.println("Please enter a number between 1 and 9.");
            }
        }

        return selectedOperation;
    }
}
