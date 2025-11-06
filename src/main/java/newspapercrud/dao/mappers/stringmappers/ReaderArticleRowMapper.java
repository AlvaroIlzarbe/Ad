package newspapercrud.dao.mappers.stringmappers;

import newspapercrud.dao.model.ReaderArticleEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReaderArticleRowMapper implements RowMapper<ReaderArticleEntity> {
    @Override
    public ReaderArticleEntity mapRow(ResultSet rd, int rowNum) throws SQLException {
        return new ReaderArticleEntity(
                rd.getInt("id_article"),
                rd.getInt("id_reader"),
                rd.getInt("rating")
        );
    }
}
