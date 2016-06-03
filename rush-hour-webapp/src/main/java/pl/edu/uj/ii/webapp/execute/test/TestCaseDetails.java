package pl.edu.uj.ii.webapp.execute.test;

import pl.edu.uj.ii.webapp.db.ResultDetail;

import java.util.concurrent.Future;

/**
 * Created by gauee on 5/31/16.
 */
public class TestCaseDetails {
    private final String id;
    private final int casesAmount;
    private final Future<TestResult> resultFuture;
    private ResultDetail resultDetail;

    public TestCaseDetails(String id, int casesAmount, Future<TestResult> resultFuture) {
        this.id = id;
        this.casesAmount = casesAmount;
        this.resultFuture = resultFuture;
    }

    public String getId() {
        return id;
    }

    public int getCasesAmount() {
        return casesAmount;
    }

    public Future<TestResult> getResultFuture() {
        return resultFuture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestCaseDetails that = (TestCaseDetails) o;

        if (casesAmount != that.casesAmount) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return !(resultFuture != null ? !resultFuture.equals(that.resultFuture) : that.resultFuture != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + casesAmount;
        result = 31 * result + (resultFuture != null ? resultFuture.hashCode() : 0);
        return result;
    }

    public void setResultDetail(ResultDetail resultDetail) {
        this.resultDetail = resultDetail;
    }

    public ResultDetail getResultDetail() {
        return resultDetail;
    }
}
