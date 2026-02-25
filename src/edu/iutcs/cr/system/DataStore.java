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
 */
public class DataStore {

    private static final String INVOICES_FILE = "invoices.txt";
    private static final String BUYERS_FILE   = "buyers.txt";
    private static final String SELLERS_FILE  = "sellers.txt";
    private static final String VEHICLES_FILE = "cars.txt";

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

    private void saveData(Object data, String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
