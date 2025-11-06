package newspapercrud.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CredentialEntity {
    private String username;
    private String password;
    private int idReader;

    public CredentialEntity(String userName, String password) {
        this.username = userName;
        this.password = password;
    }
}
