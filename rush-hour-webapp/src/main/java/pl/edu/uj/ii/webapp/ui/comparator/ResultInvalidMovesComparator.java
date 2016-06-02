package pl.edu.uj.ii.webapp.ui.comparator;

import pl.edu.uj.ii.webapp.db.ResultDetail;
import pl.edu.uj.ii.webapp.ui.TotalResult;

import java.util.Comparator;

import static pl.edu.uj.ii.verify.MovesChecker.ESCAPE_UNREACHABLE;

/**
 * Created by gauee on 5/30/16.
 */
public class ResultInvalidMovesComparator implements Comparator<TotalResult> {

    @Override
    public int compare(TotalResult o1, TotalResult o2) {
        int firstInvalidMovesAmount = countInvalidMoves(o1);
        int secondInvalidMovesAmount = countInvalidMoves(o2);

        return firstInvalidMovesAmount - secondInvalidMovesAmount;
    }

    private int countInvalidMoves(TotalResult totalResult) {
        int invalidMoves = 0;
        for (ResultDetail resultDetail : totalResult.getResult().getDetails()) {
            for (Integer moves : resultDetail.getMoves()) {
                if (moves == ESCAPE_UNREACHABLE) {
                    ++invalidMoves;
                }
            }
        }
        return invalidMoves;
    }

}
