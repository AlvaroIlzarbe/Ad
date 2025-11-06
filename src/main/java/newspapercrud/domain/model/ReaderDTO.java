package newspapercrud.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ReaderDTO {
    private int idReader;
    private String nameReader;
    private LocalDate dobReader;
    private CredentialDTO credentialDTO;


    public ReaderDTO(int id, String name, LocalDate birthDate) {
        this.idReader = id;
        this.nameReader = name;
        this.dobReader = birthDate;
    }
}
