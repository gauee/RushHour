package pl.edu.uj.ii;

import com.google.common.collect.Lists;
import pl.edu.uj.ii.model.Board;
import pl.edu.uj.ii.model.Car;
import pl.edu.uj.ii.model.CarId;
import pl.edu.uj.ii.model.CarMove;
import pl.edu.uj.ii.model.Direction;
import pl.edu.uj.ii.model.Position;

import java.awt.Point;
import java.util.List;

/**
 * Created by gauee on 4/8/16.
 */
public final class DataConverter {
    private DataConverter() {
    }

    public static List<Board> parseInput(List<String> lines) {
        int amountOfTestCases = Integer.valueOf(lines.remove(0));
        List<Board> boards = Lists.newLinkedList();
        for (int i = 0; i < amountOfTestCases; i++) {
            Board board = new Board();
            int amountOfCars = Integer.parseInt(lines.remove(0));
            for (int j = 0; j < amountOfCars; j++) {
                String[] carDetails = lines.remove(0).split(" ");
                Car car = new Car(
                        CarId.valueOf(carDetails[0]),
                        new Point(
                                Integer.valueOf(carDetails[1]),
                                Integer.valueOf(carDetails[2])
                        ),
                        Position.valueOf(carDetails[3]),
                        Integer.valueOf(carDetails[4])
                );
                board.add(car);

            }
            boards.add(board);
        }
        return boards;
    }

    public static List<List<CarMove>> parseOutputLines(List<String> lines) {
        List<List<CarMove>> carMovesTestResults = Lists.newLinkedList();
        while (!lines.isEmpty()) {
            int carMovesAmount = Integer.valueOf(lines.remove(0));
            List<CarMove> carMoves = Lists.newLinkedList();
            for (int i = 0; i < carMovesAmount; i++) {
                carMoves.add(parseOutputLine(lines.remove(0)));
            }
            carMovesTestResults.add(carMoves);
        }
        return carMovesTestResults;
    }

    private static CarMove parseOutputLine(String output) {
        String[] args = output.split(" ");

        if (args.length != 3) {
            throw new IllegalArgumentException("Cannot parse line: " + output);
        }
        try {
            return new CarMove(
                    CarId.valueOf(args[0]),
                    Direction.convert(args[1]),
                    Byte.valueOf(args[2])
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot parse line: " + output, e);
        }
    }
}
