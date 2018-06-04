package eco_sim;

import java.util.Calendar;
import java.util.Random;

public class TemperatureGenerator {

    private MyCalendar prevDate;
    private Random random;
    private TemperatureData temperatureData;
    private double temperatureOffsetGlobal;
    private double temperatureFluctuationFactorGlobal;
    private double temperatureOffsetMonthly;
    private double temperatureFluctuationFactorMonthly;
    private double temperatureOffsetDaily;
    private double temperatureFluctuationFactorDaily;
    private double temperatureOffsetHourly;
    private double[] peakTemperatures;
    private double[] bottomTemperatures;
    private final double alpha = 0.01;

    public TemperatureGenerator() {

        random = new Random();

        peakTemperatures = new double[] {7.1, 8, 11.3, 16, 18.3, 21.5, 24.7, 21.8, 22.9, 17.9, 14.4, 11.5};
        bottomTemperatures = new double[] {-11.7, -9.5, -4, -4.8, -2.5, 0.2, 3.7, 3.2, 4.7, -1.1, -2.7, -6.3};

        temperatureOffsetGlobal = 2 * random.nextDouble() - 1;
        temperatureFluctuationFactorGlobal = 2 * random.nextDouble() - 1;

        temperatureData = new TemperatureData();
    }

    public TemperatureData generateAnnualData(double depth) {
        MyCalendar calendar = new MyCalendar();
        calendar.set(2001, 1, 1, 0, 0);
        calculateMeanDailyTemperatures();
        calculateUndergroundTemperatures(depth);
        return temperatureData;
    }

    public double calculateAirTemperature(MyCalendar date) {

        // Initialize prevDate if not initialized (during first call of the method)
        if(prevDate == null) {
            prevDate = date;
        }

        // Hourly temperature seed
        temperatureOffsetHourly = (2 * random.nextDouble() - 1) * 0.5;

        // Daily temperature seed
        if(prevDate.get(Calendar.DAY_OF_MONTH) != date.get(Calendar.DAY_OF_MONTH)) {
            temperatureOffsetDaily = (2 * random.nextDouble() - 1) * 1.3;
            temperatureFluctuationFactorDaily = (2 * random.nextDouble() - 1) * 1.3;
        }

        // Monthly temperature seed
        if(prevDate.get(Calendar.MONTH) != date.get(Calendar.MONTH)) {
            temperatureOffsetMonthly = (2 * random.nextDouble() - 1) * 1.2;
            temperatureFluctuationFactorMonthly = (2 * random.nextDouble() - 1) * 0.7;
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

    public double[] calculateMeanDailyTemperatures() {

        MyCalendar calendar = new MyCalendar();
        int year = 2001;
        calendar.set(year, 1, 1, 0, 0);

        double[] temperatureSums = new double[calendar.getActualMaximum(Calendar.DAY_OF_YEAR)];
        double[] avgTemperatures = new double[calendar.getActualMaximum(Calendar.DAY_OF_YEAR)];
        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);
        while(calendar.get(Calendar.YEAR) == year) {
            double temperature = calculateAirTemperature(calendar);
            temperatureSums[calendar.get(Calendar.DAY_OF_YEAR) - 1] += temperature;
            if(temperature > temperatureData.getMaxTemperature()) {
                temperatureData.setMaxTemperature(temperature);
            } else if (temperature < temperatureData.getMinTemperature()) {
                temperatureData.setMinTemperature(temperature);
                temperatureData.setColdestDayNumber(calendar.get(Calendar.DAY_OF_YEAR));
            }
            calendar.nextHour();
            if(currentDay != calendar.get(Calendar.DAY_OF_YEAR)) {
                avgTemperatures[currentDay - 1] = temperatureSums[currentDay - 1] / 24;
                currentDay = calendar.get(Calendar.DAY_OF_YEAR);
            }
        }
        temperatureData.setAvgDailyAirTemperatures(avgTemperatures);
        return avgTemperatures;
    }

    public void calculateUndergroundTemperatures(double d) {

        double avgTemperature = 0;
        int coldestDayNumber = 0;
        int daysInYear = temperatureData.getAvgDailyAirTemperatures().length;
        for(int i = 0; i < daysInYear; i++) {
            avgTemperature += temperatureData.getAvgDailyAirTemperatures()[i];
            if(temperatureData.getAvgDailyAirTemperatures()[i] > temperatureData.getMaxTemperature()) {
                temperatureData.setMaxTemperature(temperatureData.getAvgDailyAirTemperatures()[i]);
            }
            if (temperatureData.getAvgDailyAirTemperatures()[i] < temperatureData.getMinTemperature()) {
                temperatureData.setMinTemperature(temperatureData.getAvgDailyAirTemperatures()[i]);
                coldestDayNumber = i + 1;
            }
        }
        avgTemperature = avgTemperature / daysInYear;
        temperatureData.setAvgTemperature(avgTemperature);
        temperatureData.setTemperatureAmplitude(temperatureData.getMaxTemperature() - temperatureData.getMinTemperature() / 2);
        for(int i = 0; i < daysInYear; i++) {
            temperatureData.avgDailyUndergroundTemperatures[i] = avgTemperature - temperatureData.getTemperatureAmplitude()
                    * Math.pow(Math.E, -d * Math.sqrt(Math.PI / (daysInYear * alpha))) * Math.cos(2 * Math.PI / daysInYear *
                    ((i + 1) - coldestDayNumber - d / 2 * Math.sqrt(daysInYear / (Math.PI * alpha))));
        }
    }
}
