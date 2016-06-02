package pl.edu.uj.ii.webapp.ui;

import java.util.List;
import java.util.Map;

/**
 * Created by gauee on 5/29/16.
 */
public class TopResults {

    private final Map<String, List<TotalResult>> bestResults;

    public TopResults(Map<String, List<TotalResult>> bestResults) {
        this.bestResults = bestResults;
    }

    public Map<String, List<TotalResult>> getBestResults() {
        return bestResults;
    }

}
