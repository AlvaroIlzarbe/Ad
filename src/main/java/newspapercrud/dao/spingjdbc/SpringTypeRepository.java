package newspapercrud.dao.spingjdbc;

import newspapercrud.dao.TypeRepository;
import newspapercrud.dao.mappers.stringmappers.TypeRowMapper;
import newspapercrud.dao.model.TypeEntity;
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
public class SpringTypeRepository implements TypeRepository {
    private JdbcClient jdbClient;
    private final TypeRowMapper typeRowMapper;



    public SpringTypeRepository(JdbcClient jdbClient, TypeRowMapper typeRowMapper) {
        this.jdbClient = jdbClient;
        this.typeRowMapper = typeRowMapper;
    }




    @Override
    public List<TypeEntity> getAll() {
        return jdbClient.sql(SQLQueries.SELECT_ALL_TYPES)
                .query(typeRowMapper)
                .list();
    }

    @Override
    public TypeEntity get(int typeID) {
        return jdbClient.sql(SQLQueries.SELECT_TYPE_BY_ID)
                .param(1, typeID)
                .query(typeRowMapper)
                .optional()
                .orElse(null);
    }

    @Override
    public int save(TypeEntity type) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbClient.sql(SQLQueries.INSERT_TYPE)
                .param(1, type.getId())
                .param(2, type.getDescription())
                .update(keyHolder);
        return Objects.requireNonNull(keyHolder.getKey(), "Key was not generated").intValue();


    }

    @Override
    public void update(TypeEntity type) {
        jdbClient.sql(SQLQueries.UPDATE_TYPE)
                .param(1, type.getDescription())
                .param(2, type.getId())

                .update();

    }

    @Override
    public boolean delete(int typeId, boolean confirmation) {
        try{
            if (confirmation) {
                jdbClient.sql(SQLQueries.DELETE_TYPE_BY_ID)
                        .param(1, typeId)
                        .update();
            }
            jdbClient.sql(SQLQueries.DELETE_TYPE_BY_ID)
                    .param(1, typeId)
                    .update();

        }catch (DataIntegrityViolationException e){
            System.out.println(e.getMessage());
            throw new ForeignKeyError();
        }

        return false;
    }


}
