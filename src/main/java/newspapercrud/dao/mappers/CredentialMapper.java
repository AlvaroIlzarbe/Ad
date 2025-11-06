package newspapercrud.dao.mappers;

import newspapercrud.dao.model.CredentialEntity;
import newspapercrud.domain.model.CredentialDTO;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CredentialMapper {

    public CredentialMapper() {
    }

    public List<CredentialEntity> mapCredential(ResultSet rs) throws SQLException {
        List<CredentialEntity> credentials = new ArrayList<>();
        while (rs.next()) {
            String user = rs.getString("user");
            String password = rs.getString("password");
            int readerId = rs.getInt("reader_id");
            credentials.add(new CredentialEntity(user, password, readerId));
        }
        return credentials;
    }

    public CredentialDTO entityToDTO(CredentialEntity entity) {
        return new CredentialDTO(
                entity.getUsername(),
                entity.getPassword()
        );
    }



}
