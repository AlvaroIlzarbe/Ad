package newspapercrud.domain.service;


import newspapercrud.dao.CredentialRepository;
import newspapercrud.dao.mappers.CredentialMapper;
import newspapercrud.domain.model.CredentialDTO;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {

    private final CredentialRepository credentialRepository;
    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialRepository credentialRepository, CredentialMapper credentialMapper) {
        this.credentialRepository = credentialRepository;
        this.credentialMapper = credentialMapper;
    }

    public boolean login(CredentialDTO userCredentialsUI) {
        CredentialDTO storedCredential = credentialMapper.entityToDTO(credentialRepository.get(userCredentialsUI.getUsername()));


        if (storedCredential == null) {
            return false;
        }

        return storedCredential.getPassword().equals(userCredentialsUI.getPassword());
    }



}
