package newspapercrud.dao.mappers;

import newspapercrud.dao.model.ReaderArticleEntity;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReaderArticleMapper {
    public ReaderArticleMapper() {
    }

    public List<ReaderArticleEntity> mapReaderArticle(ResultSet rs) throws SQLException {
        List<ReaderArticleEntity> readers = new ArrayList<>();
        while (rs.next()) {
            int articleId = rs.getInt("id_article");
            int readerId = rs.getInt("id_reader");
            int rating = rs.getInt("rating");
            readers.add(new ReaderArticleEntity(articleId, readerId, rating));
        }
        return readers;
    }
}
