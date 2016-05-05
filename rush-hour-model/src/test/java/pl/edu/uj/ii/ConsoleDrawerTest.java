package pl.edu.uj.ii;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import pl.edu.uj.ii.model.Board;
import pl.edu.uj.ii.model.Car;
import pl.edu.uj.ii.model.CarId;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static pl.edu.uj.ii.model.Position.H;
import static pl.edu.uj.ii.model.Position.V;
import static pl.edu.uj.ii.model.Position.valueOf;

/**
 * Created by gauee on 3/31/16.
 */
public class ConsoleDrawerTest {

    @Test
    public void printEmptyBoard() {
        new ConsoleDrawer().print(new Board(5, 5));
    }

    @Test
    public void printEmptyBoardWithDifferentSideLength() {
        new ConsoleDrawer().print(new Board(10, 5));
    }

    @Test
    public void printBoardWithOneCar() {
        Board board = new Board(6, 6);
        board.add(new Car(CarId.A, new Point(0, 0), H, 3));

        new ConsoleDrawer().print(board);
    }

    @Test
    public void printBoardWithFourCarsInEdges() {
        Board board = new Board(6, 6);
        board.add(new Car(CarId.A, new Point(0, 0), V, 3));
        board.add(new Car(CarId.C, new Point(0, 5), H, 2));
        board.add(new Car(CarId.E, new Point(4, 0), H, 2));
        board.add(new Car(CarId.I, new Point(5, 3), V, 3));

        new ConsoleDrawer().print(board);
    }

    @Ignore
    @Test
    public void printInputTestCases() throws IOException {
        String fileLocation = "";
        List<String> lines = FileUtils.readLines(new File(fileLocation));
        int numberOfTestCases = Integer.valueOf(lines.remove(0));
        for (int i = 0; i < numberOfTestCases; i++) {
            int carsAmount = Integer.valueOf(lines.remove(0));
            List<Car> cars = Lists.newLinkedList();
            for (int j = 0; j < carsAmount; j++) {
                cars.add(parseCarPositions(lines.remove(0)));
            }
            Board board = new Board(6, 6);
            for (Car car : cars) {
                board.add(car);
            }
            new ConsoleDrawer().print(board);
        }
    }


    private Car parseCarPositions(String carsPositions) {
        String[] args = carsPositions.split(" ");
        return new Car(
                CarId.valueOf(args[0]),
                new Point(Integer.valueOf(args[1]), Integer.valueOf(args[2])),
                valueOf(args[3]),
                Integer.valueOf(args[4])
        );
    }

}