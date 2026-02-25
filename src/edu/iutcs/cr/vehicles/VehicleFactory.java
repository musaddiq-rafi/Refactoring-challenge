package edu.iutcs.cr.vehicles;

public class VehicleFactory {

    public static final int BUS       = 1;
    public static final int CAR       = 2;
    public static final int HATCHBACK = 3;
    public static final int SEDAN     = 4;
    public static final int SUV       = 5;

    public static final int MIN_TYPE = BUS;
    public static final int MAX_TYPE = SUV;

    private VehicleFactory() {}

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
