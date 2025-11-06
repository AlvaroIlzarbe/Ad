package newspapercrud.domain.service;


import newspapercrud.dao.NewspaperRepository;
import newspapercrud.dao.ReaderArticleRepository;
import newspapercrud.dao.ReaderRepository;
import newspapercrud.dao.model.NewspaperEntity;
import newspapercrud.dao.model.ReaderEntity;
import newspapercrud.domain.model.CredentialDTO;
import newspapercrud.domain.model.ReaderDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ReaderService {

    private final ReaderRepository readerRepository;
    private final NewspaperRepository newspaperRepository;
    private final ReaderArticleRepository readArticleRepository;




    public ReaderService(ReaderRepository readerRepository,
                         NewspaperRepository newspaperRepository,
                         ReaderArticleRepository readArticleRepository) {
        this.readerRepository = readerRepository;
        this.newspaperRepository = newspaperRepository;
        this.readArticleRepository = readArticleRepository;
    }

    public List<ReaderDTO> getAll() {
        List<ReaderDTO> readersUI = new ArrayList<>();
        readerRepository.getAll().forEach(mr ->
                readersUI.add(new ReaderDTO(mr.getId(), mr.getName(),
                        mr.getBirthDate().toLocalDate(), null)));
        return readersUI;

    }

    public List<ReaderDTO> getAllReaders() {
        List<ReaderEntity> readers = readerRepository.getAll();
        List<ReaderDTO> readerDTOs = new ArrayList<>();
        for (ReaderEntity reader : readers) {
            readerDTOs.add(new ReaderDTO(
                    reader.getId(),
                    reader.getName(),
                    reader.getBirthDate().toLocalDate()
));
        }
        return readerDTOs;
    }

    public int addReader(String name, java.time.LocalDate birthDate) {
        ReaderEntity readerEntity = new ReaderEntity(0, name, java.sql.Date.valueOf(birthDate), null);
        return readerRepository.save(readerEntity);
    }

    public boolean deleteReader(int id) {
        return readerRepository.delete(new ReaderEntity(id, null, null, null).getId(), true);
    }

    private List<String> parseStringNewspaper(List<NewspaperEntity> newspapers) {
        List<String> stringNewspaper = new ArrayList<>();
        newspapers.forEach(m -> stringNewspaper.add(m.getName()));
        return stringNewspaper;
    }






}
