package newspapercrud.dao;

import newspapercrud.dao.model.ArticleEntity;

import java.util.List;

public interface ArticleRepository {
    List<ArticleEntity> getAll();
    int save(ArticleEntity article);
    void update(ArticleEntity article);
    boolean delete(int articleId,boolean confirmation);

    ArticleEntity get(int id);
}
