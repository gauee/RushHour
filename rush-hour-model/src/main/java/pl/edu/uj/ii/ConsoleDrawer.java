package pl.edu.uj.ii;

import pl.edu.uj.ii.model.Board;
import pl.edu.uj.ii.model.Car;

import java.awt.Point;

import static pl.edu.uj.ii.model.Position.H;
import static pl.edu.uj.ii.model.Position.V;

/**
 * Created by gauee on 3/31/16.
 */
public class ConsoleDrawer {

    public void print(Board board) {
        System.out.println();
        char[][] positionOfCars = generatePositionOfCars(board);
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

    private char[][] generatePositionOfCars(Board board) {
        char[][] positionOfCars = new char[board.getWidth()][board.getHeight()];
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                positionOfCars[j][i] = ' ';
            }
        }

        for (Car car : board.getCars()) {
            try {
                Point startPoint = car.getStartPoint();
                for (int i = 0; i < car.getLength(); i++) {
                    positionOfCars[startPoint.x + (H == car.getPosition() ? i : 0)][startPoint.y + (V == car.getPosition() ? i : 0)] = car.getId().getId();
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Invalid car declaration " + car, e);
            }
        }
        return positionOfCars;
    }

    private void printNewLine(Board board) {
        System.out.print('+');
        for (int j = 0; j < board.getWidth(); j++) {
            System.out.print("-+");
        }
        System.out.println();
    }

}
