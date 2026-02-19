package edu.iutcs.cr;

import edu.iutcs.cr.persons.Buyer;
import edu.iutcs.cr.persons.Seller;
import edu.iutcs.cr.system.SystemDatabase;
import edu.iutcs.cr.vehicles.*;

import java.util.Scanner;

/**
 * @author Raian Rahman
 * @since 4/19/2024
 */
public class SystemFlowRunner {

    public static void run() {
        System.out.println("Welcome to Car Hut");

        System.out.println("Loading existing system");
        SystemDatabase database = SystemDatabase.getInstance();
        System.out.println("Existing system loaded");

        MainMenu mainMenu = new MainMenu();

        while (true) {
            System.out.println("\n\n\n");

            int selectedOperation = mainMenu.showAndSelectOperation();

            if (selectedOperation == 9) {
                database.saveSystem();
                return;
            }

            if (selectedOperation == 1) {
                System.out.println("\n\n\nAdd new seller");
                database.getSellers().add(new Seller());
                promptToViewMainMenu();
            } else if (selectedOperation == 2) {
                System.out.println("\n\n\nAdd new customer");
                database.getBuyers().add(new Buyer());
                promptToViewMainMenu();
            } else if (selectedOperation == 3) {
                System.out.println("\n\n\nAdd new vehicle");
                addCar();
                promptToViewMainMenu();
            } else if (selectedOperation == 4) {
                System.out.println("\n\n\nInventory list");
                database.showInventory();
                promptToViewMainMenu();
            } else if (selectedOperation == 5) {
                System.out.println("\n\n\nSeller's list");
                database.showSellerList();
                promptToViewMainMenu();
            } else if (selectedOperation == 6) {
                System.out.println("\n\n\nCustomer's list");
                database.showBuyerList();
                promptToViewMainMenu();
            } else if (selectedOperation == 7) {
                System.out.println("\n\n\nCreate order");
                createOrder();
            } else if(selectedOperation==8) {
                System.out.println("\n\n\nInvoice list");
                database.showInvoices();
                promptToViewMainMenu();
            }
        }
    }

    private static void promptToViewMainMenu() {
        System.out.print("\n\nEnter 0 to view main menu: ");

        Scanner scanner = new Scanner(System.in);
        int val = -1;

        do {
            val = scanner.nextInt();
        } while (val != 0);
    }

    private static void addCar() {
        Scanner scanner = new Scanner(System.in);
        SystemDatabase database = SystemDatabase.getInstance();

        System.out.println("Please enter the type of vehicle [1-6]: ");
        System.out.println("1. Bus");
        System.out.println("2. Car");
        System.out.println("3. Hatchback");
        System.out.println("4. Sedan");
        System.out.println("5. SUV");

        int vehicleType = -1;
        while(vehicleType<1 || vehicleType>5) {
            System.out.print("Enter your choice: ");
            vehicleType = scanner.nextInt();

            if(vehicleType<1 || vehicleType>5) {
                System.out.println("Enter a valid vehicle type!");
            }
        }

        Vehicle newItem = null;

        if (vehicleType == 1) {
            System.out.println("\n\nCreate new bus");
            newItem = new Bus();
        } else if (vehicleType == 2) {
            System.out.println("\n\nCreate new car");
            newItem = new Car();
        } else if (vehicleType == 3) {
            System.out.println("\n\nCreate new hatchback");
            newItem = new Hatchback();
        } else if (vehicleType == 4) {
            System.out.println("\n\nCreate new sedan");
            newItem = new Sedan();
        } else {
            System.out.println("\n\nCreate new SUV");
            newItem = new SUV();
        }

        database.getVehicles().add(newItem);
    }

    private static void createOrder() {
        Scanner scanner = new Scanner(System.in);
        SystemDatabase systemDatabase = SystemDatabase.getInstance();
        ShoppingCart cart = new ShoppingCart();

        while (true) {
            int selectedOperation = -1;

            System.out.println("Please enter the type of operation: [1-5]");
            System.out.println("1. Add new vehicle to cart");
            System.out.println("2. Remove vehicle from cart");
            System.out.println("3. View cart");
            System.out.println("4. Confirm purchase");
            System.out.println();
            System.out.println("5. Return to main menu");

            selectedOperation = scanner.nextInt();

            while (selectedOperation < 1 || selectedOperation > 5) {
                System.out.print("Please select a valid operation: ");
                selectedOperation = scanner.nextInt();
            }

            if (selectedOperation == 1) {
                cart.addItem();
            } else if (selectedOperation == 2) {
                cart.removeItem();
            } else if (selectedOperation == 3) {
                cart.viewCart();
            } else if (selectedOperation == 4) {
                createInvoice(cart);
                return;
            } else {
                return;
            }
        }
    }

    private static void createInvoice(ShoppingCart cart) {
        Scanner scanner = new Scanner(System.in);
        SystemDatabase database = SystemDatabase.getInstance();

        Buyer buyer = null;
        Seller seller = null;

        do {
            System.out.print("Enter buyer id: ");
            String buyerId = scanner.nextLine();
            buyer = database.findBuyerById(buyerId);

            if (buyer == null) {
                System.out.println("Buyer not found. Try again!");
            }
        } while (buyer == null);

        do {
            System.out.print("Enter seller id: ");
            String sellerId = scanner.nextLine();
            seller = database.findSellerById(sellerId);

            if (seller == null) {
                System.out.println("Seller not found. Try again!");
            }
        } while (seller == null);

        Invoice invoice = new Invoice(buyer, seller, cart);
        invoice.printInvoice();
        database.getInvoices().add(invoice);
    }
}
