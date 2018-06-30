package eco_sim;

/**
 * Fuel class is used to calculate certain magnitudes regarding fuel.
 */
public class Fuel {

    /**
     * Calculate mass of the fuel based on its volume and temperature.
     * @param volume Fuel volume.
     * @param temperature Fuel temperature.
     * @return Mass of the fuel.
     */
    public static double calculateMass(double volume, double temperature) {
        return calculateDensity(temperature) * volume;
    }

    /**
     * Calculate volume of the fuel based on its mass and temperature.
     * @param mass Fuel mass.
     * @param temperature Fuel temperature
     * @return Volume of the fuel.
     */
    public static double calculateVolume(double mass, double temperature) {
        return mass / calculateDensity(temperature);
    }

    /**
     * Calculate the temperature of the two combined units of fuel.
     * @param t1 Temperature of the first fuel unit.
     * @param vol1 Volume of the first fuel unit.
     * @param t2 Temperature of the second fuel unit.
     * @param vol2 Volume of the second fuel unit.
     * @return Resultant temperature.
     */
    public static double calculateMixedTemperature(double t1, double vol1, double t2, double vol2) {
        return (t1 * vol1 + t2 * vol2) / (vol1 + vol2);
    }

    /**
     * Calculate the density of the fuel based on its temperature.
     * @param temperature Fuel temperature.
     * @return Density of the fuel.
     */
    public static double calculateDensity(double temperature) {
        return temperature * (-0.8333) + 756.667;
    }
}
