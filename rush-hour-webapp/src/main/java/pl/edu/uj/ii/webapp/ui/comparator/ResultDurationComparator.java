package pl.edu.uj.ii.webapp.ui.comparator;

import pl.edu.uj.ii.webapp.ui.TotalResult;

import java.util.Comparator;

/**
 * Created by gauee on 5/29/16.
 */
public class ResultDurationComparator implements Comparator<TotalResult> {

    @Override
    public int compare(TotalResult o1, TotalResult o2) {
        return (int) (o1.getDuration() - o2.getDuration());
    }

}
