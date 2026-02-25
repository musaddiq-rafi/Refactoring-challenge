package edu.iutcs.cr.system;

import edu.iutcs.cr.Invoice;
import edu.iutcs.cr.persons.Buyer;
import edu.iutcs.cr.persons.Seller;
import edu.iutcs.cr.vehicles.Vehicle;

import java.io.Serializable;
import java.util.Set;

import static java.util.Objects.isNull;

/**
 * @author Raian Rahman
 * @since 4/19/2024
 *
 * <p><strong>Refactoring notes (Singleton Pattern – thread safety):</strong>
 * The original singleton used a simple null-check without synchronisation:
 * <pre>
 *     if (isNull(instance)) { instance = new SystemDatabase(); }
 * </pre>
 * In a multi-threaded environment two threads could both pass the null-check
 * simultaneously and create two instances.  The field is now declared
 * {@code volatile} and the check uses double-checked locking, which is the
 * idiomatic thread-safe lazy-initialisation pattern in Java.
 *
 * <p>All show* methods extract entity display logic that belongs here (the database
 * knows how to display its contents) – no functional change, names clarified.
 */
public class SystemDatabase implements Serializable {

    private Set<Buyer>   buyers;
    private Set<Seller>  sellers;
    private Set<Vehicle> vehicles;
    private Set<Invoice> invoices;

    // volatile ensures the write to `instance` is visible across threads
    private static volatile SystemDatabase instance;

    private SystemDatabase() {
        DataStore dataStore = new DataStore();
        buyers   = dataStore.loadBuyers();
        sellers  = dataStore.loadSellers();
        vehicles = dataStore.loadVehicles();
        invoices = dataStore.loadInvoices();
    }

    /** Thread-safe lazy singleton getter using double-checked locking. */
    public static SystemDatabase getInstance() {
        if (isNull(instance)) {
            synchronized (SystemDatabase.class) {
                if (isNull(instance)) {
                    instance = new SystemDatabase();
                }
            }
        }
        return instance;
    }

    public void saveSystem() {
        DataStore dataStore = new DataStore();
        dataStore.saveBuyers(buyers);
        dataStore.saveSellers(sellers);
        dataStore.saveVehicles(vehicles);
        dataStore.saveInvoices(invoices);
    }

    // -------------------------------------------------------------------------
    // Collection accessors
    // -------------------------------------------------------------------------

    public Set<Buyer>   getBuyers()   { return buyers;   }
    public Set<Seller>  getSellers()  { return sellers;  }
    public Set<Vehicle> getVehicles() { return vehicles; }
    public Set<Invoice> getInvoices() { return invoices; }

    // -------------------------------------------------------------------------
    // Display helpers
    // -------------------------------------------------------------------------

    public void showInventory() {
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles present in system");
            return;
        }
        vehicles.forEach(v -> System.out.println(v.toString()));
    }

    public void showBuyerList() {
        if (buyers.isEmpty()) {
            System.out.println("No buyer present in system");
            return;
        }
        buyers.forEach(b -> System.out.println(b.toString()));
    }

    public void showSellerList() {
        if (sellers.isEmpty()) {
            System.out.println("No seller present in system");
            return;
        }
        sellers.forEach(s -> System.out.println(s.toString()));
    }

    public void showInvoices() {
        if (invoices.isEmpty()) {
            System.out.println("No invoice found in system");
            return;
        }
        invoices.forEach(i -> {
            i.printInvoice();
            System.out.println("\n\n\n");
        });
    }

    // -------------------------------------------------------------------------
    // Lookup helpers
    // -------------------------------------------------------------------------

    public Vehicle findVehicleByRegistrationNumber(String registrationNumber) {
        Vehicle key = new Vehicle(registrationNumber);
        return vehicles.stream().filter(v -> v.equals(key)).findFirst().orElse(null);
    }

    public Buyer findBuyerById(String id) {
        Buyer key = new Buyer(id);
        return buyers.stream().filter(b -> b.equals(key)).findFirst().orElse(null);
    }

    public Seller findSellerById(String id) {
        Seller key = new Seller(id);
        return sellers.stream().filter(s -> s.equals(key)).findFirst().orElse(null);
    }
}
