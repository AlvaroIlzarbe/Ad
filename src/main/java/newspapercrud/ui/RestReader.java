package newspapercrud.ui;

import newspapercrud.domain.model.ReaderArticleDTO;
import newspapercrud.domain.model.ReaderDTO;
import newspapercrud.domain.service.ReadArticleService;
import newspapercrud.domain.service.ReaderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class RestReader {
    private final ReaderService readerService;
    private final ReadArticleService readArticleService;

    public RestReader(ReaderService readerService, ReadArticleService readArticleService) {
        this.readerService = readerService;
        this.readArticleService = readArticleService;
    }

    @GetMapping("/readers")
    public List<ReaderDTO> getAll() {
        return readerService.getAll();
    }

    @GetMapping("/articles/{articleId}/readers")
    public List<ReaderDTO> getReadersByIdArticle(@PathVariable("articleId") int articleId) {
              return readerService.getAllReaders();
    }

    @GetMapping("/readers/{readerId}")
    public ReaderDTO getReader(@PathVariable("readerId") int readerId) {
        // ReaderService no expone un getReaderById; buscamos en la lista retornada por getAll().
        return readerService.getAll().stream().filter(r -> r.getIdReader() == readerId).findFirst().orElse(null);
    }

    @PostMapping("/articles/readers")
    public int addReadArticle(@RequestBody ReaderArticleDTO readerArticleUI) {
        return readArticleService.addReadArticle(readerArticleUI);

    }

    @DeleteMapping("/articles/readers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteReadArticle(@RequestBody ReaderArticleDTO readArticleUI) {
        return readArticleService.delete(readArticleUI);
    }

    @PutMapping("/articles/readers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReadArticle(@RequestBody ReaderArticleDTO readerArticleUI) {
        readArticleService.updateReadArticle(readerArticleUI);
    }

}
