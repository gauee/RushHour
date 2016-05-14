package pl.edu.uj.ii.webapp.execute.test;

import java.util.List;

/**
 * Created by gauee on 4/7/16.
 */
public class TestResult {
    private final String testCaseId;
    private final long duration;
    private final List<Integer> stepsOfAllTestCases;

    public TestResult(String testCaseId, long duration, List<Integer> stepsOfAllTestCases) {
        this.testCaseId = testCaseId;
        this.duration = duration;
        this.stepsOfAllTestCases = stepsOfAllTestCases;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public List<Integer> getStepsOfAllTestCases() {
        return stepsOfAllTestCases;
    }

    public long getDuration() {
        return duration;
    }
}
