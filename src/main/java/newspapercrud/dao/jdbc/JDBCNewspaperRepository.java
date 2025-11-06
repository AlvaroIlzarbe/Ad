package newspapercrud.dao.jdbc;

import newspapercrud.common.Constantes;
import newspapercrud.dao.NewspaperRepository;
import newspapercrud.dao.mappers.NewspaperMapper;
import newspapercrud.dao.model.NewspaperEntity;
import newspapercrud.dao.utilites.ConnectionDB;
import newspapercrud.dao.utilites.DBConnectionPool;
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
public class JDBCNewspaperRepository implements NewspaperRepository {
    private final Logger logger = Logger.getLogger(Constantes.LOGGER);
    private final DBConnectionPool pool;
    private final NewspaperMapper newspaperMapper;
    private final ConnectionDB db;

    public JDBCNewspaperRepository(DBConnectionPool pool, NewspaperMapper newspaperMapper, ConnectionDB db) {
        this.pool = pool;
        this.newspaperMapper = newspaperMapper;
        this.db = db;
    }


    @Override
    public List<NewspaperEntity> getAll() {
        List<NewspaperEntity> newspapers = new ArrayList<>();
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(SQLQueries.SELECT_ALL_NEWSPAPERS);
            newspapers = newspaperMapper.mapNewspaper(rs);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return newspapers;
    }

    @Override
    public List<NewspaperEntity> getAllByReader(int readerID) {
        return List.of();
    }

    @Override
    public NewspaperEntity get(int newspaperID) {
        NewspaperEntity nPaper=null;
        try (Connection con = pool.getConnection();
             PreparedStatement getType = con.prepareStatement(SQLQueries.GET_NEWSPAPER_BY_ID)) {
            getType.setInt(1, newspaperID);
            ResultSet rs=getType.executeQuery();
            nPaper=newspaperMapper.mapNewspaper(rs).stream().findFirst().orElse(null);

        } catch (SQLException e) {
            logger.log(Level.SEVERE,e.getMessage(),e);
        }
        return nPaper;
    }

    @Override
    public int save(NewspaperEntity newspaper) {
        int rowsAffected = 0;
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQueries.INSERT_NEWSPAPER_WITH_AUTO_ID, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, newspaper.getId());
            pstmt.setString(2, newspaper.getName());
            pstmt.setDate(3, newspaper.getReleaseDate());

            rowsAffected = pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int autoId = rs.getInt(1);

            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        return rowsAffected;
    }

    @Override
    public void update(NewspaperEntity newspaper) {
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQueries.UPDATE_NEWSPAPER)) {
            pstmt.setInt(1, newspaper.getId());
            pstmt.setString(2, newspaper.getName());
            pstmt.setDate(3, newspaper.getReleaseDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public boolean delete(NewspaperEntity newspaper) {
        boolean deleted = false;
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQueries.DELETE_NEWSPAPER_BY_ID)) {
            pstmt.setInt(1, newspaper.getId());
            int rowsAffected = pstmt.executeUpdate();
            deleted = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return deleted;
    }
}
