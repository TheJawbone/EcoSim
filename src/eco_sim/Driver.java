package eco_sim;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.io.File;

/**
 * Driver class is the main class of the application. It acts as a container for the main method.
 */
public class Driver {

    /**
     * Main method of the application
     * @param args Command line arguments, none are necessary in this application.
     */
    public static void main(String[] args) {

        // Create suppliers from files
        List<Supplier> suppliers = new ArrayList<>();
        File directory = new File(System.getProperty("user.dir") + "\\data\\suppliers");
        File[] files = directory.listFiles();
        for(File file : files) {
            Supplier supplier = new Supplier(file.getAbsolutePath());
            suppliers.add(supplier);
        }

        // Create fuel stations from file
        directory = new File(System.getProperty("user.dir") + "\\data\\fuel_stations");
        files = directory.listFiles();
        List<FuelStation> stations = new ArrayList<>();
        for(File file : files) {
            FuelStation station = new FuelStation(file.getAbsolutePath());
            for(Supplier supplier : suppliers) {
                station.getSuppliers().add(supplier);
            }
            stations.add(station);
        }

        // Iterate through the data
        MyCalendar currentDate = new MyCalendar();
        currentDate.set(2000, 1, 1);
        for(int i = 0; i < currentDate.getActualMaximum(Calendar.DAY_OF_YEAR); i++) {

            // Iterate through each pair of fuel station and temperature
            for (FuelStation station : stations) {

                // Update the fuel station and store current fuel data
                station.update(currentDate);
            }
            currentDate.nextDay();
        }

        // End simulation
        for (FuelStation station : stations) {
            for(Tank tank : station.getTanks()) {
                tank.writeData();
            }
        }
    }
}
