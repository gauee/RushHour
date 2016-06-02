package pl.edu.uj.ii.webapp.ui;

import org.junit.Before;
import org.junit.Test;
import pl.edu.uj.ii.webapp.db.ResultDao;
import pl.edu.uj.ii.webapp.execute.SupportedLang;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static pl.edu.uj.ii.webapp.ui.TopResultsSource.TOP_RESULTS_AMOUNT;

/**
 * Created by gauee on 5/29/16.
 */
public class TopResultsSourceTest {
    private TopResultsSource source;

    @Before
    public void setUp() throws Exception {
        source = new TopResultsSource(new ResultDao());
    }

    @Test
    public void returnsResultForAllSupportedLang() {
        Map<String, List<TotalResult>> topResults = source.getTopResults();
        for (SupportedLang supportedLang : SupportedLang.values()) {
            assertThat("" + supportedLang.toString(), topResults.containsKey(supportedLang.toString()), is(true));
        }
    }

    @Test
    public void returnsTop5BestSolutions() {
        Map<String, List<TotalResult>> topResults = source.getTopResults();
        for (SupportedLang supportedLang : SupportedLang.values()) {
            assertThat("" + supportedLang.toString(), topResults.get(supportedLang.toString()), hasSize(TOP_RESULTS_AMOUNT));
        }
    }

    @Test
    public void returnsTop5BestSolutionWithUniqAuthor() {
        Map<String, List<TotalResult>> topResults = source.getTopResults();
        for (SupportedLang supportedLang : SupportedLang.values()) {
            Set<String> uniqueAuthors = topResults.get(supportedLang.toString()).stream().map(totalResult -> totalResult.getResult().getAuthor()).collect(toSet());
            assertThat("" + supportedLang.toString(), uniqueAuthors, hasSize(TOP_RESULTS_AMOUNT));
        }
    }

}