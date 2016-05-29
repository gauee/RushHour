package pl.edu.uj.ii.webapp.ui;

import pl.edu.uj.ii.webapp.db.Result;
import pl.edu.uj.ii.webapp.ui.comparator.ResultComparator;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by gauee on 5/29/16.
 */
public class UserResults {
    public static final ResultComparator RESULT_COMPARATOR = new ResultComparator();
    private final String author;
    private final List<TotalResult> results;

    public UserResults(String author, List<Result> results) {
        this.author = author;
        this.results = results.stream().map(result -> new TotalResult(result)).collect(toList());
        Collections.sort(this.results, RESULT_COMPARATOR);
    }

    public String getAuthor() {
        return author;
    }

    public List<TotalResult> getResults() {
        return results;
    }
}
