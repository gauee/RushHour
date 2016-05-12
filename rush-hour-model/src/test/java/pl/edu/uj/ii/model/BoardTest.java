package pl.edu.uj.ii.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.uj.ii.ConsoleDrawer;

import java.awt.Point;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static pl.edu.uj.ii.model.CarId.A;
import static pl.edu.uj.ii.model.CarId.C;
import static pl.edu.uj.ii.model.Direction.Down;
import static pl.edu.uj.ii.model.Direction.Left;
import static pl.edu.uj.ii.model.Direction.Right;
import static pl.edu.uj.ii.model.Direction.Up;
import static pl.edu.uj.ii.model.Position.H;
import static pl.edu.uj.ii.model.Position.V;

/**
 * Created by gauee on 5/12/16.
 */
public class BoardTest {
    private ConsoleDrawer consoleDrawer = new ConsoleDrawer();
    private Board board;

    @Before
    public void setUp() {
        board = new Board(6, 6);
    }

    @After
    public void drawBoard() {
        consoleDrawer.print(board);
    }

    @Test
    public void createEmptyBoard() {
    }

    @Test
    public void addSingleHorizontalCarOnBoardAtStart() {
        assertThat(board.add(new Car(A, new Point(0, 0), H, 2)), is(true));
    }

    @Test
    public void addSingleVerticalCarOnBoardAtStart() {
        assertThat(board.add(new Car(A, new Point(0, 0), Position.V, 3)), is(true));
    }

    @Test
    public void addSingleHorizontalCarOnBardAtRightTopCorner() {
        assertThat(board.add(new Car(A, new Point(4, 5), H, 2)), is(true));
    }

    @Test
    public void addSingleVerticalCarOnBardAtRightTopCorner() {
        assertThat(board.add(new Car(A, new Point(5, 3), Position.V, 3)), is(true));
    }

    @Test
    public void doesNotAddCarAtTheSamePlace() {
        board.add(new Car(A, new Point(0, 0), H, 2));

        assertThat(board.add(new Car(A, new Point(0, 0), H, 2)), is(false));
    }

    @Test
    public void doesNotAddCarWithNegativeHeight() {
        assertThat(board.add(new Car(A, new Point(0, -1), H, 2)), is(false));
    }

    @Test
    public void doesNotAddCarWithNegativeWidth() {
        assertThat(board.add(new Car(A, new Point(-1, 0), H, 2)), is(false));
    }

    @Test
    public void doesNotAddCarWithHeightOverLimit() {
        assertThat(board.add(new Car(A, new Point(0, board.getHeight()), H, 2)), is(false));
    }

    @Test
    public void doesNotAddCarWithWidthOverLimit() {
        assertThat(board.add(new Car(A, new Point(board.getWidth(), 0), H, 2)), is(false));
    }

    @Test
    public void movesCarInRightWay() {
        board.add(new Car(A, new Point(2, 2), H, 2));

        assertThat(board.move(new CarMove(A, Right, 2)), is(true));
    }

    @Test
    public void movesCarInLeftWay() {
        board.add(new Car(A, new Point(2, 2), H, 2));

        assertThat(board.move(new CarMove(A, Direction.Left, 2)), is(true));
    }

    @Test
    public void movesCarInUpWay() {
        board.add(new Car(A, new Point(2, 2), Position.V, 2));

        assertThat(board.move(new CarMove(A, Up, 2)), is(true));
    }

    @Test
    public void movesCarInDownWay() {
        board.add(new Car(A, new Point(2, 2), Position.V, 2));

        assertThat(board.move(new CarMove(A, Down, 2)), is(true));
    }

    @Test
    public void doesNotMoveCarInRightWayWhenCarIsInVerticalPosition() {
        board.add(new Car(A, new Point(2, 2), Position.V, 2));

        assertThat(board.move(new CarMove(A, Right, 2)), is(false));
    }

    @Test
    public void doesNotMoveCarInUpWayWhenCarIsInHorizontalPosition() {
        board.add(new Car(A, new Point(2, 2), H, 2));

        assertThat(board.move(new CarMove(A, Up, 2)), is(false));
    }

    @Test
    public void doesNotMoveCarInDownWayWhenCarIsInHorizontalPosition() {
        board.add(new Car(A, new Point(2, 2), H, 2));

        assertThat(board.move(new CarMove(A, Down, 2)), is(false));
    }

    @Test
    public void doesNotMoveCarInDownWayAfterBoundary() {
        board.add(new Car(A, new Point(2, 2), V, 2));

        assertThat(board.move(new CarMove(A, Down, board.getHeight())), is(false));
    }

    @Test
    public void doesNotMoveCarInUpWayAfterBoundary() {
        board.add(new Car(A, new Point(2, 2), V, 2));

        assertThat(board.move(new CarMove(A, Up, board.getHeight())), is(false));
    }

    @Test
    public void doesNotMoveCarInLeftWayAfterBoundary() {
        board.add(new Car(A, new Point(2, 2), H, 2));

        assertThat(board.move(new CarMove(A, Left, board.getWidth())), is(false));
    }

    @Test
    public void doesNotMoveCarInRightWayAfterBoundary() {
        board.add(new Car(A, new Point(2, 2), H, 2));

        assertThat(board.move(new CarMove(A, Right, board.getWidth())), is(false));
    }

    @Test
    public void doesNotMoveCarWhichCreateCollisionInLeft() {
        board.add(new Car(A, new Point(2, 2), H, 2));
        board.add(new Car(C, new Point(0, 2), H, 2));

        assertThat(board.move(new CarMove(A, Left, 1)), is(false));
    }

    @Test
    public void doesNotMoveCarWhichCreateCollisionInRight() {
        board.add(new Car(A, new Point(3, 2), H, 2));
        board.add(new Car(C, new Point(0, 2), H, 2));

        assertThat(board.move(new CarMove(C, Right, 2)), is(false));
    }

    @Test
    public void movesCarWhichCreateCollisionInRight() {
        board.add(new Car(A, new Point(3, 2), H, 2));
        board.add(new Car(C, new Point(0, 2), H, 2));

        assertThat(board.move(new CarMove(C, Right, 1)), is(true));
    }

    @Test
    public void doesNotMoveCarWhichDoesNotExistOnBoard() {
        assertThat(board.move(new CarMove(A, Right, 1)), is(false));
    }

}
