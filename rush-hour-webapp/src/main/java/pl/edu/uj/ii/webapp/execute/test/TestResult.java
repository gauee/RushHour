package pl.edu.uj.ii.webapp.execute.test;

import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * Created by gauee on 4/7/16.
 */
public class TestResult {
    private final String testCaseId;
    private final long duration;
    private final List<Integer> stepsOfAllTestCases;
    private final String executionMessage;

    public TestResult(String testCaseId, long duration, List<Integer> stepsOfAllTestCases, String executionMessage) {
        this.testCaseId = testCaseId;
        this.duration = duration;
        this.stepsOfAllTestCases = stepsOfAllTestCases;
        this.executionMessage = executionMessage;
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

    public String getExecutionMessage() {
        return executionMessage;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("testCaseId", testCaseId)
                .add("duration", duration)
                .add("stepsOfAllTestCases", stepsOfAllTestCases)
                .toString();
    }
}
