package pl.edu.uj.ii.webapp.ui.comparator;

import pl.edu.uj.ii.webapp.db.Result;

import java.util.Comparator;

/**
 * Created by gauee on 5/29/16.
 */
public class ResultComparator implements Comparator<Result> {
    private final ResultMovesComparator movesComparator = new ResultMovesComparator();
    private final ResultDurationComparator durationComparator = new ResultDurationComparator();

    @Override
    public int compare(Result o1, Result o2) {
        int sizeOfResult = o1.getDetails().size() - o2.getDetails().size();
        if (sizeOfResult != 0) {
            return sizeOfResult;
        }
        int movesCompare = movesComparator.compare(o1, o2);
        if (movesCompare != 0) {
            return movesCompare;
        }
        return durationComparator.compare(o1, o2);
    }
}
