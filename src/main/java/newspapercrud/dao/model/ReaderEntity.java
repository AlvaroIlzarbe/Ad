package newspapercrud.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ReaderEntity {
    private int id;
    private String name;
    private Date birthDate;
    private CredentialEntity credential;


    public ReaderEntity(int id, String name, Date birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }



}
