package eco_sim;

import java.util.ArrayList;
import java.util.List;

public class Driver {

    public static void main(String[] args) {

        // Create fuel stations and temperatures for them, store pairs of objects in a list
        List<List<Object>> temperaturesAndFuelStations = new ArrayList<>();
        for(int i = 0; i < 10; i++) {

            // Create temperature generator, generate year of data and add it to the list
            TemperatureGenerator generator = new TemperatureGenerator();
            TemperatureData data = generator.generateAnnualData(2.5, i - 5);
            List<Object> temperatureAndFuelStation = new ArrayList<>();
            temperatureAndFuelStation.add(data);

            // Create fuel station and add it to the list
            String stationName = "Station" + (i + 1);
            FuelStation station = new FuelStation(stationName,
                    new Tank(stationName, 1.5, 6, 0.02, 0.8 + 0.01 * i, data.getAvgDailyUndergroundTemperatures()[0]),
                    (i + 1) / 10.0f);
            temperatureAndFuelStation.add(station);

            // Add created pair to the list
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

        // End simulation
        for (List<Object> temperatureAndFuelStation : temperaturesAndFuelStations) {
            FuelStation station = (FuelStation)temperatureAndFuelStation.get(1);
            station.getTank().endSimulation();
            station.endSimulation();
        }
    }
}
