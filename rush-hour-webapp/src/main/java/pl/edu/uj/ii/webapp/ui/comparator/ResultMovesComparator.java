package pl.edu.uj.ii.webapp.ui.comparator;

import pl.edu.uj.ii.webapp.db.Result;

import java.util.Comparator;

/**
 * Created by gauee on 5/29/16.
 */
public class ResultMovesComparator implements Comparator<Result> {
    private final MovesComparator movesComparator = new MovesComparator();

    @Override
    public int compare(Result o1, Result o2) {
        int totalCompare = 0;
//        int sizeOfResult = o1.getDetails().size() - o2.getDetails().size();
//        if (sizeOfResult != 0) {
//            return sizeOfResult;
//        }
        for (int i = 0; i < o1.getDetails().size(); i++) {
            totalCompare += movesComparator.compare(o1.getDetails().get(i), o2.getDetails().get(i));
        }
        return totalCompare;
    }
}
