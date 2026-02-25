package edu.iutcs.cr.vehicles;

import edu.iutcs.cr.util.InputReader;

import java.io.Serializable;

/**
 * @author Raian Rahman
 * @since 4/19/2024
 *
 * <p><strong>Refactoring notes:</strong> Replaced {@code new Scanner(System.in)} with
 * {@link InputReader} singleton; renamed {@code setOffRoad()} (no-arg, console I/O)
 * to {@code readOffRoad()} and added a value-based setter.
 */
public class SUV extends Vehicle implements Serializable {

    private boolean isOffRoad;

    public SUV() {
        super();
        readOffRoad();
    }

    public boolean isOffRoad() {
        return isOffRoad;
    }

    /** Value-based setter – no console I/O. */
    public void setOffRoad(boolean offRoad) {
        this.isOffRoad = offRoad;
    }

    private void readOffRoad() {
        System.out.print("Is the SUV for off-road use? (true/false): ");
        this.isOffRoad = InputReader.getInstance().nextBoolean();
    }

    @Override
    public String toString() {
        return "SUV{" + super.toString() + ", " +
                "isOffRoad=" + isOffRoad() +
                "}";
    }
}
