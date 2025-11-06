package newspapercrud.domain.service;


import newspapercrud.dao.TypeRepository;
import newspapercrud.dao.model.TypeEntity;
import newspapercrud.domain.model.TypeDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeService {
    private final TypeRepository typeRepository;


    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public List<TypeDTO> getAll() {
        List<TypeEntity> types = typeRepository.getAll();
        List<TypeDTO> typeDTOs = new ArrayList<>();
        for (TypeEntity type : types) {
            typeDTOs.add(new TypeDTO(
                    type.getId(),
                    type.getDescription()
            ));
        }
        return typeDTOs;
    }

    public TypeDTO getTypeByID(int id) {
        TypeEntity typeEntity = typeRepository.get(id);
        return new TypeDTO(
                typeEntity.getId(),
                typeEntity.getDescription()
        );
    }





}
