package newspapercrud.dao.basic;

import newspapercrud.dao.ArticleRepository;
import newspapercrud.dao.model.ArticleEntity;
import newspapercrud.dao.model.TypeEntity;

import java.util.ArrayList;
import java.util.List;

public class BasicArticleRepository implements ArticleRepository {

    private int id = 0;
    private List<ArticleEntity> articles = new ArrayList<>();

    public BasicArticleRepository() {
        // Datos de prueba predefinidos
        articles.add(new ArticleEntity(++id, "Fútbol hoy", new TypeEntity(1, "sports"), 5));
        articles.add(new ArticleEntity(++id, "Política actual", new TypeEntity(2, "politics"), 3));
        articles.add(new ArticleEntity(++id, "Tecnología 2025", new TypeEntity(3, "tech"), 8));
        articles.add(new ArticleEntity(++id, "Salud y bienestar", new TypeEntity(4, "health"), 4));
        articles.add(new ArticleEntity(++id, "Cultura pop", new TypeEntity(5, "entertainment"), 6));
        articles.add(new ArticleEntity(++id, "Economía global", new TypeEntity(6, "economy"), 7));
        articles.add(new ArticleEntity(++id, "Ciencia avanzada", new TypeEntity(7, "science"), 2));
    }



    @Override
    public List<ArticleEntity> getAll() {
        return new ArrayList<>(articles);
    }

    @Override
    public int save(ArticleEntity article) {
        article.setId(++id);
        articles.add(article);
        return id;
    }


    @Override
    public boolean delete(int articleId, boolean confirmation) {
        return articles.removeIf(a -> a.getId() == articleId) && confirmation;
    }

    @Override
    public void update(ArticleEntity article) {
        delete(article.getId(), true);
        articles.add(article);
    }

    @Override
    public ArticleEntity get(int id) {
        return articles.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
