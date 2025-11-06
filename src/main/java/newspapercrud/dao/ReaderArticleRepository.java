package newspapercrud.dao;

import newspapercrud.dao.model.ReaderArticleEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReaderArticleRepository {
    List<ReaderArticleEntity> getAll();


    ReaderArticleEntity get(int articleID, int readerID);
    ReaderArticleEntity getByIdArticle(int articleID);

    int save(ReaderArticleEntity readArticle);
    void update(ReaderArticleEntity readArticle);

    @Transactional
    boolean delete(ReaderArticleEntity readerArticle, boolean confirmed);


}
