package newspapercrud.dao.mappers.stringmappers;

import newspapercrud.dao.model.ArticleEntity;
import newspapercrud.dao.model.TypeEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ArticleRowMapper implements RowMapper<ArticleEntity> {
    @Override
    public ArticleEntity mapRow(ResultSet rd, int rowNum) throws SQLException {
        return new ArticleEntity(rd.getInt("article_id"),
                rd.getString("article_name"),
                new TypeEntity(rd.getInt("type_id"), rd.getString("type_description")),
                rd.getInt("newspaper_id"));

    }
}
