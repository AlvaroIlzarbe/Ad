package newspapercrud.dao.jdbc;


import newspapercrud.common.Constantes;
import newspapercrud.dao.ArticleRepository;
import newspapercrud.dao.mappers.ArticleMapper;
import newspapercrud.dao.model.ArticleEntity;
import newspapercrud.dao.utilites.DBConnectionPool;
import newspapercrud.dao.utilites.SQLQueries;
import newspapercrud.domain.error.DataBaseError;
import newspapercrud.domain.error.ForeignKeyError;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Profile("JdBCInUse")
@Repository
public class JDBCArticleRepository implements ArticleRepository {
    private final Logger logger = Logger.getLogger(Constantes.LOGGER);
    private final ArticleMapper articleMapper;
    private final DBConnectionPool pool;


    public JDBCArticleRepository(ArticleMapper articleMapper, DBConnectionPool pool) {
        this.articleMapper = articleMapper;
        this.pool = pool;
    }


    @Override
    public List<ArticleEntity> getAll() {
        List<ArticleEntity> articles = new ArrayList<>();
        try (Connection conn = (Connection) pool.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(SQLQueries.SELECT_ALL_ARTICLES);
            articles = articleMapper.mapArticle(rs);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return articles;
    }


    @Override
    public int save(ArticleEntity article) {
        try (Connection con = pool.getConnection();
             PreparedStatement insertArticle = con.prepareStatement(SQLQueries.INSERT_ARTICLE, Statement.RETURN_GENERATED_KEYS)

        ) {
            insertArticle.setString(1, article.getName());
            insertArticle.setInt(2, article.getType().getId());
            insertArticle.setInt(3, article.getNPaperID());
            insertArticle.executeUpdate();
            ResultSet rs = insertArticle.getGeneratedKeys();
            rs.next();
            article.setId(rs.getInt(1));
            return article.getId();

        } catch (SQLException e) {
            logger.log(Level.SEVERE,e.getMessage(),e);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }


    }

    @Override
    public void update(ArticleEntity article) {
        try (Connection con = pool.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.UPDATE_ARTICLE)
        ) {
            preparedStatement.setString(1, article.getName());
            preparedStatement.setInt(2, article.getType().getId());
            preparedStatement.setInt(3, article.getNPaperID());
            preparedStatement.setInt(4, article.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException sqle) {
            logger.log(Level.SEVERE,sqle.getMessage(),sqle);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }
    }

    @Override
    public boolean delete(int articleId, boolean confirmation) {
        int result = 0;
        try (Connection con = pool.getConnection();
             PreparedStatement deleteArticle = con.prepareStatement(SQLQueries.DELETE_ARTICLE_BY_ID);
             PreparedStatement deleteReadArticle = con.prepareStatement(SQLQueries.DELETE_READERARTICLE_BY_ID)
        ) {
            try {
                con.setAutoCommit(false);
                if (confirmation) {
                    deleteReadArticle.setInt(1, articleId);
                    deleteReadArticle.executeUpdate();
                }
                deleteArticle.setInt(1, articleId);
                result = deleteArticle.executeUpdate();
                con.commit();
            } catch (SQLIntegrityConstraintViolationException e) {
                con.rollback();
                //logger.log(Level.SEVERE,e.getMessage(),e);
                throw new ForeignKeyError();
            } catch (SQLException e) {
                con.rollback();
                logger.log(Level.SEVERE,e.getMessage(),e);
                throw new DataBaseError(Constantes.DATA_BASE_ERROR);
            }
        } catch (SQLException sqle) {
            logger.log(Level.SEVERE,sqle.getMessage(),sqle);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }
        return result == 1;
    }
    @Override
    public ArticleEntity get(int id) {
        try (Connection conn = pool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQueries.SELECT_ARTICLE_BY_ID)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            List<ArticleEntity> articles = articleMapper.mapArticle(rs);
            if (!articles.isEmpty()) {
                return articles.get(0);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }
        return null;
    }

}
