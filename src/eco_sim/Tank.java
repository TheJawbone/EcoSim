package eco_sim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Tank class represents a fuel tank on the fuel station.
 */
public class Tank {

    /**
     * Index of the tank.
     */
    private int number;

    /**
     * Volume of the tank.
     */
    private double tankVolume;

    /**
     * Radius of the tank's base.
     */
    private double tankRadius;

    /**
     * Height of the tank.
     */
    private double tankHeight;

    /**
     * Surface area of the tank.
     */
    private double tankSurfaceArea;

    /**
     * Temperature of the fuel inside the tank.
     */
    private double fuelTemperature;

    /**
     * Measured height of the fuel inside the tank (includes deformations).
     */
    private double fuelHeightMeasured;

    /**
     * Expected height of the fuel inside the tank (excludes deformations).
     */
    private double fuelHeightExpected;

    /**
     * Measured volume of the fuel inside the tank (includes deformations).
     */
    private double fuelVolumeMeasured;

    /**
     * Expected volume of the fuel inside the tank (excludes deformations).
     */
    private double fuelVolumeExpected;

    /**
     * Mass of the fuel inside the tank.
     */
    private double fuelMass;

    /**
     * Tank's fill factor (quotient of the tank's volume and the volume of the fuel).
     */
    private double fillFactor;

    /**
     * Thickness of the tank's wall.
     */
    private double tankThickness;

    /**
     * Chance for the leak to appear during one day.
     */
    private double leakChance;

    /**
     * Combined intensity of all the leaks in the tank.
     */
    private double leakIntensity;

    /**
     * Thermal conductivity of steel (material with witch the tank was built).
     */
    private final double steelThermalConductivity = 58;

    /**
     * Specific heat of liquid petrol.
     */
    private final double fuelSpecificHeat = 2100;

    /**
     * Name of the station.
     */
    private String stationName;

    /**
     * Writer object used to write generated data to a file after the simulation.
     */
    private PrintWriter writer;

    /**
     * List of values to be written to the file at the end of the simulation.
     */
    private List<String> dataToWrite;

    /**
     * List of the tank's deformations.
     */
    private List<Deformation> deformations;

    /**
     * TemperatureData object keeping the data regarding air, underground and fuel temperatures.
     */
    private TemperatureData data;

    /**
     * Deformation class represents a deformation in the tank - height on which it is present and it's impact on the
     * fuel level.
     */
    public static class Deformation {
        public double height;
        public double value;
    }

    /**
     * Class constructor, initializes certain class fields and calculates/generates values of others.
     * @param stationName Name of the station the tank belongs to.
     * @param number Tank's index.
     * @param tankRadius Radius of the tank's base.
     * @param tankHeight Height of the tank.
     * @param tankThickness Thickness of the tank's wall.
     * @param initialFillFactor Factor that tells how much fuel should there be in the tank at the moment of creation.
     * @param data TemperatureData object holding data regarding air, underground and fuel temperatures.
     * @param deformations List of the tank's deformations.
     * @param leakChance Chance of a leak to occur.
     */
    public Tank(String stationName, int number, double tankRadius, double tankHeight, double tankThickness,
                double initialFillFactor, TemperatureData data, List<Deformation> deformations, double leakChance) {

        this.stationName = stationName;
        this.number = number;
        this.tankRadius = tankRadius;
        this.tankHeight = tankHeight;
        this.tankThickness = tankThickness;
        this.data = new TemperatureData(data);
        this.leakChance = leakChance;
        this.fuelTemperature = data.getTemperatureRecords().get(0).getUndergroundTemperature();
        if(deformations.size() == 1 && deformations.get(0).height < 0) {
            this.deformations = new ArrayList<>();
            generateDeformations();
        } else {
            this.deformations = deformations;
        }
        this.dataToWrite = new ArrayList<>();
        dataToWrite.add("Date\tAir temperature\tUnderground temperature\tFuel temperature (start of day)\t" +
                "Fuel volume (start of day)\tAmount of sales\tSupplier name\tDriver name\tRefill volume\t" +
                "Refill temperature\tFuel temperature (end of day)\tFuel volume (end of day)");
        fillFactor = initialFillFactor;
        tankVolume = Math.PI * Math.pow(tankRadius, 2) * tankHeight;
        tankSurfaceArea = 2 * Math.PI * tankRadius * (tankRadius + tankHeight);
        fuelVolumeExpected = tankVolume * initialFillFactor;
        measureFuelVolume();
    }

    /**
     * Randomly generates the tank's deformations.
     */
    private void generateDeformations() {
        Random random = new Random();
        for(int i = 0; i < tankHeight; i++) {
            double probabilitySeed = random.nextDouble();
            if(probabilitySeed > 0.8) {
                Deformation deformation = new Deformation();
                deformation.height = i;
                double deformationValue = random.nextDouble() * 0.4 - 0.2;
                deformation.value = deformationValue;
                deformations.add(deformation);
                System.out.println("Deformation generated: " + stationName + ", tank" + number + ", height: " + i
                        + ", value: " + deformationValue);
            }
        }
    }

    /**
     * Creates and/or applies effects of the fuel leaks.
     */
    public void applyLeaks() {
        Random random = new Random();
        if(random.nextDouble() < leakChance) {
            leakIntensity += random.nextDouble() / 10;
            System.out.println("Leak occurred, station: " + stationName + ", tank" + number + ", new intensity: " + leakIntensity);
        }
        fuelVolumeExpected -= leakIntensity;
        if(fuelVolumeExpected <= 0) {
            fuelVolumeExpected = 0.1;
        }
        measureFuelVolume();
    }

    /**
     * Calculates temperature of the fuel inside the tank based on the ambient temperature.
     * @param date Current day of the simulation.
     */
    public void calculateFuelTemperature(MyCalendar date) {
        TemperatureRecord record = data.findTemperatureRecord(date);
        double ambientTemperature = record.getUndergroundTemperature();
        double deltaEnergy = 0.0003 * steelThermalConductivity * tankSurfaceArea
                * (ambientTemperature - fuelTemperature) * 86400 / tankThickness;
        double deltaTemperature = deltaEnergy / (fuelSpecificHeat * fuelMass);
        fuelTemperature += deltaTemperature;
        record.setFuelTemperature(fuelTemperature);
        measureFuelVolume();
        dataToWrite.add("\r\n");
        dataToWrite.add(date.toString() + "\t");
        dataToWrite.add(String.valueOf(data.findTemperatureRecord(date).getAirTemperature()) + "\t");
        dataToWrite.add(String.valueOf(ambientTemperature) + "\t");
        dataToWrite.add(String.valueOf(fuelTemperature) + "\t");
        dataToWrite.add(String.valueOf(fuelVolumeMeasured) + "\t");
    }

    /**
     * Calculates expected and actual fuel height.
     */
    private void measureFuelHeight() {
        fuelHeightExpected = fuelVolumeExpected / (Math.PI * Math.pow(tankRadius, 2));
        fuelHeightMeasured = fuelHeightExpected;
        for(Deformation deformation : deformations) {
            if(fuelHeightMeasured >= deformation.height) {
                fuelHeightMeasured += deformation.value;
            }
        }
    }

    /**
     * Calculates the expected and actual volume of the fuel inside the tank.
     */
    private void measureFuelVolume() {
        fuelMass = Fuel.calculateMass(fuelVolumeExpected, fuelTemperature);
        fuelVolumeExpected = Fuel.calculateVolume(fuelMass, fuelTemperature);
        measureFuelHeight();
        fuelVolumeMeasured = Math.PI * Math.pow(tankRadius, 2) * fuelHeightMeasured;
    }

    /**
     * Applies sales by subtracting generated amount of fuel from the tank.
     * @param amount Amount of fuel to be sold (subtracted).
     */
    public void applySales(double amount) {
        fuelVolumeExpected -= amount;
        if(fuelVolumeExpected <= 0) {
            fuelVolumeExpected = 0.1;
        }
        fuelMass = Fuel.calculateMass(fuelVolumeExpected, fuelTemperature);
        fillFactor = fuelVolumeExpected / tankVolume;
        dataToWrite.add(String.valueOf(amount) + "\t");
        measureFuelVolume();
    }

    /**
     * Simulates fuel delivery by adding the refill's fuel to the tank and calculating new mass, volume and temperature
     * of the mixture.
     * @param refillDeclaredVolume Declared volume of the delivered fuel.
     * @param stolenVolume Volume of fuel that has been stolen during the delivery.
     * @param supplier Name of the supplier.
     * @param driver Name of the supply truck driver.
     * @param date Current date of the simulation.
     */
    public void refill(double refillDeclaredVolume, double stolenVolume, String supplier, String driver, MyCalendar date) {

        TemperatureRecord record = data.findTemperatureRecord(date);
        double refillTemperature = record.getAirTemperature();

        dataToWrite.add(supplier + "\t");
        dataToWrite.add(driver + "\t");
        dataToWrite.add(String.valueOf(refillDeclaredVolume) + "\t");
        dataToWrite.add(String.valueOf(refillTemperature) + "\t");

        fuelMass = Fuel.calculateMass(fuelVolumeExpected, fuelTemperature);
        double refillMass = Fuel.calculateMass(refillDeclaredVolume - stolenVolume, 15);
        double refillActualVolume = Fuel.calculateVolume(refillMass, refillTemperature);
        fuelMass += refillMass;
        fuelTemperature = Fuel.calculateMixedTemperature(refillTemperature, refillActualVolume, fuelTemperature, fuelVolumeExpected);
        fuelVolumeExpected = Fuel.calculateVolume(fuelMass, fuelTemperature);
        measureFuelVolume();
        fillFactor = fuelHeightMeasured / tankHeight;

        dataToWrite.add(String.valueOf(fuelTemperature) + "\t");
        dataToWrite.add(String.valueOf(fuelVolumeMeasured) + "\t");
    }

    /**
     * Writes all the generated and stored data to a file.
     */
    public void writeData() {
        try {
            String fileName = "data\\generated_data\\" + stationName + "_tank" + number + "_data.txt";
            writer = new PrintWriter(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        for(String entry : dataToWrite) {
            writer.print(entry);
        }
        writer.close();
    }

    public double getFillFactor() {
        return fillFactor;
    }

    public double getTankVolume() {
        return tankVolume;
    }
}
