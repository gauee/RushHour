package pl.edu.uj.ii.verify;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import pl.edu.uj.ii.model.Board;
import pl.edu.uj.ii.model.Car;
import pl.edu.uj.ii.model.CarMove;
import pl.edu.uj.ii.model.Direction;

import java.awt.Point;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static pl.edu.uj.ii.model.CarId.X;
import static pl.edu.uj.ii.model.Position.H;
import static pl.edu.uj.ii.verify.MovesChecker.ESCAPE_UNREACHABLE;

/**
 * Created by gauee on 5/12/16.
 */
public class MovesCheckerTest {
    private MovesChecker movesChecker = new MovesChecker();
    private List<CarMove> moves;
    private Board board;

    @Before
    public void setUp() {
        board = new Board(6, 6);
        board.add(new Car(X, new Point(0, 3), H, 2));
        moves = Lists.newArrayList();
    }

    @Test
    public void returnsFalseWhenPassEmptyNoMoves() {
        assertThat(movesChecker.canSpecialCarEscapeBoard(board, moves), is(ESCAPE_UNREACHABLE));
    }

    @Test
    public void returnsTrueWhenCarReachEscape() {
        moves.add(new CarMove(X, Direction.Right, 4));

        assertThat(movesChecker.canSpecialCarEscapeBoard(board, moves), is(4));
    }

    @Test
    public void returnEscapeUnreachableWhenMoveIsInvalid() {
        moves.add(new CarMove(X, Direction.Left, 4));

        assertThat(movesChecker.canSpecialCarEscapeBoard(board, moves), is(ESCAPE_UNREACHABLE));
    }

}