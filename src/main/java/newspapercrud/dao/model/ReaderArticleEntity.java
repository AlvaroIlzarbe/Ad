package newspapercrud.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReaderArticleEntity {
    private int articleId;
    private int readerId;
    private int rating;
}

