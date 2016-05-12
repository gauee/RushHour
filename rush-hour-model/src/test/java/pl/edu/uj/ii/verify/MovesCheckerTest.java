package pl.edu.uj.ii.verify;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import pl.edu.uj.ii.model.Board;
import pl.edu.uj.ii.model.CarMove;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
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
//        board.add(new Car())
        moves = Lists.newArrayList();
    }

    @Test
    public void returnsFalseWhenPassEmptyNoMoves() {
        assertThat(movesChecker.canSpecialCarEscapeBoard(board, moves), is(ESCAPE_UNREACHABLE));
    }

}