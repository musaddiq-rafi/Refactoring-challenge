package edu.iutcs.cr.vehicles;

import edu.iutcs.cr.util.InputReader;

import java.io.Serializable;

/**
 * @author Raian Rahman
 * @since 4/19/2024
 *
 * <p><strong>Refactoring notes:</strong> Replaced {@code new Scanner(System.in)} with
 * {@link InputReader} singleton; renamed {@code setHasSunroof()} (no-arg, console I/O)
 * to {@code readHasSunroof()} and added a value-based setter.
 */
public class Sedan extends Vehicle implements Serializable {

    private boolean hasSunroof;

    public Sedan() {
        super();
        readHasSunroof();
    }

    public boolean hasSunroof() {
        return hasSunroof;
    }

    /** Value-based setter – no console I/O. */
    public void setHasSunroof(boolean hasSunroof) {
        this.hasSunroof = hasSunroof;
    }

    private void readHasSunroof() {
        System.out.print("Does the sedan have a sunroof? (true/false): ");
        this.hasSunroof = InputReader.getInstance().nextBoolean();
    }

    @Override
    public String toString() {
        return "Sedan{" + super.toString() + ", " +
                "hasSunroof=" + hasSunroof() +
                "}";
    }
}
