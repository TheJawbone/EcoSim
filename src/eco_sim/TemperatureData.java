package eco_sim;

public class TemperatureData {

    int coldestDayNumber;
    double maxTemperature;
    double minTemperature;
    double temperatureAmplitude;
    double avgTemperature;
    double[] avgDailyAirTemperatures;
    double[] avgDailyUndergroundTemperatures;

    public TemperatureData() {
        maxTemperature = Double.MIN_VALUE;
        minTemperature = Double.MAX_VALUE;
        avgDailyAirTemperatures = new double[365];
        avgDailyUndergroundTemperatures = new double[365];
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

    public double[] getAvgDailyAirTemperatures() {
        return avgDailyAirTemperatures;
    }

    public void setAvgDailyAirTemperatures(double[] avgDailyAirTemperatures) {
        this.avgDailyAirTemperatures = avgDailyAirTemperatures;
    }

    public double[] getAvgDailyUndergroundTemperatures() {
        return avgDailyUndergroundTemperatures;
    }

    public void setAvgDailyUndergroundTemperatures(double[] avgDailyUndergroundTemperatures) {
        this.avgDailyUndergroundTemperatures = avgDailyUndergroundTemperatures;
    }

    public int getColdestDayNumber() {
        return coldestDayNumber;
    }

    public void setColdestDayNumber(int coldestDayNumber) {
        this.coldestDayNumber = coldestDayNumber;
    }
}
