package elefant.clone.repository;

import elefant.clone.model.Category;
import elefant.clone.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository  extends CrudRepository<Category, Integer> {

    List<Category> findAll();

}
