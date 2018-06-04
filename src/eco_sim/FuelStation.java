package eco_sim;

public class FuelStation {

    private String name;
    private Tank tank;

    public FuelStation(String name, Tank tank) {
        this.name = name;
        this.tank = tank;
    }

    public void update(double undergroundTemperature, double airTemperature) {
        tank.calculateFuelTemperature(undergroundTemperature);
        tank.subtractFuel(20);
        if(tank.getFillFactor() < 0.2) {
            tank.refill(tank.getTankVolume() * 0.6, airTemperature);
            System.out.println("REFILL!!!");
        }
    }

    public Tank getTank() {
        return tank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
