package eco_sim;

import java.io.PrintWriter;

public class Driver {

    public static void main(String[] args) {

        TemperatureGenerator climate = new TemperatureGenerator();
        MyCalendar calendar = new MyCalendar();
        calendar.set(2018, 1, 1, 0, 0);
        String tempData = "";
        for(int i = 0; i < 9000; i++) {
            tempData += climate.calculateTemperature(calendar) + "\r\n";
            calendar.nextHour();
        }
        try {
            PrintWriter writer = new PrintWriter("air_temp_data.txt", "UTF-8");
            writer.println(tempData);
            writer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
