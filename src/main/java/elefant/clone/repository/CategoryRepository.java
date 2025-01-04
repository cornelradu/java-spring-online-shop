package elefant.clone.repository;

import elefant.clone.model.Author;
import elefant.clone.model.Category;
import elefant.clone.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository  extends CrudRepository<Category, Integer> {

    List<Category> findAll();
    List<Category> findByCategoryName(String name);
    Optional<Category> findByCategoryNameIgnoreCase(String categoryName);

}
