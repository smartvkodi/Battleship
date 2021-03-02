package battleship.models;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final String playerName;
    private final List<Ship> navy = new ArrayList<>();
    private int totalPower;

    public Player(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }


    public List<Ship> getNavy() {
        return navy;
    }

    public int getTotalPower() {
        return totalPower;
    }

    public void setTotalPower(int totalPower) {
        this.totalPower = totalPower;
    }
}
