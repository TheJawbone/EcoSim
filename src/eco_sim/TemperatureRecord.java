package eco_sim;

/**
 * TemperatureRecord class is used to store a single temperature record containing air temperature, underground
 * temperature and the temperature of the fuel inside a tank during a single day.
 */
public class TemperatureRecord {

    /**
     * Air temperature.
     */
    private double airTemperature;

    /**
     * Underground temperature.
     */
    private double undergroundTemperature;

    /**
     * Fuel temperature.
     */
    private double fuelTemperature;

    /**
     * Date for which the object has been created.
     */
    private MyCalendar timestamp;

    /**
     * Alias for the choice of air temperature.
     */
    public static final int AIR_TEMP = 0;

    /**
     * Alias for the choice of the underground temperature.
     */
    public static final int UNDERGROUND_TEMP = 1;

    /**
     * Alias for the choice of the fuel temperature.
     */
    public static final int FUEL_TEMP = 2;

    /**
     * Class constructor.
     * @param date
     */
    public TemperatureRecord(MyCalendar date) {
        timestamp = new MyCalendar();
        timestamp.set(date);
    }

    public double getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(double airTemperature) {
        this.airTemperature = airTemperature;
    }

    public double getUndergroundTemperature() {
        return undergroundTemperature;
    }

    public void setUndergroundTemperature(double undergroundTemperature) {
        this.undergroundTemperature = undergroundTemperature;
    }

    public void setFuelTemperature(double fuelTemperature) {
        this.fuelTemperature = fuelTemperature;
    }

    public MyCalendar getTimestamp() {
        return timestamp;
    }
}
