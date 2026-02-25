package edu.iutcs.cr.vehicles;

import edu.iutcs.cr.util.InputReader;

import java.io.Serializable;

/**
 * @author Raian Rahman
 * @since 4/19/2024
 *
 * <p><strong>Refactoring notes:</strong>
 * <ul>
 *   <li><strong>Bug fix:</strong> {@code Hatchback} was the only vehicle subclass that did
 *       <em>not</em> implement {@link Serializable}, which would cause a
 *       {@link java.io.NotSerializableException} at runtime when saving the inventory.
 *       Added {@code implements Serializable}.</li>
 *   <li><strong>Bug fix:</strong> The original {@code setCompact()} called
 *       {@code scanner.close()}, which permanently closes {@code System.in} and breaks
 *       all subsequent console reads. Replaced with shared {@link InputReader} singleton
 *       (which is never closed).</li>
 *   <li>Renamed {@code setCompact()} to {@code readCompact()} and added a value-based
 *       {@code setCompact(boolean)} setter.</li>
 * </ul>
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

    /** Value-based setter – no console I/O. */
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
