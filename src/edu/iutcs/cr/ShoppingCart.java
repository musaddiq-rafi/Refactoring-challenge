package edu.iutcs.cr;

import edu.iutcs.cr.system.SystemDatabase;
import edu.iutcs.cr.util.InputReader;
import edu.iutcs.cr.vehicles.Vehicle;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

/**
 * @author Raian Rahman
 * @since 4/19/2024
 */
public class ShoppingCart implements Serializable {

    private final Set<Vehicle> vehicles;
    private final SystemDatabase database;

    public ShoppingCart() {
        this.vehicles = new HashSet<>();
        this.database = SystemDatabase.getInstance();
    }

    public Set<Vehicle> getVehicles() {
        return this.vehicles;
    }

    public void addItem() {
        System.out.print("Enter registration number of vehicle: ");
        String registrationNumber = InputReader.getInstance().nextLine();

        Vehicle vehicle = database.findVehicleByRegistrationNumber(registrationNumber);

        if (isNull(vehicle) || !vehicle.isAvailable()) {
            System.out.println("Vehicle not available");
            return;
        }

        vehicles.add(vehicle);
    }

    public void removeItem() {
        System.out.print("Enter the registration number of the vehicle: ");
        String registrationNumber = InputReader.getInstance().nextLine();
        vehicles.remove(new Vehicle(registrationNumber));
    }

    public void viewCart() {
        System.out.println("\n\nShopping cart\n\n");

        if (vehicles.isEmpty()) {
            System.out.println("Cart is empty");
            return;
        }

        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle.toString());
        }
    }
}
