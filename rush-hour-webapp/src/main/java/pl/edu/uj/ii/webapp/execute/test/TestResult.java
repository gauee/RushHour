package pl.edu.uj.ii.webapp.execute.test;

import com.google.common.collect.Lists;
import pl.edu.uj.ii.model.CarMove;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * Created by gauee on 4/7/16.
 */
public class TestResult {
    private final String testCaseId;
    private final List<CarMovesComparator> results;

    public TestResult(String testCaseId, List<List<CarMove>> currentMoves, List<List<CarMove>> expectedMoves) {
        this.testCaseId = testCaseId;
        this.results = Lists.newLinkedList();

        for (int i = 0; i < expectedMoves.size(); i++) {
            List<CarMove> expected = expectedMoves.get(i);
            List<CarMove> current = i < currentMoves.size() ? currentMoves.get(i) : emptyList();
            results.add(new CarMovesComparator(current, expected));
        }
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public List<CarMovesComparator> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return this.results.stream().map(r -> r.toString()).collect(Collectors.joining(", "));
    }
}
