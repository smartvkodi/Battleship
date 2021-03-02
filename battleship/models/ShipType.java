package battleship.models;

public enum ShipType {

    AIRCRAFT_CARRIER("Aircraft Carrier", 5),
    BATTLESHIP("Battleship", 4),
    SUBMARINE("Submarine", 3),
    CRUISER("Cruiser", 3),
    DESTROYER("Destroyer", 2);

    private final String type;
    private final int width;

    ShipType(String type, int width) {
        this.type = type;
        this.width = width;
    }

    public String getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }
}

