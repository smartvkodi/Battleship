package battleship.models;

public enum VisibleSigns {
    FOG("~"), HIT("X"), MISS("M");
    public final String sign;

    VisibleSigns(String sign) {
        this.sign = sign;
    }
}
