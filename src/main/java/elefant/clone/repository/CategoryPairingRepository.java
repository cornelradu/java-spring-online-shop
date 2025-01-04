package elefant.clone.repository;

import elefant.clone.model.CategoryPairing;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CategoryPairingRepository extends CrudRepository<CategoryPairing, Integer> {
    @Modifying
    @Query("DELETE FROM CategoryPairing ap WHERE ap.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}
