package edu.iutcs.cr.persons;

import edu.iutcs.cr.util.InputReader;

import java.io.Serializable;

/**
 * @author Raian Rahman
 * @since 4/18/2024
 */
public class Buyer extends Person implements Serializable {

    private String paymentMethod;

    public Buyer() {
        super();
        readPaymentMethod();
    }

    public Buyer(String id) {
        super(id);
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

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
