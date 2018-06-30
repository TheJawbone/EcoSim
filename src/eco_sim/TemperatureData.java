package eco_sim;

import java.util.ArrayList;
import java.util.List;

/**
 * TemperatureData class stores various data regarding temperatures in the simulation and renders it accessible.
 */
public class TemperatureData {

    /**
     * Index of the coldest day during the year.
     */
    private int coldestDayNumber;

    /**
     * Number of temperature records.
     */
    private int size;

    /**
     * Maximum temperature of the year.
     */
    private double maxTemperature;

    /**
     * Minimum temperature of the year.
     */
    private double minTemperature;

    /**
     * Temperature amplitude (maxTemperature - minTemperature).
     */
    private double temperatureAmplitude;

    /**
     * Average air temperature calculated from all the records.
     */
    private double avgTemperature;

    /**
     * List of temperature records.
     */
    private List<TemperatureRecord> temperatureRecords;

    /**
     * Class constructor, initializes number of temperature records and the list storing them.
     * @param size Number of temperature records.
     */
    public TemperatureData(int size) {
        this.size = size;
        maxTemperature = Double.MIN_VALUE;
        minTemperature = Double.MAX_VALUE;
        temperatureRecords = new ArrayList<>(size);
    }

    /**
     * Class copy constructor.
     * @param data
     */
    public TemperatureData(TemperatureData data) {
        coldestDayNumber = data.getColdestDayNumber();
        size = data.getSize();
        maxTemperature = data.getMaxTemperature();
        minTemperature = data.getMinTemperature();
        temperatureAmplitude = data.getTemperatureAmplitude();
        avgTemperature = data.getAvgTemperature();
        temperatureRecords = new ArrayList<>();
        for(TemperatureRecord record : data.getTemperatureRecords()) {
            temperatureRecords.add(record);
        }
    }

    /**
     * Updates chosen attribute of the temperature record for the specified day.
     * @param choice Value that determines which attribute to update.
     * @param value New value to be assigned to the attribute.
     * @param date Date used to find the record.
     */
    public void updateTemperatureRecord(int choice, double value, MyCalendar date) {
        for(TemperatureRecord record : temperatureRecords) {
            if(date.equals(record.getTimestamp())) {
                switch(choice) {
                    case TemperatureRecord.AIR_TEMP:
                        record.setAirTemperature(value);
                        break;
                    case TemperatureRecord.FUEL_TEMP:
                        record.setFuelTemperature(value);
                        break;
                    case TemperatureRecord.UNDERGROUND_TEMP:
                        record.setUndergroundTemperature(value);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Finds and returns temperature record with a specified date.
     * @param date Date used to find the record.
     * @return Found temperature record.
     */
    public TemperatureRecord findTemperatureRecord(MyCalendar date) {
        for(TemperatureRecord record : temperatureRecords) {
            if(date.equals(record.getTimestamp())) {
                return record;
            }
        }
        return null;
    }


    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getTemperatureAmplitude() {
        return temperatureAmplitude;
    }

    public void setTemperatureAmplitude(double temperatureAmplitude) {
        this.temperatureAmplitude = temperatureAmplitude;
    }

    public double getAvgTemperature() {
        return avgTemperature;
    }

    public void setAvgTemperature(double avgTemperature) {
        this.avgTemperature = avgTemperature;
    }

    public int getColdestDayNumber() {
        return coldestDayNumber;
    }

    public void setColdestDayNumber(int coldestDayNumber) {
        this.coldestDayNumber = coldestDayNumber;
    }

    public int getSize() {
        return size;
    }

    public List<TemperatureRecord> getTemperatureRecords() {
        return temperatureRecords;
    }
}
