package newspapercrud.dao.spingjdbc;

import newspapercrud.common.Constantes;
import newspapercrud.dao.CredentialRepository;
import newspapercrud.dao.mappers.stringmappers.CredentialRowMapper;
import newspapercrud.dao.model.CredentialEntity;
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
public class SpringCredentialRepository implements CredentialRepository {
    private JdbcClient jdbClient;
    private final CredentialRowMapper credentialRowMapper;
    private final Logger logger = Logger.getLogger(this.getClass().getName());


    public SpringCredentialRepository(JdbcClient jdbClient, CredentialRowMapper credentialRowMapper) {
        this.jdbClient = jdbClient;
        this.credentialRowMapper = credentialRowMapper;
    }




    @Override
    public List<CredentialEntity> getAll() {
        return jdbClient.sql(SQLQueries.SELECT_ALL_CREDENTIALS)
                .query(credentialRowMapper)
                .list();
    }

    @Override
    @Transactional
    public boolean delete(CredentialEntity credential) {
        boolean deleted;
        try {
            int rowsAffected = jdbClient.sql(SQLQueries.DELETE_CREDENTIAL_BY_USER)
                    .param(1, credential.getUsername())
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
    public int save(CredentialEntity credential) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbClient.sql(SQLQueries.INSERT_CREDENTIAL)
                .param(1, credential.getUsername())
                .param(2, credential.getPassword())
                .param(3, credential.getIdReader())
                .update(keyHolder);
        return Objects.requireNonNull(keyHolder.getKey(), "Key was not generated").intValue();


    }

    @Override
    public void update(CredentialEntity credential) {
        jdbClient.sql(SQLQueries.UPDATE_CREDENTIAL)
                .param(1, credential.getPassword())
                .param(2, credential.getIdReader())
                .param(3, credential.getUsername())
                .update();

    }


    @Override
    public CredentialEntity get(String username) {
        try {
            return jdbClient.sql(SQLQueries.SELECT_CREDENTIAL_BY_USER)
                    .param(1, username)
                    .query(credentialRowMapper)
                    .optional()
                    .orElse(null);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new DataBaseError(Constantes.DATA_BASE_ERROR);
        }
    }
}
