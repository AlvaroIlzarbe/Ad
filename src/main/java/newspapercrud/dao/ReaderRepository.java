package newspapercrud.dao;

import newspapercrud.dao.model.ReaderEntity;

import java.util.List;

public interface ReaderRepository {
    List<ReaderEntity> getAll();
    List<ReaderEntity> getAllByArticle(int articleId);
    ReaderEntity get(int readerID);
    int save(ReaderEntity reader);
    void update(ReaderEntity reader);
    boolean delete(int readerId, boolean confirmation);
}
