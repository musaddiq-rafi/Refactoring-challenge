package edu.iutcs.cr.vehicles;

import edu.iutcs.cr.util.InputReader;

import java.io.Serializable;

/**
 * @author Raian Rahman
 * @since 4/19/2024
 */
public class Hatchback extends Vehicle implements Serializable {

    private boolean isCompact;

    public Hatchback() {
        super();
        readCompact();
    }

    public boolean isCompact() {
        return isCompact;
    }

    public void setCompact(boolean compact) {
        this.isCompact = compact;
    }

    private void readCompact() {
        System.out.print("Is the hatchback compact? (true/false): ");
        this.isCompact = InputReader.getInstance().nextBoolean();
    }

    @Override
    public String toString() {
        return "Hatchback{" + super.toString() + ", " +
                "isCompact=" + isCompact() +
                "}";
    }
}
