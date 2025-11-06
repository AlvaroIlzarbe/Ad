package newspapercrud.dao;

import newspapercrud.dao.model.NewspaperEntity;

import java.util.List;

public interface NewspaperRepository {
    List<NewspaperEntity> getAll();
    List<NewspaperEntity> getAllByReader(int readerID);
    NewspaperEntity get(int newspaperID);
    int save(NewspaperEntity newspaper);
    void update(NewspaperEntity newspaper);
    boolean delete(NewspaperEntity newspaper);
}
