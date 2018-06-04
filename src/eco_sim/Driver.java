package eco_sim;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Driver {

    public static void main(String[] args) {

        TemperatureGenerator generator = new TemperatureGenerator();
        MyCalendar calendar = new MyCalendar();
        calendar.set(2018, 1, 1, 0, 0);
        String tempData = "";
        for(int i = 0; i < 9000; i++) {
            tempData += generator.calculateAirTemperature(calendar) + "\r\n";
            calendar.nextHour();
        }
        try {
            PrintWriter writer = new PrintWriter("air_temp_data.txt", "UTF-8");
            writer.println(tempData);
            writer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        double[] annualMeanTemperatures = generator.calculateMeanDailyTemperatures();
        for(double meanTemperature : annualMeanTemperatures) {
            //System.out.println(meanTemperature);
        }
        TemperatureData data = generator.generateAnnualData(2.5);

        // SYMULACJA
        Tank tank = new Tank();
        for(double ambientTemperature : data.getAvgDailyUndergroundTemperatures()) {
            tank.calculateFuelTemperature(ambientTemperature);
            System.out.println(round(tank.getFuelTemperature(), 4) + "\t" + round(ambientTemperature, 4));
        }

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
