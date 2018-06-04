package eco_sim;

public class FuelStation {

    private Tank tank;

    public FuelStation(Tank tank) {
        this.tank = tank;
    }

    public void update(double ambientTemperature) {
        tank.calculateFuelTemperature(ambientTemperature);
        tank.subtractFuel(20000);
        if(tank.getFillFactor() < 0.2) {
            tank.refill(tank.getTankVolume() * 0.6 / 1000, 15);
            System.out.println("REFILL!!!");
        }
    }

    public Tank getTank() {
        return tank;
    }
}
