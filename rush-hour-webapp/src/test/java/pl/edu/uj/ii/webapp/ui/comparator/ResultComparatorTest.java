package pl.edu.uj.ii.webapp.ui.comparator;

import org.junit.Test;
import pl.edu.uj.ii.webapp.db.Result;
import pl.edu.uj.ii.webapp.db.ResultDetail;
import pl.edu.uj.ii.webapp.ui.TotalResult;

import java.util.ArrayList;
import java.util.Collections;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static pl.edu.uj.ii.webapp.execute.SupportedLang.GPP;
import static pl.edu.uj.ii.webapp.execute.SupportedLang.JAVA_8;

/**
 * Created by gauee on 6/7/16.
 */
public class ResultComparatorTest {

    private ResultComparator comparator = new ResultComparator();

    @Test
    public void returnPositiveWheWinner_hasGreaterRanWithMoreTestCases() {
        Result firstResult = new Result()
                .withAuthor("looser")
                .withDetails(asList(new ResultDetail()));
        Result secondResult = new Result()
                .withAuthor("winner")
                .withDetails(asList(new ResultDetail(), new ResultDetail()));

        TotalResult looser = new TotalResult(firstResult);
        TotalResult winner = new TotalResult(secondResult);
        assertThat(comparator.compare(looser, winner), is(greaterThan(0)));

        ArrayList<TotalResult> totalResults = new ArrayList<>(asList(looser, winner));
        Collections.sort(totalResults, comparator);
        for (TotalResult totalResult : totalResults) {
            System.out.println(totalResult.getResult().getAuthor());
        }
        assertThat(totalResults.get(0).getResult().getAuthor(), is(equalTo("winner")));
    }

    @Test
    public void returnPositiveValueWhenWinner_hasMoreCorrectAnswers() {
        Result firstResult = new Result()
                .withAuthor("looser")
                .withDetails(asList(new ResultDetail()
                        .withMoves(asList(1, 2, 3, 4, 5, 0))
                ));
        Result secondResult = new Result()
                .withAuthor("winner")
                .withDetails(asList(
                        new ResultDetail()
                                .withMoves(asList(1, 2, 3, 4, 5, 6))
                ));

        TotalResult looser = new TotalResult(firstResult);
        TotalResult winner = new TotalResult(secondResult);
        assertThat(comparator.compare(looser, winner), is(greaterThan(0)));

        ArrayList<TotalResult> totalResults = new ArrayList<>(asList(looser, winner));
        Collections.sort(totalResults, comparator);
        for (TotalResult totalResult : totalResults) {
            System.out.println(totalResult.getResult().getAuthor());
        }
        assertThat(totalResults.get(0).getResult().getAuthor(), is(equalTo("winner")));
    }

    @Test
    public void returnPositiveValueWhenWinner_hasLessInvalidAnswers() {
        Result firstResult = new Result()
                .withAuthor("looser")
                .withDetails(asList(new ResultDetail()
                        .withMoves(asList(1, 2, 3, -1, -1, 0))
                ));
        Result secondResult = new Result()
                .withAuthor("winner")
                .withDetails(asList(
                        new ResultDetail()
                                .withMoves(asList(1, 2, 3, 0, -1))
                ));

        TotalResult looser = new TotalResult(firstResult);
        TotalResult winner = new TotalResult(secondResult);
        assertThat(comparator.compare(looser, winner), is(greaterThan(0)));

        ArrayList<TotalResult> totalResults = new ArrayList<>(asList(looser, winner));
        Collections.sort(totalResults, comparator);
        for (TotalResult totalResult : totalResults) {
            System.out.println(totalResult.getResult().getAuthor());
        }
        assertThat(totalResults.get(0).getResult().getAuthor(), is(equalTo("winner")));
    }

    @Test
    public void returnPositiveValueWhenWinner_hasLessMoves() {
        Result firstResult = new Result()
                .withAuthor("looser")
                .withDetails(asList(new ResultDetail()
                        .withMoves(asList(10, 10, 10))
                ));
        Result secondResult = new Result()
                .withAuthor("winner")
                .withDetails(asList(
                        new ResultDetail()
                                .withMoves(asList(10, 10, 9))
                ));

        TotalResult looser = new TotalResult(firstResult);
        TotalResult winner = new TotalResult(secondResult);
        assertThat(comparator.compare(looser, winner), is(greaterThan(0)));

        ArrayList<TotalResult> totalResults = new ArrayList<>(asList(looser, winner));
        Collections.sort(totalResults, comparator);
        for (TotalResult totalResult : totalResults) {
            System.out.println(totalResult.getResult().getAuthor());
        }
        assertThat(totalResults.get(0).getResult().getAuthor(), is(equalTo("winner")));
    }


    @Test
    public void returnPositiveValueWhenWinner_hasLessDuration() {
        Result firstResult = new Result()
                .withAuthor("looser")
                .withDetails(asList(new ResultDetail()
                        .withDuration(100)
                ));
        Result secondResult = new Result()
                .withAuthor("winner")
                .withDetails(asList(
                        new ResultDetail()
                                .withDuration(50)
                ));

        TotalResult looser = new TotalResult(firstResult);
        TotalResult winner = new TotalResult(secondResult);
        assertThat(comparator.compare(looser, winner), is(greaterThan(0)));

        ArrayList<TotalResult> totalResults = new ArrayList<>(asList(looser, winner));
        Collections.sort(totalResults, comparator);
        for (TotalResult totalResult : totalResults) {
            System.out.println(totalResult.getResult().getAuthor());
        }
        assertThat(totalResults.get(0).getResult().getAuthor(), is(equalTo("winner")));
    }

    @Test
    public void returnsPositiveValueWhenWinner_hasLangWithHighestPriority() {
        Result firstResult = new Result()
                .withAuthor("looser")
                .withLang(GPP.toString());
        Result secondResult = new Result()
                .withAuthor("winner")
                .withLang(JAVA_8.name());

        TotalResult looser = new TotalResult(firstResult);
        TotalResult winner = new TotalResult(secondResult);
        assertThat(comparator.compare(looser, winner), is(greaterThan(0)));

        ArrayList<TotalResult> totalResults = new ArrayList<>(asList(looser, winner));
        Collections.sort(totalResults, comparator);
        for (TotalResult totalResult : totalResults) {
            System.out.println(totalResult.getResult().getAuthor());
        }
        assertThat(totalResults.get(0).getResult().getAuthor(), is(equalTo("winner")));
    }

}