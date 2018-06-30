package eco_sim;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * FuelStation class represents a fuel station.
 */
public class FuelStation {

    /**
     * Name of the fuel station.
     */
    private String name;

    /**
     * List of the station's fuel tanks.
     */
    private List<Tank> tanks;

    /**
     * Factor that determines how much fuel will be sold daily on avarage.
     */
    private double fuelSalesFactor;

    /**
     * Object used for generating randomized values.
     */
    private Random random;

    /**
     * List of fuel suppliers.
     */
    private List<Supplier> suppliers;

    /**
     * Main constructor that loads the station's parameters from a file.
     * @param fileName Name of the file containing the station's parameters.
     */
    public FuelStation(String fileName) {

        File file = new File(fileName);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            name = br.readLine();
            fuelSalesFactor = Double.parseDouble(br.readLine());
            tanks = new ArrayList<>();
            suppliers = new ArrayList<>();
            random = new Random();
            int climateOffset = Integer.parseInt(br.readLine());
            int numberOfTanks = Integer.parseInt(br.readLine());
            TemperatureGenerator generator = new TemperatureGenerator();
            TemperatureData data = generator.generateAnnualData(2000, 2.5, climateOffset);

            for (int i = 0; i < numberOfTanks; i++) {
                br.readLine();
                double tankRadius = Double.parseDouble(br.readLine());
                double tankHeight = Double.parseDouble(br.readLine());
                double tankThickness = Double.parseDouble(br.readLine());
                double initialFillFactor = Double.parseDouble(br.readLine());
                double leakChance = Double.parseDouble(br.readLine());
                int deformationsNumber = Integer.parseInt(br.readLine());
                List<Tank.Deformation> deformations = new ArrayList<>();
                for(int j = 0; j < deformationsNumber; j++) {
                    Tank.Deformation deformation = new Tank.Deformation();
                    deformation.height = Double.parseDouble(br.readLine());
                    deformation.value = Double.parseDouble(br.readLine());
                    deformations.add(deformation);
                }
                Tank tank = new Tank(name, i + 1, tankRadius, tankHeight, tankThickness, initialFillFactor, data,
                        deformations, leakChance);
                tanks.add(tank);
            }
            br.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    /**
     * Update the state of each tank of the station.
     * @param date Current date of the simulation.
     */
    public void update(MyCalendar date) {

        // For each tank in the station...
        for(Tank tank : tanks) {

            // Calculate temperature
            tank.calculateFuelTemperature(date);

            // Apply leaks
            tank.applyLeaks();

            // Apply sales
            double salesAmount = (5 + random.nextDouble() * 5) * fuelSalesFactor;
            tank.applySales(salesAmount);

            // Resupply
            if (tank.getFillFactor() < 0.2) {

                // Pick the supplier
                Supplier supplier = suppliers.get(ThreadLocalRandom.current().nextInt(0, suppliers.size()));

                // Pick the driver and check if he steals fuel. If yes, calculate stolen amount.
                int driverNumber = ThreadLocalRandom.current().nextInt(0, supplier.getDrivers().size());
                double randomSeed = random.nextDouble();
                int stolenPercentageAmount = 0;
                if(randomSeed <= supplier.getDrivers().get(driverNumber).getTheftChance() * supplier.getTheftChance()) {
                    stolenPercentageAmount = ThreadLocalRandom.current().nextInt(1, 10);
                }
                tank.refill(tank.getTankVolume() * 0.6, tank.getTankVolume() * stolenPercentageAmount / 100 * 0.6,
                        supplier.getName(), supplier.getDrivers().get(driverNumber).getName(), date);
            } else {
                tank.refill(0, 0, " ", " ", date);
            }
        }
    }

    public List<Tank> getTanks() {
        return tanks;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }
}
