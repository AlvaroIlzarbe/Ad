package newspapercrud.domain.service;


import newspapercrud.dao.ReaderArticleRepository;
import newspapercrud.dao.model.ReaderArticleEntity;
import newspapercrud.domain.model.ReaderArticleDTO;
import org.springframework.stereotype.Service;

@Service
public class ReadArticleService {
    private final ReaderArticleRepository readArticleRepository;

    public ReadArticleService(ReaderArticleRepository readArticleRepository) {
        this.readArticleRepository = readArticleRepository;
    }


    public int addReadArticle(ReaderArticleDTO readerArticleUI) {
        ReaderArticleEntity readArticle = new ReaderArticleEntity(readerArticleUI.getIdArticle(),
                readerArticleUI.getIdReader(), 0);
        return readArticleRepository.save(readArticle);
    }

    public void updateReadArticle(ReaderArticleDTO readerArticleUI) {
        ReaderArticleEntity readArticle = new ReaderArticleEntity(readerArticleUI.getIdArticle(),
                readerArticleUI.getIdReader(), readerArticleUI.getRating()
        );
        readArticleRepository.update(readArticle);
    }

    public boolean delete(ReaderArticleDTO readerArticleUI) {
        ReaderArticleEntity readArticle = new ReaderArticleEntity(readerArticleUI.getIdArticle(), readerArticleUI.getIdReader(), 0);
        return readArticleRepository.delete(readArticle, true);
    }
}
