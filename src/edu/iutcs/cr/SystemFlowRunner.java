package edu.iutcs.cr;

import edu.iutcs.cr.persons.Buyer;
import edu.iutcs.cr.persons.Seller;
import edu.iutcs.cr.system.SystemDatabase;
import edu.iutcs.cr.util.InputReader;
import edu.iutcs.cr.vehicles.Vehicle;
import edu.iutcs.cr.vehicles.VehicleFactory;

/**
 * @author Raian Rahman
 * @since 4/19/2024
 *
 * <p><strong>Refactoring notes:</strong>
 * <ul>
 *   <li><strong>Factory Method:</strong> The long {@code if-else} chain in the original
 *       {@code addCar()} that both selected <em>and</em> constructed a vehicle was replaced
 *       by a call to {@link VehicleFactory#create(int)}, which is the authoritative place
 *       for vehicle construction (Open/Closed Principle).</li>
 *   <li><strong>Switch expression:</strong> Both the main-loop dispatch and the
 *       {@code createOrder()} inner-loop dispatch were converted from {@code if-else} chains
 *       to {@code switch} statements, which are clearer and exhaustive by construction.</li>
 *   <li><strong>InputReader singleton:</strong> All {@code new Scanner(System.in)} calls
 *       replaced with the shared {@link InputReader} singleton.</li>
 *   <li><strong>Invoice constructor:</strong> The {@code isPaid} flag is now read here
 *       (where the I/O belongs) and passed into {@link Invoice#Invoice(Buyer, Seller, ShoppingCart, boolean)}
 *       rather than being read inside the constructor.</li>
 * </ul>
 */
public class SystemFlowRunner {

    public static void run() {
        System.out.println("Welcome to Car Hut");
        System.out.println("Loading existing system...");
        SystemDatabase database = SystemDatabase.getInstance();
        System.out.println("Existing system loaded.");

        MainMenu mainMenu = new MainMenu();

        while (true) {
            System.out.println("\n\n\n");
            int op = mainMenu.showAndSelectOperation();

            if (op == 9) {
                database.saveSystem();
                return;
            }

            switch (op) {
                case 1 -> {
                    System.out.println("\n\n\nAdd new seller");
                    database.getSellers().add(new Seller());
                    promptToViewMainMenu();
                }
                case 2 -> {
                    System.out.println("\n\n\nAdd new customer");
                    database.getBuyers().add(new Buyer());
                    promptToViewMainMenu();
                }
                case 3 -> {
                    System.out.println("\n\n\nAdd new vehicle");
                    addCar();
                    promptToViewMainMenu();
                }
                case 4 -> {
                    System.out.println("\n\n\nInventory list");
                    database.showInventory();
                    promptToViewMainMenu();
                }
                case 5 -> {
                    System.out.println("\n\n\nSeller's list");
                    database.showSellerList();
                    promptToViewMainMenu();
                }
                case 6 -> {
                    System.out.println("\n\n\nCustomer's list");
                    database.showBuyerList();
                    promptToViewMainMenu();
                }
                case 7 -> {
                    System.out.println("\n\n\nCreate order");
                    createOrder();
                }
                case 8 -> {
                    System.out.println("\n\n\nInvoice list");
                    database.showInvoices();
                    promptToViewMainMenu();
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private static void promptToViewMainMenu() {
        InputReader reader = InputReader.getInstance();
        int val = -1;
        System.out.print("\n\nEnter 0 to view main menu: ");
        do {
            val = reader.nextInt();
        } while (val != 0);
    }

    /**
     * Prompts the user to choose a vehicle type and delegates construction to
     * {@link VehicleFactory}, eliminating the original {@code if-else} chain.
     */
    private static void addCar() {
        InputReader reader = InputReader.getInstance();
        SystemDatabase database = SystemDatabase.getInstance();

        System.out.println("Please enter the type of vehicle [1-" + VehicleFactory.MAX_TYPE + "]: ");
        for (int i = VehicleFactory.MIN_TYPE; i <= VehicleFactory.MAX_TYPE; i++) {
            System.out.println(i + ". " + VehicleFactory.getTypeName(i));
        }

        int vehicleType = -1;
        while (vehicleType < VehicleFactory.MIN_TYPE || vehicleType > VehicleFactory.MAX_TYPE) {
            System.out.print("Enter your choice: ");
            vehicleType = reader.nextInt();
            if (vehicleType < VehicleFactory.MIN_TYPE || vehicleType > VehicleFactory.MAX_TYPE) {
                System.out.println("Enter a valid vehicle type!");
            }
        }

        System.out.println("\n\nCreate new " + VehicleFactory.getTypeName(vehicleType));
        Vehicle newItem = VehicleFactory.create(vehicleType);
        database.getVehicles().add(newItem);
    }

    private static void createOrder() {
        InputReader reader = InputReader.getInstance();
        ShoppingCart cart = new ShoppingCart();

        while (true) {
            System.out.println("Please enter the type of operation: [1-5]");
            System.out.println("1. Add new vehicle to cart");
            System.out.println("2. Remove vehicle from cart");
            System.out.println("3. View cart");
            System.out.println("4. Confirm purchase");
            System.out.println();
            System.out.println("5. Return to main menu");

            int op = reader.nextInt();
            while (op < 1 || op > 5) {
                System.out.print("Please select a valid operation: ");
                op = reader.nextInt();
            }

            switch (op) {
                case 1 -> cart.addItem();
                case 2 -> cart.removeItem();
                case 3 -> cart.viewCart();
                case 4 -> { createInvoice(cart); return; }
                case 5 -> { return; }
            }
        }
    }

    /**
     * Reads buyer/seller IDs and the payment flag, then creates and stores an
     * {@link Invoice}.  The payment flag ({@code isPaid}) is read here &mdash; in the
     * presentation/flow layer &mdash; and passed as a constructor argument rather than
     * being read inside {@link Invoice}'s constructor.
     */
    private static void createInvoice(ShoppingCart cart) {
        InputReader reader = InputReader.getInstance();
        SystemDatabase database = SystemDatabase.getInstance();

        Buyer buyer = null;
        do {
            System.out.print("Enter buyer id: ");
            String buyerId = reader.nextLine();
            buyer = database.findBuyerById(buyerId);
            if (buyer == null) System.out.println("Buyer not found. Try again!");
        } while (buyer == null);

        Seller seller = null;
        do {
            System.out.print("Enter seller id: ");
            String sellerId = reader.nextLine();
            seller = database.findSellerById(sellerId);
            if (seller == null) System.out.println("Seller not found. Try again!");
        } while (seller == null);

        System.out.print("Is payment done (true/false): ");
        boolean isPaid = reader.nextBoolean();

        Invoice invoice = new Invoice(buyer, seller, cart, isPaid);
        invoice.printInvoice();
        database.getInvoices().add(invoice);
    }
}
