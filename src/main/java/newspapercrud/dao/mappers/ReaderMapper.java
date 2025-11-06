package newspapercrud.dao.mappers;

import newspapercrud.dao.model.ReaderEntity;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component

public class ReaderMapper {

    public ReaderMapper() {
    }

    public List<ReaderEntity> mapReader(ResultSet rs) throws SQLException {
        List<ReaderEntity> readers = new ArrayList<>();
        try{
        while (rs.next()) {
            int readerId = rs.getInt("id");
            String name = rs.getString("name");
            Date birthDate = rs.getDate("birth_date");
            readers.add(new ReaderEntity(readerId, name, birthDate, null));
        }
        return readers;
    }catch(SQLException e){
        throw new SQLException("Error mapping ReaderEntity from ResultSet", e);}
    }



}
