package pl.edu.uj.ii.webapp.db;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static pl.edu.uj.ii.webapp.db.ResultDetail.MOVES_SEPARATOR;

/**
 * Created by gauee on 5/29/16.
 */
public class ResultDetailRowMapper implements RowMapper<ResultDetail> {
    @Override
    public ResultDetail mapRow(ResultSet resultSet, int i) throws SQLException {
        List<Integer> moves = Lists.newLinkedList();
        for (String singleMove : resultSet.getString("moves").split(MOVES_SEPARATOR)) {
            if (StringUtils.isNoneEmpty(singleMove)) {
                moves.add(Integer.valueOf(singleMove));
            }
        }

        return new ResultDetail()
                .withResultId(resultSet.getString("result_id"))
                .withTestCaseId(resultSet.getString("test_case_id"))
                .withMoves(moves)
                .withDuration(resultSet.getLong("duration"))
                .withMsg(resultSet.getString("msg"));
    }
}
