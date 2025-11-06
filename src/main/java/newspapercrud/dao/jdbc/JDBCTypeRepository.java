package newspapercrud.dao.jdbc;


import newspapercrud.dao.TypeRepository;
import newspapercrud.dao.mappers.TypeMapper;
import newspapercrud.dao.model.TypeEntity;
import newspapercrud.dao.utilites.ConnectionDB;
import newspapercrud.dao.utilites.SQLQueries;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
@Profile("JdBCInUse")

@Repository
public class JDBCTypeRepository implements TypeRepository {
    private ConnectionDB connection;
    private TypeMapper typeMapper;
    private final Logger logger = Logger.getLogger(JDBCTypeRepository.class.getName());

    public JDBCTypeRepository(ConnectionDB conn, TypeMapper typeMapper, ConnectionDB connection) {
        this.connection = conn;
        this.typeMapper = typeMapper;
        this.connection = connection;
    }

    @Override
    public List<TypeEntity> getAll() {
        List<TypeEntity> types = new ArrayList<>();
        try (Connection conn = connection.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(SQLQueries.SELECT_ALL_TYPES);
            types = typeMapper.mapType(rs);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return types;
    }

    @Override
    public TypeEntity get(int typeID) {
        try (Connection conn = connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQueries.SELECT_TYPE_BY_ID)) {
            pstmt.setInt(1, typeID);
            ResultSet rs = pstmt.executeQuery();
            List<TypeEntity> types = typeMapper.mapType(rs);
            if (!types.isEmpty()) {
                return types.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return null;
    }


    @Override
    public int save(TypeEntity type) {
        int rowsAffected = 0;
        try (Connection conn = connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQueries.INSERT_TYPE_WITH_AUTO_ID, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, type.getDescription());

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
    public void update(TypeEntity type) {
        try (Connection conn = connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQueries.UPDATE_TYPE)) {
            pstmt.setString(1, type.getDescription());
            pstmt.setInt(2, type.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public boolean delete(int typeId, boolean confirmation) {
        boolean deleted = false;
        try (Connection conn = connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQueries.DELETE_TYPE_BY_ID)) {
            pstmt.setInt(1, typeId);
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
