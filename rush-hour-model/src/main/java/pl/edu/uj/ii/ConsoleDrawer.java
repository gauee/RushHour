package pl.edu.uj.ii;

import pl.edu.uj.ii.model.Board;

/**
 * Created by gauee on 3/31/16.
 */
public class ConsoleDrawer {
    public static final String NEW_LINE = "\n";

    public String printWithLog(Board board) {
        StringBuilder sb = new StringBuilder();
        sb.append(NEW_LINE);
        char[][] positionOfCars = board.getCarsOnBoard();
        printNewLine(board, sb);
        for (int i = board.getHeight(); i-- > 0; ) {
            sb.append("|");
            for (int j = 0; j < board.getWidth(); j++) {
                sb.append(positionOfCars[j][i] + "|");
            }
            sb.append(NEW_LINE);
            printNewLine(board, sb);
        }
        return sb.toString();
    }

    private void printNewLine(Board board, StringBuilder sb) {
        sb.append('+');
        for (int j = 0; j < board.getWidth(); j++) {
            sb.append("-+");
        }
        sb.append(NEW_LINE);
    }

    public void print(Board board) {
        System.out.println();
        char[][] positionOfCars = board.getCarsOnBoard();
        printNewLine(board);
        for (int i = board.getHeight(); i-- > 0; ) {
            System.out.print("|");
            for (int j = 0; j < board.getWidth(); j++) {
                System.out.print(positionOfCars[j][i] + "|");
            }
            System.out.println();
            printNewLine(board);
        }
    }

    private void printNewLine(Board board) {
        System.out.print('+');
        for (int j = 0; j < board.getWidth(); j++) {
            System.out.print("-+");
        }
        System.out.println();
    }

}
