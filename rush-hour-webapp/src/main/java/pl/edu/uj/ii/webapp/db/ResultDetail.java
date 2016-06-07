package pl.edu.uj.ii.webapp.db;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by gauee on 5/29/16.
 */
public class ResultDetail {
    public static final String MOVES_SEPARATOR = ",";
    private String resultId;
    private String testCaseId;
    private String msg;
    private List<Integer> moves = Lists.newArrayList();
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

    public List<Integer> getMoves() {
        return moves;
    }

    public ResultDetail withMoves(List<Integer> moves) {
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

    public String getMsg() {
        return msg;
    }

    public ResultDetail withMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object[] toObjects() {
        return new Object[]{
                resultId,
                testCaseId,
                StringUtils.join(moves, MOVES_SEPARATOR),
                duration,
                msg
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultDetail that = (ResultDetail) o;
        return duration == that.duration &&
                Objects.equal(moves, that.moves) &&
                Objects.equal(resultId, that.resultId) &&
                Objects.equal(testCaseId, that.testCaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(resultId, testCaseId, moves, duration);
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
