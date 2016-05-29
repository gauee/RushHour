package pl.edu.uj.ii.webapp.db;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by gauee on 5/29/16.
 */
public class ResultDetail {
    private String resultId;
    private String testCaseId;
    private int moves;
    private long duration;

    public String getResultId() {
        return resultId;
    }

    public ResultDetail withResultId(String resultId) {
        this.resultId = resultId;
        return this;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public ResultDetail withTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
        return this;
    }

    public int getMoves() {
        return moves;
    }

    public ResultDetail withMoves(int moves) {
        this.moves = moves;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public ResultDetail withDuration(long duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultDetail that = (ResultDetail) o;
        return moves == that.moves &&
                duration == that.duration &&
                Objects.equal(resultId, that.resultId) &&
                Objects.equal(testCaseId, that.testCaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(resultId, testCaseId, moves, duration);
    }

    public Object[] toObjects() {
        return new Object[]{
                resultId,
                testCaseId,
                moves,
                duration
        };
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("resultId", resultId)
                .add("testCaseId", testCaseId)
                .add("moves", moves)
                .add("duration", duration)
                .toString();
    }
}
