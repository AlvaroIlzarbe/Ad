package newspapercrud.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class NewspaperEntity {
    private int id;
    private String name;
    private Date releaseDate;
}
