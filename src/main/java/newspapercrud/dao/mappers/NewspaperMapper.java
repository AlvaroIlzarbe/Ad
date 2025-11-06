package newspapercrud.dao.mappers;

import newspapercrud.dao.model.NewspaperEntity;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class NewspaperMapper {

    public NewspaperMapper() {
    }

    public List<NewspaperEntity> mapNewspaper(ResultSet rs) throws SQLException {
        List<NewspaperEntity> newspapers = new ArrayList<>();
        while (rs.next()) {
            int newspaperId = rs.getInt("id");
            String name = rs.getString("name");
            Date releaseDate = rs.getDate("release_date");
            newspapers.add(new NewspaperEntity(newspaperId, name, releaseDate));
        }
        return newspapers;
    }
}
