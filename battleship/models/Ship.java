package battleship.models;

public class Ship {
    private int id;
    private int power;

    public Ship(ShipType type) {
        this.power = type.getWidth();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPower() {
        return power;
    }

    public void decreaseShipPower() {
        power--;
    }
}
