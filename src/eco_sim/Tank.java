package eco_sim;

public class Tank {

    private double tankVolume;
    private double tankRadius;
    private double tankHeight;
    private double tankSurfaceArea;
    private double fuelTemperature;
    private double fuelVolume;
    private double fuelMass;
    private double fuelAmount;
    private double fillFactor;
    private final double steelThermalConductivity = 58;
    private final double fuelSpecificHeat = 2100;
    private double tankThickness;

    public Tank(double tankRadius, double tankHeight, double tankThickness, double initialFillFactor,
                double initialFuelTemperature) {
        this.tankRadius = tankRadius;
        this.tankHeight = tankHeight;
        this.tankThickness = tankThickness;
        this.fuelTemperature = initialFuelTemperature;
        fillFactor = initialFillFactor;
        tankVolume = Math.PI * Math.pow(tankRadius, 2) * tankHeight;
        tankSurfaceArea = 2 * Math.PI * tankRadius * (tankRadius + tankHeight);
        fuelVolume = tankVolume * initialFillFactor;
        fuelAmount = fuelVolume * 1000;
        fuelMass = Fuel.calculateMass(fuelVolume, fuelTemperature);
    }

    public void calculateFuelTemperature(double ambientTemperature) {
        double deltaEnergy = 0.0003 * steelThermalConductivity * tankSurfaceArea
                * (ambientTemperature - fuelTemperature) * 86400 / tankThickness;
        double deltaTemperature = deltaEnergy / (fuelSpecificHeat * fuelMass);
        fuelTemperature += deltaTemperature;
        fuelVolume = Fuel.calculateVolume(fuelMass, fuelTemperature);
    }

    public void refill(double volume, double temperature) {
        double refillMass = Fuel.calculateMass(volume, temperature);
        fuelTemperature = Fuel.calculateMixedTemperature(temperature, volume, fuelTemperature, fuelVolume);
        fuelMass += refillMass;
        fuelVolume = Fuel.calculateVolume(fuelMass, fuelTemperature);
        fuelAmount = fuelVolume * 1000;
    }

    public void subtractFuel(double amount) {
        fuelAmount -= amount;
        fuelVolume = fuelAmount / 1000;
        fillFactor = fuelVolume / tankVolume;
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
