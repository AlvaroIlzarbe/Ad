package newspapercrud.dao.jdbc;


import newspapercrud.common.Constantes;
import newspapercrud.dao.ReaderRepository;
import newspapercrud.dao.mappers.ReaderArticleMapper;
import newspapercrud.dao.mappers.ReaderMapper;
import newspapercrud.dao.model.ReaderEntity;
import newspapercrud.dao.utilites.ConnectionDB;
import newspapercrud.dao.utilites.SQLQueries;
import newspapercrud.domain.error.DataBaseError;
import newspapercrud.domain.error.DuplicatedUsernameError;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
@Profile("JdBCInUse")

@Repository
public class JDBCReaderRepository implements ReaderRepository {

    private final ReaderArticleMapper articleMapper;
    private final ReaderMapper readerMapper;
    private final ConnectionDB connection;
    private final Logger logger = Logger.getLogger(Constantes.LOGGER);


    public JDBCReaderRepository(ReaderArticleMapper articleMapper, ReaderMapper readerMapper, ConnectionDB connection) {
        this.articleMapper = articleMapper;
        this.readerMapper = readerMapper;
        this.connection = connection;
    }

    @Override
    public List<ReaderEntity> getAll() {
        List<ReaderEntity> readers = new ArrayList<>();
        try (Connection conn = connection.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(SQLQueries.SELECT_ALL_READERS);
            readers = readerMapper.mapReader(rs);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return readers;
    }

    @Override
    public List<ReaderEntity> getAllByArticle(int articleId) {
        try (Connection con = connection.getConnection();
             PreparedStatement pstm = con.prepareStatement(SQLQueries.GET_ALL_READERS_BY_ARTICLEID)) {
            pstm.setInt(1, articleId);
            ResultSet rs=pstm.executeQuery();
            return readerMapper.mapReader(rs);
        } catch (SQLException e) {
            logger.log(Level.SEVERE,e.getMessage(),e);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }
    }

    @Override
    public ReaderEntity get(int readerID) {
        try (Connection conn = connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQueries.SELECT_READER_BY_ID)) {
            pstmt.setInt(1, readerID);
            ResultSet rs = pstmt.executeQuery();
            List<ReaderEntity> readers = readerMapper.mapReader(rs);
            if (!readers.isEmpty()) {
                return readers.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    @Override
    public int save(ReaderEntity reader) {
        try (Connection con = connection.getConnection();
             PreparedStatement insertReader = con.prepareStatement(SQLQueries.INSERT_READER, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertCredential = con.prepareStatement(SQLQueries.INSERT_CREDENTIAL)

        ) {
            try {
                con.setAutoCommit(false);
                insertReader.setString(1, reader.getName());
                insertReader.setDate(2, Date.valueOf(reader.getBirthDate().toLocalDate()));
                insertReader.executeUpdate();
                ResultSet rs = insertReader.getGeneratedKeys();
                rs.next();
                reader.setId(rs.getInt(1));
                insertCredential.setString(1, reader.getCredential().getUsername());
                insertCredential.setString(2, reader.getCredential().getPassword());
                insertCredential.setInt(3, reader.getId());
                insertCredential.executeUpdate();
                con.commit();
                return reader.getId();
            } catch (SQLIntegrityConstraintViolationException e) {
                con.rollback();
                logger.log(Level.SEVERE,e.getMessage(),e);
                throw new DuplicatedUsernameError();

            } catch (SQLException e) {
                con.rollback();
                logger.log(Level.SEVERE, e.getMessage(), e);
                throw new DataBaseError(Constantes.DATA_BASE_ERROR);
            } finally {
                con.setAutoCommit(true);
            }} catch (SQLException sqle) {
            logger.log(Level.SEVERE,sqle.getMessage(),sqle);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }
    }

    @Override
    public void update(ReaderEntity reader) {
        try (Connection conn = connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQueries.UPDATE_READER)) {
            pstmt.setInt(3, reader.getId());
            pstmt.setString(1, reader.getName());
            pstmt.setDate(2, reader.getBirthDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public boolean delete(int readerId, boolean confirmation) {
        int result = 0;
        try (Connection con = connection.getConnection();
             PreparedStatement deleteReader = con.prepareStatement(SQLQueries.DELETE_READER_BY_ID);
             PreparedStatement deleteReadArticle = con.prepareStatement(SQLQueries.DELETE_READER_READARTICLES);
             PreparedStatement deleteSubscription = con.prepareStatement(SQLQueries.DELETE_READER_SUBSCRIPTIONS);
             PreparedStatement deleteLogin = con.prepareStatement(SQLQueries.DELETE_READER_LOGIN)

        ) {
            try {
                con.setAutoCommit(false);
                deleteReadArticle.setInt(1, readerId);
                deleteReadArticle.executeUpdate();
                deleteSubscription.setInt(1, readerId);
                deleteSubscription.executeUpdate();
                deleteLogin.setInt(1, readerId);
                deleteLogin.executeUpdate();
                deleteReader.setInt(1, readerId);
                result = deleteReader.executeUpdate();
                con.commit();

            } catch (SQLException e) {
                con.rollback();
                logger.log(Level.SEVERE,e.getMessage(),e);
                throw new DataBaseError(Constantes.DATA_BASE_ERROR);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException sqle) {
            logger.log(Level.SEVERE,sqle.getMessage(),sqle);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }
        return result == 1;
    }
    }

