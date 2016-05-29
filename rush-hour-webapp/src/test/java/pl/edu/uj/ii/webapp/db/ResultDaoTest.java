package pl.edu.uj.ii.webapp.db;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pl.edu.uj.ii.webapp.execute.SupportedLang;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by gauee on 5/29/16.
 */
public class ResultDaoTest {
    private static final Logger LOGGER = Logger.getLogger(ResultDaoTest.class);
    private ResultDao resultDao = new ResultDao(new DriverManagerDataSource("jdbc:sqlite:" + dbName));

    private static final String dbName = "test.db";

    @BeforeClass
    public static void createDb() throws IOException {
        new File(dbName).createNewFile();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new DriverManagerDataSource("jdbc:sqlite:" + dbName));
        StringBuilder script = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ResultDaoTest.class.getClassLoader().getResourceAsStream("schema.sql")))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                script.append(line);
                if (line.endsWith(";")) {
                    try {
                        jdbcTemplate.execute(script.toString());
                    } catch (UncategorizedSQLException e) {
                        LOGGER.warn("Cannot execute script: " + script.toString());
                    }
                    script.setLength(0);
                }
            }
        }
    }

    @AfterClass
    public static void deleteDb() {
        new File(dbName).delete();
    }

    @Test
    @Ignore
    public void generateRandomData() {
        for (SupportedLang supportedLang : SupportedLang.values()) {
            int amountOfUser = (int) (Math.random() * 100) % 10 + 4;
            for (int i = 0; i < amountOfUser; i++) {
                String user = "user_" + supportedLang.toString() + "_" + i;
                int amountOfSolutions = (int) (Math.random() * 100) % 20;
                for (int j = 0; j < amountOfSolutions; j++) {
                    Result result = new Result()
                            .withId(UUID.randomUUID().toString())
                            .withAuthor(user)
                            .withLang(supportedLang.toString())
                            .withCreationDate(new Date());
                    resultDao.save(result);
                    for (int k = 0; k < 4; k++) {
                        LinkedList<Integer> moves = Lists.newLinkedList();
                        for (int l = 0; l < 10; l++) {
                            moves.add((int) (Math.random() * 1000) % 50);
                        }
                        resultDao.save(new ResultDetail()
                                .withResultId(result.getId())
                                .withDuration((long) (Math.random() * 100000))
                                .withMoves(moves)
                                .withTestCaseId("testCase_" + k));
                    }
                }

            }
        }
    }

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
                .withMoves(singletonList(12))
                .withResultId(uid)
                .withTestCaseId("easy");

        ResultDetail hardDetails = new ResultDetail()
                .withDuration(345678)
                .withMoves(singletonList(12))
                .withResultId(uid)
                .withTestCaseId("hard");

        resultDao.save(easyDetails);

        testAuthor.withDetails(singletonList(easyDetails));

        assertThat(resultDao.get(uid), is(testAuthor));

        resultDao.save(hardDetails);

        testAuthor.withDetails(Arrays.asList(easyDetails, hardDetails));

        assertThat(resultDao.get(uid), is(testAuthor));
    }

    @Test
    public void returnsAllAuthorResults() {
        String authorId = SupportedLang.GPP + "_0";
        List<Result> authorResults = resultDao.getAuthorResults(authorId);
        for (Result authorResult : authorResults) {
            assertThat(authorResult.getAuthor(), is(equalTo(authorId)));
            assertThat(authorResult.getDetails(), is(not(empty())));
        }
    }

}