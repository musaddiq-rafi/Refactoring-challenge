package edu.iutcs.cr.vehicles;

import edu.iutcs.cr.util.InputReader;

import java.io.Serializable;

/**
 * @author Raian Rahman
 * @since 4/18/2024
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
