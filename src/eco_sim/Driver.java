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

        FuelStation fuelStation = new FuelStation(new Tank(3, 10, 0.02, 0.8, 15));
        for(double ambientTemperature : data.getAvgDailyUndergroundTemperatures()) {
            fuelStation.update(ambientTemperature);
            System.out.println(Util.round(fuelStation.getTank().getFuelTemperature(), 4) + "\t"
                    + Util.round(ambientTemperature, 4) + "\t" + fuelStation.getTank().getFillFactor() * 100 + "%");
        }

    }
}
