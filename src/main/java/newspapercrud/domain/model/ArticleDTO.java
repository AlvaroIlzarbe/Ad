package newspapercrud.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleDTO {
    private int id;
    private String name;
    private TypeDTO typeUI;
    private int npaperId;
    private double avgRating;
}
