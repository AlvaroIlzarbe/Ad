package newspapercrud.dao.mappers.stringmappers;

import newspapercrud.dao.model.NewspaperEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class NewspaperRowMapper implements RowMapper<NewspaperEntity> {
    @Override
    public NewspaperEntity mapRow(ResultSet rd, int rowNum) throws SQLException {
        return new NewspaperEntity(
                rd.getInt("id"),
                rd.getString("name"),
                rd.getDate("release_date")
        );
    }
}