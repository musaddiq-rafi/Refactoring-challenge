package edu.iutcs.cr.vehicles;

import edu.iutcs.cr.util.InputReader;

import java.io.Serializable;

/**
 * @author Raian Rahman
 * @since 4/18/2024
 *
 * <p><strong>Refactoring notes:</strong> Replaced {@code new Scanner(System.in)} with
 * {@link InputReader} singleton; renamed {@code setPassengerCapacity()} (no-arg, console I/O)
 * to {@code readPassengerCapacity()} and added a value-based setter.
 */
public class Bus extends Vehicle implements Serializable {

    private int passengerCapacity;

    public Bus() {
        super();
        readPassengerCapacity();
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    /** Value-based setter – no console I/O. */
    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    private void readPassengerCapacity() {
        System.out.print("Enter passenger capacity: ");
        this.passengerCapacity = InputReader.getInstance().nextInt();
    }

    @Override
    public String toString() {
        return "Bus{" + super.toString() + ", " +
                "passengerCapacity=" + getPassengerCapacity() +
                "}";
    }
}
