package battleship;

public class Main {
    public static void main(String[] args) throws Exception {
        Game.setBattleFields(10);
        Game.setPlayersNavies();
        Game.shootToWin();
    }
}

