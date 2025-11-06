package newspapercrud.dao.mappers.stringmappers;

import newspapercrud.dao.model.TypeEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TypeRowMapper implements RowMapper<TypeEntity> {
    @Override
    public TypeEntity mapRow(ResultSet rd, int rowNum) throws SQLException {
        return new TypeEntity(
                rd.getInt("id"),
                rd.getString("description")
        );
    }
}
