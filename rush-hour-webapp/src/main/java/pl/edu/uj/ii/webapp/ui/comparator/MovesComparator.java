package pl.edu.uj.ii.webapp.ui.comparator;

import pl.edu.uj.ii.webapp.db.ResultDetail;

import java.util.Comparator;

/**
 * Created by gauee on 5/29/16.
 */
public class MovesComparator implements Comparator<ResultDetail> {
    @Override
    public int compare(ResultDetail o1, ResultDetail o2) {
        return o1.getMoves() - o2.getMoves();
    }
}
