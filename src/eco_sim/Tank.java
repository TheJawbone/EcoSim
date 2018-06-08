package eco_sim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Tank {

    private String stationName;
    private double tankVolume;
    private double tankRadius;
    private double tankHeight;
    private double tankSurfaceArea;
    private double fuelTemperature;
    private double fuelVolume;
    private double fuelMass;
    private double fillFactor;
    private final double steelThermalConductivity = 58;
    private final double fuelSpecificHeat = 2100;
    private double tankThickness;
    private PrintWriter refuelWriter;

    public Tank(String stationName, double tankRadius, double tankHeight, double tankThickness,
                double initialFillFactor, double initialFuelTemperature) {
        this.stationName = stationName;
        this.tankRadius = tankRadius;
        this.tankHeight = tankHeight;
        this.tankThickness = tankThickness;
        this.fuelTemperature = initialFuelTemperature;
        fillFactor = initialFillFactor;
        tankVolume = Math.PI * Math.pow(tankRadius, 2) * tankHeight;
        tankSurfaceArea = 2 * Math.PI * tankRadius * (tankRadius + tankHeight);
        fuelVolume = tankVolume * initialFillFactor;
        fuelMass = Fuel.calculateMass(fuelVolume, fuelTemperature);
        try {
            String fileName = stationName + "_refill_data.txt";
            refuelWriter = new PrintWriter(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void calculateFuelTemperature(double ambientTemperature) {
        double deltaEnergy = 0.0003 * steelThermalConductivity * tankSurfaceArea
                * (ambientTemperature - fuelTemperature) * 86400 / tankThickness;
        double deltaTemperature = deltaEnergy / (fuelSpecificHeat * fuelMass);
        fuelTemperature += deltaTemperature;
        fuelVolume = Fuel.calculateVolume(fuelMass, fuelTemperature);
    }

    public void refill(double refillDeclaredVolume, double refillTemperature) {

        refuelWriter.print(fuelVolume + "\t" + fuelTemperature + "\t" + refillDeclaredVolume + "\t" + refillTemperature + "\t");

        fuelMass = Fuel.calculateMass(fuelVolume, fuelTemperature);
        double refillMass = Fuel.calculateMass(refillDeclaredVolume, 15);
        double refillActualVolume = Fuel.calculateVolume(refillMass, refillTemperature);
        fuelMass += refillMass;
        fuelTemperature = Fuel.calculateMixedTemperature(refillTemperature, refillActualVolume, fuelTemperature, fuelVolume);
        fuelVolume = Fuel.calculateVolume(fuelMass, fuelTemperature);
        fillFactor = fuelVolume / tankVolume;

        refuelWriter.print(fuelVolume + "\t" + fuelTemperature + "\r\n");
    }

    public void subtractFuel(double amount) {
        fuelVolume -= amount;
        fuelMass = Fuel.calculateMass(fuelVolume, fuelTemperature);
        fillFactor = fuelVolume / tankVolume;
    }

    public void endSimulation() {
        refuelWriter.close();
    }

    public double getFuelTemperature() {
        return fuelTemperature;
    }

    public double getFillFactor() {
        return fillFactor;
    }

    public double getTankVolume() {
        return tankVolume;
    }
}
