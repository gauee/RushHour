package pl.edu.uj.ii.webapp.ui;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import pl.edu.uj.ii.webapp.db.Result;
import pl.edu.uj.ii.webapp.db.ResultDetail;

import static pl.edu.uj.ii.verify.MovesChecker.ESCAPE_UNREACHABLE;

/**
 * Created by gauee on 5/29/16.
 */
public class TotalResult {
    private final Result result;
    private long duration = 0;
    private int moves = 0;

    public TotalResult(Result result) {
        this.result = result;
        for (ResultDetail resultDetail : result.getDetails()) {
            this.duration += resultDetail.getDuration();
            this.moves += resultDetail.getMoves() != ESCAPE_UNREACHABLE ? resultDetail.getMoves() : 0;
        }
    }

    public Result getResult() {
        return result;
    }

    public long getDuration() {
        return duration;
    }

    public int getMoves() {
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TotalResult that = (TotalResult) o;
        return duration == that.duration &&
                moves == that.moves &&
                Objects.equal(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(result, duration, moves);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("moves", moves)
                .add("duration", duration)
                .add("result", result)
                .toString();
    }
}
