package newspapercrud.dao.mappers;

import newspapercrud.dao.model.ArticleEntity;
import newspapercrud.dao.model.TypeEntity;
import newspapercrud.domain.model.ArticleDTO;
import newspapercrud.domain.model.TypeDTO;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component

public class ArticleMapper {

    public ArticleMapper() {
    }

    public List<ArticleEntity> mapArticle(ResultSet rs) throws SQLException {
        List<ArticleEntity> articles = new ArrayList<>();
        while (rs.next()) {
            int articleId = rs.getInt("article_id");
            String name = rs.getString("article_name");
            int typeId = rs.getInt("type_id");
            String typeDescription = rs.getString("type_description");
            int newspaperId = rs.getInt("newspaper_id");
            articles.add(new ArticleEntity(articleId, name, new TypeEntity(typeId, typeDescription), newspaperId));
        }
        return articles;
    }

    public ArticleDTO entityToDto(ArticleEntity entity) {
        return new ArticleDTO(
                entity.getId(),
                entity.getName(),
                new TypeDTO(entity.getType().getId(), entity.getType().getDescription()),
                entity.getNPaperID(), 0);
    }

}



