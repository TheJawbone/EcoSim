package eco_sim;

public class Fuel {

    public static double calculateMass(double volume, double temperature) {
        return calculateDensity(temperature) * volume;
    }

    public static double calculateVolume(double mass, double temperature) {
        return mass / calculateDensity(temperature);
    }

    public static double calculateMixedTemperature(double t1, double vol1, double t2, double vol2) {
        return (t1 * vol1 + t2 * vol2) / (vol1 + vol2);
    }

    public static double calculateDensity(double temperature) {
        return temperature * (-0.8333) + 756.667;
    }
}
