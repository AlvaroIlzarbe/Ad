package newspapercrud.dao.mappers.stringmappers;

import newspapercrud.dao.model.CredentialEntity;
import newspapercrud.dao.model.TypeEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CredentialRowMapper implements RowMapper<CredentialEntity> {
    @Override
    public CredentialEntity mapRow(ResultSet rd, int rowNum) throws SQLException {
        return new CredentialEntity(
                rd.getString("user"),
                rd.getString("password"),
                rd.getInt("reader_id")
        );
    }
}