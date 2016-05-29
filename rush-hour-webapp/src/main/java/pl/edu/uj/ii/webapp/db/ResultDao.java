package pl.edu.uj.ii.webapp.db;

import com.google.common.collect.Sets;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;

public class ResultDao {
    public static final String CREATE_RESULT = "insert into result values(?,?,?,?);";
    public static final String CREATE_RESULT_DETAIL = "insert into result_detail values(?,?,?,?);";
    public static final String SELECT_RESULTS = "select * from result;";
    public static final String SELECT_RESULT = "select * from result where id=?;";
    public static final String SELECT_RESULT_DETAILS = "select * from result_detail where result_id=?;";
    private final DataSource dataSource;

    public ResultDao() {
        this(new DriverManagerDataSource("jdbc:sqlite:rush-hour.db"));
    }

    public ResultDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Result result) {
        getJdbcTemplate()
                .update(CREATE_RESULT, result.toObjects());
    }

    public void addDetails(ResultDetail resultDetail) {
        getJdbcTemplate()
                .update(CREATE_RESULT_DETAIL, resultDetail.toObjects());
    }

    public Result get(String id) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        Result result = jdbcTemplate
                .queryForObject(SELECT_RESULT, new Object[]{id}, new ResultRowMapper());
        return result.withDetails(getResultDetails(jdbcTemplate, id));
    }

    public List<Result> get() {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        List<Result> results = jdbcTemplate.query(SELECT_RESULTS, new ResultRowMapper());
        for (Result result : results) {
            result.withDetails(getResultDetails(jdbcTemplate, result.getId()));
        }
        return results;
    }

    private Set<ResultDetail> getResultDetails(JdbcTemplate jdbcTemplate, String id) {
        return Sets.newHashSet(jdbcTemplate.query(SELECT_RESULT_DETAILS, new Object[]{id}, new ResultDetailRowMapper()));
    }

    private JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
}
