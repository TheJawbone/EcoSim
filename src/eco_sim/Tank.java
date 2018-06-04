package eco_sim;

public class Tank {

    private double tankVolume;
    private double tankRadius;
    private double tankHeight;
    private double fuelTemperature;
    private double fuelVolume;
    private double fuelMass;
    private double fuelDensity;
    private double tankThickness;
    private double temperatureExchangeConstant;

    public Tank() {
        tankRadius = 3;
        tankHeight = 10;
        tankVolume = Math.PI * Math.pow(tankRadius, 2) * tankHeight;
        tankThickness = 0.02;
        fuelVolume = tankVolume;
        fuelTemperature = 15;
        calculateFuelDensity();
        calculateFuelMass();
        temperatureExchangeConstant = 1;
    }

    private void calculateFuelDensity() {
        fuelDensity = fuelTemperature * (-0.8333) + 756.667;
    }

    private void calculateFuelMass() {
        fuelMass = fuelDensity * fuelVolume;
    }

    public void calculateFuelTemperature(double ambientTemperature) {
        double delta = temperatureExchangeConstant * (2 * Math.PI * Math.pow(tankRadius, 2)
                + 2 * Math.PI * tankRadius * tankHeight)
                * (ambientTemperature - fuelTemperature) / (tankThickness * fuelMass);
        fuelTemperature += delta;
    }

    public double getFuelTemperature() {
        return fuelTemperature;
    }
}
