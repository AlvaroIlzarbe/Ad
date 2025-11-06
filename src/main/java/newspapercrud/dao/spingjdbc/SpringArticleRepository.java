package newspapercrud.dao.spingjdbc;

import newspapercrud.dao.ArticleRepository;

import newspapercrud.dao.mappers.stringmappers.ArticleRowMapper;
import newspapercrud.dao.model.ArticleEntity;
import newspapercrud.dao.utilites.SQLQueries;
import newspapercrud.domain.error.ForeignKeyError;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;


@Profile("inUse")
@Repository
public class SpringArticleRepository implements ArticleRepository {
    private JdbcClient jdbClient;
    private final ArticleRowMapper articleRowMapper;



    public SpringArticleRepository(JdbcClient jdbClient, ArticleRowMapper articleRowMapper) {
        this.jdbClient = jdbClient;
        this.articleRowMapper = articleRowMapper;
    }




    @Override
    public List<ArticleEntity> getAll() {
        return jdbClient.sql(SQLQueries.SELECT_ALL_ARTICLES)
                .query(articleRowMapper)
                .list();
    }

    @Override
    public int save(ArticleEntity article) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbClient.sql(SQLQueries.INSERT_ARTICLE_WITH_AUTO_ID)
                .param(1, article.getName())
                .param(2, article.getType().getId())
                .param(3, article.getNPaperID())
                .update(keyHolder);
        return Objects.requireNonNull(keyHolder.getKey(), "Key was not generated").intValue();


    }

    @Override
    public void update(ArticleEntity article) {
        jdbClient.sql(SQLQueries.UPDATE_ARTICLE)
                .param(1, article.getName())
                .param(2, article.getType().getId())
                .param(3, article.getNPaperID())
                .param(4, article.getId())
                .update();

    }

    @Override
    public boolean delete(int articleId, boolean confirmation) {
        try{
            if (confirmation) {
                jdbClient.sql(SQLQueries.DELETE_READERARTICLE_BY_ARTICLE_ID)
                        .param(1, articleId)
                        .update();
            }
            jdbClient.sql(SQLQueries.DELETE_ARTICLE_BY_ID)
                    .param(1, articleId)
                    .update();

        }catch (DataIntegrityViolationException e){
            System.out.println(e.getMessage());
            throw new ForeignKeyError();
        }

        return false;
    }

    @Override
    public ArticleEntity get(int id) {
        return null;
    }


}
