package pl.edu.uj.ii.webapp.execute.test;

import com.google.common.base.Objects;

/**
 * Created by gauee on 5/31/16.
 */
public class TestCaseDetails {
    private final String id;
    private final int casesAmount;

    public TestCaseDetails(String id, int casesAmount) {
        this.id = id;
        this.casesAmount = casesAmount;
    }

    public String getId() {
        return id;
    }

    public int getCasesAmount() {
        return casesAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCaseDetails that = (TestCaseDetails) o;
        return casesAmount == that.casesAmount &&
                Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, casesAmount);
    }
}
