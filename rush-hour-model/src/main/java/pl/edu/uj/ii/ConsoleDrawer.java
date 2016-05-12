package pl.edu.uj.ii;

import pl.edu.uj.ii.model.Board;

/**
 * Created by gauee on 3/31/16.
 */
public class ConsoleDrawer {

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
