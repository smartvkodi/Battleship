package battleship;

import battleship.models.*;

import java.util.*;

public class Game {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<String, BattleField> battleFields = new HashMap<>();
    private static final List<Player> players =
            Arrays.asList(new Player("Player 1"), new Player("Player 2"));

    public static void setBattleFields(int dimension) {
        for (Player player : players) {
            battleFields.put(player.getPlayerName(), new BattleField(dimension));
        }
    }

    public static void setPlayersNavies() {
        for (Player player : players) {
            System.out.println(player.getPlayerName() + ", place your ships on the game field\n");

            for (ShipType shipType : ShipType.values()) {
                setShipForPlayer(player, shipType);
            }
            showPlayerBattleField(player.getPlayerName(), false);
            System.out.println();

            System.out.print("Press Enter and pass the move to another player:\n");
            while (!scanner.hasNextLine());
            clearScreen();
            System.out.println();
        }
    }

    public static void shootToWin() {
        Player player1 = players.get(0);
        Player player2 = players.get(1);
        String winner;
        while (true) {
            shoot(player1);
            if (player1.getTotalPower() == 0) {
                winner = player1.getPlayerName();
                break;
            }

            System.out.println();
            shoot(player2);
            if (player2.getTotalPower() == 0) {
                winner = player1.getPlayerName();
                break;
            }
        }
        System.out.println("The Winner is " + winner);
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void setShipForPlayer(Player player, ShipType shipType) {
        showPlayerBattleField(player.getPlayerName(), false);
        System.out.println();
        System.out.printf("Enter the coordinates of the %s (%d cells): \n\n", shipType.getType(), shipType.getWidth());
        Ship ship = setPlayerShip(player, shipType);
        player.getNavy().add(ship);
        player.setTotalPower(player.getTotalPower() + ship.getPower());
        System.out.println();
    }

    private static void shoot(Player player) {
        System.out.println(player.getPlayerName() + ", it's your turn:\n");
        Player opponent = players
                .stream()
                .filter(p -> !player.getPlayerName().equals(p.getPlayerName()))
                .findAny().get();
        String opponentName = opponent.getPlayerName();

        showPlayerBattleField(opponentName, true);
        System.out.println("---------------------\n");
        showPlayerBattleField(player.getPlayerName(), false);

        shootToTargetLocation(opponent);

        System.out.print("Press Enter and pass the move to another player:\n");
        while (!scanner.hasNextLine());
        clearScreen();
        System.out.println();
    }

    private static void shootToTargetLocation(Player opponent) {
        scanner.nextLine();
        String input = scanner.nextLine().toUpperCase().trim();
        MapLocation targetLocation;
        try {
            targetLocation = new MapLocation(input);
            String[][] opponentBattleMap = battleFields.get(opponent.getPlayerName()).getBattleMap();
            int row = targetLocation.getRow();
            int column = targetLocation.getColumn();
            String whatIsOnMap = opponentBattleMap[row][column];
            String message;
            if (VisibleSigns.MISS.sign.equals(whatIsOnMap) || VisibleSigns.HIT.sign.equals(whatIsOnMap)) {
                message = "Twin shot! \n";
            } else if (VisibleSigns.FOG.sign.equals(whatIsOnMap)) {
                opponentBattleMap[row][column] = "M";
                message = "You missed! \n";
            } else {
                int shipId = Integer.parseInt(whatIsOnMap);
                opponentBattleMap[row][column] = "X";
                message = "You hit a ship! \n";
                List<Ship> opponentShips = opponent.getNavy();
                for (Ship ship : opponentShips) {
                    if (ship.getId() == shipId) {
                        ship.decreaseShipPower();
                        if (ship.getPower() == 0) {
                            message = "You sank a ship! Specify a new target: \n";
                        }
                        break;
                    }
                }
                int totalPower = opponent.getTotalPower() - 1;
                opponent.setTotalPower(totalPower);
                if (totalPower == 0) {
                    message = "You sank the last ship. You won. Congratulations!\n";
                }
            }
            System.out.println(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Ship setPlayerShip(Player player, ShipType shipType) {
        String[][] battleMap = battleFields.get(player.getPlayerName()).getBattleMap();
        Ship ship = null;
        while (ship == null) {
            try {
                String[] input = scanner.nextLine().toUpperCase().trim().split("\\s+");

                MapLocation fromLocation = new MapLocation(input[0]);
                MapLocation toLocation = new MapLocation(input[1]);

                int fromRow = fromLocation.getRow();
                int fromColumn = fromLocation.getColumn();

                int toRow = toLocation.getRow();
                int toColumn = toLocation.getColumn();

                int fromRowIdx = Math.min(fromRow, toRow);
                int toRowIdx = Math.max(fromRow, toRow);
                int fromColumnIdx = Math.min(fromColumn, toColumn);
                int toColumnIdx = Math.max(fromColumn, toColumn);

                if ((fromRowIdx == -1 || toRowIdx == -1 || fromColumnIdx == -1 || toColumnIdx == -1)
                        || (fromRowIdx != toRowIdx) && (fromColumnIdx != toColumnIdx)) {
                    throw new Exception("\nError! Wrong ship location! Try again: \n");
                }

                int width = shipType.getWidth();
                if (width != Math.max(toRowIdx - fromRowIdx + 1, toColumnIdx - fromColumnIdx + 1)) {
                    throw new Exception("\nError! Wrong length of the "
                            + shipType.getType()
                            + " (" + width + " cells) Try again: \n");
                }

                int rowMin = fromRowIdx == 0 ? 0 : fromRowIdx - 1;
                int rowMax = toRowIdx == 9 ? 9 : toRowIdx + 1;
                int colMin = fromColumnIdx == 0 ? 0 : fromColumnIdx - 1;
                int colMax = toColumnIdx == 9 ? 9 : toColumnIdx + 1;

                for (int i = rowMin; i <= rowMax; i++) {
                    for (int j = colMin; j <= colMax; j++) {
                        if (!VisibleSigns.FOG.sign.equals(battleMap[i][j])) {
                            throw new Exception("\nError! You placed it too close to another one. Try again: \n");
                        }
                    }
                }

                ship = new Ship(shipType);
                int id = shipType.ordinal();
                ship.setId(id);
                String ch = String.valueOf(id);
                for (int i = fromRowIdx; i <= toRowIdx; i++) {
                    for (int j = fromColumnIdx; j <= toColumnIdx; j++) {
                        battleMap[i][j] = ch;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return ship;
    }

    private static void showPlayerBattleField(String playerName, boolean isFoggy) {
        String[][] battleMap = battleFields.get(playerName).getBattleMap();
        int dim = battleMap.length;
        for (int i = 1; i <= dim; i++) {
            System.out.print(" " + i);
        }
        System.out.println();
        for (int i = 0; i < dim; i++) {
            System.out.print((char) (65 + i));
            for (int j = 0; j < 10; j++) {
                String currentChar = battleMap[i][j];
                if (currentChar.matches("\\d+")) {
                    currentChar = "O";
                }
                if (isFoggy && currentChar.equals("O")) {
                    currentChar = VisibleSigns.FOG.sign;
                }
                System.out.print(" " + currentChar);
            }
            System.out.println();
        }
        System.out.println();
    }

}