package pl.edu.uj.ii.webapp.db;

import org.junit.Test;
import pl.edu.uj.ii.webapp.execute.SupportedLang;

import java.util.Date;
import java.util.UUID;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by gauee on 5/29/16.
 */
public class ResultDaoTest {

    private ResultDao resultDao = new ResultDao();

    @Test
    public void createsNewResult() {
        Result testAuthor = new Result()
                .withId(UUID.randomUUID().toString())
                .withAuthor("test_author")
                .withCreationDate(new Date())
                .withLang(SupportedLang.JAVA_8.toString());

        resultDao.save(testAuthor);

        Result result = resultDao.get(testAuthor.getId());
        assertThat(result, is(testAuthor));
    }

    @Test
    public void appendDetailsForResult() {
        String uid = UUID.randomUUID().toString();
        Result testAuthor = new Result()
                .withId(uid)
                .withAuthor("test_author")
                .withCreationDate(new Date())
                .withLang(SupportedLang.JAVA_8.toString());
        resultDao.save(testAuthor);

        ResultDetail easyDetails = new ResultDetail()
                .withDuration(12345)
                .withMoves(12)
                .withResultId(uid)
                .withTestCaseId("easy");

        ResultDetail hardDetails = new ResultDetail()
                .withDuration(345678)
                .withMoves(12)
                .withResultId(uid)
                .withTestCaseId("hard");

        resultDao.addDetails(easyDetails);

        testAuthor.withDetails(singleton(easyDetails));

        assertThat(resultDao.get(uid), is(testAuthor));

        resultDao.addDetails(hardDetails);

        testAuthor.withDetails(newHashSet(easyDetails, hardDetails));

        assertThat(resultDao.get(uid), is(testAuthor));
    }

    @Test
    public void selectsAllResults() {
        assertThat(resultDao.get(), is(not(emptyList())));
    }

}