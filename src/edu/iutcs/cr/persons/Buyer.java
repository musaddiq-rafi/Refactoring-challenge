package edu.iutcs.cr.persons;

import edu.iutcs.cr.util.InputReader;

import java.io.Serializable;

/**
 * @author Raian Rahman
 * @since 4/18/2024
 *
 * <p><strong>Refactoring notes:</strong>
 * <ul>
 *   <li>Replaced {@code new Scanner(System.in)} with shared {@link InputReader} singleton.</li>
 *   <li>Renamed no-arg {@code setPaymentMethod()} to {@code readPaymentMethod()} for
 *       clarity (SRP: setters set, readers prompt).</li>
 *   <li>Added value-based {@code setPaymentMethod(String)} for programmatic use.</li>
 * </ul>
 */
public class Buyer extends Person implements Serializable {

    private String paymentMethod;

    /** Full constructor: prompts the console for all fields including payment method. */
    public Buyer() {
        super();
        readPaymentMethod();
    }

    /** Lookup constructor: creates a partial Buyer used only for equality checks. */
    public Buyer(String id) {
        super(id);
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    /** Value-based setter – no console I/O. */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /** Prompts the console and sets the payment method field. */
    private void readPaymentMethod() {
        System.out.print("Enter payment method: ");
        this.paymentMethod = InputReader.getInstance().nextLine();
    }

    @Override
    public String toString() {
        return super.toString() +
                ", paymentMethod='" + paymentMethod + '\'';
    }
}
