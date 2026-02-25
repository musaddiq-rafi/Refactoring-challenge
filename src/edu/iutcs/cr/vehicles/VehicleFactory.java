package edu.iutcs.cr.vehicles;

/**
 * Factory for creating {@link Vehicle} subclass instances from a numeric type code.
 *
 * <p><strong>Why this exists:</strong> {@code SystemFlowRunner.addCar()} contained a
 * long {@code if-else} chain that both <em>selected</em> and <em>constructed</em> vehicle
 * objects. Every time a new vehicle subclass is added the caller method would have needed
 * to change &mdash; violating the <em>Open/Closed Principle</em>.  Moving creation here
 * means adding a new vehicle type requires only a change to this one class.
 *
 * <p><strong>Design Pattern:</strong> Factory Method
 *
 * @author refactored
 */
public class VehicleFactory {

    public static final int BUS       = 1;
    public static final int CAR       = 2;
    public static final int HATCHBACK = 3;
    public static final int SEDAN     = 4;
    public static final int SUV       = 5;

    public static final int MIN_TYPE = BUS;
    public static final int MAX_TYPE = SUV;

    /** Utility class – do not instantiate. */
    private VehicleFactory() {}

    /**
     * Creates and returns a new {@link Vehicle} subclass instance corresponding to
     * {@code type}.  The constructor of each subclass handles its own console prompts.
     *
     * @param type one of the type constants defined in this class
     * @return a fully initialised {@link Vehicle}
     * @throws IllegalArgumentException when {@code type} is out of range
     */
    public static Vehicle create(int type) {
        return switch (type) {
            case BUS       -> new Bus();
            case CAR       -> new Car();
            case HATCHBACK -> new Hatchback();
            case SEDAN     -> new Sedan();
            case SUV       -> new SUV();
            default        -> throw new IllegalArgumentException("Unknown vehicle type: " + type);
        };
    }

    /**
     * Returns the human-readable name for a given type code.
     *
     * @param type one of the type constants defined in this class
     * @return display name string
     */
    public static String getTypeName(int type) {
        return switch (type) {
            case BUS       -> "Bus";
            case CAR       -> "Car";
            case HATCHBACK -> "Hatchback";
            case SEDAN     -> "Sedan";
            case SUV       -> "SUV";
            default        -> "Unknown";
        };
    }
}
