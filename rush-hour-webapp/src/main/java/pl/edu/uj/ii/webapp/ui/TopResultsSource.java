package pl.edu.uj.ii.webapp.ui;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import pl.edu.uj.ii.webapp.db.Result;
import pl.edu.uj.ii.webapp.db.ResultDao;
import pl.edu.uj.ii.webapp.ui.comparator.ResultComparator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 5/29/16.
 */
public class TopResultsSource {
    private final ResultComparator resultComparator = new ResultComparator();
    private final ResultDao resultDao;

    public TopResultsSource(ResultDao resultDao) {
        this.resultDao = resultDao;
    }

    public Map<String, List<TotalResult>> getTopResults() {
        List<Result> results = resultDao.get();
        HashMap<String, List<TotalResult>> topResults = newHashMap();
        for (Result result : results) {
            TotalResult totalResult = new TotalResult(result);
            List<TotalResult> langResults = retrieveLangResults(topResults, result);
            langResults = generateSortedResults(totalResult, langResults);
            topResults.put(result.getLang(), langResults);
        }
        return topResults;
    }

    private List<TotalResult> generateSortedResults(TotalResult totalResult, List<TotalResult> langResults) {
        int resultPosition = findResultPosition(totalResult, langResults);
        if (resultPosition < CONFIG.getTopSolutionAmount()) {
            langResults.add(resultPosition, totalResult);
            removeWorseResultsForAuthor(langResults);
            if (langResults.size() > CONFIG.getTopSolutionAmount()) {
                langResults = langResults.subList(0, CONFIG.getTopSolutionAmount());
            }
        }
        return langResults;
    }

    private void removeWorseResultsForAuthor(List<TotalResult> langResults) {
        HashSet<Object> authors = Sets.newHashSet();
        Iterator<TotalResult> it = langResults.iterator();
        while (it.hasNext()) {
            String author = it.next().getResult().getAuthor();
            if (authors.contains(author)) {
                it.remove();
            } else {
                authors.add(author);
            }
        }
    }

    private List<TotalResult> retrieveLangResults(HashMap<String, List<TotalResult>> topResults, Result result) {
        List<TotalResult> langResults = topResults.get(result.getLang());
        return langResults != null ? langResults : Lists.newLinkedList();
    }

    private int findResultPosition(TotalResult newResult, List<TotalResult> langResults) {
        int idx = 0;
        for (TotalResult langResult : langResults) {
            if (resultComparator.compare(newResult, langResult) < 0) {
                return idx;
            }
            ++idx;
        }
        return idx;
    }
}
