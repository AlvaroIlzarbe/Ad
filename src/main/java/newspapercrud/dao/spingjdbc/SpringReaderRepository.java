package newspapercrud.dao.spingjdbc;

import newspapercrud.common.Constantes;
import newspapercrud.dao.ReaderRepository;
import newspapercrud.dao.mappers.stringmappers.ReaderRowMapper;
import newspapercrud.dao.model.ReaderEntity;
import newspapercrud.dao.utilites.SQLQueries;
import newspapercrud.domain.error.DataBaseError;
import newspapercrud.domain.error.ForeignKeyError;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


@Profile("inUse")
@Repository
public class SpringReaderRepository implements ReaderRepository {
    private JdbcClient jdbClient;
    private final ReaderRowMapper readerRowMapper;
    private final Logger logger = Logger.getLogger(this.getClass().getName());



    public SpringReaderRepository(JdbcClient jdbClient, ReaderRowMapper readerRowMapper) {
        this.jdbClient = jdbClient;
        this.readerRowMapper = readerRowMapper;
    }




    @Override
    public List<ReaderEntity> getAll() {
        try {
            return jdbClient.sql(SQLQueries.SELECT_ALL_READERS)
                    .query(readerRowMapper)
                    .list();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }
    }

    @Override
    public ReaderEntity get(int readerID) {
        return jdbClient.sql(SQLQueries.SELECT_READER_BY_ID)
                .param(1, readerID)
                .query(readerRowMapper)
                .optional()
                .orElse(null);
    }

    @Override
    public List<ReaderEntity> getAllByArticle(int articleId) {
        return jdbClient.sql(SQLQueries.SELECT_READERS_BY_ARTICLE_ID)
                .param(1, articleId)
                .query(readerRowMapper)
                .list();
    }

    @Override
    public int save(ReaderEntity reader) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbClient.sql(SQLQueries.INSERT_READER)
                .param(1, reader.getId())
                .param(2, reader.getName())
                .param(3, reader.getBirthDate())
                .update(keyHolder);
        return Objects.requireNonNull(keyHolder.getKey(), "Key was not generated").intValue();


    }

    @Override
    public void update(ReaderEntity reader) {
        jdbClient.sql(SQLQueries.UPDATE_READER)
                .param(1, reader.getName())
                .param(2, reader.getBirthDate())
                .param(3, reader.getId())

                .update();

    }

    @Override
    public boolean delete(int readerId, boolean confirmation) {
        try{
            if (confirmation) {
                jdbClient.sql(SQLQueries.DELETE_READER_BY_ID)
                        .param(1, readerId)
                        .update();
            }
            jdbClient.sql(SQLQueries.DELETE_READER_BY_ID)
                    .param(1, readerId)
                    .update();

        }catch (DataIntegrityViolationException e){
            System.out.println(e.getMessage());
            throw new ForeignKeyError();
        }

        return false;
    }


}
