package eco_sim;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Driver {

    public static void main(String[] args) {

        // Create fuel stations and temperatures for them, store pairs of objects in a list
        List<List<Object>> temperaturesAndFuelStations = new ArrayList<>();
        for(int i = 0; i < 10; i++) {

            // Create temperature generator
            TemperatureGenerator generator = new TemperatureGenerator();
            List<Object> temperatureAndFuelStation = new ArrayList<>();
            TemperatureData data = generator.generateAnnualData(2.5, i - 5);
            temperatureAndFuelStation.add(data);
            FuelStation station = new FuelStation("Station " + (i + 1),
                    new Tank(3, 10, 0.02, 0.8 + 0.01 * i, data.getAvgDailyUndergroundTemperatures()[0]));
            temperatureAndFuelStation.add(station);
            temperaturesAndFuelStations.add(temperatureAndFuelStation);
        }

        // Iterate through each day of the year
        for(int i = 0; i < 365; i++) {

            // Iterate through each pair of fuel station and temperature
            for (List<Object> temperatureAndFuelStation : temperaturesAndFuelStations) {

                // Read temperature data and fuel station from the list
                TemperatureData data = (TemperatureData)temperatureAndFuelStation.get(0);
                FuelStation station = (FuelStation)temperatureAndFuelStation.get(1);

                // Read the underground and air temperatures
                double airTemperature = data.getAvgDailyAirTemperatures()[i];
                double undergroundTemperature = data.getAvgDailyUndergroundTemperatures()[i];

                // Update the fuel station and store current fuel data
                station.update(undergroundTemperature, airTemperature);
                data.addAvgDailyFuelTemperature(station.getTank().getFuelTemperature());

                // Print data
                System.out.println(station.getName() + "\t"
                        + String.format("%.2f \t %.2f \t %.2f \t",
                        station.getTank().getFuelTemperature(),
                        undergroundTemperature,
                        station.getTank().getFillFactor() * 100));
            }
            System.out.println("\r\n");
        }

        TemperatureData testData = (TemperatureData)temperaturesAndFuelStations.get(0).get(0);
        for(double fuelTemperature : testData.getAvgDailyFuelTemperatures()) {
            System.out.println(fuelTemperature);
        }
    }
}
