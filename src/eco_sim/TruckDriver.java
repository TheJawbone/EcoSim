package eco_sim;

/**
 * TruckDriver class represents a driver working for a supplier, whose task is to deliver fuel to the stations.
 */
public class TruckDriver {

    /**
     * Driver's name.
     */
    private String name;

    /**
     * Driver's age.
     */
    private int age;

    /**
     * Chance for the driver to steal fuel during the delivery.
     */
    private double theftChance;

    /**
     * Class constructor, initializes it's fields.
     * @param name Driver name.
     * @param age Driver age.
     * @param theftChance Driver theft chance.
     */
    public TruckDriver(String name, int age, double theftChance) {
        this.name = name;
        this.age = age;
        this.theftChance = theftChance;
    }

    public String getName() {
        return name;
    }

    public double getTheftChance() {
        return theftChance;
    }
}
