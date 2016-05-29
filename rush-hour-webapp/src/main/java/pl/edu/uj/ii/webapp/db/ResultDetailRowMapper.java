package pl.edu.uj.ii.webapp.db;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by gauee on 5/29/16.
 */
public class ResultDetailRowMapper implements RowMapper<ResultDetail> {
    @Override
    public ResultDetail mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ResultDetail()
                .withResultId(resultSet.getString("result_id"))
                .withTestCaseId(resultSet.getString("test_case_id"))
                .withMoves(resultSet.getInt("moves"))
                .withDuration(resultSet.getLong("duration"));
    }
}
