package newspapercrud.dao.mappers.stringmappers;

import newspapercrud.dao.model.ReaderEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReaderRowMapper implements RowMapper<ReaderEntity> {
    @Override
    public ReaderEntity mapRow(ResultSet rd, int rowNum) throws SQLException {
        return new ReaderEntity(
                rd.getInt("id"),
                rd.getString("name"),
                rd.getDate("birth_date")
        );
    }
}
