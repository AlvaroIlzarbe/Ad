package newspapercrud.dao;

import newspapercrud.dao.model.CredentialEntity;

import java.util.List;

public interface CredentialRepository {
    List<CredentialEntity> getAll();

    boolean delete(CredentialEntity credential);

    int save(CredentialEntity patient);
    void update(CredentialEntity credential);
    CredentialEntity get(String username);

}
