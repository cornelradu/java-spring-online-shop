package elefant.clone.repository;

import elefant.clone.model.Category;
import elefant.clone.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    @Query(value = "SELECT p FROM Product p ORDER BY RAND()")
    List<Product> findRandomProducts(Pageable pageable);

    @Query(value = "SELECT p FROM Product p JOIN CategoryPairing cp ON p.id = cp.product.id JOIN Category c ON cp.category.id = c.id WHERE c.id = :categoryId")
    List<Product>   findByCategoryId(Integer categoryId);

    List<Product> findAll();
}
