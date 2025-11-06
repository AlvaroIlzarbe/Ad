package newspapercrud.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleEntity {
    private int id;
    private String name;
    private TypeEntity type;
    private int nPaperID;
}
