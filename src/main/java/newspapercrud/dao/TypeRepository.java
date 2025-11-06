package newspapercrud.dao;

import newspapercrud.dao.model.TypeEntity;

import java.util.List;

public interface TypeRepository {
    List<TypeEntity> getAll();
    TypeEntity get(int typeID);
    int save(TypeEntity type);
    void update(TypeEntity type);
    boolean delete(int typeId,boolean confirmation);


}
