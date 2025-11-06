package newspapercrud.dao.jdbc;

import newspapercrud.common.Constantes;
import newspapercrud.dao.ReaderArticleRepository;
import newspapercrud.dao.mappers.ReaderArticleMapper;
import newspapercrud.dao.model.ReaderArticleEntity;
import newspapercrud.dao.utilites.ConnectionDB;
import newspapercrud.dao.utilites.SQLQueries;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
@Profile("JdBCInUse")

@Repository
public class JDBCReadArticleRepository implements ReaderArticleRepository {
    private final ReaderArticleMapper readerArticleMapper;
    private ReaderArticleMapper articleMapper;
    private final ConnectionDB connection;
    private final Logger logger = Logger.getLogger(Constantes.LOGGER);

    public JDBCReadArticleRepository(ConnectionDB connectionDB, ReaderArticleMapper articleMapper, ReaderArticleMapper readerArticleMapper) {
        this.connection = connectionDB;
        this.articleMapper = articleMapper;
        this.readerArticleMapper = readerArticleMapper;
    }

    @Override
    public List<ReaderArticleEntity> getAll() {
        List<ReaderArticleEntity> readerArticles = new ArrayList<>();
        try (Connection conn = connection.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(SQLQueries.SELECT_ALL_READERARTICLES);
            readerArticles = articleMapper.mapReaderArticle(rs);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return readerArticles;
    }

    @Override
    public ReaderArticleEntity get(int articleID, int readerID) {
        ReaderArticleEntity readArticle=null;
        try (Connection con = connection.getConnection();
             PreparedStatement getReadArticle = con.prepareStatement(SQLQueries.SELECT_ALL_READERS)) {
            getReadArticle.setInt(1, articleID);
            getReadArticle.setInt(2, readerID);
            ResultSet rs=getReadArticle.executeQuery();
            readArticle=articleMapper.mapReaderArticle(rs).stream().findFirst().orElse(null);
        } catch (SQLException e) {
            logger.log(Level.SEVERE,e.getMessage(),e);
        }
        return readArticle;
    }

    @Override
    public ReaderArticleEntity getByIdArticle(int articleID) {
        List<ReaderArticleEntity> results = new ArrayList<>();
        try (Connection conn = connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQueries.SELECT_READERARTICLES_BY_ARTICLE)) {
            pstmt.setInt(1, articleID);
            ResultSet rs = pstmt.executeQuery();
            results = readerArticleMapper.mapReaderArticle(rs);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return (ReaderArticleEntity) results;
    }

    @Override
    public int save(ReaderArticleEntity readArticle) {
        try (Connection con = connection.getConnection();
             PreparedStatement insertReadArticle = con.prepareStatement(SQLQueries.INSERT_READERARTICLE, Statement.RETURN_GENERATED_KEYS)

        ) {
            insertReadArticle.setInt(1, readArticle.getArticleId());
            insertReadArticle.setInt(2, readArticle.getReaderId());
            insertReadArticle.setInt(3, readArticle.getRating());
            insertReadArticle.executeUpdate();
            ResultSet rs = insertReadArticle.getGeneratedKeys();
            rs.next();
            readArticle.setArticleId(rs.getInt(1));

        } catch (SQLIntegrityConstraintViolationException e) {
            logger.log(Level.INFO, "Read article already marked...");
            return -2;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,e.getMessage(),e);
        }
        return readArticle.getArticleId();

    }

    @Override
    public void update(ReaderArticleEntity readArticle) {
        try (Connection con = connection.getConnection();
             PreparedStatement updateReadArticle = con.prepareStatement(SQLQueries.UPDATE_READERARTICLE)
        ) {
            try {
                updateReadArticle.setInt(1, readArticle.getRating());
                updateReadArticle.setInt(2, readArticle.getArticleId());
                updateReadArticle.setInt(3, readArticle.getReaderId());
                updateReadArticle.executeUpdate();
            } catch (SQLException e) {
                logger.log(Level.SEVERE,e.getMessage(),e);
            }
        } catch (SQLException sqle) {
            logger.log(Level.SEVERE,sqle.getMessage(),sqle);
        }
    }

    @Override
    public boolean delete(ReaderArticleEntity readArticle, boolean confirmation) {
        // Delete with transaction
        boolean deleted = false;
        try (Connection conn = connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQueries.DELETE_READERARTICLE_BY_ID)) {
            conn.setAutoCommit(false); // Start transaction
            pstmt.setInt(1, readArticle.getArticleId());
            pstmt.setInt(2, readArticle.getReaderId());
            int rowsAffected = pstmt.executeUpdate();
            deleted = rowsAffected > 0;
            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return deleted;
    }
}
