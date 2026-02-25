package edu.iutcs.cr.system;

import edu.iutcs.cr.Invoice;
import edu.iutcs.cr.persons.Buyer;
import edu.iutcs.cr.persons.Seller;
import edu.iutcs.cr.vehicles.Vehicle;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Raian Rahman
 * @since 4/19/2024
 *
 * <p><strong>Refactoring notes (DRY / Single Responsibility):</strong>
 * The original class had four near-identical pairs of {@code save} / {@code load} methods.
 * Each pair repeated the same try-with-resources boilerplate, differing only in the
 * filename and generic type.  This duplication was removed by introducing two private
 * generic helpers &mdash; {@link #saveData(Object, String)} and
 * {@link #loadData(String, Object)} &mdash; that all public methods delegate to.
 * Adding support for a new entity type now requires only a one-line public method,
 * not eight lines of boilerplate.
 */
public class DataStore {

    private static final String INVOICES_FILE = "invoices.txt";
    private static final String BUYERS_FILE   = "buyers.txt";
    private static final String SELLERS_FILE  = "sellers.txt";
    private static final String VEHICLES_FILE = "cars.txt";

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public void saveInvoices(Set<Invoice> invoices)   { saveData(invoices,  INVOICES_FILE); }
    public void saveBuyers(Set<Buyer> buyers)         { saveData(buyers,    BUYERS_FILE);   }
    public void saveSellers(Set<Seller> sellers)      { saveData(sellers,   SELLERS_FILE);  }
    public void saveVehicles(Set<Vehicle> vehicles)   { saveData(vehicles,  VEHICLES_FILE); }

    public Set<Invoice> loadInvoices() {
        return loadData(INVOICES_FILE, new HashSet<>());
    }

    public Set<Buyer> loadBuyers() {
        return loadData(BUYERS_FILE, new HashSet<>());
    }

    public Set<Seller> loadSellers() {
        return loadData(SELLERS_FILE, new HashSet<>());
    }

    public Set<Vehicle> loadVehicles() {
        return loadData(VEHICLES_FILE, new HashSet<>());
    }

    // -------------------------------------------------------------------------
    // Generic helpers (DRY)
    // -------------------------------------------------------------------------

    /**
     * Serialises {@code data} to {@code fileName}.
     *
     * @param data     the object to serialise
     * @param fileName path to the target file
     */
    private void saveData(Object data, String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserialises an object of type {@code T} from {@code fileName}.
     * If the file is missing or corrupt the {@code defaultValue} is persisted and returned.
     *
     * @param <T>          the expected type
     * @param fileName     path to the source file
     * @param defaultValue value to use (and persist) when the file cannot be read
     * @return the deserialised object, or {@code defaultValue} on failure
     */
    @SuppressWarnings("unchecked")
    private <T> T loadData(String fileName, T defaultValue) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (T) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            saveData(defaultValue, fileName);
            return defaultValue;
        }
    }
}
