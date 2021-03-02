package battleship.models;

public class MapLocation {

    int row;
    int column;

    public MapLocation(String input) throws Exception {
        int r = calculateRow(input);
        int c = calculateColumn(input);
        if (r == -1 || c == -1 ) {
            throw new Exception("\nError! Wrong location! Try again: \n");
        }
        this.row = r;
        this.column = c;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    private int calculateRow(String input) {
        String letter = input.replaceAll("[0-9]", "");
        return letter.length() == 1 ? input.replaceAll("[0-9]", "").charAt(0) - 65 : -1;
    }

    private int calculateColumn(String input) {
        return Integer.parseInt(input.replaceAll("[^0-9]", "")) - 1;
    }

}
