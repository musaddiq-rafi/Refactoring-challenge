package edu.iutcs.cr.vehicles;

import edu.iutcs.cr.util.InputReader;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Raian Rahman
 * @since 4/18/2024
 *
 * <p><strong>Refactoring notes:</strong>
 * <ul>
 *   <li>Replaced per-method {@code new Scanner(System.in)} with shared {@link InputReader}
 *       singleton, removing the resource-management issue present in every original setter.</li>
 *   <li>Renamed the no-arg, console-prompting methods from {@code setX()} to {@code readX()}
 *       (Single Responsibility Principle: setters should set a given value, not prompt
 *       the user for one).</li>
 *   <li>Added overloaded, value-based setters ({@code setMake(String)}, etc.) so the class
 *       can be constructed programmatically without console interaction.</li>
 * </ul>
 */
public class Vehicle implements Serializable {

    private String make;
    private String model;
    private String year;
    private double price;
    private boolean available;
    private String registrationNumber;

    /** Full constructor: prompts the console for all fields. */
    public Vehicle() {
        readRegistrationNumber();
        readMake();
        readModel();
        readYear();
        readPrice();
        this.available = true;
    }

    /** Lookup constructor: sets only the registration number for equality checks. */
    public Vehicle(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public String getRegistrationNumber() { return registrationNumber; }
    public String getMake()               { return make; }
    public String getModel()              { return model; }
    public String getYear()               { return year; }
    public double getPrice()              { return price; }
    public boolean isAvailable()          { return available; }

    // -------------------------------------------------------------------------
    // Value-based setters (no console I/O)
    // -------------------------------------------------------------------------

    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public void setMake(String make)   { this.make  = make;  }
    public void setModel(String model) { this.model = model; }
    public void setYear(String year)   { this.year  = year;  }
    public void setPrice(double price) { this.price = price; }

    /** Marks this vehicle as sold / unavailable. */
    public void setUnavailable() {
        this.available = false;
    }

    // -------------------------------------------------------------------------
    // Console-reading helpers (called by the full constructor)
    // -------------------------------------------------------------------------

    private void readRegistrationNumber() {
        InputReader reader = InputReader.getInstance();
        while (this.registrationNumber == null || registrationNumber.isBlank()) {
            System.out.print("Enter registration number: ");
            this.registrationNumber = reader.nextLine();
            if (registrationNumber.isBlank()) System.out.println("Registration number is mandatory!");
        }
    }

    private void readMake() {
        InputReader reader = InputReader.getInstance();
        while (this.make == null || this.make.isBlank()) {
            System.out.print("Enter make: ");
            this.make = reader.nextLine();
            if (make.isBlank()) System.out.println("Make is mandatory!");
        }
    }

    private void readModel() {
        InputReader reader = InputReader.getInstance();
        while (this.model == null || this.model.isBlank()) {
            System.out.print("Enter model: ");
            this.model = reader.nextLine();
            if (model.isBlank()) System.out.println("Model is mandatory!");
        }
    }

    private void readYear() {
        InputReader reader = InputReader.getInstance();
        while (this.year == null || this.year.isBlank()) {
            System.out.print("Enter year: ");
            this.year = reader.nextLine();
            if (year.isBlank()) System.out.println("Year is mandatory!");
        }
    }

    private void readPrice() {
        System.out.print("Enter price: ");
        this.price = InputReader.getInstance().nextDouble();
    }

    // -------------------------------------------------------------------------
    // Object overrides
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year='" + year + '\'' +
                ", price=" + price +
                ", available=" + available +
                ", registrationNumber='" + registrationNumber + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle vehicle)) return false;
        return Objects.equals(this.registrationNumber, vehicle.registrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber);
    }
}
