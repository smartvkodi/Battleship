package battleship;

import battleship.models.VisibleSigns;

public class BattleField {

    private final String[][] battleMap;

    public BattleField(int dim) {
        battleMap = new String[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                battleMap[i][j] = VisibleSigns.FOG.sign;
            }
        }
    }

    public String[][] getBattleMap() {
        return this.battleMap;
    }
}
