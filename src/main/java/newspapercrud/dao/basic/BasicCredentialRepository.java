package newspapercrud.dao.basic;

import newspapercrud.dao.CredentialRepository;
import newspapercrud.dao.model.CredentialEntity;

import java.util.List;

public class BasicCredentialRepository implements CredentialRepository {





    @Override
    public List<CredentialEntity> getAll() {
        return List.of();
    }

    @Override
    public boolean delete(CredentialEntity credential) {
        return false;
    }

    @Override
    public int save(CredentialEntity patient) {
        return 0;
    }

    @Override
    public void update(CredentialEntity credential) {

    }

    @Override
    public CredentialEntity get(String username) {
        return new CredentialEntity("root", "root", 0);
    }
}
