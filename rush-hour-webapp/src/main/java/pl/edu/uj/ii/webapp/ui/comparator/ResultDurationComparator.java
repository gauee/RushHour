package pl.edu.uj.ii.webapp.ui.comparator;

import pl.edu.uj.ii.webapp.db.Result;

import java.util.Comparator;

/**
 * Created by gauee on 5/29/16.
 */
public class ResultDurationComparator implements Comparator<Result> {
    private final DurationComparator durationComparator = new DurationComparator();

    @Override
    public int compare(Result o1, Result o2) {
        int totalCompare = 0;
        for (int i = 0; i < o1.getDetails().size(); i++) {
            totalCompare += durationComparator.compare(o1.getDetails().get(i), o2.getDetails().get(i));
        }
        return totalCompare;
    }
}
