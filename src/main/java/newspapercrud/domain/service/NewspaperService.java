package newspapercrud.domain.service;


import newspapercrud.dao.NewspaperRepository;
import newspapercrud.dao.model.NewspaperEntity;
import newspapercrud.domain.model.NewspaperDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewspaperService {
    private final NewspaperRepository newspaperRepository;

    public NewspaperService(NewspaperRepository newspaperRepository) {
        this.newspaperRepository = newspaperRepository;
    }

    public List<NewspaperDTO> getAllNewspapers() {
        List<NewspaperEntity> newspapers = newspaperRepository.getAll();
        List<NewspaperDTO> newspaperDTOs = new ArrayList<>();
        for (NewspaperEntity newspaper : newspapers) {
            newspaperDTOs.add(new NewspaperDTO(
                    newspaper.getId(),
                    newspaper.getName()
            ));
        }
        return newspaperDTOs;
    }

    public NewspaperDTO getNewspaperByID(int id) {
        NewspaperEntity newspaperEntity = newspaperRepository.get(id);
        return new NewspaperDTO(
                newspaperEntity.getId(),
                newspaperEntity.getName()
        );
    }



}
