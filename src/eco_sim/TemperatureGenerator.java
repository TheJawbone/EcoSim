package eco_sim;

import java.util.Calendar;
import java.util.Random;

/**
 * TemperatureGenerator class is used to generate annual data regarding air and underground temperatures as well as
 * some other temperature statistics.
 */
public class TemperatureGenerator {

    /**
     * Date previous to the current by one hour.
     */
    private MyCalendar prevDate;

    /**
     * Random object used to generate randomized values.
     */
    private Random random;

    /**
     * TemperatureData object used to store generated temperatures.
     */
    private TemperatureData temperatureData;

    /**
     * Global offset of the generated temperatures.
     */
    private double temperatureOffsetGlobal;

    /**
     * Global fluctuation factor of the generated temperatures.
     */
    private double temperatureFluctuationFactorGlobal;

    /**
     * Monthly offset of the generated temperatures.
     */
    private double temperatureOffsetMonthly;

    /**
     * Monthly fluctuation factor of the generated temperatures.
     */
    private double temperatureFluctuationFactorMonthly;

    /**
     * Daily offset of the generated temperatures.
     */
    private double temperatureOffsetDaily;

    /**
     * Daily fluctuation factor of the generated temperatures.
     */
    private double temperatureFluctuationFactorDaily;

    /**
     * Hourly offset of the generated temperatures.
     */
    private double temperatureOffsetHourly;

    /**
     * Array of peak temperatures for each month.
     */
    private double[] peakTemperatures;

    /**
     * Array of bottom temperatures for each month.
     */
    private double[] bottomTemperatures;

    /**
     * Thermal diffusivity of the soil.
     */
    private final double alpha = 0.01;

    /**
     * Class constructor, initializes certain fields.
     */
    public TemperatureGenerator() {

        random = new Random();

        peakTemperatures = new double[] {7.1, 8, 11.3, 16, 18.3, 21.5, 24.7, 21.8, 22.9, 17.9, 14.4, 11.5};
        bottomTemperatures = new double[] {-11.7, -9.5, -4, -4.8, -2.5, 0.2, 3.7, 3.2, 4.7, -1.1, -2.7, -6.3};

        temperatureOffsetGlobal = 2 * random.nextDouble() - 1;
        temperatureFluctuationFactorGlobal = 2 * random.nextDouble() - 1;
    }

    /**
     * Generates temperature data for each day of a single year.
     * @param year Year for which to generate data.
     * @param depth Underground depth for which to generate data.
     * @param offset Temperature offset for generated values.
     * @return TemperatureData object containing generated temperatures.
     */
    public TemperatureData generateAnnualData(int year, double depth, double offset) {

        // Set dates for start and end of the simulation
        MyCalendar date = new MyCalendar();
        date.set(year, 1, 1);
        int daysInYear = date.getActualMaximum(Calendar.DAY_OF_YEAR);
        temperatureData = new TemperatureData(daysInYear);
        calculateMeanDailyTemperatures(date, offset);
        calculateUndergroundTemperatures(date, depth);
        return temperatureData;
    }

    /**
     * Calculates air temperature for a specified hour and with specified temperature offset.
     * @param date Current date.
     * @param offset Temperature offset.
     * @return Air temperature for the given hour of the day.
     */
    public double calculateAirTemperature(MyCalendar date, double offset) {

        // Initialize prevDate if not initialized (during first call of the method)
        if(prevDate == null) {
            prevDate = new MyCalendar();
            prevDate.set(date);
        }

        // Hourly temperature seed
        temperatureOffsetHourly = (2 * random.nextDouble() - 1) * 0.5;

        // Daily temperature seed
        if(prevDate.get(Calendar.DAY_OF_MONTH) != date.get(Calendar.DAY_OF_MONTH)) {
            temperatureOffsetDaily = (2 * random.nextDouble() - 1) * 1.3;
            temperatureFluctuationFactorDaily = (2 * random.nextDouble() - 1) * 1.3;
            prevDate.getCalendar().set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
        }

        // Monthly temperature seed
        if(prevDate.get(Calendar.MONTH) != date.get(Calendar.MONTH)) {
            temperatureOffsetMonthly = (2 * random.nextDouble() - 1) * 1.2;
            temperatureFluctuationFactorMonthly = (2 * random.nextDouble() - 1) * 0.7;
            prevDate.getCalendar().set(Calendar.MONTH, date.get(Calendar.MONTH));
        }

        // Model the daily temperatures using fragments of sine function
        double peakTemperature = peakTemperatures[date.get(Calendar.MONTH) - 1];
        double bottomTemperature = bottomTemperatures[date.get(Calendar.MONTH) - 1];
        double temperatureSpread = peakTemperature - bottomTemperature;
        double peakBottomAvg = (peakTemperature + bottomTemperature) / 2;
        double sinOffset = 5 * Math.PI / 6;
        double sinFactor = 2 * Math.PI / 24;
        double sinArg = date.get(Calendar.HOUR_OF_DAY) * sinFactor - sinOffset;
        double temperatureOffset = (temperatureOffsetHourly + temperatureOffsetDaily + temperatureOffsetMonthly
                + temperatureOffsetGlobal) / 8 * temperatureSpread;
        double temperatureFluctuationFactor = (temperatureFluctuationFactorDaily + temperatureFluctuationFactorMonthly
                + temperatureFluctuationFactorGlobal) / 6;
        double temperature = (temperatureSpread / 2) * temperatureFluctuationFactor * Math.sin(sinArg) + peakBottomAvg
                + temperatureOffset + offset;
        temperature += (temperature * temperatureFluctuationFactor);

        return temperature;
    }

    /**
     * Calculates mean daily temperatures for each day of the year.
     * @param date Current date.
     * @param offset Temperature offset.
     * @return Array containing avarage daily temperatures for each day of the year.
     */
    public double[] calculateMeanDailyTemperatures(MyCalendar date, double offset) {

        MyCalendar currentDate = new MyCalendar();
        currentDate.set(date);
        MyCalendar endDate = new MyCalendar();
        endDate.set(date.get(Calendar.YEAR), 12, 31);

        double[] temperatureSums = new double[temperatureData.getSize()];
        double[] avgTemperatures = new double[temperatureData.getSize()];
        int currentDay = currentDate.get(Calendar.DAY_OF_YEAR);
        int daysInYear = date.getActualMaximum(Calendar.DAY_OF_YEAR);
        for(int i = 0; i < daysInYear;) {
            double temperature = calculateAirTemperature(currentDate, offset);
            temperatureSums[i] += temperature;
            if(temperature > temperatureData.getMaxTemperature()) {
                temperatureData.setMaxTemperature(temperature);
            } else if (temperature < temperatureData.getMinTemperature()) {
                temperatureData.setMinTemperature(temperature);
                temperatureData.setColdestDayNumber(currentDate.get(Calendar.DAY_OF_YEAR));
            }
            currentDate.nextHour();
            if(currentDay != currentDate.get(Calendar.DAY_OF_YEAR)) {
                avgTemperatures[i] = temperatureSums[i] / 24;
                currentDate.previousDay();
                TemperatureRecord record = new TemperatureRecord(currentDate);
                currentDate.nextDay();
                record.setAirTemperature(temperatureSums[i] / 24);
                temperatureData.getTemperatureRecords().add(record);
                currentDay = currentDate.get(Calendar.DAY_OF_YEAR);
                i++;
            }
        }
        return avgTemperatures;
    }

    /**
     * Calculates underground temperatures for each day of the year, at specified depth.
     * @param date Current date.
     * @param d Underground depth.
     */
    public void calculateUndergroundTemperatures(MyCalendar date, double d) {

        MyCalendar currentDate = new MyCalendar();
        currentDate.set(date);
        MyCalendar endDate = new MyCalendar();
        endDate.set(date.get(Calendar.YEAR), 12, 31);

        double avgTemperature = 0;
        int coldestDayNumber = 0;
        int daysInYear = date.getActualMaximum(Calendar.DAY_OF_YEAR);
        for(int i = 0; i < daysInYear; i++) {
            double airTemperature = temperatureData.getTemperatureRecords().get(i).getAirTemperature();
            avgTemperature += airTemperature;
            if(airTemperature > temperatureData.getMaxTemperature()) {
                temperatureData.setMaxTemperature(airTemperature);
            }
            if (airTemperature < temperatureData.getMinTemperature()) {
                temperatureData.setMinTemperature(airTemperature);
                coldestDayNumber = i + 1;
            }
        }
        avgTemperature = avgTemperature / daysInYear;
        temperatureData.setAvgTemperature(avgTemperature);
        temperatureData.setTemperatureAmplitude(temperatureData.getMaxTemperature() - temperatureData.getMinTemperature() / 2);
        for(int i = 0; i < daysInYear; i++) {
            double undergroundTemperature = avgTemperature - temperatureData.getTemperatureAmplitude()
                    * Math.pow(Math.E, -d * Math.sqrt(Math.PI / (daysInYear * alpha))) * Math.cos(2 * Math.PI / daysInYear *
                    ((i + 1) - coldestDayNumber - d / 2 * Math.sqrt(daysInYear / (Math.PI * alpha))));
            temperatureData.updateTemperatureRecord(TemperatureRecord.UNDERGROUND_TEMP, undergroundTemperature, currentDate);
            currentDate.nextDay();
        }
    }
}
