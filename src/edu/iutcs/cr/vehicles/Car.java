package edu.iutcs.cr.vehicles;

import edu.iutcs.cr.util.InputReader;

import java.io.Serializable;

/**
 * @author Raian Rahman
 * @since 4/18/2024
 *
 * <p><strong>Refactoring notes:</strong> Replaced {@code new Scanner(System.in)} with
 * {@link InputReader} singleton; renamed {@code setSeatingCapacity()} (no-arg, console I/O)
 * to {@code readSeatingCapacity()} and added a value-based setter.
 * Field visibility widened from package-private to {@code private} (Encapsulation).
 */
public class Car extends Vehicle implements Serializable {

    private int seatingCapacity;

    public Car() {
        super();
        readSeatingCapacity();
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    /** Value-based setter – no console I/O. */
    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    private void readSeatingCapacity() {
        System.out.print("Enter seating capacity: ");
        this.seatingCapacity = InputReader.getInstance().nextInt();
    }

    @Override
    public String toString() {
        return "Car{" + super.toString() + ", " +
                "seatingCapacity=" + getSeatingCapacity() +
                "}";
    }
}
