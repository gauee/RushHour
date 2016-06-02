package pl.edu.uj.ii.webapp.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

public class ResultDao {
    public static final String CREATE_RESULT = "insert into result values(?,?,?,?);";
    public static final String CREATE_RESULT_DETAIL = "insert into result_detail values(?,?,?,?);";
    public static final String SELECT_RESULTS = "select * from result;";
    public static final String SELECT_RESULT = "select * from result where id=?;";
    public static final String SELECT_AUTHOR_RESULTS = "select * from result where author=?;";
    public static final String SELECT_RESULT_DETAILS = "select * from result_detail where result_id=?;";
    public static final ResultRowMapper RESULT_MAPPER = new ResultRowMapper();
    public static final ResultDetailRowMapper RESULT_DETAIL_MAPPER = new ResultDetailRowMapper();
    private final DataSource dataSource;

    public ResultDao() {
        this(new DriverManagerDataSource("jdbc:sqlite:" + CONFIG.getDbLocation()));
    }

    public ResultDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Result result) {
        getJdbcTemplate()
                .update(CREATE_RESULT, result.toObjects());
    }

    public void save(ResultDetail resultDetail) {
        getJdbcTemplate()
                .update(CREATE_RESULT_DETAIL, resultDetail.toObjects());
    }

    public Result get(String id) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        Result result = jdbcTemplate
                .queryForObject(SELECT_RESULT, new Object[]{id}, RESULT_MAPPER);
        return result.withDetails(getResultDetails(jdbcTemplate, id));
    }

    public List<Result> get() {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        List<Result> results = jdbcTemplate.query(SELECT_RESULTS, RESULT_MAPPER);
        for (Result result : results) {
            result.withDetails(getResultDetails(jdbcTemplate, result.getId()));
        }
        return results;
    }

    private List<ResultDetail> getResultDetails(JdbcTemplate jdbcTemplate, String id) {
        List<ResultDetail> details = jdbcTemplate.query(SELECT_RESULT_DETAILS, new Object[]{id}, RESULT_DETAIL_MAPPER);
        Collections.sort(details, (o1, o2) -> o1.getTestCaseId().compareTo(o2.getTestCaseId()));
        return details;
    }

    private JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    public List<Result> getAuthorResults(String authorId) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        List<Result> results = jdbcTemplate.query(SELECT_AUTHOR_RESULTS, new Object[]{authorId}, RESULT_MAPPER);
        for (Result result : results) {
            result.withDetails(getResultDetails(jdbcTemplate, result.getId()));
        }
        return results;
    }
}
