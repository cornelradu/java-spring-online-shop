package elefant.clone.repository;

import elefant.clone.model.Author;
import elefant.clone.model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Integer> {
    List<Author> findByName(String name);
}
