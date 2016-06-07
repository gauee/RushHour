package pl.edu.uj.ii.webapp.db;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by gauee on 5/29/16.
 */
public class ResultRowMapper implements RowMapper<Result> {

    @Override
    public Result mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Result()
                .withId(resultSet.getString("id"))
                .withAuthor(resultSet.getString("author"))
                .withLang(resultSet.getString("lang"))
                .withMsg(resultSet.getString("msg"))
                .withCreationDate(new Date(resultSet.getLong("creation")));
    }
}
