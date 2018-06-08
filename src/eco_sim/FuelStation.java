package eco_sim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

public class FuelStation {

    private String name;
    private Tank tank;
    private double fuelSalesFactor;
    private Random random;
    private PrintWriter salesWriter;

    public FuelStation(String name, Tank tank, float fuelSalesFactor) {
        this.name = name;
        this.tank = tank;
        this.fuelSalesFactor = fuelSalesFactor;
        random = new Random();
        try {
            salesWriter = new PrintWriter(new File(name + "_sales_data.txt"));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(double undergroundTemperature, double airTemperature) {
        tank.calculateFuelTemperature(undergroundTemperature);
        applySales();
        if(tank.getFillFactor() < 0.2) {
            refill(airTemperature);
        }
    }

    /**
     * Simulates daily sales by generating a random value based on sales factor.
     */
    private void applySales() {
        double salesAmount = (5 + random.nextDouble() * 5) * fuelSalesFactor;
        salesWriter.println(salesAmount);
        tank.subtractFuel(salesAmount);
    }

    private void refill(double airTemperature) {
        tank.refill(tank.getTankVolume() * 0.6, airTemperature);
        System.out.println("REFILL!!!");
    }

    public void endSimulation() {
        salesWriter.close();
    }

    public Tank getTank() {
        return tank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
