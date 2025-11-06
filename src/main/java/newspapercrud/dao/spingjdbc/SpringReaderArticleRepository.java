package newspapercrud.dao.spingjdbc;

import newspapercrud.common.Constantes;
import newspapercrud.dao.ReaderArticleRepository;
import newspapercrud.dao.mappers.stringmappers.ReaderArticleRowMapper;
import newspapercrud.dao.model.ReaderArticleEntity;
import newspapercrud.dao.utilites.SQLQueries;
import newspapercrud.domain.error.DataBaseError;
import newspapercrud.domain.error.ForeignKeyError;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Profile("inUse")
@Repository
public class SpringReaderArticleRepository implements ReaderArticleRepository {
    private JdbcClient jdbClient;
    private final ReaderArticleRowMapper readerArticleRowMapper;
    private final Logger logger = Logger.getLogger(this.getClass().getName());


    public SpringReaderArticleRepository(JdbcClient jdbClient, ReaderArticleRowMapper readerArticleRowMapper) {
        this.jdbClient = jdbClient;
        this.readerArticleRowMapper = readerArticleRowMapper;
    }


    @Override
    public List<ReaderArticleEntity> getAll() {
        try {
            return jdbClient.sql(SQLQueries.SELECT_ALL_READERARTICLES)
                    .query(readerArticleRowMapper)
                    .list();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }
    }

    @Override
    public ReaderArticleEntity getByIdArticle(int id) {
        try {
            return jdbClient.sql(SQLQueries.SELECT_READERARTICLES_BY_ARTICLE)
                    .param(1, id)
                    .query(readerArticleRowMapper)
                    .optional()
                    .orElse(null);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }
    }


    @Override
    public ReaderArticleEntity get(int articleId, int readerId) {
        try {
            return jdbClient.sql(SQLQueries.SELECT_READERARTICLES_BY_READER)
                    .param(1, articleId)
                    .param(2, readerId)
                    .query(readerArticleRowMapper)
                    .optional()
                    .orElse(null);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }
    }

    @Transactional
    @Override
    public boolean delete(ReaderArticleEntity readerArticle, boolean confirmed) {
        boolean deleted;
        try {
            int rowsAffected = jdbClient.sql(SQLQueries.DELETE_READERARTICLE_BY_ID)
                    .param(1, readerArticle.getArticleId())
                    .param(2, readerArticle.getReaderId())
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


    @Override
    public int save(ReaderArticleEntity readerArticle) {
        try {
            var keyHolder = new GeneratedKeyHolder();

            jdbClient.sql(SQLQueries.INSERT_READERARTICLE)
                    .param(1, readerArticle.getArticleId())
                    .param(2, readerArticle.getReaderId())
                    .param(3, readerArticle.getRating())
                    .update(keyHolder);
            Number key = keyHolder.getKey();
            return key != null ? key.intValue() : 0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }
    }



    @Override
    public void update(ReaderArticleEntity readerArticle) {
        try {
            jdbClient.sql(SQLQueries.UPDATE_READERARTICLE)
                    .param(1, readerArticle.getRating())
                    .param(2, readerArticle.getArticleId())
                    .param(3, readerArticle.getReaderId())
                    .update();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }
    }








}


