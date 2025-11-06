package newspapercrud.dao.jdbc;


import newspapercrud.common.Constantes;
import newspapercrud.dao.CredentialRepository;
import newspapercrud.dao.mappers.ArticleMapper;
import newspapercrud.dao.mappers.CredentialMapper;
import newspapercrud.dao.model.CredentialEntity;
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
public class JDBCCredentialRepository implements CredentialRepository {
    private final ArticleMapper articleMapper;
    private final DBConnectionPool db;
    private final CredentialMapper credentialMapper = new CredentialMapper();
    private final Logger logger = Logger.getLogger(Constantes.LOGGER);

    public JDBCCredentialRepository(ArticleMapper articleMapper, DBConnectionPool db) {
        this.articleMapper = articleMapper;
        this.db = db;
    }


    @Override
    public List<CredentialEntity> getAll() {
        List<CredentialEntity> credentials = new ArrayList<>();
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(SQLQueries.SELECT_ALL_CREDENTIALS);
            credentials = credentialMapper.mapCredential(rs);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return credentials;
    }

    @Override
    public boolean delete(CredentialEntity credential) {
        boolean deleted = false;
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQueries.DELETE_CREDENTIAL_BY_USER)) {
            pstmt.setString(1, credential.getUsername());
            int rowsAffected = pstmt.executeUpdate();
            deleted = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return deleted;
    }

    @Override
    public int save(CredentialEntity credential) {
        int rowsAffected = 0;
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQueries.INSERT_CREDENTIAL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, credential.getUsername());
            pstmt.setString(2, credential.getPassword());
            pstmt.setInt(3, credential.getIdReader());

            rowsAffected = pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        return rowsAffected;
    }

    @Override
    public void update(CredentialEntity credential) {
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQueries.UPDATE_CREDENTIAL)) {
            pstmt.setString(3, credential.getUsername());
            pstmt.setString(1, credential.getPassword());
            pstmt.setInt(2, credential.getIdReader());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public CredentialEntity get(String username) {
        CredentialEntity credential = null;
        try (Connection con = db.getConnection();
             PreparedStatement getCredential = con.prepareStatement(SQLQueries.GET_CREDENTIAL)) {
            getCredential.setString(1, username);
            ResultSet rs = getCredential.executeQuery();
            if (rs.next()) {
                credential = new CredentialEntity(
                        rs.getString("user"),
                        rs.getString("password"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE,e.getMessage(),e);
        }
        return credential;
    }
}
