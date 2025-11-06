package newspapercrud.dao.spingjdbc;

import newspapercrud.common.Constantes;
import newspapercrud.dao.NewspaperRepository;
import newspapercrud.dao.mappers.stringmappers.NewspaperRowMapper;
import newspapercrud.dao.model.NewspaperEntity;
import newspapercrud.dao.utilites.SQLQueries;
import newspapercrud.domain.error.DataBaseError;
import newspapercrud.domain.error.ForeignKeyError;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


@Profile("inUse")
@Repository
public class SpringNewspaperRepository implements NewspaperRepository {
    private JdbcClient jdbClient;
    private final NewspaperRowMapper newspaperRowMapper;
    private final Logger logger = Logger.getLogger(this.getClass().getName());


    public SpringNewspaperRepository(JdbcClient jdbClient, NewspaperRowMapper newspaperRowMapper) {
        this.jdbClient = jdbClient;
        this.newspaperRowMapper = newspaperRowMapper;
    }


    @Override
    public List<NewspaperEntity> getAll() {
        return jdbClient.sql(SQLQueries.SELECT_ALL_NEWSPAPERS)
                .query(newspaperRowMapper)
                .list();
    }

    @Override
    public List<NewspaperEntity> getAllByReader(int readerID) {
        return jdbClient.sql(SQLQueries.SELECT_NEWSPAPERS_BY_READER)
                .param(1, readerID)
                .query(newspaperRowMapper)
                .list();
    }

    @Override

    public NewspaperEntity get(int id) {
        try {
            return jdbClient.sql(SQLQueries.SELECT_NEWSPAPER_BY_ID)
                    .param(1, id)
                    .query(newspaperRowMapper)
                    .optional()
                    .orElse(null);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }
    }


    @Override
    public int save(NewspaperEntity newspaper) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbClient.sql(SQLQueries.INSERT_NEWSPAPER)
                .param(1, newspaper.getId())
                .param(2, newspaper.getName())
                .param(3, newspaper.getReleaseDate())
                .update(keyHolder);
        return Objects.requireNonNull(keyHolder.getKey(), "Key was not generated").intValue();


    }

    @Override
    public void update(NewspaperEntity newspaper) {
        jdbClient.sql(SQLQueries.UPDATE_NEWSPAPER)
                .param(1, newspaper.getName())
                .param(2, newspaper.getReleaseDate())
                .param(3, newspaper.getId())
                .update();

    }

    @Override
    @Transactional
    public boolean delete(NewspaperEntity newspaper) {
        boolean deleted;
        try {
            int rowsAffected = jdbClient.sql(SQLQueries.DELETE_NEWSPAPER_BY_ID)
                    .param(1, newspaper.getId())
                    .update();
            deleted = rowsAffected > 0;
        } catch (DataIntegrityViolationException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new ForeignKeyError();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }
        return deleted;
    }

}