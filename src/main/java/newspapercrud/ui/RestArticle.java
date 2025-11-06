package newspapercrud.ui;

import newspapercrud.domain.model.ArticleDTO;
import newspapercrud.domain.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class RestArticle {
    private final ArticleService articleService;
    public RestArticle(ArticleService articleService) {
        this.articleService = articleService;
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping("/articles")
    public List<ArticleDTO> getArticles() {
        return articleService.getArticles();
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping("/articles")
    public int addArticle(@RequestBody ArticleDTO articleUI) {
        return articleService.addArticle(articleUI);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PutMapping("/articles")
    public void updateArticle(@RequestBody ArticleDTO articleUI) {
        articleService.updateArticle(articleUI);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @DeleteMapping("/articles/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(@PathVariable int articleId, @RequestParam(required = false) boolean confirm) {
        articleService.deleteArticle(articleId,confirm);
    }
}