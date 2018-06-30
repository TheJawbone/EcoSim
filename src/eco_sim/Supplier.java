package eco_sim;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Supplier class represents a fuel supplier.
 */
public class Supplier {

    /**
     * Name of the supplier.
     */
    private String name;

    /**
     * Chance for any driver working for the supplier to steal fuel during delivery.
     */
    private double theftChance;

    /**
     * List of drivers working for the supplier.
     */
    private List<TruckDriver> drivers;

    /**
     * Class constructor, initializes name, theft chance and driver list.
     * @param name
     * @param theftChance
     * @param drivers
     */
    public Supplier(String name, double theftChance, List<TruckDriver> drivers) {
        this.name = name;
        this.theftChance = theftChance;
        this.drivers = drivers;
    }

    /**
     * Class constructor, initializes name and theft chane.
     * @param name
     * @param theftChance
     */
    public Supplier(String name, double theftChance) {
        this.name = name;
        this.theftChance = theftChance;
        this.drivers = new ArrayList<>();
    }

    /**
     * Class constructor, loads the supplier's parameters from the specified file.
     * @param filename Name of the file for the supplier's parameters to be loaded from.
     */
    public Supplier(String filename) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
            name = reader.readLine();
            theftChance = Double.parseDouble(reader.readLine());
            drivers = new ArrayList<>();
            int driversNumber = Integer.parseInt(reader.readLine());
            for(int i = 0; i < driversNumber; i++) {
                String driverData = reader.readLine();
                String[] driverDataArray = driverData.split(" ");
                TruckDriver driver = new TruckDriver(driverDataArray[0], Integer.parseInt(driverDataArray[1]),
                        Double.parseDouble(driverDataArray[2]));
                drivers.add(driver);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public double getTheftChance() {
        return theftChance;
    }

    public List<TruckDriver> getDrivers() {
        return drivers;
    }
}
