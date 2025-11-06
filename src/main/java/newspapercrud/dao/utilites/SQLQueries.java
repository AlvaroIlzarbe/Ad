package newspapercrud.dao.utilites;

public class SQLQueries {
    private SQLQueries() {}

    public static final String SELECT_ALL_ARTICLES = "SELECT a.id AS article_id, a.name AS article_name, a.newspaper_id, a.type AS type_id, t.description AS type_description FROM article a JOIN type t ON a.type = t.id";
    public static final String INSERT_ARTICLE = "INSERT INTO article (id, name, type, newspaper_id) VALUES (?,?,?,?)";
    public static final String INSERT_ARTICLE_WITH_AUTO_ID = "INSERT INTO article (name, type, newspaper_id) VALUES (?,?,?)";
    public static final String SELECT_ARTICLE_BY_ID = "SELECT a.id AS article_id, a.name AS article_name, a.newspaper_id, a.type AS type_id, t.description AS type_description FROM article a JOIN type t ON a.type = t.id WHERE a.id = ?";
    public static final String UPDATE_ARTICLE = "UPDATE article SET name = ?, type = ?, newspaper_id = ? WHERE id = ?";
    public static final String DELETE_ARTICLE_BY_ID = "DELETE FROM article WHERE id = ?";

    public static final String SELECT_ALL_CREDENTIALS = "SELECT user, password, reader_id FROM login";
    public static final String SELECT_CREDENTIAL_BY_USER = "SELECT user, password, reader_id FROM login WHERE user = ?";
    public static final String SELECT_CREDENTIALS_BY_READER_ID = "SELECT user, password, reader_id FROM login WHERE reader_id = ?";
    public static final String INSERT_CREDENTIAL = "INSERT INTO login (user, password, reader_id) VALUES (?,?,?)";
    public static final String UPDATE_CREDENTIAL = "UPDATE login SET password = ?, reader_id = ? WHERE user = ?";
    public static final String DELETE_CREDENTIAL_BY_USER = "DELETE FROM login WHERE user = ?";
    public static final String GET_CREDENTIAL = "select * from login where user = ?";

    public static final String SELECT_ALL_NEWSPAPERS = "SELECT id, name, release_date FROM newspaper";
    public static final String SELECT_NEWSPAPER_BY_ID = "SELECT id, name, release_date FROM newspaper WHERE id = ?";
    public static final String INSERT_NEWSPAPER = "INSERT INTO newspaper (id, name, release_date) VALUES (?,?,?)";
    public static final String INSERT_NEWSPAPER_WITH_AUTO_ID = "INSERT INTO newspaper (id, name, release_date) VALUES (?,?,?)";
    public static final String UPDATE_NEWSPAPER = "UPDATE newspaper SET name = ?, release_date = ? WHERE id = ?";
    public static final String DELETE_NEWSPAPER_BY_ID = "DELETE FROM newspaper WHERE id = ?";
    public static final String GET_NEWSPAPER_BY_ID = "select * from newspaper where id = ?";

    public static final String SELECT_ALL_READERS = "SELECT id, name, birth_date FROM reader";
    public static final String SELECT_READER_BY_ID = "SELECT id, name, birth_date FROM reader WHERE id = ?";
    public static final String SELECT_READERS_BY_ARTICLE_ID = "SELECT r.id AS reader_id, r.name_reader AS reader_name, r.birth_reader AS reader_birth, rd.article_id AS article_id, rd.rating AS rating FROM `read` rd JOIN reader r ON rd.reader_id = r.id WHERE rd.article_id = ?;";
    public static final String INSERT_READER = "INSERT INTO reader (id, name, birth_date) VALUES (?,?,?)";
    public static final String INSERT_READER_WITH_AUTO_ID = "INSERT INTO reader (name, birth_date) VALUES (?,?)";
    public static final String UPDATE_READER = "UPDATE reader SET name = ?, birth_date = ? WHERE id = ?";
    public static final String DELETE_READER_BY_ID = "DELETE FROM reader WHERE id = ?";
    public static final String SELECT_NEWSPAPERS_BY_READER = "SELECT DISTINCT n.* FROM newspaper n INNER JOIN article a ON n.id = a.newspaper_id INNER JOIN readarticle ra ON a.id = ra.id_article WHERE ra.id_reader = ?";




    public static final String SELECT_ALL_READERARTICLES = "SELECT id_article, id_reader, rating FROM readarticle";
    public static final String SELECT_READERARTICLES_BY_ARTICLE = "SELECT id_article, id_reader, rating FROM readarticle WHERE id_article = ?";
    public static final String SELECT_READERARTICLES_BY_READER = "SELECT id_article, id_reader, rating FROM readarticle WHERE id_reader = ?";
    public static final String INSERT_READERARTICLE = "INSERT INTO readarticle (id_article, id_reader, rating) VALUES (?,?,?)";
    public static final String UPDATE_READERARTICLE = "UPDATE readarticle SET rating = ? WHERE id_article = ? AND id_reader = ?";
    public static final String DELETE_READERARTICLE_BY_ID = "DELETE FROM readarticle WHERE id_article = ? AND id_reader = ?";
    public static final String GET_ALL_READERS_BY_ARTICLEID = "select * from reader where id in (select id_reader from readarticle where id_article = ?)";
    public static final String DELETE_READERARTICLE_BY_ARTICLE_ID = "DELETE FROM readarticle WHERE id_article = ? ";



    public static final String SELECT_ALL_TYPES = "SELECT id, description FROM type";
    public static final String SELECT_TYPE_BY_ID = "SELECT id, description FROM type WHERE id = ?";
    public static final String INSERT_TYPE = "INSERT INTO type (id, description) VALUES (?,?)";
    public static final String INSERT_TYPE_WITH_AUTO_ID = "INSERT INTO type (description) VALUES (?)";
    public static final String UPDATE_TYPE = "UPDATE type SET description = ? WHERE id = ?";
    public static final String DELETE_TYPE_BY_ID = "DELETE FROM type WHERE id = ?";


    public static final String DELETE_READER_READARTICLES = "delete from readarticle where id_reader = ?";
    public static final String DELETE_READER_SUBSCRIPTIONS = "delete from subscribe where id_reader = ?";
    public static final String DELETE_READER_LOGIN = "delete from login where id_reader = ?";

}
