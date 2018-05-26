package eco_sim;

import java.util.Calendar;
import java.util.Random;

public class TemperatureGenerator {

    private MyCalendar prevDate;
    private Random random;
    private double temperatureOffsetGlobal;
    private double temperatureFluctuationFactorGlobal;
    private double temperatureOffsetMonthly;
    private double temperatureFluctuationFactorMonthly;
    private double temperatureOffsetDaily;
    private double temperatureFluctuationFactorDaily;
    private double temperatureOffsetHourly;
    private double[] peakTemperatures;
    private double[] bottomTemperatures;

    public TemperatureGenerator() {

        random = new Random();

        // TODO: check what are actual peak and bottom temperatures in each month.
        peakTemperatures = new double[] {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
        bottomTemperatures = new double[] {-10, -10, -10, -10, -10, -10, -10, -10, -10, -10, -10, -10};

        temperatureOffsetGlobal = 2 * random.nextDouble() - 1;
        temperatureFluctuationFactorGlobal = 2 * random.nextDouble() - 1;
    }

    public double calculateTemperature(MyCalendar date) {

        // Initialize prevDate if not initialized (during first call of the method)
        if(prevDate == null) {
            prevDate = date;
        }

        // Hourly temperature seed
        temperatureOffsetHourly = (2 * random.nextDouble() - 1) * 0.7;

        // Daily temperature seed
        if(prevDate.get(Calendar.DAY_OF_MONTH) != date.get(Calendar.DAY_OF_MONTH)) {
            temperatureOffsetDaily = 2 * random.nextDouble() - 1;
            temperatureFluctuationFactorDaily = 2 * random.nextDouble() - 1;
        }

        // Monthly temperature seed
        if(prevDate.get(Calendar.MONTH) != date.get(Calendar.MONTH)) {
            temperatureOffsetMonthly = (2 * random.nextDouble() - 1) * 1.3;
            temperatureFluctuationFactorMonthly = 2 * random.nextDouble() - 1;
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
                + temperatureFluctuationFactorGlobal) / 6 + 1;
        double temperature = (temperatureSpread / 2) * temperatureFluctuationFactor * Math.sin(sinArg) + peakBottomAvg
                + temperatureOffset;

        return temperature;
    }
}
