package pl.edu.uj.ii.webapp.ui.comparator;

import pl.edu.uj.ii.webapp.execute.SupportedLang;
import pl.edu.uj.ii.webapp.ui.TotalResult;

import java.util.Comparator;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by gauee on 5/29/16.
 */
public class ResultComparator implements Comparator<TotalResult> {
    private final TestCaseCount correctAnswersCount = totalResult -> totalResult.getResult().getDetails().stream()
            .flatMap(resultDetail -> resultDetail.getMoves().stream())
            .filter(moves -> moves > 0)
            .count();
    private final TestCaseCount incorrectAnswersCount = totalResult -> totalResult.getResult().getDetails().stream()
            .flatMap(resultDetail -> resultDetail.getMoves().stream())
            .filter(moves -> moves < 1)
            .count();
    private final LangPriority langPriority = totalResult -> {
        String lang = totalResult.getResult().getLang();
        if (isEmpty(lang)) {
            return Integer.MAX_VALUE;
        }
        return SupportedLang.valueOf(lang).ordinal();
    };

    @Override
    public int compare(TotalResult firstTotalResult, TotalResult secondTotalResult) {
        int langOrder = ((Comparator<TotalResult>) (o1, o2) -> langPriority.priority(o1) - langPriority.priority(o2))
                .compare(firstTotalResult, secondTotalResult);
        if (langOrder != 0) {
            return langOrder;
        }
        int testCaseAmount = ((Comparator<TotalResult>) (o1, o2) -> (int) (correctAnswersCount.count(o1) - correctAnswersCount.count(o2)))
                .compare(firstTotalResult, secondTotalResult);
        if (testCaseAmount != 0) {
            return -testCaseAmount;
        }

        int invalidMoves = ((Comparator<TotalResult>) (o1, o2) -> (int) (incorrectAnswersCount.count(o1) - incorrectAnswersCount.count(o2)))
                .compare(firstTotalResult, secondTotalResult);
        if (invalidMoves != 0) {
            return invalidMoves;
        }

        int movesCompare = ((Comparator<TotalResult>) (o1, o2) -> (int) (o1.getMoves() - o2.getMoves()))
                .compare(firstTotalResult, secondTotalResult);
        if (movesCompare != 0) {
            return movesCompare;
        }
        return ((Comparator<TotalResult>) (o1, o2) -> (int) (o1.getDuration() - o2.getDuration()))
                .compare(firstTotalResult, secondTotalResult);
    }


    @FunctionalInterface
    interface TestCaseCount {
        long count(TotalResult totalResult);
    }

    @FunctionalInterface
    interface LangPriority {
        int priority(TotalResult totalResult);
    }
}
