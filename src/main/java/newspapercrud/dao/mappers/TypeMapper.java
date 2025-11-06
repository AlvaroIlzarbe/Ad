package newspapercrud.dao.mappers;

import newspapercrud.dao.model.TypeEntity;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component

public class TypeMapper {

    public TypeMapper() {
    }

    public List<TypeEntity> mapType(ResultSet rs) throws SQLException {
        List<TypeEntity> types = new ArrayList<>();
        try{
        while (rs.next()) {
            int typeId = rs.getInt("id");
            String name = rs.getString("description");
            types.add(new TypeEntity(typeId, name));
        }
        return types;
    }catch (SQLException e){
        throw new SQLException("Error mapping TypeEntity from ResultSet", e);}
    }
}
