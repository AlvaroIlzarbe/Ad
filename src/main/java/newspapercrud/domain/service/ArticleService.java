package newspapercrud.domain.service;


import newspapercrud.dao.ArticleRepository;
import newspapercrud.dao.ReaderArticleRepository;
import newspapercrud.dao.mappers.ArticleMapper;
import newspapercrud.dao.model.ArticleEntity;
import newspapercrud.dao.model.ReaderArticleEntity;
import newspapercrud.dao.model.TypeEntity;
import newspapercrud.domain.model.ArticleDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ReaderArticleRepository readArticleRepository;
    private final ArticleMapper articleMapper;

    public ArticleService(ArticleRepository articleRepository, ReaderArticleRepository readArticleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.readArticleRepository = readArticleRepository;
        this.articleMapper = articleMapper;
    }

    public List<ArticleDTO> getArticles() {
        List<ArticleEntity> articles = articleRepository.getAll();

        Map<Integer, Double> avgByArticleId = readArticleRepository.getAll().stream()
                .collect(Collectors.toMap(ReaderArticleEntity::getArticleId,
                        r -> (double) r.getRating()));

        return articles.stream()
                .map(entity -> {
                    ArticleDTO dto = articleMapper.entityToDto(entity);
                    dto.setAvgRating(avgByArticleId.getOrDefault(entity.getId(), 0d));
                    return dto;
                })
                .collect(Collectors.toList());
    }
    private double calculateAverageRating(int articleId) {
        List<ReaderArticleEntity> readerArticleEntities = Collections.singletonList(readArticleRepository.getByIdArticle(articleId));
        if (readerArticleEntities.isEmpty()) {
            return 0.0;
        }

        double totalRating = 0.0;
        for (ReaderArticleEntity rae : readerArticleEntities) {
            totalRating += rae.getRating();
        }
        return totalRating / readerArticleEntities.size();
    }

    public int addArticle(ArticleDTO articleDTO) {
        ArticleEntity articleEntity = new ArticleEntity(
                articleDTO.getId(),
                articleDTO.getName(),
                new TypeEntity(articleDTO.getTypeUI().getId(), articleDTO.getTypeUI().getName()),
                articleDTO.getNpaperId()
        );
        return articleRepository.save(articleEntity);
    }
    public void updateArticle(ArticleDTO articleDTO) {
        ArticleEntity articleEntity = new ArticleEntity(
                articleDTO.getId(),
                articleDTO.getName(),
                new TypeEntity(articleDTO.getTypeUI().getId(), articleDTO.getTypeUI().getName()),
                articleDTO.getNpaperId()
        );
        articleRepository.update(articleEntity);
    }

    public boolean deleteArticle(int id, boolean confirm) {
        ArticleEntity articleEntity = articleRepository.get(id);
        return articleRepository.delete(id,confirm);
    }
}
