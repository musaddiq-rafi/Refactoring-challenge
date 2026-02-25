package edu.iutcs.cr;

import edu.iutcs.cr.persons.Buyer;
import edu.iutcs.cr.persons.Seller;
import edu.iutcs.cr.vehicles.Vehicle;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Raian Rahman
 * @since 4/19/2024
 *
 * <p><strong>Refactoring notes (SRP / Constructor cleanliness):</strong>
 * The original constructor called {@code takePayment()}, which performed console I/O
 * ({@code scanner.nextBoolean()}) as a side effect.  Constructors should only initialise
 * the object's state &mdash; performing user interaction inside a constructor violates
 * the Single Responsibility Principle and makes the class impossible to unit-test without
 * a console.
 *
 * <ul>
 *   <li>The {@code isPaid} value is now passed into the constructor by the caller
 *       ({@code SystemFlowRunner.createInvoice()}) after reading it from the console there.</li>
 *   <li>The {@code takePayment()} method is removed; payment-reading logic lives in
 *       {@code SystemFlowRunner.createInvoice()} where it belongs.</li>
 *   <li>The {@code Scanner} import and dependency are eliminated.</li>
 * </ul>
 */
public class Invoice implements Serializable {

    private final Buyer buyer;
    private final Seller seller;
    private final ShoppingCart shoppingCart;
    private final boolean isPaid;
    private final LocalDateTime dateTime;

    /**
     * @param buyer        the purchasing party
     * @param seller       the selling party
     * @param shoppingCart the cart of vehicles being purchased
     * @param isPaid       whether payment has been confirmed by the caller
     */
    public Invoice(Buyer buyer, Seller seller, ShoppingCart shoppingCart, boolean isPaid) {
        this.buyer = buyer;
        this.seller = seller;
        this.shoppingCart = shoppingCart;
        this.isPaid = isPaid;
        markVehiclesUnavailable();
        this.dateTime = LocalDateTime.now();
    }

    public void printInvoice() {
        System.out.println("Buyer: "   + this.buyer.toString());
        System.out.println("Seller: "  + this.seller.toString());
        System.out.println("Payment Status: " + (isPaid ? "Paid" : "Due"));
        System.out.println("Date: " + dateTime.toLocalDate() + " Time: " + dateTime.toLocalTime());
        this.shoppingCart.viewCart();
    }

    private void markVehiclesUnavailable() {
        for (Vehicle vehicle : shoppingCart.getVehicles()) {
            vehicle.setUnavailable();
        }
    }
}
