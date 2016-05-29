package pl.edu.uj.ii.webapp.db;

import com.google.common.collect.Sets;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;

public class ResultDao {
    public static final String CREATE_RESULT = "insert into result values(?,?,?,?);";
    public static final String CREATE_RESULT_DETAIL = "insert into result_detail values(?,?,?,?);";
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
        Object[] whereValues = {id};
        List<ResultDetail> details = jdbcTemplate.query(SELECT_RESULT_DETAILS, whereValues, new ResultDetailRowMapper());
        Result result = jdbcTemplate
                .queryForObject(SELECT_RESULT, whereValues, new ResultRowMapper());
        return result.withDetails(Sets.newHashSet(details));
    }

    private JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
}
