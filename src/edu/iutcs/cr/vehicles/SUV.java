package edu.iutcs.cr.vehicles;

import edu.iutcs.cr.util.InputReader;

import java.io.Serializable;

/**
 * @author Raian Rahman
 * @since 4/19/2024
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
