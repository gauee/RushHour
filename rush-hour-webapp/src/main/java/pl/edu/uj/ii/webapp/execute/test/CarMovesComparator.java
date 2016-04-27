package pl.edu.uj.ii.webapp.execute.test;

import pl.edu.uj.ii.model.CarMove;

import java.util.List;

/**
 * Created by gauee on 4/8/16.
 */
public class CarMovesComparator {
    final List<CarMove> current;
    final List<CarMove> expected;
    final boolean passed;

    public CarMovesComparator(List<CarMove> current, List<CarMove> expected) {
        this.current = current;
        this.expected = expected;
        this.passed = expected.equals(current);
    }

    public List<CarMove> getCurrent() {
        return current;
    }

    public List<CarMove> getExpected() {
        return expected;
    }

    public boolean isPassed() {
        return passed;
    }

    @Override
    public String toString() {
        return this.passed ? "." : "F";
    }
}
