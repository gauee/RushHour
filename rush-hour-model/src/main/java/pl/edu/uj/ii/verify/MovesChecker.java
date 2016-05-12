package pl.edu.uj.ii.verify;

import pl.edu.uj.ii.model.Board;
import pl.edu.uj.ii.model.CarMove;

import java.awt.Point;
import java.util.List;

import static pl.edu.uj.ii.model.CarId.X;

/**
 * Created by gauee on 5/12/16.
 */
public class MovesChecker {
    public static final int ESCAPE_UNREACHABLE = -1;
    public static final Point ESCAPE = new Point(4, 3);

    public int canSpecialCarEscapeBoard(Board board, List<CarMove> moves) {
        int totalSteps = 0;
        for (CarMove move : moves) {
            if (!board.move(move)) {
                return ESCAPE_UNREACHABLE;
            }
            totalSteps += move.getSteps();
        }

        return board.isCarAtPosition(X, ESCAPE) ? totalSteps : ESCAPE_UNREACHABLE;
    }


}
