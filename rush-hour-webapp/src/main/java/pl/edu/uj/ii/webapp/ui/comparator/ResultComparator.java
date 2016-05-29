package pl.edu.uj.ii.webapp.ui.comparator;

import pl.edu.uj.ii.webapp.ui.TotalResult;

import java.util.Comparator;

/**
 * Created by gauee on 5/29/16.
 */
public class ResultComparator implements Comparator<TotalResult> {
    private final ResultMovesComparator movesComparator = new ResultMovesComparator();
    private final ResultDurationComparator durationComparator = new ResultDurationComparator();

    @Override
    public int compare(TotalResult o1, TotalResult o2) {
        int sizeOfResult = o1.getResult().getDetails().size() - o2.getResult().getDetails().size();
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
